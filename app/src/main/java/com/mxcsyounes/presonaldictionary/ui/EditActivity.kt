package com.mxcsyounes.presonaldictionary.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.mxcsyounes.presonaldictionary.R
import com.mxcsyounes.presonaldictionary.database.entities.Word
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.content_edit.*
import java.util.*

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(editToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        when (intent.getIntExtra(KEY_ACTION, 0)) {
            ACTION_EDIT -> {
                val word = intent.getParcelableExtra<Word>(KEY_DATA)
                wordEditTv.setText(word.word)
                definitionEditTv.setText(word.definition)
                wordEditTv.isEnabled = false
                definitionEditTv.isEnabled = false
            }
            else -> finish()
        }
    }

    companion object {
        const val KEY_ACTION = "action"
        const val KEY_DATA = "data"
        const val ACTION_EDIT = 11
        const val ACTION_NEW = 12
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.doneMenuItem -> {
            if (validate()) {
                val word = wordEditTv.text.toString().trim()
                val definition = definitionEditTv.text.toString().trim()
                val intentBack = Intent()
                intentBack.putExtra(KEY_DATA, Word(null, Date(), word, definition))
                setResult(Activity.RESULT_OK, intentBack)
                finish()
            }
            true
        }

        R.id.editMenuItem -> {
            wordEditTv.isEnabled = true
            definitionEditTv.isEnabled = true

            true
        }

        R.id.deleteMenuItem -> {

            true
        }
        else -> false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        when (intent.getIntExtra(KEY_ACTION, 0)) {
            ACTION_EDIT -> menuInflater.inflate(R.menu.menu_edit_detail, menu)
            ACTION_NEW -> {
                menuInflater.inflate(R.menu.menu_edit_new, menu)
            }
            else -> finish()
        }
        return true
    }

    private fun validate(): Boolean {
        val word = wordEditTv.text.toString().trim()
        val definition = definitionEditTv.text.toString().trim()
        if (word.isEmpty()) {
            Snackbar.make(editCoordinator, getString(R.string.word_empty_text_message), Snackbar.LENGTH_LONG).show()
            return false
        }

        if (definition.isEmpty()) {
            Snackbar.make(editCoordinator, getString(R.string.definition_empty_text_message), Snackbar.LENGTH_LONG).show()
            return false
        }

        return true
    }

}
