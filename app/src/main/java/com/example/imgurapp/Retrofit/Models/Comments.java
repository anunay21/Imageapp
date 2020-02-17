package com.example.imgurapp.Retrofit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Comments {

    @SerializedName("data")
    @Expose
    List<Comment> comments;
}
