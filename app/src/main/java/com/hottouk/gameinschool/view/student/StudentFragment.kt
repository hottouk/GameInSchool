package com.hottouk.gameinschool.view.student

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentStudentBinding
import com.hottouk.gameinschool.model.network.Student
import com.hottouk.gameinschool.view.adapter.StudentRvAdapter
import com.hottouk.gameinschool.view.main.MainViewModel
import kotlin.system.exitProcess

class StudentFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: StudentViewModel by activityViewModels()

    private var mBinding: FragmentStudentBinding? = null
    val binding get() = mBinding!!

    //리사이클러뷰 어댑터
    private val studentRvAdapter: StudentRvAdapter by lazy {
        StudentRvAdapter()
    }

    private lateinit var callback: OnBackPressedCallback
    private var waitCloseTime = 0L

    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - waitCloseTime >= 1500) {
                    waitCloseTime = System.currentTimeMillis()
                    Toast.makeText(context, "back 버튼을 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    exitProcess(0)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.fetchEntireStudenPetList().observe(viewLifecycleOwner) {
            var studentList = it.keys.toMutableList()
            studentList.forEach { student ->
                it[student]?.let { pets -> student.getCharacterAbility(pets) } //학생의 펫 받아 합산하여 학생 경험치로 이관
            }
            setStudentAdapter(studentList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        callback.remove()
        mBinding = null
    }

    //학생 어뎁터 데이터 세팅
    private fun setStudentAdapter(studentList: MutableList<Student>) {
        val adapter = studentRvAdapter
        binding.studentRecyclerview.adapter = adapter
        adapter.submitList(studentList)
        adapter.setOnItemClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container_view, StudentDetailFragment())
                .commit()
            viewModel.selectStudent(it)
        }
    }

}