package com.hottouk.gameinschool.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hottouk.gameinschool.databinding.ItemStudentBinding
import com.hottouk.gameinschool.databinding.ItemStudentPetBinding
import com.hottouk.gameinschool.model.network.Pet

class ClassMatePetRvAdapter() :
    ListAdapter<Pet, ClassMatePetRvAdapter.ClassMatePetItemViewHolder>(differCallback) {

    inner class ClassMatePetItemViewHolder(val binding: ItemStudentPetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(item: Pet) {
            item.getPetImage(
                binding.root.context,
                item.petUrlImage,
                binding.studentCharacterImageview
            )

            item.getProgressBar(
                binding.totalExpTextview,
                binding.currentExpTextview,
                binding.progressBar
            )

            binding.studentNameContentTextview.text = item.userName
            binding.studentLevelContentTextview.text = item.petLevel.toString()
            binding.studentNumberContentTextview.text = item.userStudentNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassMatePetItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemStudentPetBinding.inflate(inflater, parent, false)
        return ClassMatePetItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClassMatePetItemViewHolder, position: Int) {
        val item = currentList[position]
        holder.bindViews(item)
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Pet>() {
            override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
                return oldItem.petId == newItem.petId
            }

            override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean {
                return oldItem == newItem
            }
        }
    }
}