package com.hottouk.gameinschool.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hottouk.gameinschool.databinding.ItemParticipatingStudentBinding
import com.hottouk.gameinschool.model.network.Pet

class PetRvAdapter : ListAdapter<Pet, PetRvAdapter.PetItemViewHolder>(differCallback) {

    var itemClickListener: ((Pet) -> Unit)? = null

    inner class PetItemViewHolder(val binding: ItemParticipatingStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViews(item: Pet) {
            item.getPetImage(binding.root.context, item.petUrlImage, binding.userProfileImageview)
            binding.infoTextview.text = item.subject
            binding.root.setOnClickListener {
                itemClickListener?.let { it(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemParticipatingStudentBinding.inflate(inflater, parent, false)
        return PetItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PetItemViewHolder, position: Int) {
        val item = currentList[position]
        holder.bindViews(item)
    }

    fun setOnItemClickListener(listener: ((Pet) -> Unit)) {
        itemClickListener = listener
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