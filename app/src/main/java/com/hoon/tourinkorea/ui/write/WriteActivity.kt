package com.hoon.tourinkorea.ui.write

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.hoon.tourinkorea.MainActivity
import com.hoon.tourinkorea.databinding.ActivityWriteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class WriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteBinding
    private lateinit var imageCountAdapter: ImageCountAdapter
    private lateinit var postSelectAdapter: PostSelectAdapter
    private val viewModel: WriteViewModel by viewModels()
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar

        setupAdapters()
        setupRecyclerView()
        setupUI()
        observeViewModel()

        val roadAddressName = intent.getStringExtra("roadAddressName")
        Log.d("WriteActivity", "Received placeName: $roadAddressName")
        if (!roadAddressName.isNullOrEmpty()) {
            binding.etWriteLocation.post {
                binding.etWriteLocation.setText(roadAddressName)
            }
        }
    }

    private fun setupAdapters() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
                if (uris.isNotEmpty()) {
                    viewModel.updateSelectedImages(uris)
                }
            }

        imageCountAdapter = ImageCountAdapter {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        postSelectAdapter = PostSelectAdapter { position: Int ->
            if (position >= 0 && position < viewModel.selectedImageUris.value.size) {
                viewModel.removeSelectedImage(position)
            }
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
        binding.etWriteLocation.setOnClickListener {

        }

        binding.btnWrite.setOnClickListener {
            val title = binding.etWriteTitle.text.toString()
            val location = binding.etWriteLocation.text.toString()
            val description = binding.etWriteDescription.text.toString()


            if (title.isEmpty() || location.isEmpty() || description.isEmpty() || viewModel.selectedImageUris.value.isEmpty()) {
                Toast.makeText(this, "이미지와 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                showProgressBar()

                lifecycleScope.launch {
                    viewModel.updateTitle(title)
                    viewModel.updateLocation(location)
                    viewModel.updateDescription(description)
                    viewModel.createPost()

                    delay(2000)

                    withContext(Dispatchers.Main) {
                        hideProgressBar()
                        val intent = Intent(this@WriteActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.selectedImageUris.collect {
                Log.d("WriteActivity", "Selected image URIs: $it")
                imageCountAdapter.updateData(it.size)
                postSelectAdapter.updateData(it)
                binding.rvWritePhoto.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}