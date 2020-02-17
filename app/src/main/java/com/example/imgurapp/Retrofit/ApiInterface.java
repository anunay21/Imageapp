package com.example.imgurapp.Retrofit;


import com.example.imgurapp.Retrofit.Models.Comments;
import com.example.imgurapp.Retrofit.Models.Entity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("3/gallery/t/science_and_tech///")
    Call<Entity> getScienceAndTechImages();

    @GET("3/gallery/{galleryHash}/comments/best")
    Call<Comments> getComments(@Path("galleryHash") String galleryHash);
}
