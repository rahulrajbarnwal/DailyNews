package com.rahulrajbarnwal.mynews.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.support.v7.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rahulrajbarnwal.mynews.R;
import com.rahulrajbarnwal.mynews.adapter.CatAdapter;
import com.rahulrajbarnwal.mynews.adapter.NewsAdapter;
import com.rahulrajbarnwal.mynews.adapter.SourceAdapter;
import com.rahulrajbarnwal.mynews.modal.Cats;
import com.rahulrajbarnwal.mynews.modal.News;
import com.rahulrajbarnwal.mynews.modal.Source;
import com.rahulrajbarnwal.mynews.utils.PaginationScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rahulrajbarnwal.mynews.utils.EndPoint.API_KEY_COUNTRY;
import static com.rahulrajbarnwal.mynews.utils.EndPoint.BASE_URL;
import static com.rahulrajbarnwal.mynews.utils.EndPoint.KW_ARTICLES;
import static com.rahulrajbarnwal.mynews.utils.EndPoint.KW_STATUS;
import static com.rahulrajbarnwal.mynews.utils.EndPoint.RB_CATE;
import static com.rahulrajbarnwal.mynews.utils.EndPoint.RB_HEADLINE;
import static com.rahulrajbarnwal.mynews.utils.EndPoint.RB_PAGE;
import static com.rahulrajbarnwal.mynews.utils.EndPoint.RB_SEARCH;
import static com.rahulrajbarnwal.mynews.utils.EndPoint.RB_SOURCES;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {


    RecyclerView rvCats, rvNews;
    final String[] catsName = {"business","entertainment","technology","more","health","science","sports","general"};
    final Integer[] catsImage = {R.drawable.ic_business, R.drawable.ic_entertainment, R.drawable.ic_technology, R.drawable.ic_more_dark,
            R.drawable.ic_health, R.drawable.ic_science, R.drawable.ic_sports, R.drawable.ic_genral};

    FloatingActionButton fabSearch;
    ArrayList<Cats> cList;
    ArrayList<Cats> caList;
    CatAdapter catAdapter;
    ArrayList<News> newList;
    RelativeLayout rlLoader;
    NewsAdapter newsAdapter;
    LinearLayout llLoader;
    Snackbar snackbar;
    CoordinatorLayout coordinator;
    SwipeRefreshLayout refreshNews;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;

    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    RecyclerView rvSource;
    SearchView searchView;
    ArrayList<Source> sourceList;
    SourceAdapter sourceAdapter;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (!isNetworkAvailable()){
            createNetErrorDialog();
        }

        coordinator = findViewById(R.id.coordinator);
        rvCats = findViewById(R.id.rv_category);
        rvCats.setHasFixedSize(true);
        refreshNews = findViewById(R.id.refresh_news);
        rvNews = findViewById(R.id.rv_news);
        rvNews.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(), 4);
        rvCats.setLayoutManager(glm);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvNews.setLayoutManager(lm);
        rlLoader = findViewById(R.id.rl_loader);
        llLoader = findViewById(R.id.ll_loader);
        fabSearch = findViewById(R.id.fab_search);
        fabSearch.setOnClickListener(this);
        setCatsData();

        filterDialog();

        newList = new ArrayList<>();
        newsAdapter = new NewsAdapter(getApplicationContext(), newList);
        rvNews.setAdapter(newsAdapter);

        getNews("", "", PAGE_START);

       rvNews.addOnScrollListener(new PaginationScrollListener(lm) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNews("", "", currentPage);
                    }
                }, 1000);
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

       refreshNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               getNews("", "", PAGE_START);
           }
       });
    }

    private void filterDialog(){
        dialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialog = inflater.inflate(R.layout.search_dialog, null);
        dialogBuilder.setView(dialog);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        searchView = dialog.findViewById(R.id.et_search);
        rvSource = dialog.findViewById(R.id.rv_source);
        rvSource.setHasFixedSize(true);
        LinearLayoutManager lama = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvSource.setLayoutManager(lama);
        sourceList = new ArrayList<>();
        searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.color_text));
        searchView.setOnClickListener(this);

        sourceAdapter = new SourceAdapter(getApplicationContext(), sourceList);
        rvSource.setAdapter(sourceAdapter);
        getNewsSources();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews(query, "", PAGE_START);
                alertDialog.dismiss();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setCatsData(){
        cList = new ArrayList<>();
        caList = new ArrayList<>();
        for (int a=0;a<8; a++){
            Cats ca = new Cats(a, catsName[a], catsImage[a]);
            if (a<4){
                cList.add(ca);
            }else {
                caList.add(ca);
            }
        }

        catAdapter = new CatAdapter(getApplicationContext(), cList, new CatAdapter.OnCatListener() {
            @Override
            public void onItemMore() {
                catAdapter.addAll(caList);
            }
            @Override
            public void onItemLess() {
                catAdapter.removeAll(caList);
            }
            @Override
            public void onDetail(String cat) {
                getNews("", cat, 0);
            }
        });
        rvCats.setAdapter(catAdapter);
    }



    private void showSnack(String sms){
        snackbar = Snackbar.make(coordinator, sms, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void createNetErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You need internet connection for this app. Please turn on mobile network or Wi-Fi in Settings.")
                .setTitle("Unable to connect")
                .setCancelable(false)
                .setPositiveButton("Setting",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(i);
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Dashboard.this.finish();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void getNews(String search, String category, final int page){
        if (page == 0){
            rlLoader.setVisibility(View.VISIBLE);
        }else {
            llLoader.setVisibility(View.VISIBLE);
        }
        String url = BASE_URL + RB_HEADLINE + API_KEY_COUNTRY + RB_PAGE + page + RB_CATE + category + RB_SEARCH + search;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        refreshNews.setRefreshing(false);
                        rlLoader.setVisibility(View.GONE);
                        llLoader.setVisibility(View.GONE);
                        Log.e("response", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String stats = jsonResponse.getString(KW_STATUS);
                            if (stats.equals("ok")){
                                JSONArray jsonArti = jsonResponse.getJSONArray(KW_ARTICLES);
                                Gson gson = new Gson();
                                Log.e("length", jsonArti.length() + "");
                                if (jsonArti.length() > 0){

                                    if (page == 0){
                                        newList.clear();
                                    }
                                    for (int a=0; a<jsonArti.length(); a++){
                                        News news = gson.fromJson(String.valueOf(jsonArti.get(a)), News.class);
                                        newList.add(news);
                                    }
                                    newsAdapter.notifyDataSetChanged();
                                    if (jsonArti.length() <= TOTAL_PAGES){
                                        isLastPage = true;
                                    }else {
                                        isLastPage = false;
                                        isLoading = false;
                                    }
                                }else {
                                    showSnack("Data not found");
                                }
                            }else {
                                showSnack("Something went wrong. Please try after some time");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                        rlLoader.setVisibility(View.GONE);
                        llLoader.setVisibility(View.GONE);
                        refreshNews.setRefreshing(false);
                        showSnack(error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getNewsSources(){
        String urlSource = BASE_URL + RB_SOURCES + API_KEY_COUNTRY;
        Log.e("urlSource", urlSource);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSource,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String stats = jsonResponse.getString(KW_STATUS);
                            if (stats.equals("ok")){
                                JSONArray jsonSource = jsonResponse.getJSONArray("sources");
                                if (jsonSource.length() > 0){
                                    sourceList.clear();
                                    Gson gson = new Gson();
                                    for (int s=0; s<jsonSource.length(); s++){
                                        Source source = gson.fromJson(String.valueOf(jsonSource.get(s)), Source.class);
                                        sourceList.add(source);
                                    }
                                    sourceAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        alertDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.fab_search: {
                if (alertDialog != null){
                    alertDialog.show();
                    searchView.setIconified(false);
                }
            }break;
            case R.id.et_search: {
                searchView.setIconified(false);
            }break;
        }
    }
}
