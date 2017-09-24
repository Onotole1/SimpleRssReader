package com.spitchenko.simplerssreader.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.spitchenko.simplerssreader.database.dao.RssDao;
import com.spitchenko.simplerssreader.model.rss.Channel;
import com.spitchenko.simplerssreader.model.rss.Item;

/**
 * Date: 24.09.2017
 * Time: 23:04
 *
 * @author Anatoliy
 */

@Database(entities = {Channel.class, Item.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RssDao getRssDao();
}
