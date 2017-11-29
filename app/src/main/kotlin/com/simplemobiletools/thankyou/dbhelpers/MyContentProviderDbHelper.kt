package com.simplemobiletools.thankyou.dbhelpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.simplemobiletools.commons.R
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_BACKGROUND_COLOR
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_ID
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_LAST_UPDATED_TS
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_PRIMARY_COLOR
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_TEXT_COLOR
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.fillThemeContentValues
import com.simplemobiletools.commons.models.SharedTheme

class MyContentProviderDbHelper private constructor(private val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val mDb = writableDatabase

    companion object {
        private val DB_NAME = "Commons.db"
        private val DB_VERSION = 1
        private val TABLE_NAME = "commons_colors"
        private val THEME_ID = 1    // for now we are storing just 1 theme

        fun newInstance(context: Context) = MyContentProviderDbHelper(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_TEXT_COLOR INTEGER DEFAULT 0, $COL_BACKGROUND_COLOR INTEGER DEFAULT 0," +
                " $COL_PRIMARY_COLOR INTEGER DEFAULT 0, $COL_LAST_UPDATED_TS INTEGER DEFAULT 0)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    private fun insertDefaultTheme() {
        val resources = context.resources
        val theme = SharedTheme(resources.getColor(R.color.theme_dark_text_color), resources.getColor(R.color.theme_dark_background_color),
                resources.getColor(R.color.color_primary))
        insertTheme(theme, mDb)
    }

    private fun insertTheme(sharedTheme: SharedTheme, db: SQLiteDatabase) {
        val values = fillThemeContentValues(sharedTheme)
        db.insert(TABLE_NAME, null, values)
    }

    fun updateTheme(values: ContentValues): Int {
        val selection = "$COL_ID = ?"
        val selectionArgs = arrayOf(THEME_ID.toString())
        return mDb.update(TABLE_NAME, values, selection, selectionArgs)
    }

    fun isThemeAvailable(): Boolean {
        val cols = arrayOf(COL_ID)
        val selection = "$COL_ID = ?"
        val selectionArgs = arrayOf(THEME_ID.toString())
        var cursor: Cursor? = null
        try {
            cursor = mDb.query(TABLE_NAME, cols, selection, selectionArgs, null, null, null)
            return cursor.moveToFirst()
        } finally {
            cursor?.close()
        }
    }

    fun getSharedTheme(): Cursor? {
        if (!isThemeAvailable()) {
            insertDefaultTheme()
        }

        val cols = arrayOf(COL_TEXT_COLOR, COL_BACKGROUND_COLOR, COL_PRIMARY_COLOR, COL_LAST_UPDATED_TS)
        val selection = "$COL_ID = ?"
        val selectionArgs = arrayOf(THEME_ID.toString())
        return mDb.query(TABLE_NAME, cols, selection, selectionArgs, null, null, null)
    }
}
