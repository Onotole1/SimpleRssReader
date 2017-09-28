package com.spitchenko.simplerssreader.model.atom;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 16:20
 *
 * @author anatoliy
 */

@Entity
@Data
public class Content {

    @SerializedName("$")
    private String textValue;

    @SerializedName("type")
    private String type;

}
