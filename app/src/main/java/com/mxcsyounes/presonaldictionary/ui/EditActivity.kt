package com.mxcsyounes.presonaldictionary.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.mxcsyounes.presonaldictionary.R
import com.mxcsyounes.presonaldictionary.database.entities.Word
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.content_edit.*
import java.util.*

class EditActivity : AppCompatActivity() {

    private var statePhoto = true
    private var selectedImages = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(editToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        when (intent.getIntExtra(KEY_ACTION, 0)) {
            ACTION_DETAIL -> {
                val word = intent.getParcelableExtra<Word>(KEY_DATA)
                wordEditTv.setText(word.word)
                editToolbar.title = getString(R.string.detail)
                definitionEditTv.setText(word.definition)
                wordEditTv.isClickable = false
                wordEditTv.isFocusable = false
                definitionEditTv.isClickable = false
                definitionEditTv.isFocusable = false
                //addPhotoButton.visibility = View.GONE

                if (word.paths != null) {
                    photoTitleTv.visibility = View.VISIBLE

                    val paths = word.paths!!.split(";")
                    for (path in paths) {
                        if (path.trim().isEmpty()) break
                        val imageView = ImageView(this)
                        val params = LinearLayout.LayoutParams(500, 600)

                        params.setMargins(0, 8, 0, 8)
                        params.gravity = Gravity.CENTER_HORIZONTAL
                        imageView.layoutParams = params

                        imageView.setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(path)))
                        }

                        photoLayout.addView(imageView)

                        Glide.with(this).load(Uri.parse(path)).into(imageView)
                    }
                } else
                    photoTitleTv.visibility = View.GONE


            }
            ACTION_NEW -> {
                editToolbar.title = getString(R.string.new_word)
                photoTitleTv.visibility = View.GONE
                statePhoto = false
            }
            else -> finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
            when (item?.itemId) {

                android.R.id.home -> {
                    val word = intent.getParcelableExtra<Word>(KEY_DATA)
                    if (selectedImages.isNotEmpty()) {
                        if (word?.paths == null)
                            word?.paths = selectedImages
                        else
                            word.paths += selectedImages
                    }
                    val intentBack = Intent()
                    intentBack.putExtra(KEY_ACTION, UPDATE)
                    intentBack.putExtra(KEY_DATA, word)
                    setResult(Activity.RESULT_OK, intentBack)
                    onBackPressed()
                    true
                }

                R.id.doneMenuItem -> {
                    if (validate()) {
                        val word = wordEditTv.text.toString().trim()
                        val definition = definitionEditTv.text.toString().trim()
                        val intentBack = Intent()
                        val wordObject: Word
                        wordObject = if (selectedImages.isEmpty())
                            Word(null, Date(), word, definition, null)
                        else
                            Word(null, Date(), word, definition, selectedImages)
                        intentBack.putExtra(KEY_DATA, wordObject)
                        setResult(Activity.RESULT_OK, intentBack)
                        finish()
                    }
                    true
                }

                R.id.addPhotoMenuItem -> {
                    getPhotoFromGallery()
                    true
                }

                R.id.deleteMenuItem -> {
                    AlertDialog.Builder(this)
                            .setTitle(getString(R.string.are_you_sure))
                            .setMessage(getString(R.string.delete_dialog_message))
                            .setPositiveButton(getString(R.string.yes_sting), { _, _ ->
                                val intentBack = Intent()
                                intentBack.putExtra(KEY_ACTION, DELETE)
                                intentBack.putExtra(KEY_DATA, intent.getParcelableExtra<Word>(KEY_DATA))
                                setResult(Activity.RESULT_OK, intentBack)
                                finish()
                            })
                            .setNegativeButton(getString(R.string.cancel), null)
                            .show()
                    true
                }

                else -> false
            }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        when (intent.getIntExtra(KEY_ACTION, 0)) {
            ACTION_DETAIL -> menuInflater.inflate(R.menu.menu_edit_detail, menu)
            ACTION_NEW -> {
                menuInflater.inflate(R.menu.menu_edit_new, menu)
            }
            else -> finish()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            val image = data?.data
            selectedImages += "${image.toString()};"
        }
        super.onActivityResult(requestCode, resultCode, data)
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

    private fun getPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
            return
        }
        startActivityForResult(intent, REQUEST_PHOTO)
    }

    companion object {
        const val REQUEST_PHOTO = 4451
        const val REQUEST_PERMISSION = 4451
        const val KEY_ACTION = "action"
        const val KEY_DATA = "data"
        const val ACTION_DETAIL = 11
        const val ACTION_NEW = 12
        const val DELETE = 114
        const val UPDATE = 112
    }

}
