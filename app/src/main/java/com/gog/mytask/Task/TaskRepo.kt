package com.gog.mytask.Task

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gog.mytask.R
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

data class Product(
    val _id: String,
    val name: List<String>,
    val price: Int,
    val item_taxes: List<Int>,
    val specifications: List<Specification>
)

data class Specification(
    val _id: String,
    val name: List<String>,
    val sequence_number: Int,
    val unique_id: Int,
    val list: List<SpecificationItem>,
    val max_range: Int,
    val range: Int,
    val type: Int,
    val is_required: Boolean,
    val isParentAssociate: Boolean,
    val modifierId: String? = null,
    val modifierGroupId: String? = null,
    val modifierGroupName: String? = null,
    val modifierName: String? = null,
    val isAssociated: Boolean? = null,
    val user_can_add_specification_quantity: Boolean? = null
)

data class SpecificationItem(
    val _id: String,
    val name: List<String>,
    val price: Int,
    val sequence_number: Int,
    val is_default_selected: Boolean,
    val specification_group_id: String,
    val unique_id: Int
)


fun readJsonFromRaw(context: Context, resourceId: Int): String {
    val inputStream = context.resources.openRawResource(resourceId)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()
    var line: String?
    while (bufferedReader.readLine().also { line = it } != null) {
        stringBuilder.append(line)
    }
    return stringBuilder.toString()
}


class MyViewModel : ViewModel() {
    private val _packageData = MutableStateFlow<Product?>(null)
    val packageData: StateFlow<Product?> = _packageData.asStateFlow()

    private val _parentData = MutableStateFlow<List<Specification>>(emptyList())
    val parentData: StateFlow<List<Specification>> = _parentData.asStateFlow()



//
//    private val _transformedData = MutableStateFlow<List<Parent>>(emptyList())
//    val transformedData: StateFlow<List<Parent>> = _transformedData.asStateFlow()

    fun loadPackageData(context: Context, resourceId: Int) {
        viewModelScope.launch {
            val jsonString = readJsonFromRaw(context, resourceId)
            val packageData = Gson().fromJson(jsonString, Product::class.java)
            val parentAssociateList = packageData.specifications.filter { it.isParentAssociate }
            _packageData.value = packageData
            _parentData.value = parentAssociateList
        }
    }
}

data class SelectedChild(
    val quantity: Int = 0,
    val price: Int = 0,
    val id: String
)

data class SelectedParent(
    val quantity: Int = 0,
    val price: Int = 0,
    val childs: List<SelectedChild>
)
//fun transformProductToParent(product: Product): List<Parent> {
//    return product.specifications.map { spec ->
//        Parent(
//            name = spec.name.firstOrNull() ?: "",
//            max_range = spec.max_range,
//            range = spec.range,
//            type = spec.type,
//            is_required = spec.is_required,
//            childList = spec.list.filter { it._id == spec.modifierId }.map { item ->
//                Child(
//                    name = item.name.firstOrNull() ?: "",
//                    price = item.price,
//                    is_default_selected = item.is_default_selected,
//                    unique_id = item.unique_id,
//                    childList = spec.list.map { subItem ->
//                        SubChild(
//                            name = subItem.name.firstOrNull() ?: "",
//                            type = spec.type,
//                            child = listOf(
//                                SubSubChild(name = "Example Name 1", price = 100),
//                                SubSubChild(name = "Example Name 2", price = 200)
//                            )
//                        )
//                    }
//                )
//            }
//        )
//    }
//}


//
//fun transformProductToParent(product: Product): List<Parent> {
//    return product.specifications.map { spec ->
//        Parent(
//            name = spec.name.firstOrNull() ?: "",
//            max_range = spec.max_range,
//            range = spec.range,
//            type = spec.type,
//            is_required = spec.is_required,
//            childList = spec.list.map { item ->
//                Child(
//                    name = item.name.firstOrNull() ?: "",
//                    price = item.price,
//                    is_default_selected = item.is_default_selected,
//                    unique_id = item.unique_id,
//                    childList = product.specifications.filter { it.modifierId == item._id }
//                        .map { subSpec ->
//                            SubChild(
//                                name = subSpec.name.firstOrNull() ?: "",
//                                type = subSpec.type,
//                                child = subSpec.list.map { subItem ->
//                                    SubSubChild(
//                                        name = subItem.name.firstOrNull() ?: "",
//                                        price = subItem.price
//                                    )
//                                }
//                            )
//                        }
//                )
//            }
//        )
//    }
//}

