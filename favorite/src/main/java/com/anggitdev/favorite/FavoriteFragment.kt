package com.anggitdev.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggit.favorite.FavoriteViewModel
import com.anggit.favorite.favoriteModule
import com.anggitdev.myapplication.ui.main.MainActivity
import com.anggitdev.favorite.databinding.FragmentFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private val activityContext: MainActivity by lazy {
        (activity as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityContext.supportActionBar?.apply {
            title = getString(R.string.favorite)
            elevation = 0f
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        loadKoinModules(favoriteModule)
        val favAdapter = FavoriteAdapter { selectedUser ->
            val direction =
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(selectedUser.username)
            findNavController().navigate(direction)
        }
        favoriteViewModel.getFavorites()
        binding?.apply {
            rvFavorites.apply {
                layoutManager = LinearLayoutManager(activityContext.applicationContext)
                setHasFixedSize(true)
                adapter = favAdapter
            }
            favoriteViewModel.favoritesResponse.observe(viewLifecycleOwner) {
                favAdapter.submitList(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.rvFavorites?.adapter = null
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.rvFavorites?.adapter = null
    }

    override fun onPause() {
        super.onPause()
        binding?.rvFavorites?.adapter = null
        _binding = null
    }
}