package com.mxcsyounes.presonaldictionary.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.mxcsyounes.presonaldictionary.database.converters.DateConverter
import com.mxcsyounes.presonaldictionary.database.dao.WordDao
import com.mxcsyounes.presonaldictionary.database.entities.Word

@Database(entities = [Word::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class WordDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "words.db"

        private var sInstance: WordDatabase? = null

        fun getInstance(context: Context, inMemory: Boolean): WordDatabase? {
            if (sInstance == null)
                if (!inMemory) {
                    sInstance = Room.databaseBuilder(context,
                            WordDatabase::class.java,
                            DB_NAME).build()
                } else {
                    sInstance = Room.inMemoryDatabaseBuilder(context, WordDatabase::class.java)
                            .build()
                }
            return sInstance
        }
    }

    abstract fun wordDao() : WordDao

}