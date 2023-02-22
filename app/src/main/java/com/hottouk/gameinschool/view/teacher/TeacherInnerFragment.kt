package com.hottouk.gameinschool.view.teacher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.hottouk.gameinschool.databinding.FragmentTeacherInnerBinding
import com.hottouk.gameinschool.model.network.Teacher
import com.hottouk.gameinschool.view.adapter.TeacherOuterRvAdapter
import com.hottouk.gameinschool.view.main.MainViewModel

class TeacherInnerFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: TeacherViewModel by activityViewModels()

    private var mBinding: FragmentTeacherInnerBinding? = null
    val binding get() = mBinding!!

    //리사이클러뷰 어댑터
    private val teacherRvAdapter: TeacherOuterRvAdapter by lazy {
        TeacherOuterRvAdapter()
    }

    //---------------------------------------------------------------------------------------생명주기

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTeacherInnerBinding.inflate(inflater, container, false)
        mainViewModel.fetchEntireTeacherList().observe(viewLifecycleOwner) {
            setTeacherListAdapter(it)
        }
        return binding.root
    }

    //교사 어뎁터 세팅
    private fun setTeacherListAdapter(teacherList: MutableList<Teacher>) {
        val adapter = teacherRvAdapter
        binding.teacherOuterRecyclerview.adapter = adapter
        adapter.submitList(teacherList)
        adapter.setOnItemClickListener { teacher ->
            viewModel.selectTeacher(teacher)
        }
    }
}