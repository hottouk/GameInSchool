package com.hottouk.gameinschool.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hottouk.gameinschool.databinding.ItemCompleteMonsterBinding
import com.hottouk.gameinschool.databinding.ItemMonsterBinding
import com.hottouk.gameinschool.databinding.ItemParticipatingMonsterBinding
import com.hottouk.gameinschool.model.network.SchoolMonster

class SchoolMonsterRvAdapter :
    ListAdapter<SchoolMonster, RecyclerView.ViewHolder>(differCallback) {

    var participatingMonsterList = mutableListOf<SchoolMonster>()
    var completeMonsterList = mutableListOf<SchoolMonster>()
    var itemClickListener: ((SchoolMonster) -> Unit)? = null

    inner class SchoolMonsterItemViewHolder(val binding: ItemMonsterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViews(item: SchoolMonster) {
            item.getMonsterImage(binding.schoolWorkMonsterImageview)
            binding.schoolWorkTitleTextview.text = item.schoolWorkTitle
            binding.schoolWorkSimpleInfoTextview.text = item.schoolWorkSimpleInfo
            binding.difficultyRating.rating = item.difficulty?.toFloat() ?: 0f
            binding.schoolWorkMoney.text = item.money.toString()
            binding.schoolWorkExp.text = item.getTotalScore().toString()
            binding.root.setOnClickListener {
                itemClickListener?.let { it(item) }
            }
        }
    }

    inner class ParticipatingMonsterItemViewHolder(val binding: ItemParticipatingMonsterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViews(item: SchoolMonster) {
            item.getMonsterImage(binding.schoolWorkMonsterImageview)
            binding.schoolWorkTitleTextview.text = item.schoolWorkTitle
            binding.schoolWorkSimpleInfoTextview.text = item.schoolWorkSimpleInfo
            binding.difficultyRating.rating = item.difficulty?.toFloat() ?: 0f
            binding.schoolWorkMoney.text = item.money.toString()
            binding.schoolWorkExp.text = item.getTotalScore().toString()
            binding.root.setOnClickListener {
                itemClickListener?.let { it(item) }
            }
        }
    }

    inner class CompleteMonsterItemViewHolder(val binding: ItemCompleteMonsterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViews(item: SchoolMonster) {
            item.getMonsterImage(binding.schoolWorkMonsterImageview)
            binding.schoolWorkTitleTextview.text = item.schoolWorkTitle
            binding.schoolWorkSimpleInfoTextview.text = item.schoolWorkSimpleInfo
            binding.difficultyRating.rating = item.difficulty?.toFloat() ?: 0f
            binding.schoolWorkMoney.text = item.money.toString()
            binding.schoolWorkExp.text = item.getTotalScore().toString()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return checkState(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IS_COMPLETE -> {
                val inflater = LayoutInflater.from(parent.context)
                val itemView = ItemCompleteMonsterBinding.inflate(inflater, parent, false)
                CompleteMonsterItemViewHolder(itemView)
            }
            IS_PARTICIPATING -> {
                val inflater = LayoutInflater.from(parent.context)
                val itemView = ItemParticipatingMonsterBinding.inflate(inflater, parent, false)
                ParticipatingMonsterItemViewHolder(itemView)
            }
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val itemView = ItemMonsterBinding.inflate(inflater, parent, false)
                SchoolMonsterItemViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        when (checkState(item)) {
            IS_COMPLETE -> (holder as CompleteMonsterItemViewHolder).bindViews(item)
            IS_PARTICIPATING -> (holder as ParticipatingMonsterItemViewHolder).bindViews(item)
            else -> (holder as SchoolMonsterItemViewHolder).bindViews(item)
        }
    }

    fun setOnItemClickListener(listener: ((SchoolMonster) -> Unit)) {
        itemClickListener = listener
    }

    fun getParticipatingMonsterList(monsterList: MutableList<SchoolMonster>) {
        participatingMonsterList = monsterList
    }

    fun getCompleteMonsterList(monsterList: MutableList<SchoolMonster>) {
        completeMonsterList = monsterList
    }

    fun checkState(item: SchoolMonster): Int {
        return when {
            participatingMonsterList.contains(item) -> IS_PARTICIPATING
            completeMonsterList.contains(item) -> IS_COMPLETE
            else -> ELSE
        }
    }

    companion object {
        const val IS_PARTICIPATING = 1
        const val IS_COMPLETE = 2
        const val ELSE = 3

        val differCallback = object : DiffUtil.ItemCallback<SchoolMonster>() {
            override fun areItemsTheSame(oldItem: SchoolMonster, newItem: SchoolMonster): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SchoolMonster,
                newItem: SchoolMonster
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}