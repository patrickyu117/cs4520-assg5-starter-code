package com.cs4520.assignment5

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Product Dao to create the functions to perform on the local room database
@Dao
interface ProductDao {
    // Insert all products into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    // Retrieve all the products from the database
    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<Product>

    // Remove all the products from the database
    @Query("DELETE FROM product")
    suspend fun removeAll()
}