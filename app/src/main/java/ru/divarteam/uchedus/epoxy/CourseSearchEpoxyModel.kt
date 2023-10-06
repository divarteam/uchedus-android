package ru.divarteam.uchedus.epoxy

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.divarteam.uchedus.R
import ru.divarteam.uchedus.util.KotlinHolder

@EpoxyModelClass
abstract class CourseSearchEpoxyModel : EpoxyModelWithHolder<CourseSearchEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var handleQuery: (String) -> Unit

    override fun getDefaultLayout() = R.layout.item_search

    override fun bind(holder: Holder) {
        holder.searchBar.doAfterTextChanged {
            handleQuery(it.toString())
        }
    }

    inner class Holder: KotlinHolder() {
        val searchBar by bind<EditText>(R.id.search)
    }
}