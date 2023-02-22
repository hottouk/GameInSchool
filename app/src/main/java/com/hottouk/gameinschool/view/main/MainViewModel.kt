package com.hottouk.gameinschool.view.main

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hottouk.gameinschool.repository.Repository
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.hottouk.gameinschool.App
import com.hottouk.gameinschool.databinding.FragmentMyInfoBinding
import com.hottouk.gameinschool.model.network.*
import com.hottouk.gameinschool.util.KeyValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val user: Student) : ViewModel() {

    private val repo = Repository()

    private val firebaseStorage: FirebaseStorage by lazy { Firebase.storage }
    private val studentDB = Firebase.database.reference.child(KeyValue.DB_STUDENTS)
    private val teacherDB = Firebase.database.reference.child(KeyValue.DB_TEACHER_USERS)
    private val classDB = Firebase.database.reference.child(KeyValue.DB_SCHOOL_CLASSES)

    private var mMyInfo = MutableLiveData<Student>()
    private val myInfo get() = mMyInfo.value!!

    //사진 선택하기
    private val mSelectedPhotoUri = MutableLiveData<Uri>()

    init {
        getMyInfo()
    }

    fun selectPhotoUri(uri: Uri) = viewModelScope.launch {
        mSelectedPhotoUri.value = uri
    }

    fun getSelectedPhotoUri(): LiveData<Uri> = mSelectedPhotoUri

    fun getMyId(): String {
        return user.userId
    }

    fun getMyInfo(): LiveData<Student> {
        viewModelScope.launch {
            repo.getEntireStudentList().observeForever() { students ->
                students.forEach {
                    if (it.userId == user.userId) {
                        mMyInfo.value = it
                    }
                }
            }
        }
        return mMyInfo
    }

    //전체 학생들 데이터 불러오기 from FireBase #네트워크
    fun fetchEntireStudentList(): LiveData<MutableList<Student>> {
        val entireStudentList = MutableLiveData<MutableList<Student>>()
        viewModelScope.launch {
            repo.getEntireStudentList().observeForever {
                entireStudentList.value = it
            }
        }
        return entireStudentList
    }

    //전체 교사 데이터 불러오기 from FireBase #네트워크
    fun fetchEntireTeacherList(): LiveData<MutableList<Teacher>> {
        val entireTeacherList = MutableLiveData<MutableList<Teacher>>()
        viewModelScope.launch {
            repo.getEntireTeacherList().observeForever {
                entireTeacherList.value = it
            }
        }
        return entireTeacherList
    }

    //나의 펫 데이터 가져오기(등록한 반 가져오기) #네트워크
    fun fetchMyPetList(): LiveData<MutableList<Pet>> {
        val myPetList = MutableLiveData<MutableList<Pet>>()
        viewModelScope.launch {
            repo.getPetList(user.userId).observeForever {
                myPetList.value = it
            }
        }
        return myPetList
    }


    //선택한 교사 반에 가입하기 #네트워크
    fun postSignUpClass(teacherId: String, schoolClass: SchoolClass) {
        viewModelScope.launch {
            val subject = schoolClass.subject
            val newPet = Pet(
                subject = subject,
                belongToUser = myInfo.userId,
                userName = myInfo.userName,
                userStudentNumber = myInfo.studentNumber,
            ).apply { combinePetId(teacherId, schoolClass.classId, user.userId) }

            //학생 DB에 펫 등록
            studentDB.child(getMyId()).child(KeyValue.DB_PETS).child(newPet.petId)
                .setValue(newPet)

            //반 DB에 학생 펫 등록
            val classDB = Firebase.database.reference.child(KeyValue.DB_SCHOOL_CLASSES)
            classDB.child(teacherId).child(schoolClass.classId).child(KeyValue.DB_STUDENTS)
                .child(getMyId()).setValue(newPet)
        }
    }

    //반 탈퇴하기 #네트워크
    fun postDropClass(teacherId: String, classId: String) = viewModelScope.launch {
        val petId = "$classId-${getMyId()}"
        Log.d("탈퇴", petId)
        repo.classDB.child(teacherId).child(classId).child(KeyValue.DB_STUDENTS).child(user.userId)
            .removeValue()
        repo.studentDB.child(getMyId()).child(KeyValue.DB_PETS).child(petId).removeValue()
    }

    //펫 이미지 타입 등록하기 #네트워크 todo 코드 간결화하기
    fun postPetImageData(teacherId: String, classId: String, type: String) =
        viewModelScope.launch {
            classDB.child(teacherId).child(classId).child(KeyValue.DB_STUDENTS).child(getMyId())
                .child(KeyValue.DB_PROPERTY_PET_IMG).setValue(type) //반DB 저장

            val petId = com.hottouk.gameinschool.model.network.Pet()
                .combinePetId(teacherId, classId, user.userId)
            studentDB.child(user.userId).child(KeyValue.DB_PETS).child(petId)
                .child(KeyValue.DB_PROPERTY_PET_IMG).setValue(type) //학생 DB 저장
            delay(1000)
        }

    //선택 활동에 참가 등록하기 #네트워크
    fun postSignUpSchoolWork(teacher: String, subject: String, monster: SchoolMonster) =
        viewModelScope.launch {
            teacherDB.child(teacher).child(KeyValue.DB_SCHOOL_ACTIVITIES).child(subject)
                .child(monster.schoolWorkTitle).child(KeyValue.DB_PARTICIPANTS).child(getMyId())
                .setValue(myInfo) //활동에 본인 등록하기
            studentDB.child(user.userId).child(KeyValue.DB_PARTICIPANTS)
                .child(monster.schoolWorkTitle)
                .setValue(monster) //본인 DB에 진행중 활동 기록하기
        }

    //선택 몬스터 활동에서 참가 삭제하기 #네트워크
    fun postDropSchoolWork(teacher: String, subject: String, monster: SchoolMonster) =
        viewModelScope.launch {
            teacherDB.child(teacher).child(KeyValue.DB_SCHOOL_ACTIVITIES).child(subject)
                .child(monster.schoolWorkTitle).child(KeyValue.DB_PARTICIPANTS).child(getMyId())
                .removeValue() //활동에 본인 등록하기
            studentDB.child(user.userId).child(KeyValue.DB_PARTICIPANTS)
                .child(monster.schoolWorkTitle)
                .removeValue() //본인 DB에 진행중 활동 기록하기
        }

    //정보 수정하기 #네트워크
    fun postEditMyInfo(
        binding: FragmentMyInfoBinding,
    ) = viewModelScope.launch {
        val newSchool = binding.mySchoolEdittext.text.toString()
        val newNick = binding.myNicknameEdittext.text.toString()
        val newBirth = binding.myBirthdayEdittext.text.toString()
        studentDB.child(user.userId).child("studentSchool").setValue(newSchool)
        studentDB.child(user.userId).child("studentNickname").setValue(newNick)
        studentDB.child(user.userId).child("studentBirth").setValue(newBirth)
    }

    //프로필 사진 수정하기 #네트워크
    fun postUploadPhoto(successHandler: () -> Unit, errorHandler: () -> Unit) =
        viewModelScope.launch {
            val fileName = "${getMyId()}UserProfileImg.png"
            val photoUri = mSelectedPhotoUri.value
            if (photoUri != null) {
                firebaseStorage.reference.child("${getMyId()}/profileImg").child(fileName) //사진 저장
                    .putFile(photoUri).addOnCompleteListener {
                        if (it.isSuccessful) {
                            firebaseStorage.reference.child("${getMyId()}/profileImg")
                                .child(fileName).downloadUrl.addOnSuccessListener { uri ->
                                    successHandler()
                                    val newUrl = uri.toString()
                                    studentDB.child(user.userId).child("userProfileImageUrl")
                                        .setValue(newUrl)
                                }.addOnFailureListener {
                                    errorHandler()
                                }
                        } else {
                            errorHandler()
                        }
                    }
            } else {
                Toast.makeText(App.context(), "사진 외의 정보가 수정되었습니다", Toast.LENGTH_SHORT).show()
            }
        }


    fun makeMockData(number: Int) {
        for (i in 1..number) {
            val studentPet = Pet(
                "",
                "묭묭$i",
                "국어",
                "grass",
                "student$i",
                "student$i"
            )
            repo.classDB.child("kakao2618040363T") //todo 여기도 변수 받아야함.
                .child("국어-104")
                .child(KeyValue.DB_STUDENTS)
                .child("student$i").setValue(studentPet)
        }
    }
}