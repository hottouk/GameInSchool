package com.hottouk.gameinschool.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hottouk.gameinschool.databinding.ItemStudentBinding
import com.hottouk.gameinschool.model.network.Student

class StudentRvAdapter() :
    ListAdapter<Student, StudentRvAdapter.StudentItemViewHolder>(differCallback) {

    private var itemClickListener: ((Student) -> Unit)? = null //리스너

    inner class StudentItemViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(student: Student) {
            binding.studentNameContentTextview.text = student.userName
            binding.studentNumberContentTextview.text = student.studentNumber
            binding.studentLevelContentTextview.text = student.studentLevel.toString()
            binding.root.setOnClickListener {
                itemClickListener?.let { it(student) }
            }
            student.getUserProfileImage(binding.root.context, binding.studentProfileImageview)
            student.getCharacterImage(binding.root.context, binding.studentCharacterImageview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemStudentBinding.inflate(inflater, parent, false)
        return StudentItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentItemViewHolder, position: Int) {
        val item = currentList[position]
        holder.bindViews(item)
    }

    fun setOnItemClickListener(listener: (Student) -> Unit) {
        itemClickListener = listener
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

