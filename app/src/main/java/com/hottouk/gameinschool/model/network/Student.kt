package com.hottouk.gameinschool.model.network

import android.content.Context
import android.os.Parcelable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.hottouk.gameinschool.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    @PrimaryKey val userId: String,
    var userName: String? = "",
    var userEmail: String? = "",
    var userProfileImageUrl: String? = "",
    var studentCharacterImg: String = "",
    var userClass: String = "",

    //회원정보
    var studentNumber: String = "",
    var studentNickname: String = "",
    var studentSchool: String = "",
    var studentBirth: String = "",

    //능력치
    var studentLevel: Int = 1,
    var studentDetailInfo: String = "",
    var studentSimpleInfo: String = "",
    var studentExp: Long = 0,

    var leadership: Int = 0,
    var academicAbility: Int = 0,
    var cooperation: Int = 0,
    var sincerity: Int = 0,
    var career: Int = 0,

    //재화
    var money: Int = 0

) : Parcelable {
    constructor() : this(
        "", "", "", "", "", "",
        "", "", "", "", 1, "", "",
        0, 0, 0, 0, 0, 0
    )

    fun getUserProfileImage(context: Context, view: ImageView) {
        if (userProfileImageUrl == "") {
            view.setImageResource(R.drawable.ic_image_picker)
        } else {
            Glide.with(context)
                .load(userProfileImageUrl)
                .centerCrop()
                .into(view)
        }
    }

    fun getLevel() {
        studentExp = (leadership + academicAbility + cooperation + sincerity + career).toLong()
        when (studentExp) {
            in 0 until 25 -> {
                studentLevel = 1
            }
            in 25 until 80 -> {
                studentLevel = 2
            }
            in 50 until 200 -> {
                studentLevel = 3
            }
            in 200 until 500 -> {
                studentLevel = 4
            }
            else -> {
                studentLevel = 5
            }
        }
    }

    fun getCharacterAbility(studentPetList: MutableList<Pet>) {
        var sumLeadership = 0
        var sumAcademic = 0
        var sumCooperation = 0
        var sumSincerity = 0
        var sumCareer = 0
        studentPetList.forEach {
            sumLeadership += it.leadership
            sumAcademic += it.academicAbility
            sumCooperation += it.cooperation
            sumSincerity += it.sincerity
            sumCareer += it.career
        }
        leadership = sumLeadership
        academicAbility = sumAcademic
        cooperation = sumCooperation
        sincerity = sumSincerity
        career = sumCareer
        getLevel()
    }

    fun getCharacterImage(context: Context, imageView: ImageView) {
        if (studentCharacterImg == "남") {
            Glide.with(context)
                .load(R.drawable.character_student_male)
                .into(imageView)
        } else {
            Glide.with(context)
                .load(R.drawable.character_student_female)
                .into(imageView)
        }
    }

    fun getProgressBar(totalExp: TextView, currentExp: TextView, progressBar: ProgressBar) {
        when (studentLevel) {
            1 -> {
                progressBar.max = 25
                progressBar.progress = studentExp.toInt()
                totalExp.text = "/25"
                currentExp.text = studentExp.toString()
            }
            2 -> {
                progressBar.max = 80
                progressBar.progress = studentExp.toInt() - 25
                totalExp.text = "/80"
                currentExp.text = (studentExp - 25).toString()
            }
            3 -> {
                progressBar.max = 200
                progressBar.progress = studentExp.toInt() - 80
                totalExp.text = "/200"
                currentExp.text = (studentExp - 80).toString()
            }
            4 -> {
                progressBar.max = 500
                progressBar.progress = studentExp.toInt() - 200
                totalExp.text = "/500"
                currentExp.text = (studentExp - 200).toString()
            }
            else -> {
                progressBar.max = 1000
                progressBar.progress = studentExp.toInt() - 500
                totalExp.text = "/1000"
                currentExp.text = (studentExp - 500).toString()
            }
        }
    }
}