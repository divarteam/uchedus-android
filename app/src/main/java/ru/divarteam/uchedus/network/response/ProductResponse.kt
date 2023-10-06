package ru.divarteam.uchedus.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProductResponse(
    @SerializedName("price") val price: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("created") val created: String?,
    // In get all
    @SerializedName("bought") val bought: Boolean?,
    // In get all
    @SerializedName("can_buy") val canBuy: Boolean?,
    // In buy single
    @SerializedName("done") val done: Boolean?,
)