package com.cs4520.assignment5

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

// ViewModel class for the product list
class ProductListViewModel : ViewModel() {

    private lateinit var repo: ProductRepository

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val _noProducts = MutableStateFlow<Boolean>(false)
    val noProducts: StateFlow<Boolean> get() = _noProducts


    // Initialize the repository
    fun initialize(repository: ProductRepository) {
        this.repo = repository
    }

    // Retrieves the product list from the repository in a coroutine
    fun fetchProducts() {
        viewModelScope.launch {
            // Start loading and set the loading value to true
            _loading.value = true
            try {
                // Try fetching the products from the repo
                val productList = repo.getProducts()
                // Make sure the product list is not null or empty
                if (!productList.isNullOrEmpty()) {
                    _products.value = productList
                } else {
                    // If the product list is empty, set the no products value to true
                    _noProducts.value = true
                }
            } catch (e: Exception) {
                // Catch any error exception and set it to the error value
                _error.value = e.message
            } finally {
                // After loading successfully or unsuccessfully, set the loading value to false
                _loading.value = false
            }
        }
    }

    fun scheduleProductRefresh(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val productRefreshRequest = PeriodicWorkRequestBuilder<ProductRefreshWorker>(
            repeatInterval = 1, // Repeat every hour
            repeatIntervalTimeUnit = TimeUnit.HOURS,
            flexTimeInterval = 15, // Flex interval is 15 minutes
            flexTimeIntervalUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "ProductRefresh",
            ExistingPeriodicWorkPolicy.UPDATE,
            productRefreshRequest
        )
    }
}