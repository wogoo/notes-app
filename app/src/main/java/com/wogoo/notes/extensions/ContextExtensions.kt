package com.wogoo.notes.extensions

import android.content.Context
import android.content.Intent

fun Context.goTo(
    clazz: Class<*>,
    intent: Intent.() -> Unit = {}
) {
    Intent(this, clazz).apply {
        intent()
        startActivity(this)
    }
}