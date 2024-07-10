package com.example.demo.util

import android.content.Context
import android.content.SharedPreferences

object ShareTool {

    private const val PREFERENCES_FILE_NAME = "app_prefs"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun saveString(context: Context, key: String, value: String) {
        val prefs = getPreferences(context)
        with(prefs.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(context: Context, key: String, defaultValue: String? = null): String? {
        val prefs = getPreferences(context)
        return prefs.getString(key, defaultValue)
    }

    fun saveInt(context: Context, key: String, value: Int) {
        val prefs = getPreferences(context)
        with(prefs.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun getInt(context: Context, key: String, defaultValue: Int = 0): Int {
        val prefs = getPreferences(context)
        return prefs.getInt(key, defaultValue)
    }

    fun saveBoolean(context: Context, key: String, value: Boolean) {
        val prefs = getPreferences(context)
        with(prefs.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean = false): Boolean {
        val prefs = getPreferences(context)
        return prefs.getBoolean(key, defaultValue)
    }

    fun saveFloat(context: Context, key: String, value: Float) {
        val prefs = getPreferences(context)
        with(prefs.edit()) {
            putFloat(key, value)
            apply()
        }
    }

    fun getFloat(context: Context, key: String, defaultValue: Float = 0f): Float {
        val prefs = getPreferences(context)
        return prefs.getFloat(key, defaultValue)
    }

    fun saveLong(context: Context, key: String, value: Long) {
        val prefs = getPreferences(context)
        with(prefs.edit()) {
            putLong(key, value)
            apply()
        }
    }

    fun getLong(context: Context, key: String, defaultValue: Long = 0L): Long {
        val prefs = getPreferences(context)
        return prefs.getLong(key, defaultValue)
    }

    fun remove(context: Context, key: String) {
        val prefs = getPreferences(context)
        with(prefs.edit()) {
            remove(key)
            apply()
        }
    }

    fun clear(context: Context) {
        val prefs = getPreferences(context)
        with(prefs.edit()) {
            clear()
            apply()
        }
    }
}