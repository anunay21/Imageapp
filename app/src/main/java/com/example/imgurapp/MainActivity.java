package com.example.imgurapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.imgurapp.Retrofit.ApiClient;
import com.example.imgurapp.Retrofit.ApiInterface;
import com.example.imgurapp.Retrofit.Models.Comment;
import com.example.imgurapp.Retrofit.Models.Comments;
import com.example.imgurapp.Retrofit.Models.Data;
import com.example.imgurapp.Retrofit.Models.Entity;
import com.example.imgurapp.Retrofit.Models.Image;
import com.example.imgurapp.Retrofit.Models.Items;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    private ApiInterface apiInterface;
    private ImageSwitcher imageSwitcher;
    private Data mData;
    private LinkedList<Items> itemList;
    private final int listSize = 9;
    private static int currentPosition = 0;
    private View progressView;
    private final long animateDuration = 4000;
    private int screenWidth, initWidth =0;
    private ValueAnimator progressBarAnimator;
    private View detailPageView;
    private ImageView closeDetailPageButton;
    private ImageView detailPageImage;
    private RecyclerView tagRecyclerView;
    private TagRecyclerViewAdapter adapter;
    private RecyclerView commentRecyclerView;
    private CommentRecyclerViewAdapter commentRecyclerViewAdapter;
    private TextView comment;
    boolean firstReq = true;
    List<Comment> comments;
    private int pageNo = 0;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageSwitcher = findViewById(R.id.imageSwitcher);
        progressView = findViewById(R.id.progressView);
        detailPageView  = findViewById(R.id.detailPage);
        closeDetailPageButton = findViewById(R.id.detailPageCloseButton);
        detailPageImage = findViewById(R.id.detailViewImage);
        tagRecyclerView = findViewById(R.id.tagRecyclerView);
        tagRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));
        commentRecyclerView=  findViewById(R.id.commentRecyclerView);
        commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(this);
        commentRecyclerView.setAdapter(commentRecyclerViewAdapter);
        comment = findViewById(R.id.comment);
        findViewById(R.id.shoWMoreComments).setOnClickListener(this);

        itemList = new LinkedList<>();
        if (apiInterface == null) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
        }
        adapter = new TagRecyclerViewAdapter(this);
        tagRecyclerView.setAdapter(adapter);
        imageSwitcher.setOnClickListener(this);
        closeDetailPageButton.setOnClickListener(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = (int) (displayMetrics.widthPixels - Utils.pxFromDp(this, getResources().getDimension(R.dimen.default_margin))/2);
        initProgressBarAnimation();
        initImageSwitcher();
        makeScienceAndTechReq();
    }

    private void initImageSwitcher(){
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                ViewGroup.LayoutParams params = new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(params);
                return imageView;
            }
        });
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_left));

    }

    private void makeScienceAndTechReq(){
        Call<Entity> entity = apiInterface.getScienceAndTechImages(pageNo++);
        entity.enqueue(new Callback<Entity>() {
            @Override
            public void onResponse(Call<Entity> call, Response<Entity> response) {
                Log.d(TAG,"Science and Tech Response Success");
                mData = response.body().getData();
                initDataToLinkedList();
            }

            @Override
            public void onFailure(Call<Entity> call, Throwable t) {
                Log.d(TAG, (t == null ? "Some unknown error" : "" + t.getMessage()) + ", Science and Tech Response Failed");
            }
        });
    }

    private void moveToNext(){
        addNextItemToList();
        cleanBitmapAndthenRemoveFromList();
        if(itemList.size() == 0){
            Toast.makeText(this, "Querying the next batch.",Toast.LENGTH_SHORT).show();
            progressBarAnimator.end();
            imageSwitcher.setClickable(false);
            queryNext();
            return;
        }
        if(itemList != null && itemList.size() >0 &&itemList.get(0)!=null){
            try {
                if(itemList.get(0).getImages().get(0).getBmp() == null){
                    Toast.makeText(this, "Couldn't download this image, network issue.",Toast.LENGTH_SHORT).show();
                }
                Drawable drawable = new BitmapDrawable(getResources(), itemList.get(0).getImages().get(0).getBmp());
                imageSwitcher.setImageDrawable(drawable);
                makeCommentsCall();
            } catch (Exception e){
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cleanBitmapAndthenRemoveFromList(){
        for(Image image : itemList.get(0).getImages()){
            image.setBmp(null);
        }
        itemList.remove(0);
    }

    private void moveToPrevious(){

    }

    private void initDataToLinkedList(){
        int size = 0;
        if(mData!=null && mData.getItems()!=null) {
            size = mData.getItems().size() < 9 ? mData.getItems().size() : 9;
        }
        for(int i=0; i < size; i++){
            addNextItemToList();
        }
    }

    private void addNextItemToList(){
        if(currentPosition>=mData.getItems().size()) {
            return;
        }
        while(mData.getItems().get(currentPosition).getImagesCount() == null || (mData.getItems().get(currentPosition).getImages().size() != 0 && mData.getItems().get(currentPosition).getImagesCount() > 10) || !mData.getItems().get(currentPosition).getImages().get(0).getType().contains("image")){
            currentPosition++;
            if(currentPosition>=mData.getItems().size()){
                return;
            }
        }
        itemList.add(mData.getItems().get(currentPosition));
        mData.getItems().get(currentPosition).setPosiitonInData(currentPosition);
        for(int i = 0; i < mData.getItems().get(currentPosition).getImagesCount() && i < 3; i++){
            ImageLoadAsyncTask asyncTask = new ImageLoadAsyncTask(mData,currentPosition,i, imageSwitcher, firstReq, progressBarAnimator);
            firstReq = false;
            asyncTask.execute(mData.getItems().get(currentPosition).getImages().get(i).getLink());
        }
        currentPosition++;
    }

    private void initProgressBarAnimation(){
        progressBarAnimator = ValueAnimator.ofInt(initWidth,screenWidth);
        progressBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams params = progressView.getLayoutParams();
                params.width = (int) valueAnimator.getAnimatedValue();
                progressView.setLayoutParams(params);
            }
        });
        progressBarAnimator.setDuration(animateDuration);
        progressBarAnimator.setRepeatCount(Animation.INFINITE);
        progressBarAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                moveToNext();
            }
        });
    }

    private void openDetailPage(){
        if(comments != null && comments.size()>20){
            commentRecyclerViewAdapter.setComments(comments.subList(0,19));
            findViewById(R.id.shoWMoreComments).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.shoWMoreComments).setVisibility(View.GONE);
            commentRecyclerViewAdapter.setComments(comments);
        }
        if(comments == null || comments.size() == 0){
            comment.setVisibility(View.GONE);
        }else{
            comment.setVisibility(View.VISIBLE);
        }
        adapter.setTags(itemList.get(0).getTags());
        try{
            detailPageImage.setImageBitmap(itemList.get(0).getImages().get(0).getBmp());
        } catch (Exception e){
            e.printStackTrace();
        }

        ((TextView)findViewById(R.id.title)).setText(itemList.get(0).getTitle());
        String desc = itemList.get(0).getImages().get(0).getDescription();
        if(desc == null || "".equals(desc)){
            findViewById(R.id.imageDescription).setVisibility(View.GONE);
        } else {
            findViewById(R.id.imageDescription).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.imageDescription)).setText(desc);
        }
        ((AppCompatTextView)findViewById(R.id.upvotes)).setText(itemList.get(0).getUps().toString());
        ((AppCompatTextView)findViewById(R.id.downVotes)).setText(itemList.get(0).getDowns().toString());
        ((AppCompatTextView)findViewById(R.id.viewCount)).setText(itemList.get(0).getViews().toString());
        progressBarAnimator.pause();
        Utils.slideUp(detailPageView);
        closeDetailPageButton.setVisibility(View.VISIBLE);
    }

    private void closeDetailPage(){
        progressBarAnimator.resume();
        Utils.slideDown(detailPageView);
        closeDetailPageButton.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if(closeDetailPageButton.getVisibility() == View.VISIBLE){
            closeDetailPage();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageSwitcher:
                openDetailPage();
                break;
            case R.id.detailPageCloseButton:
                closeDetailPage();
                break;
            case R.id.shoWMoreComments:
                showMoreComments();
                break;
        }
    }

    private void makeCommentsCall(){
        Call<Comments> commentsCall = apiInterface.getComments(itemList.get(0).getId());
        commentsCall.enqueue(new Callback<Comments>() {
            @Override
            public void onResponse(Call<Comments> call, Response<Comments> response) {
                comments = response.body().getComments();
            }

            @Override
            public void onFailure(Call<Comments> call, Throwable t) {
                Log.d(TAG, "Comments call failure");
            }
        });
    }

    @Override
    protected void onResume() {
        if(!firstReq){
            progressBarAnimator.resume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressBarAnimator.pause();
    }

    private void queryNext(){
        firstReq = true;
        currentPosition = 0;
        makeScienceAndTechReq();
    }

    private void showMoreComments(){
        commentRecyclerViewAdapter.setComments(comments);
        findViewById(R.id.shoWMoreComments).setVisibility(View.GONE);
    }
}
