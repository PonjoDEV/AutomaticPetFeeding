// FeedingTimeDatabaseHelper.kt
package com.example.automaticpetfeeding.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FeedingTimeDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "feeding_times.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "feeding_times"
        const val COLUMN_ID = "_id"
        const val COLUMN_HOUR = "hour"
        const val COLUMN_MINUTE = "minute"

        const val CREATE_TABLE ="CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_HOUR INTEGER, $COLUMN_MINUTE INTEGER);"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO
    }
}
