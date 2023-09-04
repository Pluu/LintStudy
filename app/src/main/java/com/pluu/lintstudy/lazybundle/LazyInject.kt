package com.pluu.lintstudy.lazybundle

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

inline fun <F, reified T> argumentDelegate(
    crossinline provideArguments: (F) -> Bundle?
): LazyProvider<F, T> =
    object : LazyProvider<F, T> {

        override fun provideDelegate(thisRef: F, prop: KProperty<*>) =
            lazy {
                val bundle = provideArguments(thisRef)
                bundle?.get(prop.name) as T
            }
    }

interface LazyProvider<A, T> {
    operator fun provideDelegate(thisRef: A, prop: KProperty<*>): Lazy<T>
}

inline fun <reified T> Activity.inject(): LazyProvider<Activity, T> {
    return argumentDelegate { it.intent?.extras }
}

inline fun <reified T> Fragment.inject(): LazyProvider<Fragment, T> {
    return argumentDelegate { it.arguments }
}