package com.spitchenko.simplerssreader.model.rss;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 14:34
 *
 * @author anatoliy
 */

@Data
class Image {

    @SerializedName("link")
    private String link;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;
}