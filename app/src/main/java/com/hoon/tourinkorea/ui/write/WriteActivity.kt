package com.hoon.tourinkorea.ui.write

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import com.hoon.tourinkorea.MainActivity
import com.hoon.tourinkorea.databinding.ActivityWriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteBinding
    private lateinit var imageCountAdapter: ImageCountAdapter
    private lateinit var postSelectAdapter: PostSelectAdapter
    private var selectedImageUriList: MutableList<Uri> = mutableListOf()
    private val viewModel: WriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapters()
        setupRecyclerView()
        setupUI()
    }

    private fun setupAdapters() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
                if (uris.isNotEmpty()) {
                    selectedImageUriList = uris.toMutableList()
                    imageCountAdapter.updateData(selectedImageUriList.size)
                    postSelectAdapter.updateData(selectedImageUriList)
                }
            }

        imageCountAdapter = ImageCountAdapter {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        postSelectAdapter = PostSelectAdapter { position: Int ->
            if (position >= 0 && position < selectedImageUriList.size) {
                removeSelectedImage(position)
            }
        }
    }

    private fun removeSelectedImage(position: Int) {
        selectedImageUriList.removeAt(position)
        imageCountAdapter.updateData(selectedImageUriList.size)
        postSelectAdapter.notifyItemRemoved(position)

        for (i in position until selectedImageUriList.size) {
            postSelectAdapter.notifyItemChanged(i)
        }
    }

    private fun setupRecyclerView() {
        val concatAdapter = ConcatAdapter(imageCountAdapter, postSelectAdapter)
        binding.rvWritePhoto.adapter = concatAdapter
    }

    private fun setupUI() {
        binding.toolBarWrite.setNavigationOnClickListener {
            finish()
        }

        binding.btnWrite.setOnClickListener {
            val title = binding.etWriteTitle.text.toString()
            val location = binding.etWriteLocation.text.toString()
            val description = binding.etWriteDescription.text.toString()

            if (title.isEmpty() || location.isEmpty() || description.isEmpty() || selectedImageUriList.isEmpty()) {
                Toast.makeText(this, "이미지와 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.updateSelectedImages(selectedImageUriList)
                viewModel.createPost(title, location, description)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}