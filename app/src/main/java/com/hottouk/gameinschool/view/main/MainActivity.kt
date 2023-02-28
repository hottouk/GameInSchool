package com.hottouk.gameinschool.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hottouk.gameinschool.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hottouk.gameinschool.databinding.ActivityMainBinding
import com.hottouk.gameinschool.model.network.Student
import com.hottouk.gameinschool.util.KeyValue
import com.hottouk.gameinschool.view.home.HomeFragment
import com.hottouk.gameinschool.view.schoolmonster.SchoolClassFragment
import com.hottouk.gameinschool.view.student.StudentFragment
import com.hottouk.gameinschool.view.teacher.ClassMateRankFragment
import com.hottouk.gameinschool.view.teacher.TeacherFragment

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    //프래그먼트
    private val homeFragment = HomeFragment()
    private val teacherFragment = TeacherFragment()
    private val schoolClassFragment = SchoolClassFragment()
    private val studentFragment = StudentFragment()
    private val classMateFragment = ClassMateRankFragment()

    //UI관련
    private val bottomNavigationView: BottomNavigationView by lazy {
        findViewById(R.id.bottom_navigation_view)
    }

    //---------------------------------------------------------------------------------------생명주기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //유저 정보 뷰모델로 전달
        val user = intent.getParcelableExtra<Student>(KeyValue.INTENT_EXTRA_USER_INFO)
        if (user != null) {
            viewModelFactory = MainViewModelFactory(user) //main에서 viewModel로 값 전달
            viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        }
        initViews()
    }

    //--------------------------------------------------------------------------------------사용자함수
    private fun initViews() {
        replaceFragment(homeFragment)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_menu -> replaceFragment(homeFragment)
                R.id.teacher_supporter_menu -> replaceFragment(teacherFragment)
                R.id.schoolwork_monster_menu -> replaceFragment(schoolClassFragment)
                R.id.student_member_menu -> replaceFragment(studentFragment)
            }
            true
        }
    }

    //프래그먼트 교체
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.main_fragment_container_view, fragment)
                commit()
            }
    }
}