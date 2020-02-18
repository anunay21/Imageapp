package com.example.imgurapp;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageSwitcher;

import com.example.imgurapp.Retrofit.Models.Data;

import java.lang.ref.WeakReference;
import java.net.URL;

public class ImageLoadAsyncTask extends AsyncTask<String, Integer, Bitmap> {

    private WeakReference<Data> dataWeakReference;
    private int dataItemPositionX, dataItemPositionY;
    private WeakReference<ImageSwitcher> imageSwitcherWeakReference;
    private WeakReference<ValueAnimator> valueAnimatorWeakReference;
    private boolean firstReq;

    ImageLoadAsyncTask(Data data, int x, int y, ImageSwitcher imageView, boolean firstReq, ValueAnimator val){
        dataWeakReference = new WeakReference<>(data);
        dataItemPositionX = x;
        dataItemPositionY = y;
        this.imageSwitcherWeakReference = new WeakReference<>(imageView);
        this.firstReq = firstReq;
        this.valueAnimatorWeakReference = new WeakReference<>(val);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bmp = null;
        for (String s : strings){
            try {
                URL url = new URL(s);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap!= null && bitmap.getByteCount()>100000000) {
            return;
        }
        dataWeakReference.get().getItems().get(dataItemPositionX).getImages().get(dataItemPositionY).setBmp(bitmap);
        if(firstReq) {
            imageSwitcherWeakReference.get().setVisibility(View.VISIBLE);
            Drawable drawable = new BitmapDrawable(bitmap);
            imageSwitcherWeakReference.get().setImageDrawable(drawable);
            imageSwitcherWeakReference.get().setClickable(true);
            valueAnimatorWeakReference.get().start();
        }
    }
}
