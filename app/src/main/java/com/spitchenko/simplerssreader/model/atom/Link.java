package com.spitchenko.simplerssreader.model.atom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 16:14
 *
 * @author anatoliy
 */

@Entity(foreignKeys = @ForeignKey(entity = Feed.class, parentColumns = "id", childColumns = "feed_id"))
@Data
public class Link {

    @SerializedName("rel")
    private String rel;

    @SerializedName("href")
    private String href;

    @ColumnInfo(name = "feed_id")
    private String feedId;
}