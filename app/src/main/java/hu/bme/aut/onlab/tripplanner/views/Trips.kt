package hu.bme.aut.onlab.tripplanner.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.views.helpers.SegmentedControl
import hu.bme.aut.onlab.tripplanner.views.theme.*
import java.util.Locale

@Composable
fun Trips(
    trips: List<TripListItem>,
    onItemClicked: (TripListItem) -> Unit,
    onFabClicked: () -> Unit,
    onItemChanged: (TripListItem) -> Unit,
    onEditClicked: (TripListItem) -> Unit,
    onDeleteClicked: (TripListItem) -> Unit
) {
    val data  = mutableListOf<TripListItem>()
    data.addAll(trips)
    data.sortBy { it.date }
    var switchState by remember { mutableIntStateOf(0) }

    val calendar = java.util.Calendar.getInstance()
    val calString = String.format(
        Locale.getDefault(), "%04d.%02d.%02d.",
        calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH) + 1, calendar.get(
            java.util.Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onFabClicked() },
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 100.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
            innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            SegmentedControl(
                listOf("Future", "Past"),
                switchState
            ) { switchState = it }

            if(switchState == 0)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp, 5.dp, 5.dp, 70.dp)
                ) {
                    itemsIndexed(data.filter { it.date >= calString }) { _, item ->
                        TripItem(
                            item = item,
                            onItemClicked = onItemClicked,
                            onItemChanged = onItemChanged,
                            onEditClicked = onEditClicked,
                            onDeleteClicked = onDeleteClicked
                        )
                    }
                }
            
            if(switchState == 1)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp, 5.dp, 5.dp, 70.dp)
                ) {
                    itemsIndexed(data.filter { it.date < calString }) { _, item ->
                        TripItem(
                            item = item,
                            onItemClicked = onItemClicked,
                            onItemChanged = onItemChanged,
                            onEditClicked = onEditClicked,
                            onDeleteClicked = onDeleteClicked
                        )
                    }
                }
        }
    }
}

@Composable
fun TripItem(
    item: TripListItem,
    onItemClicked: (TripListItem) -> Unit,
    onItemChanged: (TripListItem) -> Unit,
    onEditClicked: (TripListItem) -> Unit,
    onDeleteClicked: (TripListItem) -> Unit
) {
    var checkedState by remember { mutableStateOf(item.visited) }
    checkedState = item.visited

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
            .clickable(onClick = {
                onItemClicked(item)
            }),
        shape = RoundedCornerShape(20),
        elevation = 0.dp,
        backgroundColor = when(item.category) {
            TripListItem.Category.OUTDOORS -> Outdoors
            TripListItem.Category.BEACHES -> Beaches
            TripListItem.Category.SIGHTSEEING -> Sightseeing
            TripListItem.Category.SKIING -> Skiing
            TripListItem.Category.BUSINESS -> Business
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 5.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = {
                        checkedState = it
                        item.visited = checkedState
                        onItemChanged(item)
                    },
                    modifier = Modifier.offset(x = (-2).dp),
                    colors = CheckboxDefaults.colors(
                        checkmarkColor = Color.LightGray,
                        checkedColor = Color.Black
                    )
                )
                Text(
                    text = stringResource(R.string.visited),
                    Modifier.padding(end = 5.dp)
                )
            }
            Image(
                painter = painterResource(
                    id = when(item.category) {
                        TripListItem.Category.OUTDOORS -> R.drawable.outdoors
                        TripListItem.Category.BEACHES -> R.drawable.beaches
                        TripListItem.Category.SIGHTSEEING -> R.drawable.sightseeing
                        TripListItem.Category.SKIING -> R.drawable.skiing
                        TripListItem.Category.BUSINESS -> R.drawable.business
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .width(90.dp)
            ) {
                Text(
                    text = item.country, color = Color.Black, fontSize = 14.sp, maxLines = 1
                )
                Text(
                    text = item.place, color = Color.Black, fontSize = 14.sp, maxLines = 1
                )
                Text(
                    text = item.date, color = Color.Black, fontSize = 14.sp, maxLines = 1
                )
                Text(
                    text = item.category.name, color = Color.Black, fontSize = 14.sp, maxLines = 1
                )
            }
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {onEditClicked(item)},
                    modifier = Modifier.offset(x = 10.dp)
                ) {
                    Icon(imageVector  = Icons.Filled.Edit, "")
                }
                IconButton(
                    onClick = {onDeleteClicked(item)}
                ) {
                    Icon(imageVector  = Icons.Filled.Delete, "")
                }
            }
        }
    }
}