package com.hottouk.gameinschool.view.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentHomeBinding
import com.hottouk.gameinschool.util.KeyValue
import com.hottouk.gameinschool.view.main.MainViewModel
import com.hottouk.gameinschool.view.student.StudentViewModel
import kotlin.system.exitProcess

class HomeFragment : Fragment() {

    private var mBinding: FragmentHomeBinding? = null
    val binding get() = mBinding!!

    val mainViewModel: MainViewModel by activityViewModels()
    val viewModel: StudentViewModel by activityViewModels()

    //뒤로가기
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
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        bindViews()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
        callback.remove()
    }
    //---------------------------------------------------------------------------------------뷰그리기

    private fun bindViews() {
        binding.mainCharacterImageview.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container_view, MyInfoFragment())
                .commit()
        }


        mainViewModel.getMyInfo().observe(viewLifecycleOwner) {
            it.getCharacterImage(requireContext(), binding.mainCharacterImageview)
            Glide.with(binding.root.context)
                .load(it.userProfileImageUrl)
                .centerCrop()
                .into(binding.userProfileImageview)
            binding.studentLevelIdTextview.text = "LV${it.studentLevel} ${it.userName}"
        }

        mainViewModel.fetchMyPetList().observe(viewLifecycleOwner) {
            binding.numberOfClassTextview.text = "${it.size}개의 반에 등록되어 있습니다."
        }
    }

}