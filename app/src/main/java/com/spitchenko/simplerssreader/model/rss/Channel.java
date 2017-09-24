package com.spitchenko.simplerssreader.model.rss;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 14:34
 *
 * @author anatoliy
 */

@Data
public class Channel {

    @SerializedName("image")
    private Image image;

    @SerializedName("item")
    private List<Item> item;

    @SerializedName("lastBuildDate")
    private String lastBuildDate;

    @SerializedName("link")
    private String link;

    @SerializedName("description")
    private String description;

    @SerializedName("title")
    private String title;

}
