package com.mxcsyounes.presonaldictionary.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.mxcsyounes.presonaldictionary.R
import com.mxcsyounes.presonaldictionary.database.entities.Word
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.content_edit.*
import java.util.*

class EditActivity : AppCompatActivity() {

    private var stateEdit = true

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
            ACTION_NEW -> {

            }
            else -> finish()
        }
    }

    companion object {
        const val KEY_ACTION = "action"
        const val KEY_DATA = "data"
        const val ACTION_EDIT = 11
        const val ACTION_NEW = 12
        const val DELETE = 114
        const val TAG = "EditActivity"
        const val UPDATE = 112
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.doneMenuItem -> {
            if (validate()) {
                val word = wordEditTv.text.toString().trim()
                val definition = definitionEditTv.text.toString().trim()
                val intentBack = Intent()
                val wordObject = Word(null, Date(), word, definition)
                Log.d(TAG, "the word is $wordObject")
                intentBack.putExtra(KEY_DATA, wordObject)
                setResult(Activity.RESULT_OK, intentBack)
                finish()
            }
            true
        }

        R.id.editMenuItem -> {
            if (stateEdit) {
                item.icon = ContextCompat.getDrawable(this, R.drawable.ic_done_white_24dp)
                stateEdit = false
                wordEditTv.isEnabled = true
                definitionEditTv.isEnabled = true
            } else {
                if (validate()) {
                    AlertDialog.Builder(this)
                            .setTitle("Are you sure?")
                            .setMessage("Keep changes an update word.")
                            .setPositiveButton("Yes", { _, _ ->
                                val word = wordEditTv.text.toString().trim()
                                val definition = definitionEditTv.text.toString().trim()
                                val wordU = intent.getParcelableExtra<Word>(KEY_DATA)
                                wordU.word = word
                                wordU.definition = definition
                                wordU.date = Date()
                                val intentBack = Intent()
                                intentBack.putExtra(KEY_ACTION, UPDATE)
                                intentBack.putExtra(KEY_DATA, wordU)
                                setResult(Activity.RESULT_OK, intentBack)
                                finish()
                            })
                            .setNegativeButton("Cancel", null)
                            .show()
                }
            }

            true
        }

        R.id.deleteMenuItem -> {
            AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setMessage("Delete this word for ever.")
                    .setPositiveButton("Yes", { _, _ ->
                        val intentBack = Intent()
                        intentBack.putExtra(KEY_ACTION, DELETE)
                        intentBack.putExtra(KEY_DATA, intent.getParcelableExtra<Word>(KEY_DATA))
                        setResult(Activity.RESULT_OK, intentBack)
                        finish()
                    })
                    .setNegativeButton("Cancel", null)
                    .show()
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
