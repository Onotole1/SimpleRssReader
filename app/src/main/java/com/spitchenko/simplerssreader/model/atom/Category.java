package com.spitchenko.simplerssreader.model.atom;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 16:19
 *
 * @author anatoliy
 */

@Entity
@Data
public class Category {
    @SerializedName("term")
    private String term;
}
