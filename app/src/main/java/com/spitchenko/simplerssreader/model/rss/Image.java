package com.spitchenko.simplerssreader.model.rss;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 14:34
 *
 * @author anatoliy
 */

@Entity
@Data
public class Image {

    @ColumnInfo(name = "image_link")
    @SerializedName("link")
    private String link;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;
}