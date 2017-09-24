package com.spitchenko.simplerssreader.model.rss;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 14:32
 *
 * @author anatoliy
 */

@Data
class Item {

    @SerializedName("link")
    private String link;

    @SerializedName("description")
    private String description;

    @SerializedName("guid")
    private String guid;

    @SerializedName("title")
    private String title;

    @SerializedName("pubDate")
    private String pubDate;

}