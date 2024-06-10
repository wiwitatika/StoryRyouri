package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding

@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyItem = intent.getParcelableExtra<ListStoryItem>(/* name = */ "LIST_STORY_ITEM")

        storyItem?.apply {
            binding.apply {
                nameTextView.text = name
                descTextView.text = description
            }

            Glide.with(this@DetailStoryActivity)
                .load(photoUrl)
                .into(binding.imageView)
        }
    }
}