package com.hottouk.gameinschool.view.teacher

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentDialogBinding
import com.hottouk.gameinschool.view.main.MainViewModel

class ClassSignUpDialogFragment : Fragment() {

    private val viewModel: TeacherViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private var mBinding: FragmentDialogBinding? = null
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
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentDialogBinding.inflate(inflater, container, false)
        viewModel.selectedTeacher.observe(viewLifecycleOwner) { teacher ->
            viewModel.selectedClass.observe(viewLifecycleOwner) { schoolClass ->
                binding.questionTextview.text =
                    "${teacher.userName} 선생님의 ${schoolClass.className}반에 등록하시겠습니까?"
                binding.confirmBtn.setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.dialog_fragment_container_view, SelectYourPetDialogFragment())
                        .commit()
                }
            }
        }
        binding.cancelBtn.setOnClickListener {
            viewModel.signUpModeOff()
        }
        return binding.root
    }
}