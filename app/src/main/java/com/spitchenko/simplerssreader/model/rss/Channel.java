package com.spitchenko.simplerssreader.model.rss;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 14:34
 *
 * @author anatoliy
 */

@Entity
@Data
public class Channel {

    @Embedded
    @SerializedName("image")
    private Image image;

    @Ignore
    @SerializedName("item")
    private List<Item> item;

    @SerializedName("lastBuildDate")
    private String lastBuildDate;

    @PrimaryKey
    @ColumnInfo(name = "channel_link")
    @SerializedName("link")
    private String link;

    @SerializedName("description")
    private String description;

    @ColumnInfo(name = "channel_title")
    @SerializedName("title")
    private String title;

}
