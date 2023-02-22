package com.hottouk.gameinschool.model.network

import android.content.Context
import android.os.Parcelable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hottouk.gameinschool.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    //펫정보
    var petId: String = "",
    val petName: String = "",
    val subject: String = "",
    val petUrlImage: String = "",

    //펫유저 정보
    val belongToUser: String = "",
    val userName: String? = "",
    val userStudentNumber: String = "",

    //레벨
    var petLevel: Int = 1,
    var petExp: Long = 0,

    //능력치
    var leadership: Int = 0,
    var academicAbility: Int = 0,
    var cooperation: Int = 0,
    var sincerity: Int = 0,
    var career: Int = 0,
    var money: Int = 0,

    //생기부 정보
    var petSimpleInfo: String = "",
    var petDetailInfo: String = ""
) : Parcelable {
    constructor() : this(
        "", "", "", "", "", "", "",
        1, 0, 0, 0, 0, 0, 0, 0, "", ""
    )

    init {
        getLevel()
    }

    fun combinePetId(teacherId: String, classId: String, userId: String): String {
        petId = "${teacherId}-${classId.split("-")[1]}-" +
                "${classId.split("-").last()}-${userId}"
        return petId
    }

    private fun getLevel() {
        petExp = (leadership + academicAbility + cooperation + sincerity + career).toLong()
        when (petExp) {
            in 0 until 25 -> {
                petLevel = 1
            }
            in 25 until 80 -> {
                petLevel = 2
            }
            in 50 until 200 -> {
                petLevel = 3
            }
            in 200 until 500 -> {
                petLevel = 4
            }
            else -> {
                petLevel = 5
            }
        }
    }

    fun getPetImage(context: Context, type: String, imageView: ImageView) {
        when (type) {
            "grass" -> {
                Glide.with(context)
                    .load(R.drawable.pet_grass1)
                    .circleCrop()
                    .into(imageView)
            }
            "water" -> {
                Glide.with(context)
                    .load(R.drawable.pet_water1)
                    .circleCrop()
                    .into(imageView)
            }

            else -> {
                Glide.with(context)
                    .load(R.drawable.pet_fire1)
                    .circleCrop()
                    .into(imageView)
            }
        }
    }


    fun getProgressBar(totalExp: TextView, currentExp: TextView, progressBar: ProgressBar) {
        when (petLevel) {
            1 -> {
                progressBar.max = 25
                progressBar.progress = petExp.toInt()
                totalExp.text = "/25"
                currentExp.text = petExp.toString()
            }
            2 -> {
                progressBar.max = 80
                progressBar.progress = petExp.toInt() - 25
                totalExp.text = "/80"
                currentExp.text = (petExp - 25).toString()
            }
            3 -> {
                progressBar.max = 200
                progressBar.progress = petExp.toInt() - 80
                totalExp.text = "/80"
                currentExp.text = (petExp - 80).toString()

            }
            4 -> {
                progressBar.max = 500
                progressBar.progress = petExp.toInt() - 200
                totalExp.text = "/80"
                currentExp.text = (petExp - 200).toString()
            }
            else -> {
                progressBar.max = 1000
                progressBar.progress = petExp.toInt() - 500
                totalExp.text = "/80"
                currentExp.text = (petExp - 500).toString()
            }
        }
    }
}
