package com.hottouk.gameinschool.view.schoolmonster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hottouk.gameinschool.model.network.SchoolClass
import com.hottouk.gameinschool.model.network.SchoolMonster
import com.hottouk.gameinschool.model.network.Student
import com.hottouk.gameinschool.repository.Repository
import kotlinx.coroutines.launch

class SchoolMonsterViewModel : ViewModel() {

    //네트워크 데이터
    private val repo = Repository()
    private var mMyPetList =
        MutableLiveData<MutableList<com.hottouk.gameinschool.model.network.Pet>>()
    private var mSelectedClassMonsterList = MutableLiveData<MutableList<SchoolMonster>>()
    private var mParticipantList = MutableLiveData<MutableList<Student>>()
    private var mMyParticipationMonsterList = MutableLiveData<MutableList<SchoolMonster>>()

    //선택 사항
    private var mSelectedTeacher = MutableLiveData<String>() //교사 선택
    private var mSelectedSubject = MutableLiveData<String>() //과목 선택
    private var mSelectedClass = MutableLiveData<SchoolClass>() //반 선택
    private var mSelectedMonster = MutableLiveData<SchoolMonster>() //몬스터 선택

    //---------------------------------------------------------------------------------------네트워크
    //내 펫 데이터 가져오기(등록한 반 가져오기) #네트워크
    fun fetchMyPetList(user: String): LiveData<MutableList<com.hottouk.gameinschool.model.network.Pet>> {
        viewModelScope.launch {
            repo.getPetList(user).observeForever {
                mMyPetList.value = it
            }
        }
        return mMyPetList
    }

    //등록 반 활동 데이터 가져오기(등록한 반 가져오기) #네트워크
    fun fetchMonsterList(teacher: String, subject: String): LiveData<MutableList<SchoolMonster>> {
        viewModelScope.launch {
            repo.getMonsterList(teacher, subject).observeForever() {
                mSelectedClassMonsterList.value = it
            }
        }
        return mSelectedClassMonsterList
    }

    //등록 반 활동 데이터 참여자 데이터 가져오기(몬스터 디테일) #네트워크
    fun fetchParticipantList(
        teacher: String,
        subject: String,
        monster: String
    ): LiveData<MutableList<Student>> {
        viewModelScope.launch {
            repo.getParticipantsList(teacher, subject, monster).observeForever { students ->
                mParticipantList.value = students
            }
        }
        return mParticipantList
    }

    fun fetchMyParticipationMonsterList(myId: String): LiveData<MutableList<SchoolMonster>> {
        viewModelScope.launch {
            repo.getMyParticipationSchoolWorkList(myId).observeForever {
                mMyParticipationMonsterList.value = it
            }
        }
        return mMyParticipationMonsterList
    }

    //---------------------------------------------------------------------------------------내부로직
    //교사 선택하기
    fun selectTeacher(teacher: String) = viewModelScope.launch {
        mSelectedTeacher.value = teacher
    }

    //선택한 교사 받기
    fun fetchSelectedTeacher(): LiveData<String> {
        return mSelectedTeacher
    }

    //과목 선택하기
    fun selectSubject(subject: String) = viewModelScope.launch {
        mSelectedSubject.value = subject
    }

    //선택한 과목 받기
    fun fetchSelectedSubject(): LiveData<String> {
        return mSelectedSubject
    }

    //학생이 등록한 반 리스트 프래그에서 반 선택하기
    fun selectClass(schoolClass: SchoolClass) = viewModelScope.launch {
        mSelectedClass.value = schoolClass
    }

    //몬스터 리스트 프래그에서 선택한 반 받기
    fun fetchSelectedClass(): LiveData<SchoolClass> {
        return mSelectedClass
    }

    //몬스터 리스트 프래그에서 몬스터 선택하기
    fun selectMonster(monster: SchoolMonster) = viewModelScope.launch {
        mSelectedMonster.value = monster
    }

    //몬스터 디테일 프래그에서 선택한 몬스터 받기
    fun fetchSelectedMonster(): LiveData<SchoolMonster> {
        return mSelectedMonster
    }

    fun isParticipating(students: MutableList<Student>?) {

    }
}