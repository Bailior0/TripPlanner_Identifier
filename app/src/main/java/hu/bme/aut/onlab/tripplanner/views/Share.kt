package hu.bme.aut.onlab.tripplanner.views

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import kotlinx.coroutines.tasks.await

@Composable
fun Share(
    posts: List<SharedData>,
    user: String?,
    onAddCommentClick: () -> Unit,
    onEditClicked: (SharedData) -> Unit,
    onDeleteClicked: (SharedData) -> Unit,
    onLikeClicked: (SharedData) -> Unit
) {
    val storage = Firebase.storage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(12.dp, 10.dp, 12.dp, 10.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (
                button,
                list
            ) = createRefs()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 0.dp, 0.dp, 5.dp)
                    .constrainAs(list) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            ) {
                itemsIndexed(posts.sortedByDescending { it.liked.size }) { _, item ->
                    ListItem(
                        item = item,
                        user = user,
                        storage = storage,
                        onEditClicked = onEditClicked,
                        onDeleteClicked = onDeleteClicked,
                        onLikeClicked = onLikeClicked
                    )
                }
            }

            FloatingActionButton(
                onClick = onAddCommentClick,
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                Icon(Icons.Filled.Share,"")
            }
        }
    }
}

@Composable
fun ListItem(
    item: SharedData,
    user: String?,
    storage: FirebaseStorage,
    onEditClicked: (SharedData) -> Unit,
    onDeleteClicked: (SharedData) -> Unit,
    onLikeClicked: (SharedData) -> Unit
) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    if(item.pic != null && item.pic != "") {
        val storagePath = item.pic
        val imageRef = storage.reference.child(item.pic!!)

        LaunchedEffect(storagePath) {
            try {
                imageUrl = imageRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp),
        ) {
            Text(
                text = item.nickname.toString(), color = Color.Black, fontSize = 18.sp
            )
            Text(
                text = item.title.toString(), color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
            Text(
                text = item.body.toString(), color = Color.Black, fontSize = 18.sp
            )
            if(item.pic != null && item.pic != "")
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (isLoading) {
                        // Add a loading UI
                    } else {
                        imageUrl?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = "Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(item.liked.contains(user)) {
                    Text(
                        text = item.liked.size.toString(), color = Color.Blue, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = {onLikeClicked(item)}
                    ) {
                        Icon(imageVector  = Icons.Filled.ThumbUp, "", tint = Color.Blue)
                    }
                    Text(
                        text = stringResource(R.string.useful), color = Color.Blue, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = item.liked.size.toString(), color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = {onLikeClicked(item)}
                    ) {
                        Icon(imageVector  = Icons.Outlined.ThumbUp, "")
                    }
                    Text(
                        text = stringResource(R.string.useful), color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        if(item.uid == user) {
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {onEditClicked(item)}
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