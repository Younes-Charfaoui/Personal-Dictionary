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

                Log.i(TAG , "The prefrm was launche")

                wordListFiltered = if (keyword?.isEmpty()!!) {
                    wordList!!
                } else {
                    val filteredList = mutableListOf<Word>()

                    wordList?.forEach {
                        if (it.word.contains(keyword))
                            filteredList.add(it)
                    }
                    filteredList
                }

                return FilterResults().apply {
                    values = wordListFiltered
                    count = wordListFiltered?.size!!
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.i(TAG , "The publishResults was launche")

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

    override fun getItemCount(): Int = if (wordListFiltered == null) 0 else wordListFiltered!!.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = wordListFiltered?.get(position)
        holder.wordTv.text = word?.word
        holder.definitionTv.text = word?.definition
    }

    fun swapWordList(words: MutableList<Word>?) {
        this.wordList = words
        this.wordListFiltered = words
        notifyDataSetChanged()
    }

    inner class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordTv: TextView = view.wordTv
        val definitionTv: TextView = view.definitionTv

        init {
            view.setOnClickListener {
                listener.onWordItemClickListener(wordListFiltered!![adapterPosition])
            }
        }
    }

    fun resetTheList() {
        this.wordListFiltered = this.wordList
    }

    companion object {
        const val TAG = "WordAdapter"
    }

    interface OnWordItemsClickListener {
        fun onWordItemClickListener(word: Word)
    }
}