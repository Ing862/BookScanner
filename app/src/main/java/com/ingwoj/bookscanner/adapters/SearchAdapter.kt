package com.ingwoj.bookscanner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ingwoj.bookscanner.R
import com.ingwoj.bookscanner.databinding.BooksSearchListItemBinding
import com.ingwoj.bookscanner.data.Book
import com.ingwoj.bookscanner.data.BookResponse

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var bookList: List<Book> = emptyList()

    fun submitList(list: List<Book>) {
        bookList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = BooksSearchListItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val book = bookList[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
    class SearchViewHolder(val binding: BooksSearchListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.bookTitle.text = book.title
            binding.bookAuthors.text = if (book.authors.isNullOrEmpty()) "Brak autorów"
            else book.authors.joinToString(", ")

            val safeHttp = if (book.imageSmall != null && book.imageSmall.startsWith("http://")) book.imageSmall.replace("http://", "https://")
            else book.imageSmall
            binding.bookCover.load(safeHttp){
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
            }
        }
    }
}