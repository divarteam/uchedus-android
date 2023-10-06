package ru.divarteam.uchedus.network

import androidx.annotation.Keep
import androidx.room.Update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.divarteam.uchedus.data.model.AccountRole
import ru.divarteam.uchedus.data.repository.PreferenceRepository
import ru.divarteam.uchedus.network.request.AnswerTaskRequest
import ru.divarteam.uchedus.network.request.AuthRequest
import ru.divarteam.uchedus.network.request.CreateCourseRequest
import ru.divarteam.uchedus.network.request.RegRequest
import ru.divarteam.uchedus.network.request.UpdateMeRequest
import ru.divarteam.uchedus.network.response.AnswerResponse
import ru.divarteam.uchedus.network.response.CourseResponse
import ru.divarteam.uchedus.network.response.MailExistsResponse
import ru.divarteam.uchedus.network.response.ProductResponse
import ru.divarteam.uchedus.network.response.SuccessfulResponse
import ru.divarteam.uchedus.network.response.UserResponse
import javax.inject.Inject

@Keep
class RetrofitService(val restAPI: RestAPI) {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    // Auth methods

    fun auth(mail: String, code: Int, onResult: (response: UserResponse?, code: Int) -> Unit) =
        restAPI.auth(AuthRequest(mail, code)).enqueue(onResult)

    fun authSendCode(mail: String, onResult: (response: SuccessfulResponse?, code: Int) -> Unit) =
        restAPI.authSendCode(mail).enqueue(onResult)

    // Reg methods

    fun reg(
        mail: String,
        code: Int,
        role: AccountRole,
        onResult: (response: UserResponse?, code: Int) -> Unit
    ) = restAPI.reg(RegRequest(mail, code, role.typeString)).enqueue(onResult)

    fun regSendCode(mail: String, onResult: (response: SuccessfulResponse?, code: Int) -> Unit) =
        restAPI.regSendCode(mail).enqueue(onResult)

    // User methods

    fun getMe(token: String, onResult: (response: UserResponse?, code: Int) -> Unit) =
        restAPI.getMe(token).enqueue(onResult)

    fun mailExists(mail: String, onResult: (response: MailExistsResponse?, code: Int) -> Unit) =
        restAPI.mailExists(mail).enqueue(onResult)

    fun getAllUsers(token: String, onResult: (response: List<UserResponse>?, code: Int) -> Unit) =
        restAPI.getAllUsers(token).enqueue(onResult)

    fun getUserByIntId(
        token: String,
        intId: Int,
        onResult: (response: UserResponse?, code: Int) -> Unit
    ) = restAPI.getUserByIntId(token, intId).enqueue(onResult)

    fun updateMe(
        token: String,
        fio: String,
        division: String,
        onResult: (response: UserResponse?, code: Int) -> Unit
    ) = restAPI.updateMe(token, UpdateMeRequest(fio, division)).enqueue(onResult)

    fun joinCourse(
        token: String,
        courseIntId: Int,
        onResult: (response: UserResponse?, code: Int) -> Unit
    ) = restAPI.joinCourse(token, courseIntId).enqueue(onResult)

    // Course methods

    fun createCourse(
        token: String,
        createCourseRequest: CreateCourseRequest,
        onResult: (response: CourseResponse?, code: Int) -> Unit
    ) = restAPI.createCourse(token, createCourseRequest).enqueue(onResult)

    fun getAllCourses(
        token: String
    ) = restAPI.getAllCourses(token)

    fun getCreatedByMeCourses(
        token: String,
        onResult: (response: List<CourseResponse>?, code: Int) -> Unit
    ) = restAPI.getCreatedByMeCourses(token).enqueue(onResult)

    fun getMyCourses(
        token: String,
        onResult: (response: List<CourseResponse>?, code: Int) -> Unit
    ) = restAPI.getMyCourses(token).enqueue(onResult)

    fun getCourseByIntId(
        token: String,
        courseIntId: Int,
        onResult: (response: CourseResponse?, code: Int) -> Unit
    ) = restAPI.getCourseByIntId(token, courseIntId).enqueue(onResult)

    fun searchCourse(
        token: String,
        query: String
    ) = restAPI.searchCourse(token, query)

    // Task methods

    fun answerTask(
        token: String,
        taskIntId: Int,
        answer: String,
        onResult: (response: AnswerResponse?, code: Int) -> Unit
    ) = restAPI.answerById(token, AnswerTaskRequest(taskIntId, answer)).enqueue(onResult)

    // Product methods

    fun getAllProducts(
        token: String,
        onResult: (response: List<ProductResponse>?, code: Int) -> Unit
    ) = restAPI.getAllProducts(token).enqueue(onResult)

    fun buyProductById(
        token: String,
        productIntId: Int,
        onResult: (response: ProductResponse?, code: Int) -> Unit
    ) = restAPI.buyProductById(token, productIntId).enqueue(onResult)

    fun <T> Call<T>.enqueue(onResult: (response: T?, code: Int) -> Unit) =
        enqueue(object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) {
                onResult.invoke(response.body(), response.code())
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                t.printStackTrace()
            }
        })
}