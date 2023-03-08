package com.ladecentro.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreference @Inject constructor(@ApplicationContext context: Context) {

    private val preference =
        context.getSharedPreferences(Constants.PreferenceTag.name, Context.MODE_PRIVATE)

    fun getStoredTag(key: String): String {
        return preference.getString(key, "")!!
    }

    fun setStoredTag(key: String, query: String) {
        preference.edit().putString(key, query).apply()
    }

    fun removeStoresTag(key: String) {
        preference.edit().remove(key).apply()
    }
}