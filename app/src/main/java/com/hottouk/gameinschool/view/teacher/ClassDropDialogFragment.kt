package com.hottouk.gameinschool.view.teacher

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentDialogBinding
import com.hottouk.gameinschool.view.main.MainViewModel

class ClassDropDialogFragment : Fragment() {

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
                viewModel.dropModeOff()
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
                binding.actionBar.text = "반 탈퇴하기"
                binding.questionTextview.text =
                    "${schoolClass.className}반에서 탈퇴하시겠습니까?\n펫과 생기부가 삭제되며 복구 불가능합니다."
                binding.confirmBtn.setOnClickListener {
                    mainViewModel.postDropClass(teacher.userId, schoolClass.classId)
                    Toast.makeText(
                        context,
                        "${schoolClass.className}반에서 탈퇴되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    parentFragmentManager.beginTransaction()
                        .remove(this)
                        .commit()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container_view,TeacherFragment())
                        .commit()
                    viewModel.dropModeOff()
                }
            }
        }
        binding.cancelBtn.setOnClickListener {
            viewModel.dropModeOff()
        }
        return binding.root
    }
}