package com.gog.mytask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gog.mytask.Task.HomeCleaningScreen
import com.gog.mytask.Task.MyViewModel

import com.gog.mytask.ui.theme.MyTaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MyViewModel>()

            MyTaskTheme {
                // A surface container using the 'background' color from the theme
                Surface(

                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    LaunchedEffect(Unit) {
                        viewModel.loadPackageData(context, R.raw.item_data)
                    }
                    HomeCleaningScreen(viewModel)
                }
            }
        }
    }
}


//@Composable
//fun DisplayPackage() {
//    val context = LocalContext.current
//    fetchPackageFromJson(context)
//}
//
//@Composable
//fun DisplayPackageDetails(context: Context) {
//    val gson = Gson()
//    val jsonString = readJsonFromRaw(context, R.raw.item_data) // Replace with your resource ID
//    val packageData = gson.fromJson(jsonString, Product::class.java)
//
//    HomeCleaningScreen(packageData = packageData)
//}
//
//// Example usage in a Composable function
//@Composable
//fun DisplayPackageUI() {
//    val context = LocalContext.current
//    DisplayPackageDetails(context = context)
//}