package com.hoon.tourinkorea.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hoon.tourinkorea.HomeGraphDirections
import com.hoon.tourinkorea.ItemClickListener
import com.hoon.tourinkorea.data.model.Post
import com.hoon.tourinkorea.databinding.FragmentBookmarkBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BookmarkAdapter
    private val viewModel:BookmarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
    }

    private fun setLayout() {
        adapter = BookmarkAdapter(this)
        binding.rvBookmarkList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { post ->
                adapter.submitList(post)
            }
        }
        viewModel.loadArticle()
    }

    override fun onItemClick(post: Post, view: View) {
        val action = HomeGraphDirections.actionGlobalDetail(post)
        findNavController().navigate(action)
    }
}