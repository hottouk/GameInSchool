package com.hottouk.gameinschool.view.teacher

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.hottouk.gameinschool.databinding.FragmentLastConfirmBinding
import com.hottouk.gameinschool.view.main.MainViewModel

class PetConfirmationFragment : Fragment() {

    private val viewModel: TeacherViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private var mBinding: FragmentLastConfirmBinding? = null
    val binding get() = mBinding!!

    //뒤로가기
    private lateinit var callback: OnBackPressedCallback

    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            //뒤로가기 버튼 클릭 시
            override fun handleOnBackPressed() {
                viewModel.signUpModeOff()
                viewModel.confirmModeOff()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentLastConfirmBinding.inflate(inflater, container, false)
        viewModel.selectedType.observe(viewLifecycleOwner) { type ->
            //중첩하여 사용시는 상위 자료가 바뀌지 않으면 하위 코드는 실행되지 않는다. 따라서 뷰 변경이 즉각 필요하다면 코드를 재작성한다.
            when (type) {
                "grass" -> {
                    binding.questionTextview.text =
                        "풀의 아이를 당신의 펫으로 하시겠습니까? \n펫은 한번 결정되면 바꿀 수 없습니다."
                }
                "water" -> {
                    binding.questionTextview.text =
                        "물의 아이를 당신의 펫으로 하시겠습니까? \n펫은 한번 결정되면 바꿀 수 없습니다."
                }
                else -> {
                    binding.questionTextview.text =
                        "불의 아이를 당신의 펫으로 하시겠습니까? \n펫은 한번 결정되면 바꿀 수 없습니다."
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedTeacher.observe(viewLifecycleOwner) { teacher ->
            viewModel.selectedClass.observe(viewLifecycleOwner) { schoolClass ->
                viewModel.selectedType.observe(viewLifecycleOwner) { type ->
                    binding.signUpBtn.setOnClickListener {
                        mainViewModel.postSignUpClass(teacher.userId, schoolClass) //반 DB에 등록
                        mainViewModel.postPetImageData(teacher.userId, schoolClass.classId, type) //학생 DB에 펫 등록
                        viewModel.signUpModeOff()
                        viewModel.confirmModeOff()
                    }
                }
            }
        }

        binding.cancelBtn.setOnClickListener {
            viewModel.signUpModeOff()
            viewModel.confirmModeOff()
        }
    }
}