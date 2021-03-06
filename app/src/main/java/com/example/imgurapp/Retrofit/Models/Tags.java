package com.example.imgurapp.Retrofit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tags {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("followers")
    @Expose
    private Integer followers;
    @SerializedName("total_items")
    @Expose
    private Integer totalItems;
    @SerializedName("following")
    @Expose
    private Boolean following;
    @SerializedName("is_whitelisted")
    @Expose
    private Boolean isWhitelisted;
    @SerializedName("background_hash")
    @Expose
    private String backgroundHash;
    @SerializedName("thumbnail_hash")
    @Expose
    private Object thumbnailHash;
    @SerializedName("accent")
    @Expose
    private Object accent;
    @SerializedName("background_is_animated")
    @Expose
    private Boolean backgroundIsAnimated;
    @SerializedName("thumbnail_is_animated")
    @Expose
    private Boolean thumbnailIsAnimated;
    @SerializedName("is_promoted")
    @Expose
    private Boolean isPromoted;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("logo_hash")
    @Expose
    private Object logoHash;
    @SerializedName("logo_destination_url")
    @Expose
    private Object logoDestinationUrl;
    //@SerializedName("description_annotations")
    //@Expose
    //private DescriptionAnnotations descriptionAnnotations;

}
