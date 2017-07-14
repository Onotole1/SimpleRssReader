package com.spitchenko.simplerssreader.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Date: 14.07.17
 * Time: 8:19
 *
 * @author anatoliy
 */

public class Theme {
    @Getter @Setter @NonNull @SerializedName("themeName")
    private String themeName;
    @Getter @Setter @NonNull @SerializedName("colorPrimary")
    private Integer colorPrimary;
    @Getter @Setter @NonNull @SerializedName("colorPrimaryDark")
    private Integer colorPrimaryDark;
    @Getter @Setter @NonNull @SerializedName("colorAccent")
    private Integer colorAccent;
    @Getter @Setter @NonNull @SerializedName("textColorPrimary")
    private Integer textColorPrimary;
    @Getter @Setter @NonNull @SerializedName("textColorContent")
    private Integer textColorContent;
    @Getter @Setter @NonNull @SerializedName("windowBackground")
    private Integer windowBackground;
    @Getter @Setter @NonNull @SerializedName("contentBackground")
    private Integer contentBackground;

    @Override
    public String toString() {
        return "\"themeName\":\"" + themeName + "\",\n" +
                "         \"colorPrimary\":\"" + colorPrimary + "\",\n" +
                "         \"colorPrimaryDark\":\"" + colorPrimaryDark + "\",\n" +
                "         \"colorAccent\":\"" + colorAccent + "\",\n" +
                "         \"textColorPrimary\":\"" + textColorPrimary + "\",\n" +
                "         \"textColorContent\":\"" + textColorContent + "\",\n" +
                "         \"windowBackground\":\"" + windowBackground + "\",\n" +
                "         \"contentBackground\":\"" + contentBackground + "\"";
    }
}
