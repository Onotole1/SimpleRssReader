package com.spitchenko.simplerssreader.repository;

/**
 * Date: 25.09.17
 * Time: 13:04
 *
 * @author anatoliy
 */

public class RssAtomRepository {
    private static final RssAtomRepository ourInstance = new RssAtomRepository();

    public static RssAtomRepository getInstance() {
        return ourInstance;
    }

    private RssAtomRepository() {
    }
}
