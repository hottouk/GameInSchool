package com.hottouk.gameinschool.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hottouk.gameinschool.databinding.ItemTeacherBinding
import com.hottouk.gameinschool.model.network.Teacher

class TeacherOuterRvAdapter :
    ListAdapter<Teacher, TeacherOuterRvAdapter.TeacherItemViewHolder>(differCallback) {

    private var itemClickListener: ((Teacher) -> Unit)? = null //리스너

    inner class TeacherItemViewHolder(val binding: ItemTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(item: Teacher) {
            binding.teacherNameTextview.text = item.userName.toString()
            binding.teacherSchoolTextview.text = item.school
            binding.teacherSubjectTextview.text = item.subject
            Glide.with(binding.root)
                .load(item.userProfileImageUrl)
                .into(binding.teacherProfileImageview)
            binding.root.setOnClickListener {
                itemClickListener?.let { it(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemTeacherBinding.inflate(inflater, parent, false)
        return TeacherItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TeacherItemViewHolder, position: Int) {
        val item = currentList[position]
        holder.bindViews(item)
    }

    fun setOnItemClickListener(listener: ((Teacher) -> Unit)) {
        itemClickListener = listener
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Teacher>() {
            override fun areItemsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
                return oldItem == newItem
            }
        }
    }
}