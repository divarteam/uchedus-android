package ru.divarteam.uchedus.ui.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.divarteam.uchedus.data.model.AccountRole
import ru.divarteam.uchedus.data.repository.PreferenceRepository
import ru.divarteam.uchedus.network.RetrofitService
import ru.divarteam.uchedus.network.response.AnswerResponse
import ru.divarteam.uchedus.network.response.CourseResponse
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val retrofitService: RetrofitService,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _courseState: MutableLiveData<CourseState> =
        MutableLiveData(CourseState.Initializing)
    val courseState: LiveData<CourseState>
        get() = _courseState

    private val _currentCourse = MutableLiveData<CourseResponse>()
    val currentCourse: LiveData<CourseResponse>
        get() = _currentCourse

    val accountRole: AccountRole
        get() = when (preferenceRepository.userRole) {
            AccountRole.STUDENT.typeString -> AccountRole.STUDENT
            AccountRole.TEACHER.typeString -> AccountRole.TEACHER
            else -> AccountRole.ADMIN
        }

    fun updateCurrentCourse(courseIntId: Int) {
        retrofitService.getCourseByIntId(
            token = preferenceRepository.userToken, courseIntId = courseIntId
        ) { response, code ->

            _courseState.postValue(
                if (code / 100 > 2) CourseState.Error(Exception("$code"))
                else {
                    _currentCourse.postValue(response!!)
                    CourseState.Idle
                }
            )
        }
    }


    fun joinCourse(courseIntId: Int) {
        _courseState.postValue(CourseState.Loading)
        retrofitService.joinCourse(preferenceRepository.userToken, courseIntId) { response, code ->
            updateCurrentCourse(currentCourse.value?.intId ?: 0)
            _courseState.postValue(
                if (code / 100 > 2) CourseState.Error(Exception("$code"))
                else CourseState.Idle
            )
        }
    }

    fun checkAnswer(taskIntId: Int, answer: String, action: (AnswerResponse) -> Unit) {
        _courseState.postValue(CourseState.Loading)
        retrofitService.answerTask(
            preferenceRepository.userToken,
            taskIntId,
            answer
        ) { response, code ->
            updateCurrentCourse(currentCourse.value?.intId ?: 0)
            _courseState.postValue(
                if (code / 100 > 2) CourseState.Error(Exception("$code"))
                else CourseState.Idle
            )
            response?.let { action(it) }
        }
    }

}