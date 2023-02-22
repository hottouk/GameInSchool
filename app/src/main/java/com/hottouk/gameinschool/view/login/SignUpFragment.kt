package com.hottouk.gameinschool.view.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentSignUpBinding
import com.hottouk.gameinschool.model.network.Student
import com.hottouk.gameinschool.util.KeyValue
import com.hottouk.gameinschool.view.main.MainActivity

class SignUpFragment : Fragment() {

    val viewModel: LoginViewModel by activityViewModels()

    private var mBinding: FragmentSignUpBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        bindViews()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    private fun bindViews() {
        binding.characterSelectionMaleImageview.setOnClickListener {
            viewModel.characterImage = "남"
            it.setBackgroundResource(R.drawable.draw_selected_outline)
            binding.characterSelectionFemaleImageview.setBackgroundResource(R.drawable.draw_outlines)
        }
        binding.characterSelectionFemaleImageview.setOnClickListener {
            viewModel.characterImage = "여"
            it.setBackgroundResource(R.drawable.draw_selected_outline)
            binding.characterSelectionMaleImageview.setBackgroundResource(R.drawable.draw_outlines)
        }
        binding.signUpBtn.setOnClickListener {
            viewModel.addInfoToStudentUser(binding)
            viewModel.saveUserPref() //Pref에 저장
            viewModel.userToFirebase() //DB에 저장
            moveToMainPage(viewModel.fetchCurrentUser())
        }
    }

    //페이지 이동
    private fun moveToMainPage(studentUser: Student) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(KeyValue.INTENT_EXTRA_USER_INFO, studentUser)
        startActivity(intent)
    }
}