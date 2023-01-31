package com.hanas.hnsnavigation

import android.os.Bundle
import android.os.Parcelable
import android.os.Parcelable.ClassLoaderCreator
import android.util.Log
import androidx.navigation.NavType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KClass

class HnsNavType<T : Parcelable>(
    private val clazz: KClass<T>
) : NavType<T>(false) {
    override val name: String = clazz.simpleName.orEmpty()
    override fun get(bundle: Bundle, key: String): T? = bundle.getParcelable(key)

    override fun parseValue(value: String): T {
        Log.d("HANAS", value)
        return Gson().fromJson(value, TypeToken.getParameterized(clazz.java).type)
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }
}
