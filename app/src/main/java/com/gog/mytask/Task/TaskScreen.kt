package com.gog.mytask.Task

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gog.mytask.R

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCleaningScreen(viewModel: MyViewModel) {
    val data = viewModel.packageData.collectAsState()
    val selectedOption = remember { mutableStateOf<String?>(null) }
    val selectTotal = remember { mutableStateOf<Int?>(0) }


    //    val mainState = remember {
//        mutableStateOf(
//            mutableMapOf<String, MutableMap<String, List<MutableMap<String, Any>>>>()
//        )
//    }


//    val mainState = remember {
//        mutableStateOf(
//            mutableMapOf<HashMap<String, SelectedParent>>()
//        )
//    }


    val mainState = remember {
        mutableStateOf(
            mutableMapOf<String, HashMap<String, SelectedParent>?>()
        )
    }

    fun calculatePriceSum(data: Map<String, Map<String, List<Map<String, Any>>>>): Int {
        var totalSum = 0

        for (innerMap in data.values) {
            for (items in innerMap.values) {
                for (item in items) {
                    val price = item["price"] as? Int ?: 0
                    val quantity = item["quantity"] as? Int ?: 0
                    totalSum += price * quantity
                }
            }
        }

        return totalSum
    }


//    var sum = calculatePriceSum(mainState.value)
//    Log.d("sumsum", sum.toString())
    LaunchedEffect(data) {
        mainState.value = viewModel.createMainState(null)
    }

    Log.d("selectedOption", "${selectedOption.value}")

    Log.d("selectedOption", "${selectedOption.value}")
    Scaffold(
        bottomBar = { BottomBar(0) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "${data.value?.name?.get(0)}") },
                navigationIcon = {
                    IconButton(
                        onClick = { }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                })
        }
    ) { innerpadding ->

        Log.d("HomeCleaningScreen", "HomeCleaningScreen: $data")
        Column(
            modifier = Modifier
                .padding(innerpadding)
                .padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 16.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.taskearn),
                    contentDescription = "image description",
//                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            data.value?.specifications?.filter { it.isParentAssociate }?.map { doc ->
                Log.d("checkData", mainState.value.toString())
                val selectedId = mainState.value[doc._id]
                val childData =
                    data.value?.specifications?.filter {
                        it.isAssociated == true && selectedId?.get(
                            it.modifierId
                        ) != null
                    }

//
                if (childData != null) {
                    DataCard(
                        doc,
                        childData,
                        mainState,
                        viewModel
                    )
                }
            }


        }

    }

}


@Composable
fun MainAllTitle(name: String, range: Int, isRequired: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight(700),

                )
            if (isRequired) {
                Text(text = "*", fontSize = 10.sp, color = Color.Red)
            }
        }


        Text(
            text = if (isRequired) "Choose $range" else "Choose Up to $range",
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}


@Composable
fun CheckOrRadio(
    type: Int,
    name: String,
    price: Int,
    selectedOption: Boolean,
    quantity: Int? = 0,
    onOptionSelected: () -> Unit // Callback to notify parent of selection changes){}){}

) {

//    Log.d("childSearch1", " selectedOption selectedOption ${selectedOption.toString()}")

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (type == 1) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption,
                        onClick = {
                            onOptionSelected()
                        },
                    )
                    Text("${name}", maxLines = 2)
                }
                Row {
                    Text(text = "₹ ${price}")
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(6f),
//                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Checkbox(
                        checked = selectedOption,
                        onCheckedChange = { onOptionSelected() },

                        )
                    Text(name, maxLines = 1) // maxLines 2 working
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(4f)
                ) {
                    Row() {
                        if (selectedOption) {
                            IncrementDecrementComponent(size = 1, quantity, {})
                        }
                    }
                    Text(text = "₹$price")
                }
            }
        }
    }
}


