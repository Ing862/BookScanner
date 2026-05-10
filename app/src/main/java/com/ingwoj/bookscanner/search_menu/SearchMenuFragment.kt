package com.ingwoj.bookscanner.search_menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ingwoj.bookscanner.R
import com.ingwoj.bookscanner.adapters.SearchAdapter
import com.ingwoj.bookscanner.book.BookDetailsActivity
import com.ingwoj.bookscanner.databinding.FragmentSearchMenuBinding
import kotlinx.coroutines.launch

import com.ingwoj.bookscanner.repository.BooksRepository

class SearchMenuFragment() : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    // FIXME: co to ?????
//    private val viewModel: SearchViewModel by viewModels{
//        object: ViewModelProvider.Factory {
//            override fun <T: ViewModel> create(modelClass: Class<T>): T {
//                return SearchViewModel(BooksRepository()) as T
//            }
//        }
//    }

    private var _binding: FragmentSearchMenuBinding? = null
    private val binding get() = _binding!!

    val adapter = SearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchMenuBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(divider)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { event ->
                Log.d("SearchFragment", "event: $event")
                when (event) {
                    is SearchViewModel.SearchEvent.OpenBook -> {
                        startActivity(BookDetailsActivity.createIntent(requireContext(), event.isbn))
                    }
                }

                Log.d("ACTIVITY", "start BookDetailsActivity")
            }

        }

        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.addTextChangedListener { text ->
            binding.searchBar.setText(text)
            // TODO: filtrowanie wyików i odświeżanie

            viewModel.onQueryChanged(text.toString())
        }

        viewModel.bookList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        // skaner
        binding.searchBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.scan_btn -> {
                    scanBarcode(requireContext(),
                    onResult = {isbn ->
                        viewModel.onScan(isbn)
                    },
                    onError = { message ->
                        //TODO: scanner error message popup
                    })
                    true
                }
                else -> false

            }
        }



    }

}