package com.mxcsyounes.presonaldictionary.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.mxcsyounes.presonaldictionary.R
import com.mxcsyounes.presonaldictionary.database.entities.Word
import com.mxcsyounes.presonaldictionary.ui.EditActivity.Companion.ACTION_EDIT
import com.mxcsyounes.presonaldictionary.ui.EditActivity.Companion.ACTION_NEW
import com.mxcsyounes.presonaldictionary.ui.EditActivity.Companion.KEY_ACTION
import com.mxcsyounes.presonaldictionary.ui.EditActivity.Companion.KEY_DATA
import com.mxcsyounes.presonaldictionary.viewModel.WordViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), WordAdapter.OnWordItemsClickListener {


    private var mWordViewModel: WordViewModel? = null

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
            startActivity(intent)
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
        startActivity(intent)
    }
}
