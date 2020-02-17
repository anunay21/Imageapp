package com.example.imgurapp.Retrofit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("image_id")
    @Expose
    public String imageId;
    @SerializedName("comment")
    @Expose
    public String comment;
    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("author_id")
    @Expose
    public Integer authorId;
    @SerializedName("on_album")
    @Expose
    public Boolean onAlbum;
    @SerializedName("album_cover")
    @Expose
    public String albumCover;
    @SerializedName("ups")
    @Expose
    public Integer ups;
    @SerializedName("downs")
    @Expose
    public Integer downs;
    @SerializedName("points")
    @Expose
    public Integer points;
    @SerializedName("datetime")
    @Expose
    public Integer datetime;
    @SerializedName("parent_id")
    @Expose
    public Integer parentId;
    @SerializedName("deleted")
    @Expose
    public Boolean deleted;
    @SerializedName("vote")
    @Expose
    public Object vote;
    @SerializedName("platform")
    @Expose
    public String platform;
    @SerializedName("has_admin_badge")
    @Expose
    public Boolean hasAdminBadge;

}
