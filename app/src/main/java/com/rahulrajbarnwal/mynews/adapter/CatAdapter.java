package com.rahulrajbarnwal.mynews.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rahulrajbarnwal.mynews.R;
import com.rahulrajbarnwal.mynews.modal.Cats;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {

    Context context;
    ArrayList<Cats> catsList;
    OnCatListener onCatListener;
    boolean isOpen = false;
    private int index = -1;

    public interface OnCatListener{
        void onItemMore();
        void onItemLess();
        void onDetail(String cat);
    }

    public CatAdapter(Context context, ArrayList<Cats> catsList, OnCatListener onCatListener) {
        this.context = context;
        this.catsList = catsList;
        this.onCatListener = onCatListener;
    }

    View view;
    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        view = inflater.inflate(R.layout.item_category, viewGroup, false);
        return new CatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CatViewHolder holder, int i) {
        final Cats cats = catsList.get(i);

        holder.tvCat.setText(cats.getCatName());
        final int pos = i;

        Picasso.with(context)
                .load("wjfipjfipeo")
                .resize(100, 100)
                .placeholder(cats.getCatImage())
                .into(holder.ivCat);

       /* if (i == 3){
            holder.llCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }*/

        holder.llCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos == 3){
                    if (isOpen){
                        holder.ivCat.setRotation(360);
                        holder.tvCat.setText("more");
                        isOpen = false;
                        onCatListener.onItemLess();
                    }else {
                        holder.ivCat.setRotation(180);
                        holder.tvCat.setText("less");
                        isOpen = true;
                        onCatListener.onItemMore();
                    }
                }else{
                    if (pos == index){
                        holder.llBg.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
                        index = -1;
                    }else {
                        index = pos;
                        notifyDataSetChanged();
                    }

                    onCatListener.onDetail(cats.getCatName());
                }
            }
        });

        if (index == pos){
            holder.llBg.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }else {
            holder.llBg.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
        }

    }

    @Override
    public int getItemCount() {
        return catsList.size();
    }

    private void add(Cats cM){
        catsList.add(cM);
        notifyDataSetChanged();
        // notifyItemInserted(categoryModels.size()-1);
    }
    public void addAll(List<Cats> cMList){
        for (Cats cM : cMList){
            add(cM);
        }
    }

    private void removeItem(Cats cM) {
        catsList.remove(cM);
        notifyDataSetChanged();
    }
    public void removeAll(List<Cats> cMList){
        for (Cats cM : cMList){
            removeItem(cM);
        }
    }

    public class CatViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCat;
        TextView tvCat;
        LinearLayout llCat, llBg;
        public CatViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCat = itemView.findViewById(R.id.iv_image);
            tvCat = itemView.findViewById(R.id.tv_name);
            llCat = itemView.findViewById(R.id.ll_holder);
            llBg = itemView.findViewById(R.id.ll_cat);
        }
    }
}
