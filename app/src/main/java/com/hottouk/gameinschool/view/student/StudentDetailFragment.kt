package com.hottouk.gameinschool.view.student

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentStudentDetailBinding
import com.hottouk.gameinschool.view.adapter.PetRvAdapter
import com.hottouk.gameinschool.view.main.MainViewModel


class StudentDetailFragment : Fragment() {

    val viewModel: StudentViewModel by activityViewModels()
    val mainViewModel: MainViewModel by activityViewModels()

    private var mBinding: FragmentStudentDetailBinding? = null
    val binding get() = mBinding!!

    //뒤로가기
    private lateinit var callback: OnBackPressedCallback

    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            //뒤로가기 버튼 클릭 시
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction() //이전 프래그먼트로 돌아간다.
                    .replace(R.id.main_fragment_container_view, StudentFragment())
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStudentDetailBinding.inflate(inflater, container, false)
        bindViews()
        return binding.root
    }


    fun bindViews() {
        binding.showRadarChartBtn.setOnClickListener {
            viewModel.radarModeOn()
        }
        viewModel.getRadarMode().observe(viewLifecycleOwner) {
            if (it) {
                binding.chartFragmentContainer.visibility = View.VISIBLE
                binding.petContainer.visibility = View.INVISIBLE
            } else {
                binding.chartFragmentContainer.visibility = View.GONE
                binding.petContainer.visibility = View.VISIBLE
            }
        }
        viewModel.fetchSelectedStudent().observe(viewLifecycleOwner) { student ->
            binding.studentDetailNumberTextview.text = student.studentNumber
            binding.studentDetailNameTextview.text = "LV${student.studentLevel} ${student.userName}"
            binding.leadershipStudentScoreTextview.text = student.leadership.toString()
            binding.academicStudentScoreTextview.text = student.academicAbility.toString()
            binding.careerStudentScoreTextview.text = student.career.toString()
            binding.cooperationStudentScoreTextview.text = student.cooperation.toString()
            binding.sincerityStudentScoreTextview.text = student.sincerity.toString()
            student.getCharacterImage(binding.root.context, binding.studentDetailCharacterImageview)
            viewModel.fetchStudentPetList(student.userId).observe(viewLifecycleOwner) {
                student.getCharacterAbility(it)
                setPetAdapter(it)
                binding.leadershipStudentScoreTextview.text = student.leadership.toString()
                binding.academicStudentScoreTextview.text = student.academicAbility.toString()
                binding.cooperationStudentScoreTextview.text = student.cooperation.toString()
                binding.careerStudentScoreTextview.text = student.career.toString()
                binding.sincerityStudentScoreTextview.text = student.sincerity.toString()
            }
        }
    }

    private fun setPetAdapter(petList: MutableList<com.hottouk.gameinschool.model.network.Pet>) {
        val adapter = PetRvAdapter()
        binding.petRecyclerview.adapter = adapter
        adapter.submitList(petList)
        adapter.setOnItemClickListener {
            binding.studentDetailInfoTextview.text = it.petDetailInfo
            binding.studentAchievedQuestTextview.text = it.petSimpleInfo
        }
    }
}