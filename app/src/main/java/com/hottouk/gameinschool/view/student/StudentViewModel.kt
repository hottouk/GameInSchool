package com.hottouk.gameinschool.view.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hottouk.gameinschool.model.network.Pet
import com.hottouk.gameinschool.model.network.Student
import com.hottouk.gameinschool.repository.Repository
import kotlinx.coroutines.launch

class StudentViewModel : ViewModel() {

    //네트워크 데이터
    private val repo = Repository()
    private var mStudentPetList = MutableLiveData<MutableList<Pet>>()

    //선택 학생
    private var mSelectedStudent = MutableLiveData<Student>()

    //챠트 모드
    private var mRadarChartMode = MutableLiveData<Boolean>(false)

    //학생 펫 데이터 가져오기(등록한 반 가져오기) #네트워크
    fun fetchStudentPetList(studentId: String): LiveData<MutableList<Pet>> {
        viewModelScope.launch {
            repo.getPetList(studentId).observeForever {
                mStudentPetList.value = it
            }
        }
        return mStudentPetList
    }

    fun selectStudent(student: Student) {
        mSelectedStudent.value = student
    }

    fun fetchSelectedStudent(): LiveData<Student> = mSelectedStudent

    fun radarModeOn() {
        mRadarChartMode.value = true
    }

    fun radarModeOff() {
        mRadarChartMode.value = false
    }

    fun getRadarMode(): LiveData<Boolean> = mRadarChartMode


}