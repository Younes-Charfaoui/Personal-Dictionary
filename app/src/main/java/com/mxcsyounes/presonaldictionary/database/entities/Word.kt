package com.mxcsyounes.presonaldictionary.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.os.Parcel
import android.os.Parcelable
import com.mxcsyounes.presonaldictionary.database.converters.DateConverter
import java.util.*

@Entity(tableName = "words")
data class Word(@PrimaryKey(autoGenerate = false) var id: Int?,
                @TypeConverters(DateConverter::class) var date: Date?,
                @ColumnInfo(index = true) var word: String,
                var definition: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            Date(parcel.readLong()),
            parcel.readString(),
            parcel.readString())

    constructor() : this(null, null, "", "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(word)
        parcel.writeString(definition)
        parcel.writeLong(date!!.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Word> {
        override fun createFromParcel(parcel: Parcel): Word {
            return Word(parcel)
        }

        override fun newArray(size: Int): Array<Word?> {
            return arrayOfNulls(size)
        }
    }
}