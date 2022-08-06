package com.anggitdev.myapplication.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.anggitdev.core.data.Resource
import com.anggitdev.myapplication.R
import com.anggitdev.myapplication.databinding.FragmentDetailBinding
import com.anggitdev.myapplication.firstUpper
import com.anggitdev.myapplication.loadImage
import com.anggitdev.myapplication.ui.adapter.ViewPagerAdapter
import com.anggitdev.myapplication.ui.main.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    private var isFavoriteUser = false

    private val detailViewModel: DetailViewModel by viewModel()
    private val activityContext: MainActivity by lazy {
        (activity as MainActivity)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = DetailFragmentArgs.fromBundle(arguments as Bundle).username

        val adapter = ViewPagerAdapter(activityContext,username)

        activityContext.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.detail, username.firstUpper())
        }

        binding?.apply {
            viewpager.adapter = adapter
            detailViewModel.getDetailUser(username)
            detailViewModel.isFavorite(username)
            detailViewModel.isFavorite.observe(viewLifecycleOwner) {
                isFavorite(it)
                isFavoriteUser = it
            }
            fbFavorite.setOnClickListener {
                if (isFavoriteUser) {
                    detailViewModel.deleteFavorite(username)
                } else {
                    detailViewModel.setFavorite(username)
                }
                detailViewModel.isFavorite(username)
            }
            detailViewModel.detailResponse.observe(viewLifecycleOwner) { responses ->
                if (responses != null) {
                    when (responses) {
                        is Resource.Loading -> {
                            tvUsername.text = username
                            tvCompany.text = getString(R.string.loading)
                            tvJmlrepo.text = getString(R.string.loading)
                            tvJmlfollower.text = getString(R.string.loading)
                            tvJmlfollowing.text = getString(R.string.loading)
                            tvLoc.text = getString(R.string.loading)
                        }
                        is Resource.Success -> {
                            val userData = responses.data
                            userData?.let { detailData ->
                                tvUsername.text = detailData.username
                                tvCompany.text = detailData.company.toString()
                                tvJmlrepo.text = detailData.publicRepos.toString()
                                tvJmlfollower.text = detailData.followers.toString()
                                tvJmlfollowing.text = detailData.following.toString()
                                tvLoc.text = detailData.location.toString()
                                circleImageView.loadImage(detailData.avatarUrl)
                            }
                        }
                        is Resource.Error -> {
                            tvUsername.text = username
                            tvCompany.text = getString(R.string.errorr)
                            tvJmlrepo.text = getString(R.string.errorr)
                            tvJmlfollower.text = getString(R.string.errorr)
                            tvJmlfollowing.text = getString(R.string.errorr)
                            tvLoc.text = getString(R.string.errorr)
                        }
                        else -> {}
                    }
                }
            }
            TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.viewpager?.adapter = null
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.viewpager?.adapter = null
    }
    private fun isFavorite(isFavorite: Boolean) {
        binding?.fbFavorite?.visibility = View.VISIBLE
        if (isFavorite) {
            binding?.fbFavorite?.setImageDrawable(
                ContextCompat.getDrawable(
                    activityContext.applicationContext,
                    R.drawable.ic_baseline_favorite_24
                )
            )
        } else {
            binding?.fbFavorite?.setImageDrawable(
                ContextCompat.getDrawable(
                    activityContext.applicationContext,
                    R.drawable.ic_baseline_favorite_border_24
                )
            )
        }
    }
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.detail_tab_follower,
            R.string.detail_tab_following
        )
    }
}