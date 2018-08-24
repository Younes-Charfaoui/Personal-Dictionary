package com.mxcsyounes.presonaldictionary.database.converters

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun toLong(date: Date?) = date?.time

    @TypeConverter
    fun toDate(time: Long?) = if (time == null) null else Date(time)
}