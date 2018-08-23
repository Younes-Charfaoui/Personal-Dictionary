package com.mxcsyounes.presonaldictionary.database.entities.converters

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun toLong(date: Date) = date.time

    @TypeConverter
    fun toDate(time: Long) = Date(time)
}