package com.bangkit.nest.utils

import android.content.Context

fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}