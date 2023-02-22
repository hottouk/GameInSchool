package com.hottouk.gameinschool.model.network

import android.content.Context
import android.os.Parcelable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hottouk.gameinschool.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Teacher(
    val userId: String,
    var userEmail: String? = "",
    var userName: String? = "",
    var userNickName: String? = "",
    var userProfileImageUrl: String? = "",

    var school: String = "깨민고",
    var subject: String = "영어",
    var teacherCharacterImg: String = ""
) : Parcelable {
    constructor() : this(
        "","","","",""
    )

    fun getCharacterImage(context: Context, imageView: ImageView) {
        if (teacherCharacterImg == "남") {
            Glide.with(context)
                .load(R.drawable.character_teacher_male)
                .circleCrop()
                .into(imageView)
        } else {
            Glide.with(context)
                .load(R.drawable.character_teacher_female)
                .circleCrop()
                .into(imageView)
        }
    }

}