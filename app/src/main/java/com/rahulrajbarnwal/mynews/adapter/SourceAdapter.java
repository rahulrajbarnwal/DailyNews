package com.rahulrajbarnwal.mynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rahulrajbarnwal.mynews.R;
import com.rahulrajbarnwal.mynews.activity.WebActivity;
import com.rahulrajbarnwal.mynews.modal.Source;

import java.util.ArrayList;

import static com.rahulrajbarnwal.mynews.utils.EndPoint.KW_STATUS;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceViewHolder> {

    Context context;
    ArrayList<Source> sourceList;

    public SourceAdapter(Context context, ArrayList<Source> sourceList) {
        this.context = context;
        this.sourceList = sourceList;
    }

    View view;
    @NonNull
    @Override
    public SourceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        view = inflater.inflate(R.layout.item_source, viewGroup, false);
        return new SourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceViewHolder holder, int i) {
        Source source = sourceList.get(i);

        if (source.getName() != null){
            holder.tvName.setText(source.getName());
        }
        final String urll = source.getUrl();

        holder.ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urll != null){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(urll));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    /*Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra(KW_STATUS, urll);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

    public class SourceViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivNext;
        CardView cvSource;
        public SourceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivNext = itemView.findViewById(R.id.iv_next);
            cvSource = itemView.findViewById(R.id.cv_source);
        }
    }
}
