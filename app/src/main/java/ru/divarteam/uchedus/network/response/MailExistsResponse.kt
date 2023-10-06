package ru.divarteam.uchedus.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MailExistsResponse(
    @SerializedName("mail_exists") val exists: Boolean?
)