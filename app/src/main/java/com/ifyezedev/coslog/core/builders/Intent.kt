package com.ifyezedev.coslog.core.builders

import android.content.Intent

fun intent(init: Intent.()-> Unit) : Intent {
    val intent = Intent()
    intent.init()
    return intent
}