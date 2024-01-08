package com.example.automaticpetfeeding.control

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.automaticpetfeeding.model.FeedingTime
import com.example.automaticpetfeeding.model.FeedingTimeDatabaseHelper

class FeedingTimeController(private val context: Context) {

    private val dbHelper: FeedingTimeDatabaseHelper = FeedingTimeDatabaseHelper(context)

    fun addFeedingTime(hour: Int, minute: Int): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(FeedingTimeDatabaseHelper.COLUMN_HOUR, hour)
            put(FeedingTimeDatabaseHelper.COLUMN_MINUTE, minute)
        }

        return db.insert(FeedingTimeDatabaseHelper.TABLE_NAME, null, values)
    }

    fun getAllFeedingTimes(): List<FeedingTime> {
        val db = dbHelper.readableDatabase
        val columns = arrayOf(
            FeedingTimeDatabaseHelper.COLUMN_ID,
            FeedingTimeDatabaseHelper.COLUMN_HOUR,
            FeedingTimeDatabaseHelper.COLUMN_MINUTE
        )

        val cursor: Cursor = db.query(FeedingTimeDatabaseHelper.TABLE_NAME,columns,null,null,null,null,null)

        val feedingTimes = mutableListOf<FeedingTime>()

        with(cursor) {
            while (moveToNext()) {
                val idColumnIndex = getColumnIndex(FeedingTimeDatabaseHelper.COLUMN_ID)
                val hourColumnIndex = getColumnIndex(FeedingTimeDatabaseHelper.COLUMN_HOUR)
                val minuteColumnIndex = getColumnIndex(FeedingTimeDatabaseHelper.COLUMN_MINUTE)
                val id = if (idColumnIndex != -1) getLong(idColumnIndex) else -1
                val hour = if (hourColumnIndex != -1) getInt(hourColumnIndex) else -1
                val minute = if (minuteColumnIndex != -1) getInt(minuteColumnIndex) else -1

                feedingTimes.add(FeedingTime(id, hour, minute))
            }
        }

        return feedingTimes
    }

    fun deleteAllData() {
        val db = dbHelper.writableDatabase

        db.delete(FeedingTimeDatabaseHelper.TABLE_NAME, null, null)
        db.close()
    }

    fun deleteFeedingTime(selectedId: Long): List<FeedingTime> {
        val db = dbHelper.writableDatabase

        val selection = "${FeedingTimeDatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(selectedId.toString())

        db.delete(FeedingTimeDatabaseHelper.TABLE_NAME, selection, selectionArgs)
        db.close()
        // Obtenha a lista de IDs atualizada após a exclusão
        return getAllFeedingTimes()
    }

    fun getFeedingTimesAsStringList(): List<String> {
        val feedingTimes = getAllFeedingTimes()
        val feedingTimesStringList = mutableListOf<String>()

        for (feedingTime in feedingTimes) {
            feedingTimesStringList.add("${feedingTime.hour}:${feedingTime.minute}")
        }

        return feedingTimesStringList
    }
}
