package ru.divarteam.uchedus.ui.auth

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import ru.divarteam.uchedus.R
import ru.divarteam.uchedus.data.model.AccountRole
import ru.divarteam.uchedus.data.repository.PreferenceRepository
import ru.divarteam.uchedus.databinding.ActivityAuthBinding
import ru.divarteam.uchedus.network.RetrofitService
import ru.divarteam.uchedus.ui.main.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var retrofitService: RetrofitService

    private lateinit var binding: ActivityAuthBinding

    private val compositeDisposable = CompositeDisposable()
    private var wereAttemptToSendCode = false
    private var codeSent = false
    private var currentSelectedAccountRole = AccountRole.STUDENT
    private var currentAuthState = AuthState.STATE_AUTH

    override fun onCreate(savedInstanceState: Bundle?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        } else {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        }

        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(R.drawable.uchedus)
            .into(binding.uchedusLogo)

        binding.email.editText?.doAfterTextChanged {
            if (it.isNullOrBlank() || wereAttemptToSendCode.not())
                return@doAfterTextChanged

            if ("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$".toRegex().matches(it))
                binding.email.isErrorEnabled = false
            else
                binding.email.error = "Введён некорректный e-mail"
        }

        binding.sendCode.setOnClickListener {
            wereAttemptToSendCode = true
            binding.email.editText?.setText(binding.email.editText?.text)
            if (binding.email.isErrorEnabled.not())
                checkMailExists("${binding.email.editText?.text}")
        }

        binding.confirmCode.setOnClickListener {
            println("test")
            if (binding.code.editText?.text.isNullOrBlank())
                return@setOnClickListener

            println("test")
            confirmCode(
                "${binding.email.editText?.text}",
                "${binding.code.editText?.text}".toInt()
            )
        }

        binding.accountRole.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                binding.roleStudent.id -> {
                    currentSelectedAccountRole = AccountRole.STUDENT
                }

                binding.roleTeacher.id -> {
                    currentSelectedAccountRole = AccountRole.TEACHER
                }

                binding.roleAdmin.id -> {
                    currentSelectedAccountRole = AccountRole.ADMIN
                }
            }
        }

        binding.roleStudent.isChecked = true

        binding.telegramBot.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://t.me/uchedus_bot")
                )
            )
        }

        binding.vkBot.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://vk.com/uchedus")
                )
            )
        }

        binding.website.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://uchedus.divarteam.ru/")
                )
            )
        }
    }

    fun checkMailExists(mail: String) {
        showLoading()
        retrofitService.mailExists(mail) { response, code ->
            if (code == 200 && response != null) {
                currentAuthState = if (response.exists!!)
                    AuthState.STATE_AUTH
                else
                    AuthState.STATE_REG
                sendCode(mail)
            } else
                Toast.makeText(
                    this@AuthActivity,
                    "Произошла ошибка при отправке кода (C).",
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    fun sendCode(mail: String) {
        showLoading()
        when (currentAuthState) {
            AuthState.STATE_AUTH -> {
                retrofitService.authSendCode(mail) { response, code ->
                    if (code == 200 && response != null) {
                        codeSent = response.isDone == true
                        Toast.makeText(
                            this@AuthActivity,
                            "Код подтверждения отправлен",
                            Toast.LENGTH_LONG
                        ).show()
                    } else
                        Toast.makeText(
                            this@AuthActivity,
                            "Произошла ошибка при отправке кода (A).",
                            Toast.LENGTH_LONG
                        ).show()
                    hideLoading()
                }
            }

            AuthState.STATE_REG -> {
                retrofitService.regSendCode(mail) { response, code ->
                    if (code == 200 && response != null) {
                        codeSent = response.isDone == true
                        Toast.makeText(
                            this@AuthActivity,
                            "Код подтверждения отправлен",
                            Toast.LENGTH_LONG
                        ).show()
                    } else
                        Toast.makeText(
                            this@AuthActivity,
                            "Произошла ошибка при отправке кода (R).",
                            Toast.LENGTH_LONG
                        ).show()
                    hideLoading()
                }
            }
        }

    }

    fun confirmCode(mail: String, estimatedCode: Int) {
        showLoading()
        when (currentAuthState) {
            AuthState.STATE_AUTH ->
                retrofitService.auth(mail, estimatedCode) { response, code ->
                    if (code == 200 && response != null) {
                        if (response.roles?.joinToString(" ").toString().contains("student")) {
                            preferenceRepository.userToken = response.token.toString()
                            preferenceRepository.userId = response.intId ?: -1
                            preferenceRepository.userRole =
                                response.roles?.joinToString(" ").toString()
                            println("new token: ${response.token}")

                            Toast.makeText(
                                this@AuthActivity,
                                "Авторизация прошла успешно",
                                Toast.LENGTH_LONG
                            ).show()

                            startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                            finish()
                        } else
                            Toast.makeText(
                                this,
                                "Мобильное приложение пока только для учеников.",
                                Toast.LENGTH_SHORT
                            ).show()
                    } else
                        Toast.makeText(
                            this@AuthActivity,
                            "Произошла ошибка. $code Проверьте правильность данных",
                            Toast.LENGTH_LONG
                        ).show()

                    hideLoading()
                }

            AuthState.STATE_REG ->
                retrofitService.reg(mail, estimatedCode, currentSelectedAccountRole) { response, code ->
                    if (code == 200 && response != null) {
                        if (response.roles?.joinToString(" ").toString().contains("student")) {
                            preferenceRepository.userToken = response.token.toString()
                            preferenceRepository.userId = response.intId ?: -1
                            preferenceRepository.userRole =
                                response.roles?.joinToString(" ").toString()
                            println("new token: ${response.token}")

                            Toast.makeText(
                                this@AuthActivity,
                                "Авторизация прошла успешно",
                                Toast.LENGTH_LONG
                            ).show()

                            startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                            finish()
                        } else
                            Toast.makeText(
                                this,
                                "Мобильное приложение пока только для учеников.",
                                Toast.LENGTH_SHORT
                            ).show()
                    } else
                        Toast.makeText(
                            this@AuthActivity,
                            "Произошла ошибка. Проверьте правильность данных",
                            Toast.LENGTH_LONG
                        ).show()

                    hideLoading()
                }
        }
    }

    fun showLoading() {
        if (binding.loading.visibility == View.GONE)
            binding.loading.visibility = View.VISIBLE
    }

    fun hideLoading() {
        if (binding.loading.visibility == View.VISIBLE)
            binding.loading.visibility = View.GONE
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    enum class AuthState {
        STATE_AUTH, STATE_REG
    }
}