package ru.divarteam.uchedus.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.divarteam.uchedus.data.model.AccountRole
import ru.divarteam.uchedus.data.repository.PreferenceRepository
import ru.divarteam.uchedus.network.RetrofitService
import ru.divarteam.uchedus.network.response.UserResponse
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val preferenceRepository: PreferenceRepository,
    val retrofitService: RetrofitService
) : ViewModel() {

    private val _currentProfile = MutableLiveData<UserResponse>()
    val currentProfile: LiveData<UserResponse>
        get() = _currentProfile

    private val _profileState = MutableLiveData<ProfileState>(ProfileState.Initializing)
    val profileState: LiveData<ProfileState>
        get() = _profileState

    val accountRole: AccountRole
        get() = when (preferenceRepository.userRole) {
            AccountRole.STUDENT.typeString -> AccountRole.STUDENT
            AccountRole.TEACHER.typeString -> AccountRole.TEACHER
            else -> AccountRole.ADMIN
        }

    val userResponse = { response: UserResponse?, code: Int ->
        _profileState.postValue(
            if (code / 100 > 2) ProfileState.Error(Exception("$code"))
            else {
                response?.let {
                    _currentProfile.postValue(it)
                }
                ProfileState.Idle
            }
        )
    }

    fun loadProfile(userIntId: Int) {
        _profileState.postValue(ProfileState.Loading)
        retrofitService.getMe(preferenceRepository.userToken, userResponse)
    }

    fun updateUser(fio: String = "", division: String = "") {
        _profileState.postValue(ProfileState.Loading)
        retrofitService.updateMe(
            preferenceRepository.userToken,
            fio,
            division,
            userResponse
        )
    }

    fun clearPreferences() {
        preferenceRepository.userToken = ""
        preferenceRepository.userId = 0
        preferenceRepository.userRole = "student"
    }


}