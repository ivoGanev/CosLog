package com.ifyezedev.coslog.core.builders

import android.content.Intent

fun buildIntent(init: Intent.()-> Unit) : Intent {
    val intent = Intent()
    intent.init()
    return intent
}

fun buildIntent(action: String, init: Intent.()-> Unit) : Intent {
    val intent = Intent(action)
    intent.init()
    return intent
}