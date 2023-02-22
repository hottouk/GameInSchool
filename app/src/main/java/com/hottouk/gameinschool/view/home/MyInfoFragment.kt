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
import com.hottouk.gameinschool.R
import com.hottouk.gameinschool.databinding.FragmentMyInfoBinding
import com.hottouk.gameinschool.util.KeyValue
import com.hottouk.gameinschool.view.adapter.PetRvAdapter
import com.hottouk.gameinschool.view.main.MainViewModel
import com.hottouk.gameinschool.view.student.StudentViewModel


class MyInfoFragment : Fragment() {

    private var mBinding: FragmentMyInfoBinding? = null
    val binding get() = mBinding!!

    val mainViewModel: MainViewModel by activityViewModels()
    val viewModel: StudentViewModel by activityViewModels()

    private lateinit var callback: OnBackPressedCallback

    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container_view, HomeFragment())
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMyInfoBinding.inflate(layoutInflater, container, false)
        bindViews()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        callback.remove()
        mBinding = null
    }

    //---------------------------------------------------------------------------------------뷰그리기
    fun bindViews() {
        radarBtnClick()
        userProfileBtnClick()
        editMyInfoBtnClick()

        viewModel.getRadarMode().observe(viewLifecycleOwner) {
            if (it) {
                binding.chartFragmentContainer.visibility = View.VISIBLE
                binding.petContainer.visibility = View.INVISIBLE
            } else {
                binding.chartFragmentContainer.visibility = View.GONE
                binding.petContainer.visibility = View.VISIBLE
            }
        }

        mainViewModel.getMyInfo().observe(viewLifecycleOwner) { myInfo ->
            myInfo.getUserProfileImage(binding.root.context, binding.myUserProfileImageview)
            binding.mySchoolEdittext.setText(myInfo.studentSchool)
            binding.myStudentNumberTextview.text = myInfo.studentNumber
            binding.myNameTextview.text = myInfo.userName
            binding.myNicknameEdittext.setText(myInfo.studentNickname)
            binding.myBirthdayEdittext.setText(myInfo.studentBirth)

            viewModel.selectStudent(myInfo) //선택 학생 정보를 나의 정보로 한다.

            mainViewModel.fetchMyPetList().observe(viewLifecycleOwner) {
                setPetAdapter(it)
                myInfo.getCharacterAbility(it)
                binding.leadershipStudentScoreTextview.text = myInfo.leadership.toString()
                binding.academicStudentScoreTextview.text = myInfo.academicAbility.toString()
                binding.cooperationStudentScoreTextview.text = myInfo.cooperation.toString()
                binding.careerStudentScoreTextview.text = myInfo.career.toString()
                binding.sincerityStudentScoreTextview.text = myInfo.sincerity.toString()
            }
        }
    }

    private fun radarBtnClick() {
        binding.showRadarChartBtn.setOnClickListener {
            viewModel.radarModeOn()
        }
    }

    private fun userProfileBtnClick() {
        binding.myUserProfileImageview.setOnClickListener {
            acquirePermission()
        }
    }

    private fun editMyInfoBtnClick() {
        binding.editMyInfoBtn.setOnClickListener {
            mainViewModel.postEditMyInfo(binding)
            mainViewModel.postUploadPhoto(
                successHandler = {
                    Toast.makeText(context, "나의 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                },
                errorHandler = {
                    Toast.makeText(context, "사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    //---------------------------------------------------------------------------------------권한요청
    private fun acquirePermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                startContentProvider()
            }
            else -> {
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, KeyValue.REQUEST_PERMISSION_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            KeyValue.REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContentProvider()
                } else {
                    Toast.makeText(context, "권한 거부", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startContentProvider() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, KeyValue.INTENT_ACTIVITY_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            KeyValue.INTENT_ACTIVITY_RESULT_CODE -> {
                val uri = data?.data
                uri?.let {
                    binding.myUserProfileImageview.setImageURI(uri)
                    mainViewModel.selectPhotoUri(it)
                }
            }
            else -> {
                Toast.makeText(context, "사진을 가져오는데 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //-----------------------------------------------------------------------------------------어댑터

    private fun setPetAdapter(petList: MutableList<com.hottouk.gameinschool.model.network.Pet>) {
        val adapter = PetRvAdapter()
        binding.myPetRecyclerview.adapter = adapter
        adapter.setOnItemClickListener {
            val detailInfo = it.petDetailInfo
            binding.studentDetailInfoEdittext.setText(detailInfo)
        }
        adapter.submitList(petList)
    }

}