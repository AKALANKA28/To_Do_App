package com.example.todo.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "TODO_DATABASE"
        private const val DATABASE_VERSION = 8
        const val TABLE_NAME = "TODO_TABLE"
        const val COL_1 = "ID"
        const val COL_2 = "TASK"
        const val COL_3 = "STATUS"
        const val COL_4 = "DESCRIPTION"
        const val COL_5 = "TIME"
        const val COL_6 = "PRIORITY"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COL_1 INTEGER PRIMARY KEY AUTOINCREMENT, $COL_2 TEXT, $COL_3 INTEGER, $COL_4 TEXT, $COL_5 TEXT, $COL_6 TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
