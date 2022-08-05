package com.anggitdev.myapplication.ui.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggitdev.core.data.Resource
import com.anggitdev.myapplication.R
import com.anggitdev.myapplication.databinding.FragmentFollowBinding
import com.anggitdev.myapplication.ui.adapter.UserAdapter
import com.anggitdev.myapplication.ui.search.SearchFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel


class FollowFragment : Fragment() {


    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding

    private val followViewModel: FollowViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var username = ""
        var type = FollowType.FOLLOWERS
        if (arguments?.containsKey(ARG_USERNAME) == true){
            username = arguments?.getString(ARG_USERNAME).toString()
        }
        if (arguments?.containsKey(ARG_SECTION_TYPE) == true) {
            type = arguments?.getSerializable(ARG_SECTION_TYPE) as FollowType
        }
        val followAdapter = UserAdapter { selectedUser ->
            val direction =
                SearchFragmentDirections.actionSearchFragmentToDetailFragment(selectedUser.username)
            findNavController().popBackStack(R.id.searchFragment, false)
            findNavController().navigate(direction)
        }
        followViewModel.getFollow(username, type)
        binding?.apply {
            rvDetail.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = followAdapter
            }
            followViewModel.followResponse.observe(viewLifecycleOwner){ responses ->
                if (responses != null) {
                    notAvailable.visibility =View.GONE
                    rvDetail.visibility = View.VISIBLE
                    when (responses) {
                        is Resource.Loading ->{
                            progressBar.visibility = View.VISIBLE
                            rvDetail.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            progressBar.visibility = View.GONE
                            rvDetail.visibility = View.VISIBLE
                            followAdapter.submitList(responses.data)
                        }
                        is Resource.Empty -> {
                            notAvailable.visibility = View.VISIBLE
                            rvDetail.visibility = View.GONE
                        }
                        is Resource.Error -> {
                            progressBar.visibility = View.GONE
                            rvDetail.visibility = View.GONE
                            notAvailable.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.rvDetail?.adapter = null
    }

    enum class FollowType {
        FOLLOWERS,
        FOLLOWING,
    }
    companion object {
        const val ARG_SECTION_TYPE = "section_type"
        const val ARG_USERNAME = "username"
    }
}