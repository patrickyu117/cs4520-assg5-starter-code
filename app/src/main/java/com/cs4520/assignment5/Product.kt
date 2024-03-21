package com.cs4520.assignment5

import androidx.room.Entity
import androidx.room.PrimaryKey

// Product entity to grab the JSON from the API call and convert it into a product
@Entity
data class Product(
    @PrimaryKey val name: String,
    val type: String,
    val expiryDate: String?,
    val price: Double
) {
    // Checks that the name is not blank, price is greater than 0, and type is not blank to make sure
    // the product is valid (has all the data necessary)
    fun isValidProduct(): Boolean {
        return name.isNotBlank() && price > 0 && type.isNotBlank()
    }
}