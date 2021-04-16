package com.ifyezedev.coslog

import java.text.SimpleDateFormat
import java.util.*

//convert string date to long
fun Long.toFormattedDate(simpleDateFormat: SimpleDateFormat) : String {
    simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = Date(this)
    return simpleDateFormat.format(date)
}
