package com.spitchenko.simplerssreader.model.rss;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Rss {

    @SerializedName("$")
    private String textValue;

    @SerializedName("channel")
    private Channel channel;

    @SerializedName("version")
    private Double version;

}