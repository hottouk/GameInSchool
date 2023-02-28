package com.hottouk.gameinschool.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.hottouk.gameinschool.databinding.ActivityLogInActivityBinding
import com.hottouk.gameinschool.view.main.MainActivity
import com.hottouk.gameinschool.model.network.Student
import com.hottouk.gameinschool.util.KeyValue
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LogInActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()

    private var mBinding: ActivityLogInActivityBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLogInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var keyHash = Utility.getKeyHash(this)
        Log.d("로그인",keyHash)
        binding.kakaoLoginBtn.setOnClickListener {
            checkKaKaoLogin()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    //카카오 로그인 체크
    fun checkKaKaoLogin() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error -> //쓰레드
            if (error != null) { //토큰 정보 보기 실패
                Log.d("로그인", "카카오 세션 실패")
                kakaoInitLogin()
            } else if (tokenInfo != null) { //카카오 토큰 정보가 있는 경우
                fetchKakaoUserInfo()
                Toast.makeText(this, "저장된 카카오톡으로 로그인합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //카카오 처음 로그인
    private fun kakaoInitLogin() { //계정 로그인 콜백
        val kakaoLogincallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) { //계정으로 로그인한 경우
                Log.e("로그인", "카카오계정으로 로그인 실패", error)
                Toast.makeText(this, "카카오 계정으로 로그인하는데 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                fetchKakaoUserInfo()
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("로그인", "카카오톡으로 로그인 실패", error)
                    Toast.makeText(this, "본 기기에 인식되는 카카오톡 어플이 없습니다.", Toast.LENGTH_SHORT).show()
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡 로그인에 에러 나서, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        this,
                        callback = kakaoLogincallback
                    )
                } else if (token != null) { //카카오톡 어플로 로그인 성공한 경우
                    fetchKakaoUserInfo()
                }
            }
        } else { //카카오톡이 없어 계정으로 로그인 시도
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoLogincallback)
        }
    }

    //인증 성공하여 카카오 사용자 정보 불러오기
    private fun fetchKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Toast.makeText(this, "카카오 사용자 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            } else if (user != null) { // 사용자정보 요청 성공
                viewModel.savedKakaoUserInfo = com.hottouk.gameinschool.model.kakao.KakaoUserInfo(
                    userId = user.id.toString(),
                    userEmail = user.kakaoAccount?.email,
                    user.kakaoAccount?.name,
                    userNickName = user.kakaoAccount?.profile?.nickname,
                    userProfileImageUrl = user.kakaoAccount?.profile?.thumbnailImageUrl
                )

                viewModel.checkCurrentMember()
                viewModel.isCurrentUser.observe(this@LogInActivity) {
                    if (it) { //기존유저
                        Log.d("로그인", "카카오 기존")
                        viewModel.setUpKakaoFlag() //카카오 아이디로 접속
                        val studentUser = viewModel.getUserInfoFromPref(binding)
                        moveToMainPage(studentUser!!)
                    } else { //신규유저
                        Log.d("로그인", "카카오 신규")
                        viewModel.setUpKakaoFlag() //카카오 아이디로 접속
                        viewModel.kakaoUserToStudentUser()
                        binding.signUpFragmentContainer.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun moveToMainPage(studentUser: Student) {
        val intent = Intent(this@LogInActivity, MainActivity::class.java)
        intent.putExtra(KeyValue.INTENT_EXTRA_USER_INFO, studentUser)
        startActivity(intent)
    }
}