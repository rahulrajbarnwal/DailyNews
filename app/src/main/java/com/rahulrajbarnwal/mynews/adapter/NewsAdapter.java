package com.rahulrajbarnwal.mynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rahulrajbarnwal.mynews.R;
import com.rahulrajbarnwal.mynews.activity.NewsDetail;
import com.rahulrajbarnwal.mynews.modal.News;

import java.util.ArrayList;

import static com.rahulrajbarnwal.mynews.utils.EndPoint.KW_DETAIL;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    Context context;
    ArrayList<News> newsList;

    public NewsAdapter(Context context, ArrayList<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    View view;
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        view = inflater.inflate(R.layout.item_news, viewGroup, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int i) {
        final News news = newsList.get(i);

        holder.tvTitle.setText(news.getTitle());
        holder.tvDateTime.setText(news.getPublishedAt());
        if (news.getSource().getName() != null){
            holder.tvSource.setText(news.getSource().getName() + "");
        }
        if (news.getDescription() != null){
            holder.tvDesc.setText(news.getDescription());
        }

        holder.cvNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetail.class);
                intent.putExtra(KW_DETAIL, news);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSource, tvDateTime, tvDesc;
        CardView cvNews;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSource = itemView.findViewById(R.id.tv_source_name);
            tvDateTime = itemView.findViewById(R.id.tv_time);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            cvNews = itemView.findViewById(R.id.cv_news);
        }
    }
}
