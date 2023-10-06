package ru.divarteam.uchedus.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.uchedus.databinding.FragmentProfileBinding
import ru.divarteam.uchedus.ui.auth.AuthActivity
import ru.divarteam.uchedus.ui.main.MainActivity

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    val profileViewModel: ProfileViewModel by viewModels()
    val args: ProfileFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileViewModel.loadProfile(args.userIntId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProfileBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        profileViewModel.profileState.observe(viewLifecycleOwner) {
            when (it) {
                is ProfileState.Error -> {
                    it.e.printStackTrace()
                }
                ProfileState.Idle -> {
                    binding.loading.root.visibility = View.GONE
                }
                ProfileState.Initializing -> {
                    binding.loading.root.visibility = View.VISIBLE
                }
                ProfileState.Loading -> {
                    binding.loading.root.visibility = View.VISIBLE
                }
                ProfileState.Unauthorized -> TODO()
            }
        }

        profileViewModel.currentProfile.observe(viewLifecycleOwner) {
            binding.fullname.editText?.setText(it.fio)
            binding.email.editText?.setText(it.mail)
            binding.division.editText?.setText(it.division)
            binding.cost.setText("${it.coins}")

            if (it.telegramId.isNullOrBlank()) {
                binding.noTelegram.visibility = View.VISIBLE
                binding.telegram.visibility = View.GONE
            } else {
                binding.noTelegram.visibility = View.GONE
                binding.telegram.visibility = View.VISIBLE
            }
            binding.roleTv.setText(it.roles?.map {
                when (it) {
                    "student" -> "Студент"
                    "teacher" -> "Преподаватель"
                    else -> "Администратор"
                }
            }?.joinToString(", "))

            binding.loading.root.visibility = View.GONE
        }


        binding.logout.setOnClickListener {
            profileViewModel.clearPreferences()
            startActivity(Intent(context, AuthActivity::class.java))
            activity?.finish()
        }

        binding.telegramMore.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://t.me/uchedus_bot?start=${profileViewModel.preferenceRepository.userId}")
                )
            )
        }

        binding.confirmChanges.setOnClickListener {
            profileViewModel.updateUser(
                fio = binding.fullname.editText?.text.toString(),
                division = binding.division.editText?.text.toString()
            )
        }

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


}
