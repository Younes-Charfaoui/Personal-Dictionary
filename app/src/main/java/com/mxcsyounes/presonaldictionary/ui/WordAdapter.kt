package com.mxcsyounes.presonaldictionary.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mxcsyounes.presonaldictionary.R
import com.mxcsyounes.presonaldictionary.database.entities.Word
import kotlinx.android.synthetic.main.word_list_item.view.*

class WordAdapter(context: Context, onWordItemsClickListener: OnWordItemsClickListener) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    var wordList: MutableList<Word>? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val listener = onWordItemsClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = inflater.inflate(R.layout.word_list_item, parent, false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int = if (wordList == null) 0 else wordList!!.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = wordList?.get(position)
        holder.wordTv.text = word?.word
        holder.definitionTv.text = word?.definition
    }


    inner class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordTv: TextView = view.wordTv
        val definitionTv: TextView = view.definitionTv

        init {
            view.setOnClickListener {
                listener.onWordItemClickListener(wordList!![adapterPosition])
            }
        }
    }


    interface OnWordItemsClickListener {
        fun onWordItemClickListener(word: Word)
    }
}