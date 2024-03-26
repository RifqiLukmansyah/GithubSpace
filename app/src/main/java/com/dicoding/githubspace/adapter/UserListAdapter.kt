package com.dicoding.githubspace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubspace.R
import com.dicoding.githubspace.data.response.Users
import com.dicoding.githubspace.databinding.ItemRowUserBinding

class UserListAdapter(private val userList: List<Users>) :
    RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Users)
    }

    override fun getItemCount(): Int = userList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    class ListViewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val githubUser = userList[position]

        with(holder.binding) {
            tvitemname.text = githubUser.login
            tvitemurlgithub.text = githubUser.urlgithub
            Glide.with(holder.itemView.context)
                .load(githubUser.avatarUrl)
                .into(imgitemphoto)
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(githubUser)
        }
    }
}
