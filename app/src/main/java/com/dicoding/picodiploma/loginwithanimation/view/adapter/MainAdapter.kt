package com.dicoding.picodiploma.loginwithanimation.view.adapter

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemRowBinding
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailStoryActivity

@Suppress("DEPRECATION")
class MainAdapter(private val context: Context) : PagingDataAdapter<ListStoryItem, MainAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bindData(it)
        }
    }

    inner class ViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val context = binding.root.context
                    val item = getItem(position)
                    item?.let {
                        val detailIntent = Intent(context, DetailStoryActivity::class.java).apply {
                            putExtra("LIST_STORY_ITEM", it as Parcelable)
                        }
                        context.startActivity(detailIntent)
                    }
                }
            }
        }

        fun bindData(storyItem: ListStoryItem) {
            binding.name.text = storyItem.name
            binding.description.text = storyItem.description
            Glide.with(binding.root.context)
                .load(storyItem.photoUrl)
                .into(binding.imageView)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoryItem> = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
