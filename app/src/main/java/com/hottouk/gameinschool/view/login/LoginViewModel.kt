package com.hottouk.gameinschool.view.login

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hottouk.gameinschool.databinding.ActivityLogInActivityBinding
import com.hottouk.gameinschool.model.network.Student
import com.hottouk.gameinschool.repository.MyDataStore
import com.hottouk.gameinschool.repository.Repository
import com.hottouk.gameinschool.util.KeyValue
import com.google.gson.Gson
import com.hottouk.gameinschool.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    //저장소
    private val repo = Repository()
    private val myDataStore = MyDataStore()

    //기존 유저 관련 변수
    private val mIsCurrentUser = MutableLiveData<Boolean>()
    val isCurrentUser: LiveData<Boolean> get() = mIsCurrentUser

    //카카오 유저
    var savedKakaoUserInfo: com.hottouk.gameinschool.model.kakao.KakaoUserInfo? = null
    private lateinit var mSavedStudentUser: Student

    //케릭터 정하기
    var characterImage: String? = null

    //로그인 정보 pref에 저장하기
    fun saveUserPref() = viewModelScope.launch {
        repo.prefEditor
        mSavedStudentUser?.let {
            val json: String = Gson().toJson(it)
            repo.prefEditor.putString(KeyValue.SHARED_PREF_USER_INFO, json)
                .commit()
        }
    }

    //pref에 저장된 로그인 정보 불러오기
    fun getUserInfoFromPref(binding: ActivityLogInActivityBinding): Student? {
        var currentUser: Student? = null
        viewModelScope.launch {
            try {
                val jsonString = repo.pref.getString(KeyValue.SHARED_PREF_USER_INFO, "정보 없음")
                currentUser = Gson().fromJson(jsonString, Student::class.java)
            } catch (e: Exception) {
                Toast.makeText(binding.root.context, "등록 사용자가 아닙니다.", Toast.LENGTH_SHORT).show()
                binding.signUpFragmentContainer.visibility = View.VISIBLE
            }
        }
        return currentUser
    }

    //기존 유저인지 확인하기
    fun checkCurrentMember() = viewModelScope.launch {
        val jsonString = repo.pref.getString(KeyValue.SHARED_PREF_USER_INFO, "정보없음")
        mIsCurrentUser.value = jsonString != "정보없음"
    }

    //뷰모델에 학생 유저로 저장
    fun kakaoUserToStudentUser() = viewModelScope.launch {
        val student = savedKakaoUserInfo?.let { kakaoUser ->
            Student(
                userId = "kakao${kakaoUser.userId}S",
                userName = kakaoUser.userNickName,
                userEmail = kakaoUser.userEmail,
                kakaoUser.userProfileImageUrl, "",
                "", "", "", "", "",
                1, "", "",
                0, 0, 0, 0, 0, 0, 0
            )
        }
        student?.let { mSavedStudentUser = it }
    }

    fun addInfoToStudentUser(binding: FragmentSignUpBinding) = viewModelScope.launch {
        mSavedStudentUser.studentSchool = binding.studentSchoolEdittext.text.toString()
        mSavedStudentUser.userName = binding.studentNameEdittext.text.toString()
        mSavedStudentUser.studentNumber = binding.studentNumberEdittext.text.toString()
        mSavedStudentUser.studentBirth = binding.studentBirthEdittext.text.toString()
        mSavedStudentUser.studentCharacterImg = characterImage ?: "남"
    }

    //신규 유저 파이어베이스 DB에 저장하기
    fun userToFirebase() = viewModelScope.launch {
        repo.studentDB.child("${mSavedStudentUser.userId}").setValue(mSavedStudentUser)
    }

    fun setUpKakaoFlag() = viewModelScope.launch {
        myDataStore.setUpKakaoFlag()
    }

    //교사 정보 내보내기
    fun fetchCurrentUser() = mSavedStudentUser

    fun makeMockData(number: Int, schoolClass: String) {
        for (i in 1..number) {
            repo.classDB.child("kakao2618040363") //todo 여기도 변수 받아야함.
                .child(schoolClass)
                .child(KeyValue.DB_STUDENTS)
                .child("kakao${i}abc30${i}").setValue(
                    Student(
                        userId = "kakao${i}abc30${i}",
                        userName = "학생${i}",
                        userEmail = "학생${i}.naver.com",
                        userProfileImageUrl = ""
                    )
                )
        }
    }

}