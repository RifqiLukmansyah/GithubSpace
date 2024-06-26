package com.dicoding.githubspace.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubspace.adapter.UserListAdapter
import com.dicoding.githubspace.data.response.Users
import com.dicoding.githubspace.databinding.FragmentFollowingsBinding
import com.dicoding.githubspace.viewmodel.detail.DetailViewModel


class FollowingsFragment : Fragment() {
    private lateinit var binding: FragmentFollowingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingsBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(context)
        binding.rvuser.layoutManager = layoutManager

        val itemDecor = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvuser.addItemDecoration(itemDecor)

        val detailVM = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        detailVM.followings.observe(viewLifecycleOwner) {
            setDataFollowings(it)
        }

        detailVM.isLoading.observe(viewLifecycleOwner) {
            loading(it)
        }

        return binding.root
    }

    private fun setDataFollowings(user: List<Users>) {
        binding.textnotfound.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
        binding.rvuser.apply {
            binding.rvuser.layoutManager = LinearLayoutManager(context)
            val listUserAdapter = UserListAdapter(user)
            binding.rvuser.adapter = listUserAdapter
            listUserAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Users) {
                    val detailViewModel =
                        ViewModelProvider(requireActivity())[DetailViewModel::class.java]
                    detailViewModel.dataUser = data.login
                }
            })
        }
    }

    private fun loading(isLoading: Boolean) {
        binding.followingsProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}