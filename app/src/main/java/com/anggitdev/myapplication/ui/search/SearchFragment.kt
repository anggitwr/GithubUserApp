package com.anggitdev.myapplication.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggitdev.core.data.Resource
import com.anggitdev.myapplication.R
import com.anggitdev.myapplication.databinding.FragmentSearchBinding
import com.anggitdev.myapplication.ui.adapter.UserAdapter
import com.anggitdev.myapplication.ui.main.MainActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


@FlowPreview
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() =_binding

    private val searchViewModel: SearchViewModel by viewModel()
    private var isNotValidData: Boolean = false

    private val activityContext: MainActivity by lazy {
        (activity as MainActivity)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onResume() {
        super.onResume()
        activityContext.title = getString(R.string.app_name)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityContext.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            title = getString(R.string.app_name)
        }

        binding?.apply {
            val userAdapter = UserAdapter { selectedUser ->
                val direction =
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(selectedUser.username)
                findNavController().navigate(direction)
            }

            rvItem.apply {
                layoutManager = LinearLayoutManager(activity)
                setHasFixedSize(true)
                adapter = userAdapter
            }
            val searchStream = RxTextView.textChanges(searchView)
                .skipInitialValue()
                .map { search ->
                    search.length < 2
                }
            searchStream.subscribe {
                isNotValidData = it
                showSearchAlert(it)
            }
            searchView.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    if (!isNotValidData) {
                        searchViewModel.searchUser(searchView.text.toString())
                    }
                    true
                } else {
                    false
                }
            }
            searchViewModel.searchResponse.observe(viewLifecycleOwner) { responses ->
                if (responses != null) {
                    notAvailable.visibility = View.GONE
                    when (responses) {
                        is Resource.Loading -> progressBar.visibility = View.VISIBLE
                        is Resource.Success -> {
                            progressBar.visibility = View.GONE
                            userAdapter.submitList(responses.data)
                        }
                        is Resource.Error -> {
                            progressBar.visibility = View.GONE
                            notAvailable.visibility = View.VISIBLE
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showSearchAlert(isNotValid: Boolean) {
        binding?.searchView?.error =
            if (isNotValid) getString(R.string.min_character_search) else null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.rvItem?.adapter = null
    }

    override fun onPause() {
        super.onPause()
        _binding = null
    }
}