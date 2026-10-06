package com.example.pengmob_jovan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pengmob_jovan.data.model.Category
import com.example.pengmob_jovan.data.model.Product
import com.example.pengmob_jovan.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ProductUiState {
    object Loading : ProductUiState
    data class Success(val categories: List<Category>, val products: List<Product>) : ProductUiState
    data class Error(val message: String) : ProductUiState
}

class ProductViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            try {
                val categories = ApiClient.instance.getCategories()
                val products = ApiClient.instance.getProducts()
                
                val mappedProducts = products.map { product ->
                    val matchingCategory = categories.find { it.id == product.categoryId }
                    if (matchingCategory != null) {
                        product.copy(category = matchingCategory)
                    } else {
                        product
                    }
                }
                
                _uiState.value = ProductUiState.Success(categories, mappedProducts)
            } catch (e: Exception) {
                _uiState.value = ProductUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}
