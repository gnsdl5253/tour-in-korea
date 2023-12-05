package com.hoon.tourinkorea.ui.download

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.tabs.TabLayoutMediator
import com.hoon.tourinkorea.databinding.ActivityDownloadBinding

class DownloadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDownloadBinding
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter
    private var hasReadPermission = false
    private var hasWritePermission = false
    private val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            hasReadPermission =
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: hasReadPermission
            hasWritePermission =
                permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: hasWritePermission
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrls = intent.getStringArrayListExtra("imageUrls")
        Log.e("down","$imageUrls" )
        val position = intent.getIntExtra("position", 0)

        imageViewPagerAdapter = ImageViewPagerAdapter(imageUrls ?: emptyList())
        binding.viewPagerDownload.adapter = imageViewPagerAdapter
        binding.viewPagerDownload.currentItem = position

        TabLayoutMediator(binding.tabLayoutDownload, binding.viewPagerDownload) { _, _ ->

        }.attach()

        binding.toolBarDownload.setNavigationOnClickListener {
            finish()
        }

        binding.ivDownload.setOnClickListener {
            if (imageUrls.isNullOrEmpty()) {
                Toast.makeText(this, "이미지 URL이 비어있습니다.", Toast.LENGTH_SHORT).show()
            } else {
                requestDownloadPermissions()
                downloadFile(imageUrls.joinToString(), position)
                Toast.makeText(this, "다운로드 성공", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestDownloadPermissions() {
        hasReadPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        hasWritePermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val permissions = mutableListOf<String>()
        if (!(hasWritePermission || minSdk29)) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!hasReadPermission) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissions.isNotEmpty()) {
            permissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun downloadFile(imageUrls: String, position: Int) {
        val workManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder()
            .build()
        val data = Data.Builder()
            .putString("imageUrls", imageUrls)
            .putInt("position", position)
            .build()
        val work = OneTimeWorkRequestBuilder<ImageDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        workManager.enqueue(work)
    }
}