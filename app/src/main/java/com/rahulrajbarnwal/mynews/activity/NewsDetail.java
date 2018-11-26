package com.rahulrajbarnwal.mynews.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rahulrajbarnwal.mynews.R;
import com.rahulrajbarnwal.mynews.modal.News;
import com.squareup.picasso.Picasso;

import static com.rahulrajbarnwal.mynews.utils.EndPoint.KW_DETAIL;
import static com.rahulrajbarnwal.mynews.utils.EndPoint.KW_STATUS;

public class NewsDetail extends AppCompatActivity implements View.OnClickListener {

    TextView tvSource, tvDate, tvTitle, tvDesc, tvContent, tvDetail;
    ImageView btnBack, ivNews;
    News news;
    String webUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        tvSource = findViewById(R.id.tv_source_name);
        tvDate = findViewById(R.id.tv_date_time);
        tvTitle = findViewById(R.id.tv_title);
        tvDesc = findViewById(R.id.tv_desc);
        tvContent = findViewById(R.id.tv_content);
        tvDetail = findViewById(R.id.tv_detail);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        ivNews = findViewById(R.id.iv_news);
        tvDetail.setOnClickListener(this);

        if (getIntent() != null){
            news = (News) getIntent().getSerializableExtra(KW_DETAIL);
            tvDate.setText(news.getPublishedAt());
            tvTitle.setText(news.getTitle());
            tvDesc.setText(news.getDescription());
            tvContent.setText(news.getContent());
            String name = news.getSource().getName();
            if (name != null){
                tvSource.setText(name);
            }

            String imgUrl = news.getUrlToImage();
            if (imgUrl != null){
                Picasso.with(getApplicationContext())
                        .load(imgUrl)
                        .resize(300, 200)
                        .placeholder(R.drawable.ic_place_holder)
                        .into(ivNews);
            }else {
                ivNews.setVisibility(View.GONE);
            }
            webUrl = news.getUrl();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back: {
                finish();
            }break;
            case R.id.tv_detail: {
                if ((webUrl != null) && (!webUrl.equals(""))){
                    Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                    intent.putExtra(KW_STATUS, webUrl);
                    startActivity(intent);
                }
            }break;
        }
    }
}
