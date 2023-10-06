package ru.divarteam.uchedus.ui.course

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.uchedus.R
import ru.divarteam.uchedus.databinding.FragmentCourseBinding
import ru.divarteam.uchedus.ui.main.MainActivity
import ru.divarteam.uchedus.util.fromDefaultFormatToRuFormatString

@AndroidEntryPoint
class CourseFragment : Fragment() {

    lateinit var binding: FragmentCourseBinding
    val courseViewModel: CourseViewModel by viewModels()
    val args: CourseFragmentArgs by navArgs()
    val courseController = courseController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        courseViewModel.updateCurrentCourse(args.courseIntId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCourseBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.appbar.setStartButtonOnClickListener {
            findNavController().popBackStack()
        }

        binding.courseRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.courseRecycler.setController(courseController)
        courseViewModel.currentCourse.observe(viewLifecycleOwner) {
            courseController.setCourseData(it)
            if (it.amountOfCompletedTasks == it.amountOfTasks)
                (activity as MainActivity?)?.showConfetti()
        }
    }

    fun courseController() = CourseController(
        joinCourse = {
            courseViewModel.joinCourse(it.intId ?: 0)
        }, checkAnswer = { taskId, answer ->
            courseViewModel.checkAnswer(taskId, answer) {
                Toast.makeText(
                    context,
                    if (it.isCorrect ?: false)
                        "Ответ засчитан."
                    else
                        "Неправильный ответ.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

}