package com.mxcsyounes.presonaldictionary.ui

import android.app.Activity
import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.mxcsyounes.presonaldictionary.R
import com.mxcsyounes.presonaldictionary.database.entities.Word
import com.mxcsyounes.presonaldictionary.database.repo.WORDS_ALPHA_ASC
import com.mxcsyounes.presonaldictionary.database.repo.WORDS_DESC
import com.mxcsyounes.presonaldictionary.ui.EditActivity.Companion.ACTION_DETAIL
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
    private var adapter: WordAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        wordRecycler.layoutManager = LinearLayoutManager(this, VERTICAL, false)

        adapter = WordAdapter(this, this)
        wordRecycler.adapter = adapter

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)

        changeObserver()

        addWordFab.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra(KEY_ACTION, ACTION_NEW)
            startActivityForResult(intent, REQUEST_NEW_WORD)
        }
    }

    private var searchView: SearchView? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView?.maxWidth = Int.MAX_VALUE


        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_alphabet -> {
            mWordViewModel?.getWordsWith(WORDS_ALPHA_ASC)
            changeObserver()
            true
        }

        R.id.action_recent -> {
            mWordViewModel?.getWordsWith(WORDS_DESC)
            changeObserver()
            true
        }

        R.id.action_search -> true

        else -> super.onOptionsItemSelected(item)

    }

    override fun onWordItemClickListener(word: Word) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra(KEY_ACTION, ACTION_DETAIL)
        intent.putExtra(KEY_DATA, word)
        if (searchView?.query?.isNotEmpty()!!)
            mWordViewModel?.currentSearch = searchView?.query.toString()
        startActivityForResult(intent, REQUEST_DETAIL_WORD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            REQUEST_NEW_WORD -> {
                if (resultCode == Activity.RESULT_OK) {
                    val word = data?.getParcelableExtra<Word>(KEY_DATA)
                    mWordViewModel?.insertWord(word!!)
                    searchView?.setQuery("", false)
                    searchView?.isIconified = true
                }
            }

            REQUEST_DETAIL_WORD -> {
                if (resultCode == Activity.RESULT_OK) {
                    searchView?.setQuery("", false)
                    searchView?.isIconified = true
                    val word = data?.getParcelableExtra<Word>(KEY_DATA)
                    when (data?.getIntExtra(KEY_ACTION, 0)) {
                        DELETE -> mWordViewModel?.deleteWord(word!!)
                        UPDATE -> mWordViewModel?.updateWord(word!!)
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_NEW_WORD = 1224
        const val REQUEST_DETAIL_WORD = 1225
        const val TAG = "MainActivity"
    }

    private fun changeObserver() {
        mWordViewModel?.words?.observe(this, android.arch.lifecycle.Observer {
            if (it?.size == 0) {
                emptyLayout.visibility = View.VISIBLE
                wordRecycler.visibility = View.GONE
            } else {
                emptyLayout.visibility = View.GONE
                wordRecycler.visibility = View.VISIBLE
                adapter?.swapWordList(it)
            }
        })
    }

    override fun onBackPressed() {
        if (!searchView?.isIconified!!) {
            searchView?.isIconified = true
        }
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        searchView?.setQuery(searchView?.query, true)

    }
}

