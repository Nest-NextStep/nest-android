package com.bangkit.nest.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}

fun dpToPixels(dp: Float, context: Context): Float {
    val density = context.resources.displayMetrics.density
    return dp * density
}

fun convertDate(dateString: String, toJson: Boolean): String {
    val jsonFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val idFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    if (toJson) {
        val dateResult = idFormat.parse(dateString)
        return jsonFormat.format(dateResult!!)
    } else {
        val dateResult = jsonFormat.parse(dateString)
        return idFormat.format(dateResult!!)
    }
}

fun calculateAge(birthdate: String): Int {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val birthDate = dateFormat.parse(birthdate)

    // Get the current date
    val currentDate = Calendar.getInstance()

    // Get birth date as Calendar
    val birthDateCalendar = Calendar.getInstance()
    birthDateCalendar.time = birthDate!!

    // Calculate age
    var age = currentDate.get(Calendar.YEAR) - birthDateCalendar.get(Calendar.YEAR)

    // Adjust age if current date is before the birth date in the current year
    if (currentDate.get(Calendar.DAY_OF_YEAR) < birthDateCalendar.get(Calendar.DAY_OF_YEAR)) {
        age--
    }

    return age
}