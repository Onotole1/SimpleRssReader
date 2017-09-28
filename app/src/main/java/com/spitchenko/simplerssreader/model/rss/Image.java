package com.spitchenko.simplerssreader.model.rss;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Date: 21.09.17
 * Time: 14:34
 *
 * @author anatoliy
 */

@EqualsAndHashCode
@RequiredArgsConstructor
@Entity
public class Image {

    @ColumnInfo(name = "image_link")
    @SerializedName("link")
    private String link;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}