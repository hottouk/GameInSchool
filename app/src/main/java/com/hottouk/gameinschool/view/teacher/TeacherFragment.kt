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
import androidx.lifecycle.withStateAtLeast
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentTeacherBinding
import com.hottouk.gameinschool.view.home.HomeFragment
import com.hottouk.gameinschool.view.schoolmonster.SchoolClassFragment
import okhttp3.internal.wait
import kotlin.system.exitProcess

class TeacherFragment : Fragment() {

    private val viewModel: TeacherViewModel by activityViewModels()

    private var mBinding: FragmentTeacherBinding? = null
    val binding get() = mBinding!!

    //뒤로가기
    private lateinit var callback: OnBackPressedCallback
    private var waitCloseTime = 0L

    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - waitCloseTime > 1500L) {
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
        mBinding = FragmentTeacherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSignUpMode()
        observePetConfirmMode()
    }

    override fun onPause() {
        super.onPause()
        viewModel.signUpModeOff()
        viewModel.confirmModeOff()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
        callback.remove()
    }

    //-------------------------------------------------------------------------------------사용자함수
    private fun observeSignUpMode(){
        viewModel.getSignUpMode().observe(viewLifecycleOwner) {
            if (it) {
                binding.dialogFragmentContainerView.visibility = View.VISIBLE
                binding.coverLayout.visibility = View.VISIBLE
                childFragmentManager.beginTransaction()
                    .replace(
                        R.id.dialog_fragment_container_view,
                        ClassSignUpDialogFragment(),
                        "classSignUp"
                    )
                    .commit()
            } else {
                binding.dialogFragmentContainerView.visibility = View.GONE
                binding.coverLayout.visibility = View.GONE
                childFragmentManager.findFragmentByTag("classSignUp")?.let { frag ->
                    childFragmentManager.beginTransaction().remove(frag).commit()
                }
            }
        }
    }

    private fun observePetConfirmMode(){
        viewModel.getConfirmMode().observe(viewLifecycleOwner) {
            if (it) {
                binding.petConfirmFragmentContainerView.visibility = View.VISIBLE
                childFragmentManager.beginTransaction()
                    .replace(
                        R.id.pet_confirm_fragment_container_view,
                        PetConfirmationFragment(),
                        "petSignUp"
                    )
                    .commit()
            } else {
                binding.petConfirmFragmentContainerView.visibility = View.GONE
                childFragmentManager.findFragmentByTag("petSignUp")?.let { frag ->
                    childFragmentManager.beginTransaction().remove(frag).commit()
                }
            }
        }
    }
}