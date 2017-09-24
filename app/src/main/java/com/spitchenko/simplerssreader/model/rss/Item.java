package com.spitchenko.simplerssreader.model.rss;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 14:32
 *
 * @author anatoliy
 */

@Entity(foreignKeys = @ForeignKey(entity = Channel.class, parentColumns = "channel_link", childColumns = "channel_link"))
@Data
public class Item {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "link")
    @SerializedName("link")
    private String link;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;

    @ColumnInfo(name = "guid")
    @SerializedName("guid")
    private String guid;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "pub_date")
    @SerializedName("pubDate")
    private String pubDate;

    @ColumnInfo(name = "channel_link")
    private String channelLink;
}