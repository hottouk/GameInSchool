package com.hottouk.gameinschool.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hottouk.gameinschool.databinding.ItemInnerClassBinding
import com.hottouk.gameinschool.model.network.SchoolClass

class TeacherClassRvAdapter(val context: Context) :
    ListAdapter<SchoolClass, TeacherClassRvAdapter.SchoolClassHolder>(differCallback) {
    private var itemClickListener: ((SchoolClass) -> Unit)? = null
    private var signedUpClassIdList = mutableListOf<String>()

    inner class SchoolClassHolder(val binding: ItemInnerClassBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(item: SchoolClass) {
            binding.classNameTextview.text = item.className
            binding.root.setOnClickListener {
                if (signedUpClassIdList.contains(item.classId)) {
                    Toast.makeText(context, "이미 등록한 반입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    itemClickListener?.let { it(item) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolClassHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemInnerClassBinding.inflate(inflater, parent, false)
        return SchoolClassHolder(itemView)
    }

    override fun onBindViewHolder(holder: SchoolClassHolder, position: Int) {
        val item = currentList[position]
        holder.bindViews(item)
    }

    //외부 참조 함수
    fun setOnItemClickListener(listener: (SchoolClass) -> Unit) {
        itemClickListener = listener
    }

    fun getAlreadySignedUpClassIdList(classList: MutableList<String>) {
        signedUpClassIdList = classList
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