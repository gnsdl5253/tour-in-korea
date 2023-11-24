package com.hoon.tourinkorea.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hoon.tourinkorea.data.model.Post
import com.hoon.tourinkorea.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HomeAdapter { post ->
            navigateToDetail(post)
        }
        binding.rvHomeList.adapter = adapter
        viewModel.getPost()
        viewModel.items.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }


    private fun navigateToDetail(post: Post) {
        val action = HomeFragmentDirections.actionGlobalDetail(post)
        findNavController().navigate(action)
    }
}
