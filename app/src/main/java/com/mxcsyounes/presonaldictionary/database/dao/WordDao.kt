package com.mxcsyounes.presonaldictionary.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.mxcsyounes.presonaldictionary.database.entities.Word

@Dao
interface WordDao {

    @Insert
    fun insertWord(word: Word)

    @Delete
    fun deleteWord(word: Word)

    @Update
    fun updateWord(word: Word)

    @Query("SELECT * FROM words ORDER BY id ASC")
    fun getWordsASC(): LiveData<MutableList<Word>>

    @Query("SELECT * FROM words ORDER BY id DESC")
    fun getWordsDESC(): LiveData<MutableList<Word>>

    @Query("SELECT * FROM words ORDER BY word ASC")
    fun getWordsAlphabeticallyASC(): LiveData<MutableList<Word>>

    @Query("SELECT * FROM words ORDER BY word DESC")
    fun getWordsAlphabeticallyDESC(): LiveData<MutableList<Word>>

    @Query("DELETE FROM words")
    fun deleteAll() : Int
}