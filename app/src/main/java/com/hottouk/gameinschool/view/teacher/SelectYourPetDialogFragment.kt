package com.hottouk.gameinschool.view.teacher

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.hottouk.gameinschool.databinding.FragmentSelectYourPetBinding
import com.hottouk.gameinschool.view.main.MainViewModel

class SelectYourPetDialogFragment : Fragment() {

    private val viewModel: TeacherViewModel by activityViewModels()

    private var mBinding: FragmentSelectYourPetBinding? = null
    val binding get() = mBinding!!

    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.signUpModeOff()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSelectYourPetBinding.inflate(inflater, container, false)
        binding.fireSelectionBtn.setOnClickListener {
            viewModel.selectPetType("fire")
            viewModel.confirmModeOn()
        }
        binding.waterSelectionBtn.setOnClickListener {
            viewModel.selectPetType("water")
            viewModel.confirmModeOn()
        }
        binding.grassSelectionBtn.setOnClickListener {
            viewModel.selectPetType("grass")
            viewModel.confirmModeOn()
        }
        return binding.root
    }
}