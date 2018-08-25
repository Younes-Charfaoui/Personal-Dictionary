package com.mxcsyounes.presonaldictionary.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.mxcsyounes.presonaldictionary.R
import com.mxcsyounes.presonaldictionary.database.entities.Word
import com.mxcsyounes.presonaldictionary.ui.EditActivity.Companion.ACTION_EDIT
import com.mxcsyounes.presonaldictionary.ui.EditActivity.Companion.ACTION_NEW
import com.mxcsyounes.presonaldictionary.ui.EditActivity.Companion.DELETE
import com.mxcsyounes.presonaldictionary.ui.EditActivity.Companion.KEY_ACTION
import com.mxcsyounes.presonaldictionary.ui.EditActivity.Companion.KEY_DATA
import com.mxcsyounes.presonaldictionary.ui.EditActivity.Companion.UPDATE
import com.mxcsyounes.presonaldictionary.viewModel.WordViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), WordAdapter.OnWordItemsClickListener {

    private var mWordViewModel: WordViewModel? = null

    companion object {
        const val REQUEST_NEW_WORD = 1224
        const val REQUEST_DETAIL_WORD = 1225
        const val TAG = "MainActivity"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        wordRecycler.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        val adapter = WordAdapter(this, this)
        wordRecycler.adapter = adapter

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)

        mWordViewModel?.words?.observe(this, android.arch.lifecycle.Observer {
            if (it?.size == 0) {
                emptyLayout.visibility = View.VISIBLE
                wordRecycler.visibility = View.GONE
            } else {
                emptyLayout.visibility = View.GONE
                wordRecycler.visibility = View.VISIBLE
                adapter.swapWordList(it)
            }
        })

        addWordFab.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra(KEY_ACTION, ACTION_NEW)
            startActivityForResult(intent, REQUEST_NEW_WORD)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> return true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onWordItemClickListener(word: Word) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra(KEY_ACTION, ACTION_EDIT)
        intent.putExtra(KEY_DATA, word)
        startActivityForResult(intent, REQUEST_DETAIL_WORD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            REQUEST_NEW_WORD -> {
                if (resultCode == Activity.RESULT_OK) {
                    val word = data?.getParcelableExtra<Word>(KEY_DATA)
                    Log.d(TAG, "the word is ${word?.toString()}")
                    mWordViewModel?.insertWord(word!!)
                }
            }
            REQUEST_DETAIL_WORD -> {
                if (resultCode == Activity.RESULT_OK) {
                    val word = data?.getParcelableExtra<Word>(KEY_DATA)
                    when (data?.getIntExtra(KEY_ACTION, 0)) {
                        UPDATE -> {
                            mWordViewModel?.updateWord(word!!)
                        }
                        DELETE -> {
                            mWordViewModel?.deleteWord(word!!)
                        }
                    }

                }
            }

        }
    }
}
