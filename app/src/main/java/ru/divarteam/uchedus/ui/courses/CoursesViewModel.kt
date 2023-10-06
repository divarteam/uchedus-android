package ru.divarteam.uchedus.ui.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.divarteam.uchedus.data.model.AccountRole
import ru.divarteam.uchedus.data.repository.PreferenceRepository
import ru.divarteam.uchedus.network.RetrofitService
import ru.divarteam.uchedus.network.response.CourseResponse
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val retrofitService: RetrofitService
) : ViewModel() {

    private val _coursesState: MutableLiveData<CoursesState> =
        MutableLiveData(CoursesState.Initializing)
    val coursesState: LiveData<CoursesState>
        get() = _coursesState

    private val _queriedList = MutableLiveData(listOf<CourseResponse>())
    val queriedList: LiveData<List<CourseResponse>>
        get() = _queriedList

    private var lastQuery = String()
    private val compositeDisposable = CompositeDisposable()
    private val searchSubject = PublishSubject.create<String>()

    val accountRole: AccountRole
        get() = when (preferenceRepository.userRole) {
            AccountRole.STUDENT.typeString -> AccountRole.STUDENT
            AccountRole.TEACHER.typeString -> AccountRole.TEACHER
            else -> AccountRole.ADMIN
        }

    fun joinCourse(courseIntId: Int) {
        _coursesState.postValue(CoursesState.Loading)
        retrofitService.joinCourse(preferenceRepository.userToken, courseIntId) { response, code ->
            _coursesState.postValue(CoursesState.Idle)
            refreshSearchHistory()
        }
    }

    fun refreshSearchHistory() {
        searchSubject.onNext(lastQuery)
    }

    fun setupSearchSubject() {
        searchSubject
            .map { query -> query.trim() }
            .debounce(200, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .switchMapSingle {
                _coursesState.postValue(CoursesState.Loading)
                lastQuery = it

                if (it.isEmpty())
                    retrofitService.getAllCourses(
                        token = preferenceRepository.userToken
                    )
                else
                    retrofitService.searchCourse(
                        token = preferenceRepository.userToken,
                        query = it
                    )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    val code = it.code()
                    val body = it.body()

                    _queriedList.postValue(
                        if (code == 200 && body != null && body.isNotEmpty())
                            body
                        else
                            emptyList()
                    )

                    _coursesState.postValue(
                        if (code == 401)
                            CoursesState.Unauthorized
                        else if (code / 100 > 2)
                            CoursesState.Error(Exception())
                        else
                            CoursesState.Idle
                    )
                }, onError = {
                    _coursesState.postValue(CoursesState.Error(it))
                }
            )
            .addTo(compositeDisposable)

        search("")
    }

    fun clearPreferences() {
        preferenceRepository.userToken = ""
        preferenceRepository.userId = 0
        preferenceRepository.userRole = ""
    }

    fun search(query: String) {
        searchSubject.onNext(query)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}