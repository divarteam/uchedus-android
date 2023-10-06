package ru.divarteam.uchedus.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.divarteam.uchedus.data.model.AccountRole
import ru.divarteam.uchedus.data.repository.PreferenceRepository
import ru.divarteam.uchedus.network.RetrofitService
import ru.divarteam.uchedus.network.response.UserResponse
import ru.divarteam.uchedus.ui.profile.ProfileState
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val retrofitService: RetrofitService
) : ViewModel() {

    private val _currentProfile = MutableLiveData<UserResponse>()
    val currentProfile: LiveData<UserResponse>
        get() = _currentProfile

    private val _statsState = MutableLiveData<StatsState>(StatsState.Initializing)
    val statsState: LiveData<StatsState>
        get() = _statsState
    
    val accountRole: AccountRole
        get() = when (preferenceRepository.userRole) {
            AccountRole.STUDENT.typeString -> AccountRole.STUDENT
            AccountRole.TEACHER.typeString -> AccountRole.TEACHER
            else -> AccountRole.ADMIN
        }

    fun loadProfile(userIntId: Int) {
        _statsState.postValue(StatsState.Loading)
        retrofitService.getMe(preferenceRepository.userToken) { response, code ->
            _statsState.postValue(
                if (code / 100 > 2) StatsState.Error(Exception("$code"))
                else {
                    response?.let {
                        _currentProfile.postValue(it)
                    }
                    StatsState.Idle
                }
            )
        }
    }
}