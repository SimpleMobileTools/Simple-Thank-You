package com.simplemobiletools.thankyou.dbhelpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.simplemobiletools.commons.R
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_ACCENT_COLOR
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_APP_ICON_COLOR
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_BACKGROUND_COLOR
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_ID
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_LAST_UPDATED_TS
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_PRIMARY_COLOR
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.COL_TEXT_COLOR
import com.simplemobiletools.commons.helpers.MyContentProvider.Companion.fillThemeContentValues
import com.simplemobiletools.commons.models.SharedTheme
import com.simplemobiletools.thankyou.extensions.config

class MyContentProviderDbHelper private constructor(private val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val mDb = writableDatabase

    companion object {
        private const val DB_NAME = "Commons.db"
        private const val DB_VERSION = 4
        private const val TABLE_NAME = "commons_colors"
        private const val THEME_ID = 1    // for now we are storing just 1 theme

        fun newInstance(context: Context) = MyContentProviderDbHelper(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_TEXT_COLOR INTEGER DEFAULT 0, $COL_BACKGROUND_COLOR INTEGER DEFAULT 0," +
                " $COL_PRIMARY_COLOR INTEGER DEFAULT 0, $COL_APP_ICON_COLOR INTEGER DEFAULT 0, $COL_LAST_UPDATED_TS INTEGER DEFAULT 0, $COL_ACCENT_COLOR INTEGER DEFAULT 0)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COL_APP_ICON_COLOR INTEGER DEFAULT 0")
        }

        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COL_ACCENT_COLOR INTEGER DEFAULT 0")
        }
    }

    private fun insertDefaultTheme() {
        val resources = context.resources
        val theme = SharedTheme(
            resources.getColor(R.color.theme_dark_text_color), resources.getColor(R.color.theme_dark_background_color),
            resources.getColor(R.color.color_primary), resources.getColor(R.color.color_primary), 0, resources.getColor(R.color.color_primary)
        )
        insertTheme(theme, mDb)
    }

    private fun insertTheme(sharedTheme: SharedTheme, db: SQLiteDatabase) {
        val values = fillThemeContentValues(sharedTheme)
        db.insert(TABLE_NAME, null, values)
    }

    fun updateTheme(values: ContentValues): Int {
        if (!isThemeAvailable()) {
            insertDefaultTheme()
        }

        val selection = "$COL_ID = ?"
        val selectionArgs = arrayOf(THEME_ID.toString())
        return mDb.update(TABLE_NAME, values, selection, selectionArgs)
    }

    private fun isThemeAvailable(): Boolean {
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
        if (!context.config.shouldUseSharedTheme) {
            return null
        }

        val cols = arrayOf(
            COL_TEXT_COLOR,
            COL_BACKGROUND_COLOR,
            COL_PRIMARY_COLOR,
            COL_APP_ICON_COLOR,
            COL_LAST_UPDATED_TS,
            COL_ACCENT_COLOR
        )

        val selection = "$COL_ID = ?"
        val selectionArgs = arrayOf(THEME_ID.toString())
        return mDb.query(TABLE_NAME, cols, selection, selectionArgs, null, null, null)
    }
}
