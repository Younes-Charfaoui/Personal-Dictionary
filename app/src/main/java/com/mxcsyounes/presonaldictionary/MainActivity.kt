package com.mxcsyounes.presonaldictionary

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.mxcsyounes.presonaldictionary.database.entities.Word
import com.mxcsyounes.presonaldictionary.viewModel.WordViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mWordViewModel: WordViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)

        mWordViewModel?.words?.observe(this, android.arch.lifecycle.Observer {
        })

        fab.setOnClickListener {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings ->  return true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
