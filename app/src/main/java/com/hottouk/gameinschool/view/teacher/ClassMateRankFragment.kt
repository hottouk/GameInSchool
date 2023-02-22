package com.hottouk.gameinschool.view.teacher

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentClassMateRankBinding
import com.hottouk.gameinschool.model.network.Pet
import com.hottouk.gameinschool.view.adapter.ClassMatePetRvAdapter
import com.hottouk.gameinschool.view.main.MainViewModel

class ClassMateRankFragment : Fragment() {

    private val viewModel: TeacherViewModel by activityViewModels()

    private var mBinding: FragmentClassMateRankBinding? = null
    val binding get() = mBinding!!

    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container_view, TeacherFragment())
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentClassMateRankBinding.inflate(inflater, container, false)
        viewModel.selectedClass.observe(viewLifecycleOwner) {
            binding.actionBar.text = "${it.className}반 학생 랭킹"
        }
        binding.dropClassBtn.setOnClickListener {
            viewModel.dropModeOn()
        }
        viewModel.dropMode.observe(viewLifecycleOwner) {
            if (it) {
                binding.coverLayout.visibility = View.VISIBLE
                binding.dialogFragmentContainerView.visibility = View.VISIBLE
            } else {
                binding.coverLayout.visibility = View.INVISIBLE
                binding.dialogFragmentContainerView.visibility = View.GONE
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchClassMateList().observe(viewLifecycleOwner) {
            var petList = mutableListOf<Pet>()
            var studentIdList = mutableListOf<String>()
            it.forEach { map ->
                studentIdList = map.keys.toMutableList() //키값 받아오기
                petList = map.values.toMutableList() //펫값 받아오기
            }
            setClassMateAdapter(petList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    private fun setClassMateAdapter(studentPetList: MutableList<Pet>) {
        val adapter = ClassMatePetRvAdapter()
        binding.classmateRankRecyclerview.adapter = adapter
        adapter.submitList(studentPetList)
    }
}