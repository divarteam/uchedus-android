package ru.divarteam.uchedus.epoxy

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.transition.TransitionManager
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.MaterialSharedAxis
import ru.divarteam.uchedus.R
import ru.divarteam.uchedus.network.response.TaskResponse
import ru.divarteam.uchedus.util.KotlinHolder

@EpoxyModelClass
abstract class TaskEpoxyModel : EpoxyModelWithHolder<TaskEpoxyModel.Holder>() {

    @EpoxyAttribute
    var number: Int = 0

    @EpoxyAttribute
    lateinit var taskResponse: TaskResponse

    @EpoxyAttribute
    lateinit var checkAnswer: (taskId: Int, answer: String) -> Unit

    override fun getDefaultLayout() = R.layout.item_task

    override fun bind(holder: Holder) {
        holder.questionNumber.setText("Вопрос $number")
        holder.questionTitle.setText(taskResponse.question)

        if (taskResponse.photoFileName.isNullOrEmpty().not()) {
            holder.questionImage.visibility = View.VISIBLE

            Glide.with(holder.questionImage)
                .load("https://api.hackathon.divarteam.ru/static/${taskResponse.photoFileName}")
                .thumbnail(
                    Glide.with(holder.questionImage)
                        .load("https://api.hackathon.divarteam.ru/static/${taskResponse.photoFileName}")
                        .sizeMultiplier(0.2f)
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.questionImage)
//            Glide.with(holder.questionImage)
//                .load("https://api.hackathon.divarteam.ru/static/${taskResponse.photoFileName}")
//                .sizeMultiplier(0.5f)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.questionImage)
        } else
            holder.questionImage.visibility = View.GONE

        if (taskResponse.isCorrect == true) {
            holder.answer.editText?.setText(taskResponse.answer)
            holder.answer.isEnabled = false

            holder.checkButton.setText("Отвечено верно")
            holder.checkButton.isEnabled = false
        } else {
            holder.answer.editText?.setText("")
            holder.answer.isEnabled = true

            holder.checkButton.setText("Ответить")
            holder.checkButton.isEnabled = true
        }

        holder.checkButton.setOnClickListener {
            checkAnswer(
                taskResponse.intId ?: 0, "${holder.answer.editText?.text}"
            )
        }

        holder.cost.setText("${taskResponse.bonus}")
    }

    inner class Holder : KotlinHolder() {
        val questionNumber by bind<TextView>(R.id.question_number)
        val questionTitle by bind<TextView>(R.id.title)
        val questionImage by bind<ImageView>(R.id.task_image)
        val answer by bind<TextInputLayout>(R.id.answer)
        val checkButton by bind<Button>(R.id.check)
        val cost by bind<TextView>(R.id.cost)
    }
}