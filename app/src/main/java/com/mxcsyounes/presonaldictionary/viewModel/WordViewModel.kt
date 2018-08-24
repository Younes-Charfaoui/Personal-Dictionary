package com.mxcsyounes.presonaldictionary.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.mxcsyounes.presonaldictionary.database.entities.Word
import com.mxcsyounes.presonaldictionary.database.repo.WordRepository
import com.mxcsyounes.presonaldictionary.database.repo.WordsListConstant

class WordViewModel(application: Application) : AndroidViewModel(application) {

    val words: LiveData<MutableList<Word>>
    private val wordRepository = WordRepository(application)

    init {
        words = wordRepository.allWords
    }

    fun insertWord(word: Word) = wordRepository.insertWord(word)

    fun deleteWord(word: Word) = wordRepository.deleteWord(word)

    fun updateWord(word: Word) = wordRepository.updateWord(word)

    fun deleteAll() = wordRepository.deleteAllWord()

    fun getWordsWith(@WordsListConstant condition: Int) = wordRepository.getWordsWith(condition)
}