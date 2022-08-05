package com.anggitdev.myapplication.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.anggitdev.myapplication.ui.follow.FollowFragment
import com.anggitdev.myapplication.ui.follow.FollowFragment.Companion.ARG_SECTION_TYPE
import com.anggitdev.myapplication.ui.follow.FollowFragment.Companion.ARG_USERNAME

class ViewPagerAdapter (activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowFragment()
                fragment.arguments = Bundle().apply {
                    putSerializable(ARG_SECTION_TYPE, FollowFragment.FollowType.FOLLOWERS)
                    putString(ARG_USERNAME, username)
                }
            }
            1 -> {
                fragment = FollowFragment()
                fragment.arguments = Bundle().apply {
                    putSerializable(ARG_SECTION_TYPE, FollowFragment.FollowType.FOLLOWING)
                    putString(ARG_USERNAME, username)
                }
            }
        }
        return fragment as Fragment
    }
}