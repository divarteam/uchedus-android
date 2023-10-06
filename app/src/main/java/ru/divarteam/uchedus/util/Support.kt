package ru.divarteam.uchedus.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun String.fromDefaultFormatToRuFormatString(): String =
    fromDefaultStringToDate()!!.let { originalDate ->
        SimpleDateFormat(
            Date().let {
                if (it.year == originalDate.year)
                    "dd MMMM HH:mm"
                else
                    "dd MMMM yyyy года HH:mm"
            },
            Locale("ru")
        ).format(
            originalDate
        )
    }

@SuppressLint("SimpleDateFormat")
fun String.fromDefaultStringToDate(): Date? =
    SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss"
    ).parse(this)

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}