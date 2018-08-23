package com.mxcsyounes.presonaldictionary.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.mxcsyounes.presonaldictionary.database.entities.converters.DateConverter
import java.util.*

@Entity(tableName = "words")
data class Word(@PrimaryKey(autoGenerate = false) var id: Int,
                @TypeConverters(DateConverter::class) var date: Date?,
                @ColumnInfo(index = true) var word: String,
                var definition: String) {

    constructor() : this(0, null, "", "")
}