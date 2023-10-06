package ru.divarteam.uchedus.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.uchedus.databinding.FragmentProductsBinding

@AndroidEntryPoint
class ProductFragment : Fragment() {

    lateinit var binding: FragmentProductsBinding

    val productViewModel: ProductViewModel by viewModels()
    val productController = productController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productViewModel.loadProducts()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProductsBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.productsRecycler.setController(productController)
        productViewModel.productsList.observe(viewLifecycleOwner) {
            productController.setData(it)
        }
    }

    fun productController() = ProductController(buyProduct = {
        productViewModel.buyProduct(it.intId ?: 0) {
            Toast.makeText(
                context,
                if (it.done == true)
                    "Операция принята."
                else
                    "У вас недостаточно средств.",
                Toast.LENGTH_SHORT
            ).show()
        }
    })
}