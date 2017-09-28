package com.spitchenko.simplerssreader.model.atom;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 16:14
 *
 * @author anatoliy
 */

@Entity
@Data
public class Author {
    @SerializedName("name")
    String name;

    @SerializedName("uri")
    String uri;

    @SerializedName("email")
    String email;
}
