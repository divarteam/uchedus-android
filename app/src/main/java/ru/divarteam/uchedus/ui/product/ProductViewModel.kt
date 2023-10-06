package ru.divarteam.uchedus.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.divarteam.uchedus.data.model.AccountRole
import ru.divarteam.uchedus.data.repository.PreferenceRepository
import ru.divarteam.uchedus.network.RetrofitService
import ru.divarteam.uchedus.network.response.CourseResponse
import ru.divarteam.uchedus.network.response.ProductResponse
import ru.divarteam.uchedus.ui.course.CourseState
import ru.divarteam.uchedus.ui.profile.ProfileState
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val retrofitService: RetrofitService
) : ViewModel() {

    private val _productState: MutableLiveData<ProductState> =
        MutableLiveData(ProductState.Initializing)
    val productState: LiveData<ProductState>
        get() = _productState

    private val _productsList = MutableLiveData<List<ProductResponse>>()
    val productsList: LiveData<List<ProductResponse>>
        get() = _productsList

    val accountRole: AccountRole
        get() = when (preferenceRepository.userRole) {
            AccountRole.STUDENT.typeString -> AccountRole.STUDENT
            AccountRole.TEACHER.typeString -> AccountRole.TEACHER
            else -> AccountRole.ADMIN
        }

    fun loadProducts() {
        _productState.postValue(ProductState.Loading)
        retrofitService.getAllProducts(preferenceRepository.userToken) { response, code ->
            _productState.postValue(
                if (code / 100 > 2) ProductState.Error(Exception("$code"))
                else {
                    _productsList.postValue(response!!)
                    ProductState.Idle
                }
            )
        }
    }

    fun buyProduct(productIntId: Int, doAfter: (ProductResponse) -> Unit) {
        _productState.postValue(ProductState.Loading)
        retrofitService.buyProductById(
            preferenceRepository.userToken,
            productIntId
        ) { response, code ->
            if (code / 100 > 2)
                _productState.postValue(ProductState.Error(Exception("$code")))
            else {
                doAfter(response!!)
                loadProducts()
            }
        }
    }
}