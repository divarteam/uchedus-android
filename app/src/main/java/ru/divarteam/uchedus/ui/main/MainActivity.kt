package ru.divarteam.uchedus.ui.main

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.uchedus.R
import ru.divarteam.uchedus.data.model.AccountRole
import ru.divarteam.uchedus.data.repository.PreferenceRepository
import ru.divarteam.uchedus.databinding.ActivityFragmentContainerBinding
import ru.divarteam.uchedus.ui.auth.AuthActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    private lateinit var binding: ActivityFragmentContainerBinding
    private lateinit var navController: NavController

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

        if (preferenceRepository.userToken.length < 10) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            Log.e("Token check", "There is no token!!!")
        } else if (preferenceRepository.userRole != AccountRole.STUDENT.typeString) {
            Toast.makeText(
                this,
                "Мобильное приложение пока только для учеников.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            Log.e("Token check", "Wrong role")
        } else {
            Log.e(
                "Token check", "Token is OK: ${preferenceRepository.userToken}"
            )
        }

        binding = ActivityFragmentContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.main_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        println(preferenceRepository.userRole)

        (supportFragmentManager.findFragmentById(binding.mainContainer.id) as NavHostFragment).navController.setGraph(
            when (preferenceRepository.userRole) {
                AccountRole.STUDENT.typeString ->
                    R.navigation.nav_student

                else ->
                    R.navigation.nav_teacher
            }
        )

        binding.bottomNavigation.apply {
            inflateMenu(
                when (preferenceRepository.userRole) {
                    AccountRole.STUDENT.typeString ->
                        R.menu.student_menu

                    else ->
                        R.menu.teacher_menu
                }
            )
            setupWithNavController(navController)
        }

        binding.confetti.addAnimatorListener(object : AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}

            override fun onAnimationEnd(p0: Animator) {
                val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)
                binding.confettiLayoutRoot.let {
                    TransitionManager.beginDelayedTransition(it, sharedAxis)
                }
                binding.confettiLayout.visibility = View.GONE
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {}
        })
    }

    fun showConfetti() {
        val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        binding.confettiLayoutRoot.let {
            TransitionManager.beginDelayedTransition(it, sharedAxis)
        }
        binding.confettiLayout.visibility = View.VISIBLE
        binding.confetti.playAnimation()
    }

    override fun onSupportNavigateUp() = navController.popBackStack()
}