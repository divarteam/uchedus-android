package ru.divarteam.uchedus.network

import androidx.annotation.Keep
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
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

@Keep
interface RestAPI {


    // Auth methods

    @POST("auth")
    fun auth(@Body authRequest: AuthRequest): Call<UserResponse>

    @GET("auth/send_code")
    fun authSendCode(@Query("mail") toMail: String): Call<SuccessfulResponse>

    // Reg methods

    @POST("reg")
    fun reg(@Body regRequest: RegRequest): Call<UserResponse>

    @GET("reg/send_code")
    fun regSendCode(@Query("mail") toMail: String): Call<SuccessfulResponse>

    // User methods

    @GET("user/me")
    fun getMe(@Header("Authorization") token: String): Call<UserResponse>

    @GET("user/mail_exists")
    fun mailExists(@Query("mail") mail: String): Call<MailExistsResponse>

    @GET("user/all")
    fun getAllUsers(@Header("Authorization") token: String): Call<List<UserResponse>>

    @GET("user/by_int_id")
    fun getUserByIntId(@Header("Authorization") token: String, @Query("int_id") intId: Int): Call<UserResponse>

    @POST("user/update_me")
    fun updateMe(@Header("Authorization") token: String, @Body updateMeRequest: UpdateMeRequest): Call<UserResponse>

    @GET("user/join_course")
    fun joinCourse(@Header("Authorization") token: String, @Query("course_int_id") courseIntId: Int): Call<UserResponse>

    // Course methods

    @POST("course")
    fun createCourse(@Header("Authorization") token: String, @Body createCourseRequest: CreateCourseRequest): Call<CourseResponse>

    @GET("course/all")
    fun getAllCourses(@Header("Authorization") token: String): Single<Response<List<CourseResponse>>>

    @GET("course/created_by_me")
    fun getCreatedByMeCourses(@Header("Authorization") token: String): Call<List<CourseResponse>>

    @GET("course/my")
    fun getMyCourses(@Header("Authorization") token: String): Call<List<CourseResponse>>

    @GET("course/by_int_id")
    fun getCourseByIntId(@Header("Authorization") token: String, @Query("course_int_id") courseIntId: Int): Call<CourseResponse>

    @GET("course/search")
    fun searchCourse(@Header("Authorization") token: String, @Query("q") query: String): Single<Response<List<CourseResponse>>>

    // Task methods

    @POST("task/answer_by_int_id")
    fun answerById(@Header("Authorization") token: String, @Body answerTaskRequest: AnswerTaskRequest): Call<AnswerResponse>

    // Task methods

    @GET("product/all")
    fun getAllProducts(@Header("Authorization") token: String): Call<List<ProductResponse>>

    @GET("product/buy_by_int_id")
    fun buyProductById(@Header("Authorization") token: String, @Query("product_int_id") productIntId: Int): Call<ProductResponse>



}