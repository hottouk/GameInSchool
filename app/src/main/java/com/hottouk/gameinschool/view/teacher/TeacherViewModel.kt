package com.hottouk.gameinschool.view.teacher

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hottouk.gameinschool.model.network.Pet
import com.hottouk.gameinschool.model.network.SchoolClass
import com.hottouk.gameinschool.model.network.Student
import com.hottouk.gameinschool.model.network.Teacher
import com.hottouk.gameinschool.repository.Repository
import kotlinx.coroutines.launch

class TeacherViewModel : ViewModel() {

    private val repo = Repository()

    private var mClassSingUpMode = MutableLiveData(false)
    private val classSignUpMode: LiveData<Boolean> get() = mClassSingUpMode

    private var mConfirmationMode = MutableLiveData(false)
    private val confirmationMode: LiveData<Boolean> get() = mConfirmationMode

    private var mDropMode = MutableLiveData(false)
    val dropMode: LiveData<Boolean> get() = mDropMode

    private var mSelectedClass = MutableLiveData<SchoolClass>()
    val selectedClass: LiveData<SchoolClass> get() = mSelectedClass

    private var mSelectedTeacher = MutableLiveData<Teacher>()
    val selectedTeacher: LiveData<Teacher> get() = mSelectedTeacher

    private var mSelectedType = MutableLiveData<String>()
    val selectedType: LiveData<String> get() = mSelectedType

    fun signUpModeOn() = viewModelScope.launch {
        mClassSingUpMode.value = true
    }

    fun signUpModeOff() = viewModelScope.launch {
        mClassSingUpMode.value = false
    }

    fun confirmModeOn() = viewModelScope.launch {
        mConfirmationMode.value = true
    }

    fun confirmModeOff() = viewModelScope.launch {
        mConfirmationMode.value = false
    }

    fun dropModeOn() = viewModelScope.launch {
        mDropMode.value = true
    }

    fun dropModeOff() = viewModelScope.launch {
        mDropMode.value = false
    }

    fun selectPetType(type: String) = viewModelScope.launch {
        mSelectedType.value = type
    }

    fun selectTeacher(teacher: Teacher) = viewModelScope.launch {
        mSelectedTeacher.value = teacher
    }

    fun selectClass(schoolClass: SchoolClass) = viewModelScope.launch {
        mSelectedClass.value = schoolClass
    }

    fun getConfirmMode(): LiveData<Boolean> = confirmationMode

    fun getSignUpMode(): LiveData<Boolean> = classSignUpMode

    //선택 교사 반 데이터 불러오기 from FireBase #네트워크
    fun fetchTeacherClassList(teacherId: String): LiveData<MutableList<SchoolClass>> {
        val teacherClassList = MutableLiveData<MutableList<SchoolClass>>()
        viewModelScope.launch {
            repo.getClassList(teacherId).observeForever {
                teacherClassList.value = it
            }
        }
        return teacherClassList
    }

    //이미 가입했는지 체크하기 #네트워크
    fun signUpCheck(
        myInfo: Student, selectedClass: SchoolClass
    ): LiveData<Boolean> {
        val isOldMember = MutableLiveData<Boolean>()
        viewModelScope.launch {
            repo.getPetList(myInfo.userId).observeForever { myPets ->
                val signedUpClassList = mutableListOf<String>()
                myPets.forEach {
                    val splitNames = it.petId.split("-")
                    val newName = "${splitNames[0]}-${splitNames[1]}-${splitNames[2]}"
                    signedUpClassList.add(newName)
                }
                isOldMember.value = signedUpClassList.contains(selectedClass.classId)
            }
        }
        return isOldMember
    }

    fun fetchClassMateList(): LiveData<MutableMap<String, Pet>> {
        val classMatePetList = MutableLiveData<MutableMap<String, Pet>>()
        viewModelScope.launch {
            mSelectedTeacher.value?.let { teacher ->
                mSelectedClass.value?.let { schoolClass ->
                    repo.getClassMateList(teacher.userId, schoolClass.classId).observeForever {
                        classMatePetList.value = it
                    }
                }
            }
        }
        return classMatePetList
    }

}