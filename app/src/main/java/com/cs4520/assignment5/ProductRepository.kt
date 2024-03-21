package com.cs4520.assignment5

// Local product repository database
class ProductRepository(private val apiService: ApiService, private val productDao: ProductDao) {

    // Get the products from the API
    suspend fun getProducts(): List<Product> {
        try {
            // Call the API to get the products directly
            val productResponse = apiService.getProducts(3)

            if (productResponse.isSuccessful) {
                val products = productResponse.body()
                if (products is List<Product>) {
                    // Filter out duplicate products
                    val distinctProducts = products.distinctBy { it.name }

                    // Filter out invalid products
                    val validProducts = distinctProducts.filter { it.isValidProduct() }

                    // Insert products into the local room database
                    productDao.insertAll(validProducts)

                    // Return the list of products
                    return products
                } else {
                    throw NoProductsException("Random error occurred")
                }
            } else {
                throw NoProductsException("Random error occurred")
            }

        } catch (e: Exception) {
            // Check the error message to see if it because the API call returned something that is
            // not a product i.e. the error message. Throws an exception if we know the error is not from
            // being offline
            if (e.message == "Random error occurred") {
                println("error1: " + e.message)
                throw NoProductsException("Random error occurred")
            } else {
                // Return the local database once we know the error was because the phone is offline
                println("error2: " + e.message)
                return productDao.getAllProducts()
            }
        }
    }
}

class NoProductsException(message: String) : Exception(message)