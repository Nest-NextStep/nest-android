package com.bangkit.nest.ui.main.task

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateTypeAdapter : JsonDeserializer<Date>, JsonSerializer<Date> {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
        return dateFormat.parse(json!!.asString)
    }

    override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(dateFormat.format(src))
    }
}

class LocalTimeTypeAdapter : JsonDeserializer<LocalTime>, JsonSerializer<LocalTime> {
    @RequiresApi(Build.VERSION_CODES.O)
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalTime {
        return LocalTime.parse(json!!.asString, timeFormatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serialize(src: LocalTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src!!.format(timeFormatter))
    }
}

class BooleanTypeAdapter : JsonDeserializer<Boolean>, JsonSerializer<Boolean> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Boolean {
        return when {
            json!!.isJsonPrimitive -> {
                val primitive = json.asJsonPrimitive
                if (primitive.isBoolean) {
                    primitive.asBoolean
                } else if (primitive.isNumber) {
                    primitive.asInt != 0
                } else {
                    throw JsonParseException("Expected a boolean or an integer but was ${json.asString}")
                }
            }
            else -> throw JsonParseException("Expected a boolean or an integer but was $json")
        }
    }

    override fun serialize(src: Boolean?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(if (src == true) 1 else 0)
    }
}

