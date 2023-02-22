package com.hottouk.gameinschool.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hottouk.gameinschool.databinding.ItemInnerClassBinding
import com.hottouk.gameinschool.model.network.SchoolClass

class StudentClassRvAdapter :
    ListAdapter<SchoolClass, StudentClassRvAdapter.StudentClassRvAdapter>(differCallback) {
    private var itemClickListener: ((SchoolClass) -> Unit)? = null

    inner class StudentClassRvAdapter(val binding: ItemInnerClassBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(item: SchoolClass) {
            binding.classNameTextview.text = "${item.subject}-${item.className}"
            binding.root.setOnClickListener {
                itemClickListener?.let { it(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentClassRvAdapter {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemInnerClassBinding.inflate(inflater, parent, false)
        return StudentClassRvAdapter(itemView)
    }

    override fun onBindViewHolder(holder: StudentClassRvAdapter, position: Int) {
        val item = currentList[position]
        holder.bindViews(item)
    }

    //외부 참조 함수
    fun setOnItemClickListener(listener: (SchoolClass) -> Unit) {
        itemClickListener = listener
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<SchoolClass>() {
            override fun areItemsTheSame(oldItem: SchoolClass, newItem: SchoolClass): Boolean {
                return oldItem.className == newItem.className
            }

            override fun areContentsTheSame(oldItem: SchoolClass, newItem: SchoolClass): Boolean {
                return oldItem == newItem
            }
        }
    }
}