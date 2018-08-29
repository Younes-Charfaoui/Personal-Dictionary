package com.mxcsyounes.presonaldictionary.database.repo

import android.app.Application
import android.arch.lifecycle.LiveData
import android.support.annotation.IntDef
import com.mxcsyounes.presonaldictionary.database.WordDatabase
import com.mxcsyounes.presonaldictionary.database.dao.WordDao
import com.mxcsyounes.presonaldictionary.database.entities.Word
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

const val WORDS_ASC = 1
const val WORDS_DESC = 2
const val WORDS_ALPHA_ASC = 3
const val WORDS_ALPHA_DESC = 4

@IntDef(WORDS_ASC, WORDS_DESC, WORDS_ALPHA_ASC, WORDS_ALPHA_DESC)
@Retention(AnnotationRetention.SOURCE)
annotation class WordsListConstant

class WordRepository(application: Application) {

    private val wordDao: WordDao? = WordDatabase.getInstance(application, false)?.wordDao()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    val allWords: LiveData<MutableList<Word>>

    init {
        allWords = wordDao?.getWordsASC()!!
    }

    fun insertWord(word: Word) = executor.execute({ wordDao?.insertWord(word) })

    fun updateWord(word: Word) = executor.execute({ wordDao?.updateWord(word) })

    fun deleteWord(word: Word) = executor.execute({ wordDao?.deleteWord(word) })

    fun getWordsWith(@WordsListConstant condition: Int) : LiveData<MutableList<Word>>? =
        when (condition) {
            WORDS_ASC -> wordDao?.getWordsASC()
            WORDS_DESC -> wordDao?.getWordsDESC()
            WORDS_ALPHA_ASC -> wordDao?.getWordsAlphabeticallyASC()
            WORDS_ALPHA_DESC -> wordDao?.getWordsAlphabeticallyDESC()
            else -> wordDao?.getWordsASC()
        }
    }



