package ru.divarteam.uchedus.ui.courses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.stickyheader.StickyHeaderLinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import hilt_aggregated_deps._ru_divarteam_uchedus_di_PreferenceModule
import ru.divarteam.uchedus.databinding.FragmentCoursesBinding
import ru.divarteam.uchedus.ui.main.MainActivity

@AndroidEntryPoint
class CoursesFragment : Fragment() {

    lateinit var binding: FragmentCoursesBinding
    val coursesViewModel: CoursesViewModel by viewModels()
    val coursesController = coursesController()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCoursesBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.coursesRecycler.layoutManager =
            StickyHeaderLinearLayoutManager(requireContext())

        binding.coursesRecycler.setController(coursesController)
        coursesViewModel.setupSearchSubject()

        coursesViewModel.queriedList.observe(viewLifecycleOwner) {
            coursesController.setData(it)
        }

        coursesViewModel.coursesState.observe(viewLifecycleOwner) {
            when (it) {
                CoursesState.Initializing -> {

                }

                CoursesState.Idle -> {

                }

                CoursesState.Loading -> {

                }

                CoursesState.Unauthorized -> {
                    coursesViewModel.clearPreferences()
                    startActivity(Intent(context, MainActivity::class.java))
                    activity?.finish()
                }

                is CoursesState.Error -> {
                    it.e.printStackTrace()
                }

                CoursesState.RoleForbiddenTemporary -> {

                }
            }
        }
    }

    fun coursesController() = CoursesController(
        checkCourseFragment = {
            findNavController().navigate(
                CoursesFragmentDirections.actionCoursesFragmentToCourseFragment().apply {
                    courseIntId = it.intId ?: 0
                }
            )
        },
        joinCourse = {
            coursesViewModel.joinCourse(it.intId ?: 0)
        },
        handleQuery = {
            coursesViewModel.search(it)
        }
    )
}