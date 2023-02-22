package com.hottouk.gameinschool.model.network

import android.os.Parcelable
import android.widget.ImageView
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.util.KeyValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class SchoolMonster(
    //기본사항(필수)
    val subject: String,
    val schoolWorkTitle: String,
    //기본사항(옵션)
    val schoolWorkSimpleInfo: String?,
    val schoolWorkDetailInfo: String?,

    //주는 경험치
    val leadership: Int,
    val academicAbility: Int,
    val cooperation: Int,
    val sincerity: Int,
    val career: Int,

    //동기부여요소
    val monsterImage: String?,
    val difficulty: Int?,
    val money: Int?,

    //만든 사람
    val madeBy: String

) : Parcelable {
    constructor() : this(
        "", "", "", "",
        0, 0, 0, 0, 0, "",
        0, 0, "",
    )

    fun getTotalScore(): Long {
        return (leadership + academicAbility + cooperation + sincerity + career).toLong()
    }

    fun getMonsterImage(imageView: ImageView) {
        when (monsterImage) {
            KeyValue.monsterLists[0] -> imageView.setImageResource(R.drawable.mon_002_d_front)
            KeyValue.monsterLists[1] -> imageView.setImageResource(R.drawable.mon_009_b_front)
            KeyValue.monsterLists[2] -> imageView.setImageResource(R.drawable.mon_011_a_front)
            KeyValue.monsterLists[3] -> imageView.setImageResource(R.drawable.mon_024_a_front)
            KeyValue.monsterLists[4] -> imageView.setImageResource(R.drawable.mon_027_c_front)
            KeyValue.monsterLists[5] -> imageView.setImageResource(R.drawable.mon_036_a_front)
            else -> imageView.setImageResource(R.drawable.ic_image_picker)
        }
    }
}
