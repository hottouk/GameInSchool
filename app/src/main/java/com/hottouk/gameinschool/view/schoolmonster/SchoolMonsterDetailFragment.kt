package com.hottouk.gameinschool.view.schoolmonster

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentSchoolMonsterDetailBinding
import com.hottouk.gameinschool.model.network.Student
import com.hottouk.gameinschool.view.adapter.ParticipationStudentRvAdapter
import com.hottouk.gameinschool.view.main.MainViewModel

class SchoolMonsterDetailFragment : Fragment() {

    val mainViewModel: MainViewModel by activityViewModels()
    val viewModel: SchoolMonsterViewModel by activityViewModels()

    private var mBinding: FragmentSchoolMonsterDetailBinding? = null
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
                    .replace(R.id.main_fragment_container_view, SchoolMonsterFragment())
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSchoolMonsterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchSelectedTeacher().observe(viewLifecycleOwner) { teacher ->
            viewModel.fetchSelectedSubject().observe(viewLifecycleOwner) { subject ->
                viewModel.fetchSelectedMonster().observe(viewLifecycleOwner) { monster ->
                    monster.getMonsterImage(binding.monsterDetailImageview)
                    binding.subjectTextview.text = monster.subject
                    binding.schoolWorkTitleEditTextview.text = monster.schoolWorkTitle
                    binding.schoolWorkSimpleInfoTextview.apply {
                        this.text = monster.schoolWorkSimpleInfo
                        this.movementMethod = ScrollingMovementMethod.getInstance()
                    }
                    binding.schoolWorkDetailTextview.apply {
                        this.text = monster.schoolWorkDetailInfo
                        this.movementMethod = ScrollingMovementMethod.getInstance()
                    }
                    binding.expectedDifficultyRating.rating = monster.difficulty?.toFloat() ?: 0f
                    binding.leadershipTextview.text = monster.leadership.toString()
                    binding.academicTextview.text = monster.academicAbility.toString()
                    binding.cooperationTextview.text = monster.cooperation.toString()
                    binding.sincerityTextview.text = monster.sincerity.toString()
                    binding.careerTextview.text = monster.career.toString()
                    binding.moneyTextview.text = monster.money.toString()

                    viewModel.fetchParticipantList(teacher, subject, monster.schoolWorkTitle)
                        .observe(viewLifecycleOwner) { participants ->
                            setParticipantListAdapter(participants)
                            val participantNameList = mutableListOf<String>()
                            participants.forEach {
                                participantNameList.add(it.userId)
                            }
                            if ((participantNameList.contains(mainViewModel.getMyId()))) {
                                binding.schoolWorkParticipationBtn.text = "참여중.."
                                binding.schoolWorkParticipationBtn.setOnClickListener {
                                    mainViewModel.postDropSchoolWork(
                                        teacher,
                                        subject,
                                        monster
                                    )
                                }
                            } else {
                                binding.schoolWorkParticipationBtn.text = "참여하기"
                                binding.schoolWorkParticipationBtn.setOnClickListener {
                                    mainViewModel.postSignUpSchoolWork(
                                        teacher,
                                        subject,
                                        monster
                                    )
                                }
                            }
                        }
                }
            }
        }

    }

    private fun setParticipantListAdapter(participants: MutableList<Student>) {
        val adapter = ParticipationStudentRvAdapter()
        binding.participationRecyclerview.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.participationRecyclerview.adapter = adapter
        adapter.submitList(participants)
    }
}