package com.hottouk.gameinschool.view.schoolmonster

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentSchoolClassBinding
import com.hottouk.gameinschool.model.network.SchoolClass
import com.hottouk.gameinschool.view.adapter.StudentClassRvAdapter
import com.hottouk.gameinschool.view.main.MainViewModel
import kotlin.system.exitProcess

class SchoolClassFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    val viewModel: SchoolMonsterViewModel by activityViewModels()

    private var mBinding: FragmentSchoolClassBinding? = null
    val binding get() = mBinding!!

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
    ): View {
        mBinding = FragmentSchoolClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchMyPetList(mainViewModel.getMyId())
            .observe(viewLifecycleOwner) { pets ->
                val schoolClassList = mutableListOf<SchoolClass>()
                for (pet in pets) {
                    val teacherId = pet.petId.split("-")[0]
                    val subject = pet.petId.split("-")[1]
                    val classTitle = pet.petId.split("-")[2]
                    val schoolClass =
                        SchoolClass(className = classTitle, subject = subject, madeBy = teacherId)
                    schoolClassList.add(schoolClass)
                }
                setClassListAdapter(schoolClassList)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        callback.remove()
        mBinding = null
    }

    //학생 등록 반 어뎁터 세팅
    private fun setClassListAdapter(classList: MutableList<SchoolClass>) {
        val adapter = StudentClassRvAdapter()
        binding.studentClassOuterRecyclerview.adapter = adapter
        binding.studentClassOuterRecyclerview.layoutManager = GridLayoutManager(context, 3)
        adapter.submitList(classList)
        adapter.setOnItemClickListener { schoolClass ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container_view, SchoolMonsterFragment())
                .commit()
            viewModel.selectClass(schoolClass)
            viewModel.selectTeacher(schoolClass.madeBy)
            viewModel.selectSubject(schoolClass.subject)
        }
    }
}