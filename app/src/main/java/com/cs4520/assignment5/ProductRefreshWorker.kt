package com.cs4520.assignment5

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

// Work Manager class
class ProductRefreshWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val apiService = ApiClient.apiService
        val productDao = ProductDatabase.getInstance(applicationContext).productDao()
        val repo = ProductRepository(apiService, productDao)

        // Fetch products from the product repository
        val products = apiService.getProducts(3).body()

        if (products != null) {
            productDao.insertAll(products)
        }

        if (products != null) {
            updateDatabase(products)
        }

        return Result.success()
    }

    private suspend fun updateDatabase(productList: List<Product>) {
        val dao = ProductDatabase.getInstance(applicationContext).productDao()

        // Iterate through the fetched products
        productList.forEach { product ->
            // Check if the product already exists in the database
            val existingProduct = dao.getProductById(product.name)

            // If the product doesn't exist, insert it into the database
            if (existingProduct == null) {
                dao.insertProduct(product)
            }
            // Otherwise, skip insertion
        }
    }
}