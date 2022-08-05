package com.anggitdev.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anggitdev.core.domain.model.User
import com.anggitdev.myapplication.firstUpper
import com.anggitdev.myapplication.loadImage
import com.anggitdev.myapplication.databinding.ItemUserBinding

class FavoriteAdapter(private val onItemClick: (User) -> Unit) :
    ListAdapter<User, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }

    class MyViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(users: User) {
            binding.listAvatarUrl.loadImage(users.avatarUrl, 56, 56)
            binding.listLogin.text = users.username.firstUpper()
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<User> =
            object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser.id == newUser.id
                }

                override fun areContentsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser == newUser
                }
            }
    }
}