@Composable
fun IncrementDecrementComponent(size: Int = 1, quantity: Int? = 0, onClick: (String) -> Unit) {


    Row(
        modifier = Modifier
//            .padding(bottom = 8.dp)
            .border(BorderStroke(2.dp, Color(0xFF00a5ce)), shape = RoundedCornerShape(40.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { onClick("-") },
            modifier = Modifier.size(if (size == 1) 20.dp else 36.dp)
        ) {
            Text(
                "-",
                fontSize = if (size == 1) 16.sp else 24.sp, modifier = Modifier
//                    .padding(bottom = 6.dp)
            )
        }
        Divider(
            color = Color(0xFF00a5ce),
            modifier = Modifier
                .height(if (size == 1) 20.dp else 36.dp)
                .width(2.dp)
        )
        Text(
            text = quantity.toString(),
            fontSize = if (size == 1) 16.sp else 24.sp,
            modifier = Modifier
                .padding(horizontal = if (size == 1) 7.dp else 16.dp)
        )

        Divider(
            color = Color(0xFF00a5ce),
            modifier = Modifier
                .height(if (size == 1) 20.dp else 36.dp)
                .width(2.dp)

        )

        IconButton(
            onClick = { onClick("+") },
            modifier = Modifier.size(if (size == 1) 20.dp else 36.dp)

        ) {
            Text(
                "+",
                fontSize = if (size == 1) 16.sp else 24.sp,

                modifier = Modifier
//                    .padding(bottom = 6.dp)
            )
        }
    }
}


@Composable
fun DataCard(
    data: Specification,
    childData: List<Specification>,
    mainState: MutableState<MutableMap<String, HashMap<String, SelectedParent>?>>,
    viewModel: MyViewModel,

    ) {
    var currentState = mainState.value.toMutableMap()
    mainState.value = currentState.toMap().toMutableMap()

    fun handleSelection(
        parentId: String?,
        data: Specification,
        parent: Boolean? = null,
        child: Boolean? = null,
        dataId: String,
        childDataId: String,
        price: Int,
        quantity: Int,
        range: Int,
        maxRange: Int,
        showToast: (String) -> Unit
    ) {

        var childMaps = currentState[dataId]?.toMutableMap() ?: hashMapOf()

        if (parent == true) {
//            Log.d("childSearch1", parent.toString())
            if (childMaps[childDataId] == null) {
                mainState.value = viewModel.createMainState(childDataId)
            }
        } else {
            val childSearch = childMaps[parentId]?.childs?.filter { it.id == childDataId }
            if (childSearch != null && childSearch.isNotEmpty()) {
                val childSearch1 = childMaps[parentId]?.childs?.filter { it.id != childDataId }
                if (childSearch1 != null) {
                    currentState[dataId]?.get(parentId)?.childs = childSearch1
                }
                mainState.value = currentState.toMutableMap()
                Log.d("childSearch1", "remove data ${mainState.value}")
            } else {
                val newChild = SelectedChild(quantity = quantity, price = price, id = childDataId)
                Log.d("childSearch1", "handleSelection: $newChild")
//                currentState[dataId]?.get(parentId)?.childs?.plus(newChild)
                val currentChilds =
                    currentState[dataId]?.get(parentId)?.childs?.toMutableList() ?: mutableListOf()
                currentChilds.add(newChild)
                currentState[dataId]?.get(parentId)?.childs = currentChilds
                Log.d("childSearch1", "find my data $currentState")

                mainState.value = currentState.toMutableMap()
            }
        }


    }



    Spacer(modifier = Modifier.height(2.dp))
    Divider(
        color = Color.LightGray,
        modifier = Modifier.fillMaxWidth()
    )
    Log.d("mainState.value", "mainState.value ${mainState.value}")
    val context = LocalContext.current
    MainAllTitle(data.name[0], data.range, data.is_required)

    val superParent = mainState.value.get(data._id)
    val parentId = superParent?.keys?.first()
    val parentData = superParent?.get(parentId)
    Log.d("childSearch1", "parentId ${parentId.toString()}")
    data.list.sortedBy { it.sequence_number }.forEach {
        CheckOrRadio(
            data.type,
            it.name[0],
            it.price,
            parentId == it._id,
        ) {
            handleSelection(
                parentId,
                data,
                data.isParentAssociate,
                data.isAssociated,
                data._id,
                it._id,
                it.price,
                if (data.user_can_add_specification_quantity == true) 1 else 0,
                data.range,
                data.max_range
            ) { message ->
                Toast.makeText(
                    context, message, Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Divider(
        color = Color.LightGray,
        modifier = Modifier.fillMaxWidth()
    )


    childData.sortedBy { it.sequence_number }.forEach {
        Spacer(modifier = Modifier.height(20.dp))
        MainAllTitle(name = it.name[0], range = it.max_range, isRequired = it.is_required)
        it.list.sortedBy { it.sequence_number }.forEach { subChild ->

//            val selectedIds = superParent?.get(parentId)?.childs?.map { doc ->
//                doc.id
//            }
            val selectedIds =
                mainState.value[data._id]?.get(parentId)?.childs?.map { doc -> doc.id }

//            val selectedIds = mainState.value[data._id]?.get(parentId)?.childs?.map { doc -> doc.id }

//            val selectedIds = parentData?.childs?.map { doc ->
//                doc.id
//            }
            val cd = parentData?.childs?.filter { it.id == subChild._id }
            val quantity = if (cd?.isNotEmpty() == true) cd[0].quantity else null


            Log.d("checkData", "@@@ ${subChild._id} ${data._id} ${it._id} ${selectedIds}")

            CheckOrRadio(
                type = it.type,
                name = subChild.name[0],
                price = subChild.price,
                selectedOption = selectedIds != null && subChild._id in selectedIds,
                quantity,
            ) {
                handleSelection(
                    parentId,
                    data,
                    it.isParentAssociate,
                    it.isAssociated,
                    data._id,
                    subChild._id,
                    subChild.price,
                    if (it.user_can_add_specification_quantity == true) 1 else 0,
                    data.range,
                    it.max_range
                ) { message ->
                    Toast.makeText(
                        context, message, Toast.LENGTH_LONG
                    ).show()
                }
            }


        }
//
        Divider(
            color = Color.LightGray,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
    }

}


@Composable
fun BottomBar(sum: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically


    ) {
        IncrementDecrementComponent(size = 2, 0, {})
        Button(
            shape = RoundedCornerShape(size = 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00a5ce)),
//            contentPadding = PaddingValues(1.dp),
            onClick = { },
        ) {
            Text(
                text = "Add to Card ${sum}",
                fontSize = 15.sp,
                fontWeight = FontWeight.W700,
                color = Color.White,
//                fontFamily = MyFont,
            )
        }
    }
}