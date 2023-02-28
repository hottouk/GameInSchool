package com.hottouk.gameinschool.repository

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hottouk.gameinschool.model.network.*
import com.hottouk.gameinschool.util.KeyValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hottouk.gameinschool.model.network.Student

class Repository {

    //로컬 Pref
    val pref: SharedPreferences =
        com.hottouk.gameinschool.App.context()
            .getSharedPreferences(KeyValue.SHARED_PREFERENCES, MODE_PRIVATE)
    val prefEditor: SharedPreferences.Editor = pref.edit()

    //원격 DB
    private val database = Firebase.database
    val teacherDB = database.reference.child(KeyValue.DB_TEACHER_USERS)
    val classDB = database.reference.child(KeyValue.DB_SCHOOL_CLASSES)
    val studentDB = database.reference.child(KeyValue.DB_STUDENTS)

    //전체 학생 데이터 불러오기 from 학생 DB FireBase
    fun getEntireStudentList(
    ): MutableLiveData<MutableList<Student>> {
        val mutableStudentData = MutableLiveData<MutableList<Student>>()
        studentDB.addValueEventListener(object : ValueEventListener {
            val listData: MutableList<Student> = mutableListOf()
            override fun onDataChange(snapshot: DataSnapshot) {
                listData.clear()
                if (snapshot.exists()) {
                    for (student in snapshot.children) {
                        val data = student.getValue(Student::class.java)
                        data?.let { listData.add(it) }
                    }
                    mutableStudentData.value = listData
                } else {
                    mutableStudentData.value = listData
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return mutableStudentData
    }

    //전체 학생&펫 데이터 불러오기 from 학생 DB FireBase
    fun getEntireStudentPetList(
    ): MutableLiveData<MutableMap<Student, MutableList<Pet>>> {
        val mutableStudentPetData = MutableLiveData<MutableMap<Student, MutableList<Pet>>>()
        studentDB.addValueEventListener(object : ValueEventListener {
            var mapData: MutableMap<Student, MutableList<Pet>> = mutableMapOf()
            override fun onDataChange(snapshot: DataSnapshot) {
                mapData.clear()
                if (snapshot.exists()) {
                    var keyData = Student() //초기화
                    for (studentAndPet in snapshot.children) { //학생
                        val studentData = studentAndPet.getValue(Student::class.java)
                        studentData?.let { keyData = it }
                        val valueData = mutableListOf<Pet>() //펫 초기화
                        for (pet in studentAndPet.child(KeyValue.DB_PETS).children) {
                            val petData = pet.getValue(Pet::class.java)
                            petData?.let { valueData.add(it) } //펫 값은 null 일 수도 있다.
                        }
                        keyData?.let { mapData.put(it, valueData) }
                    }
                    mutableStudentPetData.value = mapData
                } else {
                    mutableStudentPetData.value = mapData
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return mutableStudentPetData
    }

    //전체 교사 데이터 받아오기 from 파이어베이스
    fun getEntireTeacherList(): MutableLiveData<MutableList<Teacher>> {
        val mutableTeacherData = MutableLiveData<MutableList<Teacher>>()
        teacherDB.addValueEventListener(object : ValueEventListener {
            val listData: MutableList<Teacher> = mutableListOf()

            override fun onDataChange(snapshot: DataSnapshot) {
                listData.clear() //실시간 데이터 업데이트 시 리사이클러뷰 데이터 중복 방지
                if (snapshot.exists()) {
                    for (teacher in snapshot.children) {
                        val data = teacher.getValue(Teacher::class.java)
                        data?.let { listData.add(it) }
                    }
                    mutableTeacherData.value = listData
                } else {
                    mutableTeacherData.value = listData
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return mutableTeacherData
    }

    //선택 교사의 반 데이터 받아오기 from 파이어베이스
    fun getClassList(teacherId: String): MutableLiveData<MutableList<SchoolClass>> {
        val mutableClassData = MutableLiveData<MutableList<SchoolClass>>()
        val selectedClassDB = classDB.child(teacherId)
        selectedClassDB.addValueEventListener(object : ValueEventListener {
            val listData: MutableList<SchoolClass> = mutableListOf()
            override fun onDataChange(snapshot: DataSnapshot) {
                listData.clear()
                if (snapshot.exists()) {
                    for (schoolClass in snapshot.children) {
                        val data = schoolClass.getValue(SchoolClass::class.java)
                        data?.let { listData.add(it) }
                    }
                    mutableClassData.value = listData
                } else {
                    mutableClassData.value = listData
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return mutableClassData
    }

    //선택 반 학생 ID와 펫 데이터 받아오기 from 파이어베이스
    fun getClassMateList(
        teacherId: String,
        classId: String
    ): MutableLiveData<MutableMap<String, Pet>> {
        val mutableStudentPetData = MutableLiveData<MutableMap<String, Pet>>()
        classDB.child(teacherId).child(classId).child(KeyValue.DB_STUDENTS)
            .addValueEventListener(object : ValueEventListener {
                val mapData: MutableMap<String, Pet> = mutableMapOf()
                override fun onDataChange(snapshot: DataSnapshot) {
                    mapData.clear()
                    if (snapshot.exists()) {
                        for (pet in snapshot.children) {
                            val key = pet.key
                            val data = pet.getValue(Pet::class.java)
                            key?.let { petKey -> data?.let { data -> mapData.put(petKey, data) } }
                        }
                        mutableStudentPetData.value = mapData
                    } else {
                        mutableStudentPetData.value = mapData
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        return mutableStudentPetData
    }

    //학생 한명의 펫 데이터 불러오기 from FireBase
    fun getPetList(user: String): MutableLiveData<MutableList<Pet>> {
        val mutableMyPetData =
            MutableLiveData<MutableList<Pet>>()
        studentDB.child(user).child(KeyValue.DB_PETS)
            .addValueEventListener(object : ValueEventListener {
                val listData: MutableList<Pet> =
                    mutableListOf()

                override fun onDataChange(snapshot: DataSnapshot) {
                    listData.clear()
                    if (snapshot.exists()) {
                        for (pet in snapshot.children) {
                            val data =
                                pet.getValue(Pet::class.java)
                            data?.let { listData.add(it) }
                        }
                        mutableMyPetData.value = listData
                    } else {
                        mutableMyPetData.value = listData
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return mutableMyPetData
    }

    //학생이 선택한 교사의 반 활동 데이터 불러오기 from FireBase
    fun getMonsterList(
        teacher: String,
        subject: String
    ): MutableLiveData<MutableList<SchoolMonster>> {
        val mutableMyActivityData = MutableLiveData<MutableList<SchoolMonster>>()
        teacherDB.child(teacher).child(KeyValue.DB_SCHOOL_ACTIVITIES).child(subject)
            .addValueEventListener(object : ValueEventListener {
                val listData: MutableList<SchoolMonster> = mutableListOf()
                override fun onDataChange(snapshot: DataSnapshot) {
                    listData.clear()
                    if (snapshot.exists()) {
                        for (pet in snapshot.children) {
                            val data = pet.getValue(SchoolMonster::class.java)
                            data?.let { listData.add(it) }
                        }
                        mutableMyActivityData.value = listData
                    } else {
                        mutableMyActivityData.value = listData
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return mutableMyActivityData
    }

    //학생이 선택한 교사의 반 활동 데이터 참가자 불러오기 from FireBase
    fun getParticipantsList(
        teacherId: String,
        subjectId: String,
        monster: String,
    ): MutableLiveData<MutableList<Student>> {
        val mutableParticipantsData = MutableLiveData<MutableList<Student>>()
        teacherDB.child(teacherId).child(KeyValue.DB_SCHOOL_ACTIVITIES).child(subjectId)
            .child(monster)
            .child(KeyValue.DB_PARTICIPANTS)
            .addValueEventListener(object : ValueEventListener {
                val listData: MutableList<Student> = mutableListOf()
                override fun onDataChange(snapshot: DataSnapshot) {
                    listData.clear()
                    if (snapshot.exists()) { //데이터가 있는 경우
                        for (pet in snapshot.children) {
                            val data = pet.getValue(Student::class.java)
                            data?.let { listData.add(it) }
                        }
                        mutableParticipantsData.value = listData
                    } else { //해당 데이터가 비었을 경우
                        mutableParticipantsData.value = listData
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        return mutableParticipantsData
    }

    //나의 참가 활동 불러오기
    fun getMyParticipationSchoolWorkList(myId: String): MutableLiveData<MutableList<SchoolMonster>> {
        val mutableParticipationData = MutableLiveData<MutableList<SchoolMonster>>()
        studentDB.child(myId).child(KeyValue.DB_PARTICIPANTS)
            .addValueEventListener(object : ValueEventListener {
                val listData: MutableList<SchoolMonster> = mutableListOf()
                override fun onDataChange(snapshot: DataSnapshot) {
                    listData.clear()
                    if (snapshot.exists()) { //데이터가 있는 경우
                        for (item in snapshot.children) {
                            val data = item.getValue(SchoolMonster::class.java)
                            data?.let { listData.add(it) }
                        }
                        mutableParticipationData.value = listData
                    } else { //해당 데이터가 비었을 경우
                        mutableParticipationData.value = listData
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        return mutableParticipationData
    }

    //나의 완료활동 불러오기
    fun getMyCompleteSchoolWorkList(myId: String): MutableLiveData<MutableList<SchoolMonster>> {
        val mutableCompleteList = MutableLiveData<MutableList<SchoolMonster>>()
        studentDB.child(myId).child(KeyValue.DB_COMPLETE)
            .addValueEventListener(object : ValueEventListener {
                val listData = mutableListOf<SchoolMonster>()
                override fun onDataChange(snapshot: DataSnapshot) {
                    listData.clear()
                    if (snapshot.exists()) {
                        for (item in snapshot.children) {
                            val data = item.getValue(SchoolMonster::class.java)
                            data?.let { listData.add(it) }
                        }
                        mutableCompleteList.value = listData
                    } else {
                        mutableCompleteList.value = listData
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        return mutableCompleteList
    }

}