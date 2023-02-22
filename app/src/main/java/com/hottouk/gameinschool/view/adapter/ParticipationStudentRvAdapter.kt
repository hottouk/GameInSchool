package com.hottouk.gameinschool.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hottouk.gameinschool.databinding.ItemParticipatingStudentBinding
import com.hottouk.gameinschool.model.network.Student

class ParticipationStudentRvAdapter :
    ListAdapter<Student, ParticipationStudentRvAdapter.ParticipantItemViewHolder>(differCallback) {
    inner class ParticipantItemViewHolder(val binding: ItemParticipatingStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViews(item: Student) {
            binding.infoTextview.text = item.userName
            Glide.with(binding.root)
                .load(item.userProfileImageUrl)
                .circleCrop()
                .into(binding.userProfileImageview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemParticipatingStudentBinding.inflate(inflater, parent, false)
        return ParticipantItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ParticipantItemViewHolder, position: Int) {
        val item = currentList[position]
        holder.bindViews(item)
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Student>() {
            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem == newItem
            }
        }
    }
}