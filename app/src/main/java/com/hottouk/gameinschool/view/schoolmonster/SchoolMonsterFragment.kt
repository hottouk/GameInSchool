package com.hottouk.gameinschool.view.schoolmonster

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentSchoolMonsterBinding
import com.hottouk.gameinschool.model.network.SchoolMonster
import com.hottouk.gameinschool.model.network.Student
import com.hottouk.gameinschool.view.adapter.SchoolMonsterRvAdapter
import com.hottouk.gameinschool.view.main.MainViewModel

class SchoolMonsterFragment : Fragment() {

    val viewModel: SchoolMonsterViewModel by activityViewModels()
    val mainViewModel: MainViewModel by activityViewModels()

    private var mBinding: FragmentSchoolMonsterBinding? = null
    val binding get() = mBinding!!

    private val adapter: SchoolMonsterRvAdapter by lazy {
        SchoolMonsterRvAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSchoolMonsterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchSelectedClass().observe(viewLifecycleOwner) { selectedClass -> //선택 반 받기
            val teacherId = selectedClass.madeBy
            val subject = selectedClass.subject
            viewModel.fetchMonsterList(teacherId, subject).observe(viewLifecycleOwner) { monsters ->
                viewModel.fetchMyParticipationMonsterList(mainViewModel.getMyId())
                    .observe(viewLifecycleOwner) { participatingWorks ->
                        adapter.getParticipatingMonsterList(participatingWorks)
                    }
                viewModel.fetchMyCompleteMonsterList(mainViewModel.getMyId())
                    .observe(viewLifecycleOwner) { completeWorks ->
                        adapter.getCompleteMonsterList(completeWorks)
                    }
                setMonsterListAdapter(monsters)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수
    //몬스터 어뎁터 세팅
    private fun setMonsterListAdapter(monsterList: MutableList<SchoolMonster>) {
        binding.schoolMonsterRecyclerview.adapter = adapter
        binding.schoolMonsterRecyclerview.layoutManager = LinearLayoutManager(context)
        adapter.setOnItemClickListener {
            viewModel.selectMonster(it)
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container_view, SchoolMonsterDetailFragment())
                .commit()
        }
        adapter.submitList(monsterList)
    }
}