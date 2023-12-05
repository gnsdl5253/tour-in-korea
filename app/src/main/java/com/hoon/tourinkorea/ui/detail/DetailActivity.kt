package com.hoon.tourinkorea.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.hoon.tourinkorea.R
import com.hoon.tourinkorea.data.model.BookmarkEntity
import com.hoon.tourinkorea.data.model.Post
import com.hoon.tourinkorea.data.source.AppDatabase
import com.hoon.tourinkorea.databinding.ActivityDetailBinding
import com.hoon.tourinkorea.ui.bookmark.BookmarkPostDao
import com.hoon.tourinkorea.ui.download.DownloadActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var bookmarkPostDao: BookmarkPostDao
    private var isBookmarked: Boolean = false
    private val args: DetailActivityArgs by navArgs()
    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        post = args.post

        setLayout()
        setupDatabase()

        binding.ivBookmark.setOnClickListener {
            onBookmarkButtonClick()
        }

        binding.tvDetailLocation.setOnClickListener {

        }

        binding.toolBarWrite.setNavigationOnClickListener {
            finish()
        }
        checkBookmarkStatus()
    }

    private fun setLayout() {
        val post = args.post
        val adapter = DetailImageAdapter(post.storageUriList) { imageUrls, position ->
            navigateToDownloadActivity(imageUrls, position)
        }
        binding.post = post
        binding.viewPager.adapter = adapter

    }

    private fun navigateToDownloadActivity(imageUrls: List<String>, position: Int) {
        val intent = Intent(this, DownloadActivity::class.java)
        intent.putStringArrayListExtra("imageUrls", ArrayList(imageUrls))
        intent.putExtra("position", position)
        startActivity(intent)
    }

    private fun setupDatabase() {
        val db = AppDatabase.getInstance(this)
        bookmarkPostDao = db.bookmarkPostDao()
    }

    private fun onBookmarkButtonClick() {
        lifecycleScope.launch {
            if (isBookmarked) {
                unBookmarkArticle()
            } else {
                bookmarkArticle()
            }
            isBookmarked = !isBookmarked
            updateBookmarkButtonIcon()
        }
    }

    private suspend fun bookmarkArticle() {
        val currentTimeMillis = System.currentTimeMillis()
        val bookmarkEntity = BookmarkEntity(
            post = post,
            addedDate = currentTimeMillis.toString()
        )
        withContext(Dispatchers.IO) {
            bookmarkPostDao.insert(bookmarkEntity)
        }
    }

    private suspend fun unBookmarkArticle() {
        val hashCode = post.hashCode().toString()
        val bookmarkedArticle = withContext(Dispatchers.IO) {
            bookmarkPostDao.getAll().find { it.post.hashCode().toString() == hashCode }
        }

        bookmarkedArticle?.let {
            withContext(Dispatchers.IO) {
                bookmarkPostDao.delete(it)
            }
        }
    }

    private fun checkBookmarkStatus() {
        lifecycleScope.launch {
            val hashCode = post.hashCode().toString()
            val savedArticles = withContext(Dispatchers.IO) {
                bookmarkPostDao.getAll()
            }
            isBookmarked = savedArticles.any { it.post.hashCode().toString() == hashCode }
            updateBookmarkButtonIcon()
        }
    }

    private fun updateBookmarkButtonIcon() {
        val iconResId = if (isBookmarked) {
            R.drawable.ic_bookmark
        } else {
            R.drawable.ic_bookmark_border
        }
        binding.ivBookmark.setImageResource(iconResId)
    }
}