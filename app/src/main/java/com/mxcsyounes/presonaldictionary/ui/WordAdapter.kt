package com.mxcsyounes.presonaldictionary.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.mxcsyounes.presonaldictionary.R
import com.mxcsyounes.presonaldictionary.database.entities.Word
import kotlinx.android.synthetic.main.word_list_item.view.*

class WordAdapter(context: Context, onWordItemsClickListener: OnWordItemsClickListener)
    : RecyclerView.Adapter<WordAdapter.WordViewHolder>(), Filterable {


    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val keyword = constraint?.toString()
                if (keyword?.isEmpty()!!) {
                    wordListFiltered = wordList
                } else {
                    val filteredList = mutableListOf<Word>()
                    for (word in this.wordList) {
                        if (word?.) {
                            filteredList.add(word)
                        }
                    }
                    wordListFiltered = filteredList
                }

                val result = FilterResults()
                result.values = wordListFiltered
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                wordList = results?.values as MutableList<*>
                notifyDataSetChanged()
            }

        }

    }

    var wordList: MutableList<Word>? = null
    var wordListFiltered: MutableList<Word>? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val listener = onWordItemsClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = inflater.inflate(R.layout.word_list_item, parent, false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int = if (wordList == null) 0 else wordList!!.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = wordList?.get(position)
        Log.d(TAG, "the word is ${word?.toString()}")
        holder.wordTv.text = word?.word
        holder.definitionTv.text = word?.definition
    }

    fun swapWordList(words: MutableList<Word>?) {
        this.wordList = words
        notifyDataSetChanged()
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


    companion object {
        const val TAG = "WordAdapter"
    }

    interface OnWordItemsClickListener {
        fun onWordItemClickListener(word: Word)
    }
}