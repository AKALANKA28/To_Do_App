// DataBaseHelper.kt
package com.example.todo.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todo.model.ToDoModel

class DataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 8 ), ToDoDao {

    companion object {
        private const val DATABASE_NAME = "TODO_DATABASE"
        private const val TABLE_NAME = "TODO_TABLE"
        private const val COL_1 = "ID"
        private const val COL_2 = "TASK"
        private const val COL_3 = "STATUS"
        private const val COL_4 = "DESCRIPTION"
        private const val COL_5 = "TIME"
        private const val COL_6 = "PRIORITY"


    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATUS INTEGER, DESCRIPTION TEXT, TIME TEXT, PRIORITY TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    override fun insertTask(model: ToDoModel) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_2, model.task)
        values.put(COL_3, 0)
        values.put(COL_4, model.description)
        values.put(COL_5, model.time)
        values.put(COL_6, model.priority)


        db.insert(TABLE_NAME, null, values)
    }

    override fun updateTask(id: Int, task: String, description: String, time: String, priority: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_2, task)
        values.put(COL_4, description)
        values.put(COL_5, time)
        values.put(COL_6, priority)

        db.update(TABLE_NAME, values, "ID=?", arrayOf(id.toString()))
    }

    override fun updateStatus(id: Int, status: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_3, status)
        db.update(TABLE_NAME, values, "ID=?", arrayOf(id.toString()))
    }

    override fun deleteTask(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "ID=?", arrayOf(id.toString()))
    }

    @SuppressLint("Range")
    override fun getAllTasks(): List<ToDoModel> {
        val db = this.writableDatabase
        val modelList = ArrayList<ToDoModel>()
        var cursor: Cursor? = null

        db.beginTransaction()
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val task = ToDoModel()
                        task.id = cursor.getInt(cursor.getColumnIndex(COL_1))
                        task.task = cursor.getString(cursor.getColumnIndex(COL_2))
                        task.status = cursor.getInt(cursor.getColumnIndex(COL_3))
                        task.description = cursor.getString(cursor.getColumnIndex(COL_4))
                        task.time = cursor.getString(cursor.getColumnIndex(COL_5))
                        task.priority = cursor.getString(cursor.getColumnIndex(COL_6))

                        modelList.add(task)
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList
    }
}
