package ru.divarteam.uchedus.epoxy

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import ru.divarteam.uchedus.R
import ru.divarteam.uchedus.network.response.ProductResponse
import ru.divarteam.uchedus.util.KotlinHolder

@EpoxyModelClass
abstract class ProductEpoxyModel : EpoxyModelWithHolder<ProductEpoxyModel.Holder>() {

    override fun getDefaultLayout() = R.layout.item_product

    @EpoxyAttribute
    lateinit var productResponse: ProductResponse

    @EpoxyAttribute
    lateinit var buyProduct: (ProductResponse) -> Unit

    @SuppressLint("SetTextI18n")
    override fun bind(holder: Holder) {
        holder.title.setText(productResponse.title)
        holder.cost.setText("${productResponse.price}")

        if (productResponse.bought != true && productResponse.canBuy == true) {
            holder.alreadyBought.visibility = View.GONE

            holder.costCard.alpha = 1f
            holder.costCard.setOnClickListener {
                buyProduct(productResponse)
            }
        } else {
            holder.alreadyBought.visibility = View.VISIBLE
            holder.alreadyBought.setText(
                if (productResponse.bought == true)
                    "Уже куплено"
                else
                    "Недостаточно средств"
            )

            holder.costCard.alpha = 0.5f
        }

        Glide.with(holder.picture)
            .load("https://biblioteka.shop/netcat_files/4/3/shoppery_fioletovyy_0.png")
            .into(holder.picture)
    }

    inner class Holder : KotlinHolder() {
        val alreadyBought: TextView by bind(R.id.already_bought)
        val cost: TextView by bind(R.id.cost)
        val costCard: MaterialCardView by bind(R.id.cost_card)
        val title: TextView by bind(R.id.title)
        val description: TextView by bind(R.id.description)
        val picture: ImageView by bind(R.id.picture)
    }
}