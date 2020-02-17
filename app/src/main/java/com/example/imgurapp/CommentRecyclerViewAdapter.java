package com.example.imgurapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imgurapp.Retrofit.Models.Comment;
import com.example.imgurapp.Retrofit.Models.Comments;

import java.util.List;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private Context context;
    private List<Comment> comments;

    public CommentRecyclerViewAdapter(Context context){
        this.context = context;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_comments, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.commentTextView.setText(comment.getComment());
        holder.commentAuthor.setText(comment.getAuthor());
        holder.likes.setText(comment.getPoints().toString());
        holder.upVotes.setText(comment.getUps().toString());
        holder.downVotes.setText(comment.getDowns().toString());
    }

    @Override
    public int getItemCount() {
        if(comments == null){
            return 0;
        }
        return comments.size();
    }
}

class CommentViewHolder extends RecyclerView.ViewHolder {

    TextView commentTextView;
    TextView commentAuthor;
    AppCompatTextView upVotes;
    AppCompatTextView downVotes;
    AppCompatTextView likes;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        commentTextView = itemView.findViewById(R.id.commentTextView);
        commentAuthor = itemView.findViewById(R.id.commentAuthor);
        upVotes = itemView.findViewById(R.id.upVotesOnComments);
        downVotes = itemView.findViewById(R.id.downVotesOnComments);
        likes = itemView.findViewById(R.id.pointsOnComments);
    }
}
