package com.hottouk.gameinschool.model.kakao

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KakaoUserInfo(
    val userId: String,
    val userEmail: String?,
    val userName: String?,
    val userNickName: String?,
    val userProfileImageUrl: String? = ""
) : Parcelable
