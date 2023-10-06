package ru.divarteam.uchedus.ui.product

import com.airbnb.epoxy.TypedEpoxyController
import ru.divarteam.uchedus.epoxy.product
import ru.divarteam.uchedus.network.response.CourseResponse
import ru.divarteam.uchedus.network.response.ProductResponse

class ProductController(
    val buyProduct: (ProductResponse) -> Unit
) : TypedEpoxyController<List<ProductResponse>>() {
    override fun buildModels(data: List<ProductResponse>?) {
        data.orEmpty().forEachIndexed { index, productResponse ->
            product {
                id(index)
                productResponse(productResponse)
                buyProduct(this@ProductController.buyProduct)
            }
        }
    }
}