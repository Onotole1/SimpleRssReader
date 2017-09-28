package com.spitchenko.simplerssreader.model.rss;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Date: 21.09.17
 * Time: 14:32
 *
 * @author anatoliy
 */

@EqualsAndHashCode
@RequiredArgsConstructor
@Entity(foreignKeys = @ForeignKey(entity = Channel.class, parentColumns = "channel_link", childColumns = "channel_link"))
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

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(final String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(final String pubDate) {
        this.pubDate = pubDate;
    }

    public String getChannelLink() {
        return channelLink;
    }

    public void setChannelLink(final String channelLink) {
        this.channelLink = channelLink;
    }
}