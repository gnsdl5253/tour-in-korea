package com.hoon.tourinkorea.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.hoon.tourinkorea.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val args: DetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLayout()

        binding.toolBarWrite.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setLayout() {
        val post = args.post
        val adapter = DetailImageAdapter(post.storageUriList)

        binding.post = post
        binding.viewPager.adapter = adapter

    }
}