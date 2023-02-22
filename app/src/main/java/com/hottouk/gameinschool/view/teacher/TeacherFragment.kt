package com.hottouk.gameinschool.view.teacher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentTeacherBinding
import com.hottouk.gameinschool.view.main.MainViewModel

class TeacherFragment : Fragment() {

    private val viewModel: TeacherViewModel by activityViewModels()

    private var mBinding: FragmentTeacherBinding? = null
    val binding get() = mBinding!!

    //---------------------------------------------------------------------------------------생명주기

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTeacherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSignUpMode().observe(viewLifecycleOwner) {
            if (it) {
                binding.dialogFragmentContainerView.visibility = View.VISIBLE
                binding.coverLayout.visibility = View.VISIBLE

            } else {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.dialog_fragment_container_view, ClassSignUpDialogFragment())
                    .commit()
                binding.dialogFragmentContainerView.visibility = View.GONE
                binding.coverLayout.visibility = View.GONE
            }
        }

        viewModel.getConfirmMode().observe(viewLifecycleOwner) {
            if (it) {
                binding.petConfirmFragmentContainerView.visibility = View.VISIBLE
            } else {
                binding.petConfirmFragmentContainerView.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}