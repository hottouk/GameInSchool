package com.hottouk.gameinschool.util

class KeyValue {
    companion object {
        //DB
        const val DB_TEACHER_USERS = "Teachers"
        const val DB_SCHOOL_ACTIVITIES = "Activities"
        const val DB_SCHOOL_CLASSES = "Classes"
        const val DB_STUDENTS = "Students"
        const val DB_PETS = "Pets"
        const val DB_PARTICIPANTS = "IsParticipating"
        const val DB_PROPERTY_PET_IMG = "petUrlImage"

        //SharedPref
        const val SHARED_PREFERENCES = "sharedPref"
        const val SHARED_PREF_USER_INFO = "sharedPrefUser"

        const val INTENT_EXTRA_TEACHER = "teacherInfo"
        const val INTENT_EXTRA_STUDENT = "studentInfo"
        const val INTENT_EXTRA_SCHOOL_WORK = "itemActivity"
        const val INTENT_EXTRA_USER_INFO = "userInfo"

        const val LOCAL_DB_STUDENT = "database_student"

        const val FRAG_TO_FRAG_TEACHER_KEY = "frag_to_frag_teacher_request_key"
        const val FRAG_BUNDLE_TEACHER_KEY = "frag_to_frag_bundle_teacher_key"
        const val FRAG_TO_FRAG_CLASS_KEY = "frag_to_frag_class_request_key"
        const val FRAG_BUNDLE_CLASS_KEY = "frag_to_frag_bundle_class_key"
        const val DIALOG_TO_FRAG_KEY = "dialog_to_frag_key"

        const val REQUEST_PERMISSION_CODE = 1000
        const val INTENT_ACTIVITY_RESULT_CODE = 2000
        val monsterLists = arrayOf("지렁이", "원숭이", "검정맨", "하얀맨", "두더지", "나무")


    }
}