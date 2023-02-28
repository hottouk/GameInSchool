package com.hottouk.gameinschool.view.teacher

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentTeacherClassBinding
import com.hottouk.gameinschool.model.network.SchoolClass
import com.hottouk.gameinschool.view.adapter.TeacherClassRvAdapter
import com.hottouk.gameinschool.view.main.MainViewModel

class TeacherClassInnerFragment : Fragment() {

    private val viewModel: TeacherViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private var mBinding: FragmentTeacherClassBinding? = null
    val binding get() = mBinding!!

    private val teacherClassRvAdapter by lazy {
        TeacherClassRvAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTeacherClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedTeacher.observe(viewLifecycleOwner) { selectedTeacher ->
            viewModel.fetchTeacherClassList(selectedTeacher.userId)
                .observe(viewLifecycleOwner) {
                    setTeacherClassListAdapter(it)
                }
        }
    }

    //교사 반 어뎁터 세팅
    private fun setTeacherClassListAdapter(teacherClassList: MutableList<SchoolClass>) {
        val adapter = teacherClassRvAdapter
        binding.classInnerRecyclerview.adapter = adapter
        binding.classInnerRecyclerview.layoutManager = GridLayoutManager(context, 3)
        adapter.submitList(teacherClassList)
        adapter.setOnItemClickListener { schoolClass ->
            mainViewModel.getMyInfo().observe(viewLifecycleOwner) { myInfo ->
                viewModel.signUpCheck(myInfo, schoolClass).observe(viewLifecycleOwner) { result ->
                    viewModel.selectClass(schoolClass)
                    if (result) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.main_fragment_container_view, ClassMateRankFragment())
                            .commit()
                    } else {
                        viewModel.signUpModeOn()
                    }
                }
            }
        }
    }

}