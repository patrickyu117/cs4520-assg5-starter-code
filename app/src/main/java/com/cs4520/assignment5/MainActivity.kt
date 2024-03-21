package com.cs4520.assignment5

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Main Activity class
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content using Jetpack Compose
        setContent {
            MyTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(navController)
                    }
                    composable("productList") {
                        ProductListScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun MyTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    // MutableState for username and password fields
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Username TextField
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        // Login Button
        Button(
            onClick = {
                if (username == "admin" && password == "admin") {
                    // Display success login message
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    // Navigate to ProductListFragment
                    navController.navigate("productList")
                } else {
                    // Display failed login message
                    Toast.makeText(context, "Invalid Login Info", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            Text("Login")
        }
    }
}

@Composable
fun ProductListScreen(vm: ProductListViewModel = viewModel()) {
    val products by vm.products.collectAsState(emptyList())
    val isLoading by vm.loading.collectAsState(false)
    val errorMessage by vm.error.collectAsState(null)
    val noProductsAvailable by vm.noProducts.collectAsState(false)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val apiService = ApiClient.apiService
    val productDao = ProductDatabase.getInstance(context).productDao()
    val repo = ProductRepository(apiService, productDao)

    // Initialize the repo
    vm.initialize(repo)

    // Fetch the products when we launch this screen
    LaunchedEffect(key1 = true) {
        vm.fetchProducts()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        } else {
            LazyColumn {
                items(products) { product ->
                    // You may need to adjust this according to your product item layout
                    Text(text = product.name)
                    product.expiryDate?.let { Text(text = it) }
                    Text(text = "$" + product.price.toString())
                }
            }
        }
        if (noProductsAvailable) {
            Text(text = "No products available")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginScreen(rememberNavController())
}