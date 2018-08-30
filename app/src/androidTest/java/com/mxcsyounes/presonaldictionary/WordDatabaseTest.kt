package com.mxcsyounes.presonaldictionary

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.mxcsyounes.presonaldictionary.database.WordDatabase
import com.mxcsyounes.presonaldictionary.database.dao.WordDao
import com.mxcsyounes.presonaldictionary.database.entities.Word
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
class WordDatabaseTest {


    private var database: WordDatabase? = null
    private var dao: WordDao? = null
    private var responseLatch: CountDownLatch? = null
    private var wordList: MutableList<Word>? = null

    @Before
    fun setup() {
        database = WordDatabase.getInstance(InstrumentationRegistry.getTargetContext(), true)
        dao = database?.wordDao()
        responseLatch = CountDownLatch(2)
    }

    @Test
    fun testInsert() {

        dao?.getWordsASC()?.observeForever {
            wordList = it
            responseLatch?.countDown()
        }

        responseLatch?.await()

        assertEquals("initial Size should be 0", 0, wordList?.size)

        val word = Word()
        word.word = "hello"
        word.definition = "is a hello type"
        word.date = Date()
        word.paths = ""

        dao?.insertWord(word)


        dao?.getWordsASC()?.observeForever {
            wordList = it
            responseLatch?.countDown()
        }

        responseLatch?.await()

        assertEquals("Size should be 1 after inserting a word", 0, wordList?.size)
    }

    @After
    fun close() {
        database?.close()
    }
}