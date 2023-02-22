package com.hottouk.gameinschool.model.network

data class SchoolClass(
    var classId: String = "",
    val schoolName: String = "",
    val className: String = "",
    val subject: String = "",
    val classBackground: String = "",
    val madeBy: String = ""
) {
    constructor() : this(
        "", "", "", "", "", ""
    )

    fun combineId(teacherId: String, subject: String, className: String) {
        classId = "$teacherId-$subject-$className"
    }
}
