package com.example.imgurapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imgurapp.Retrofit.Models.Tags;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<TagView> {

    private List<Tags> tags;

    private Context context;
    public RecyclerViewAdapter(Context context){
        this.tags = tags;
        this.context = context;
    }

    @NonNull
    @Override
    public TagView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_tag, parent, false);
        return new TagView(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TagView holder, int position) {
        holder.textView.setText(tags.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        if(tags == null) {
            return 0;
        }
        return tags.size();
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
        notifyDataSetChanged();
    }
}

class TagView extends RecyclerView.ViewHolder{
    TextView textView;


    public TagView(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tagTextView);
    }
}
