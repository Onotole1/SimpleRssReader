package com.spitchenko.simplerssreader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.spitchenko.simplerssreader.model.Channel;
import com.spitchenko.simplerssreader.model.ChannelItem;
import com.spitchenko.simplerssreader.utils.logger.LogCatHandler;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

import lombok.Cleanup;
import lombok.NonNull;

import static com.spitchenko.simplerssreader.database.AtomRssDataBase.ChannelEntry;
import static com.spitchenko.simplerssreader.database.AtomRssDataBase.ChannelItemEntry;

/**
 * Date: 09.03.17
 * Time: 18:36
 *
 * @author anatoliy
 */
@SuppressWarnings( "deprecation" )
public final class AtomRssChannelDbHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "AtomRssChannel.db";

	private static final String TEXT_TYPE = " TEXT";
	private static final String SQL_CREATE_CHANNEL_ENTRIES =
			"CREATE TABLE " + ChannelEntry.TABLE_NAME + " (" +
			ChannelEntry._ID + " INTEGER PRIMARY KEY, " +
			ChannelEntry.CHANNEL_ID + TEXT_TYPE + "," +
			ChannelEntry.CHANNEL_TITLE + TEXT_TYPE + "," +
			ChannelEntry.CHANNEL_SUBTITLE + TEXT_TYPE + "," +
			ChannelEntry.CHANNEL_LINK + TEXT_TYPE + "," +
			ChannelEntry.CHANNEL_IMAGE + TEXT_TYPE + "," +
			ChannelEntry.CHANNEL_BUILD_DATE + TEXT_TYPE + "," +
			ChannelEntry.CHANNEL_IS_READ + " INTEGER" +
			" )";

	private static final String SQL_CREATE_CHANNEL_ITEM_ENTRIES =
			"CREATE TABLE " + ChannelItemEntry.TABLE_NAME + " (" +
			ChannelItemEntry._ID + " INTEGER PRIMARY KEY," +
			ChannelItemEntry.CHANNEL_ITEM_ID + TEXT_TYPE + "," +
			ChannelItemEntry.CHANNEL_ITEM_TITLE + TEXT_TYPE + "," +
			ChannelItemEntry.CHANNEL_ITEM_SUBTITLE + TEXT_TYPE + "," +
			ChannelItemEntry.CHANNEL_ITEM_LINK + TEXT_TYPE + "," +
			ChannelItemEntry.CHANNEL_ITEM_PUB_DATE + TEXT_TYPE + "," +
			ChannelEntry.CHANNEL_ID + " INTEGER" + "," +
			ChannelItemEntry.CHANNEL_ITEM_ISREAD + " INTEGER" + "," +
			"FOREIGN KEY(" + ChannelEntry.CHANNEL_ID +
			") REFERENCES " + ChannelEntry.TABLE_NAME + "(" + ChannelEntry.CHANNEL_ID + ")" +
			" )";

	private static final String SQL_DELETE_CHANNEL_ENTRIES =
			"DROP TABLE IF EXISTS " + ChannelEntry.TABLE_NAME;

	public AtomRssChannelDbHelper(@NonNull final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public final void onCreate(@NonNull final SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_CHANNEL_ENTRIES);
		db.execSQL(SQL_CREATE_CHANNEL_ITEM_ENTRIES);
	}

	@Override
	public final void onUpgrade(final SQLiteDatabase db, final int oldVersion
            , final int newVersion) {
		db.execSQL(SQL_DELETE_CHANNEL_ENTRIES);
		onCreate(db);
	}


	public final Channel readChannelFromDb(@NonNull final String url) {
		@Cleanup
		final SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

		@Cleanup
		final Cursor cursorChannel = sqLiteDatabase.rawQuery("SELECT *  FROM  "
		                                                     + ChannelEntry.TABLE_NAME + " WHERE "
		                                                     + ChannelEntry.CHANNEL_LINK + "= '"
		                                                     + url + "' ORDER BY "
		                                                     + ChannelEntry._ID + " DESC", null);
		cursorChannel.moveToFirst();
		if (cursorChannel.getCount() > 0) {
			return readChannelFromCursor(cursorChannel);
		}
		return null;
	}

	public final ArrayList<Channel> readAllChannelsFromDb() {
		@Cleanup
		final SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		final ArrayList<Channel> result = new ArrayList<>();

		@Cleanup
		final Cursor cursorChannel = sqLiteDatabase.rawQuery("SELECT *  FROM  "
		                                                     + ChannelEntry.TABLE_NAME, null);
		cursorChannel.moveToFirst();
		if (cursorChannel.getCount() > 0) {
			do {
				result.add(readChannelFromCursor(cursorChannel));
			} while (cursorChannel.moveToNext());
		}
		return result;
	}

	private ArrayList<ChannelItem> readChannelItemsFromDb(final long channelId) {
		@Cleanup
		final SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		final ArrayList<ChannelItem> channelItems = new ArrayList<>();
		@Cleanup
		final Cursor cursorChannelItem = sqLiteDatabase.rawQuery("SELECT *  FROM  "
                + ChannelItemEntry.TABLE_NAME + " WHERE "
                + ChannelEntry.CHANNEL_ID + "= '"
                + channelId + "' ORDER BY "
                + ChannelItemEntry._ID, null);
		cursorChannelItem.moveToFirst();
        while (!cursorChannelItem.isAfterLast()) {
			final ChannelItem channelItem = new ChannelItem();
			final String channelItemPub = cursorChannelItem.getString(cursorChannelItem
                    .getColumnIndex(ChannelItemEntry.CHANNEL_ITEM_PUB_DATE));
			channelItem.setTitle(cursorChannelItem.getString(cursorChannelItem
                    .getColumnIndex(ChannelItemEntry.CHANNEL_ITEM_TITLE)));
			channelItem.setSubtitle(cursorChannelItem.getString(cursorChannelItem
                    .getColumnIndex(ChannelItemEntry.CHANNEL_ITEM_SUBTITLE)));
			channelItem.setLink(cursorChannelItem.getString(cursorChannelItem
                    .getColumnIndex(ChannelItemEntry.CHANNEL_ITEM_LINK)));
			if (null != channelItemPub) {
				channelItem.setPubDate(new Date(channelItemPub));
			}
			channelItem.setRead(isLongBool(cursorChannelItem.getLong(cursorChannelItem
                    .getColumnIndex(ChannelItemEntry.CHANNEL_ITEM_ISREAD))));
			channelItems.add(channelItem);
            cursorChannelItem.moveToNext();
		}
		return channelItems;
	}

	public final void writeChannelToDb(@NonNull final Channel channel) {
		@Cleanup
		final SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		final ContentValues values = new ContentValues();

		values.put(ChannelEntry.CHANNEL_TITLE, channel.getTitle());
		values.put(ChannelEntry.CHANNEL_SUBTITLE, channel.getSubtitle());
		values.put(ChannelEntry.CHANNEL_LINK, channel.getLink());
		if (null != channel.getImage()) {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            channel.getImage().compress(Bitmap.CompressFormat.PNG, 0, outputStream);
			values.put(ChannelEntry.CHANNEL_IMAGE, outputStream.toByteArray());
		}
		try {
			values.put(ChannelEntry.CHANNEL_BUILD_DATE, channel.getLastBuildDate().toString());
		} catch (final NullPointerException e) {
            LogCatHandler.publishInfoRecord(e.getMessage());
		}
		values.put(ChannelEntry.CHANNEL_IS_READ, boolToLong(channel.isRead()));
		sqLiteDatabase.insert(ChannelEntry.TABLE_NAME, null, values);
		values.clear();

		@Cleanup
		final Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + ChannelEntry._ID + "  FROM  "
                + ChannelEntry.TABLE_NAME + " WHERE "
                + ChannelEntry.CHANNEL_LINK + "= '"
                + channel.getLink() + "' ORDER BY "
                + ChannelEntry._ID + " DESC", null);
		cursor.moveToFirst();
		final long id = cursor.getLong(cursor.getColumnIndex(ChannelEntry._ID));

        final ArrayList<ChannelItem> channelItems = channel.getChannelItems();
        for (int i = 0, size = channelItems.size(); i < size; i++) {
            final ChannelItem channelItem = channelItems.get(i);
            values.put(ChannelItemEntry.CHANNEL_ITEM_TITLE, channelItem.getTitle());
            values.put(ChannelItemEntry.CHANNEL_ITEM_SUBTITLE, channelItem.getSubtitle());
            values.put(ChannelItemEntry.CHANNEL_ITEM_LINK, channelItem.getLink());
            if (null != channelItem.getPubDate()) {
                values.put(ChannelItemEntry.CHANNEL_ITEM_PUB_DATE, channelItem.getPubDate()
                        .toString());
            }
            values.put(ChannelItemEntry.CHANNEL_ITEM_ISREAD, boolToLong(channelItem.isRead()));
            values.put(ChannelEntry.CHANNEL_ID, id);
            sqLiteDatabase.insert(ChannelItemEntry.TABLE_NAME, null, values);
            values.clear();
        }
	}

	public final void deleteChannelFromDb(@NonNull final Channel channel) {
		@Cleanup
		final SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		@Cleanup
		final Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + ChannelEntry._ID + "  FROM  "
                + ChannelEntry.TABLE_NAME + " WHERE "
                + ChannelEntry.CHANNEL_LINK + "= '"
                + channel.getLink() + "'", null);
		cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            final long id = cursor.getLong(cursor.getColumnIndex(ChannelEntry._ID));
            deleteChannelItems(id, sqLiteDatabase);
            sqLiteDatabase.delete(ChannelEntry.TABLE_NAME, ChannelEntry._ID + "= " + id, null);
            cursor.moveToNext();
        }
	}

	private void deleteChannelItems(final long channelId, final SQLiteDatabase sqLiteDatabase) {
        @Cleanup
        final Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + ChannelItemEntry._ID + "  FROM  "
                + ChannelItemEntry.TABLE_NAME + " WHERE "
                + ChannelEntry.CHANNEL_ID + "= '"
                + channelId + "'", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            final long id = cursor.getLong(cursor.getColumnIndex(ChannelItemEntry._ID));
            sqLiteDatabase.delete(ChannelItemEntry.TABLE_NAME, ChannelItemEntry._ID + "= " + id
                    , null);
            cursor.moveToNext();
        }
    }

	public final void updateValueFromDb(@NonNull final String tableName
            , @NonNull final String columnValue, @NonNull final String value
            , @NonNull final String whereColumn, @NonNull final String whereValue) {
		@Cleanup
		final SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		sqLiteDatabase.execSQL("UPDATE " + tableName + " SET " + columnValue + "=" + value
                + " WHERE " + whereColumn + "='" + whereValue + "'");
	}

	private Channel readChannelFromCursor(@NonNull final Cursor cursor) {
		final Channel channel = new Channel();
		channel.setTitle(cursor.getString(cursor.getColumnIndex(ChannelEntry.CHANNEL_TITLE)));
		channel.setSubtitle(cursor.getString(cursor.getColumnIndex(ChannelEntry.CHANNEL_SUBTITLE)));
		channel.setRead(isLongBool(cursor.getLong(cursor.getColumnIndex(ChannelEntry
                .CHANNEL_IS_READ))));
		try {
			channel.setLastBuildDate(new Date(cursor.getString(cursor.getColumnIndex(ChannelEntry
                    .CHANNEL_BUILD_DATE))));
		} catch (final IllegalArgumentException e) {
            LogCatHandler.publishInfoRecord(e.getMessage());
		}
		channel.setLink(cursor.getString(cursor.getColumnIndex(ChannelEntry.CHANNEL_LINK)));
        final byte[] imageCode = cursor.getBlob(cursor.getColumnIndex(ChannelEntry.CHANNEL_IMAGE));
        if (null != imageCode) {
            channel.setImage(BitmapFactory.decodeByteArray(imageCode, 0, imageCode.length));
        }

		final ArrayList<ChannelItem> channelItems =
				readChannelItemsFromDb(cursor.getLong(cursor.getColumnIndex(ChannelEntry._ID)));
		channel.setChannelItems(channelItems);

		return channel;
	}

	private boolean isLongBool(final long number) {
		return number == 1;
	}

	public long boolToLong(final boolean inputBool) {
		return inputBool ? 1 : 0;
	}

    public void refreshCurrentChannel(final Channel oldChannel, final Channel newChannel) {
        final ArrayList<ChannelItem> resultItems = new ArrayList<>();
        final ArrayList<ChannelItem> itemsFromNewChannel = newChannel.getChannelItems();
        for (int i = 0, size = itemsFromNewChannel.size(); i < size; i++) {
            final ChannelItem newItem = itemsFromNewChannel.get(i);
            ChannelItem match = null;
            final ArrayList<ChannelItem> itemsFromOldChannel = oldChannel.getChannelItems();
            for (int i1 = 0, size1 = itemsFromOldChannel.size(); i1 < size1; i1++) {
                final ChannelItem oldItem = itemsFromOldChannel.get(i1);
                if (oldItem.getLink().equals(newItem.getLink())) {
                    if (oldItem.isRead()) {
                        newItem.setRead(true);
                        match = newItem;
                    } else {
                        match = newItem;
                    }
                    break;
                }
            }
            if (null == match) {
                resultItems.add(newItem);
            } else {
                resultItems.add(match);
            }
        }
        newChannel.setChannelItems(resultItems);
        deleteChannelFromDb(oldChannel);
        writeChannelToDb(newChannel);
    }
}
