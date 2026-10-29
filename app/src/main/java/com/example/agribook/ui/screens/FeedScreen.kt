package com.example.agribook.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.agribook.data.local.PostEntity
import com.example.agribook.localization.AgriStrings
import com.example.agribook.localization.AppLanguage
import com.example.agribook.ui.AgriBookViewModel
import com.example.agribook.ui.components.CropCategoryFilterRow
import com.example.ui.theme.*

@Composable
fun FeedScreen(viewModel: AgriBookViewModel) {
    val posts by viewModel.posts.collectAsState()
    val selectedFilter by viewModel.selectedCropFilter.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val activeCommentPost by viewModel.activeCommentPost.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    var showCreatePostDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = AgriBlack,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreatePostDialog = true },
                containerColor = AgriGreenPrimary,
                contentColor = AgriBlack,
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Post") },
                text = { Text(AgriStrings.get("post_btn", currentLang), fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("create_post_fab")
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filter categories
            CropCategoryFilterRow(
                selectedCategory = selectedFilter,
                onCategorySelected = { viewModel.setSelectedCropFilter(it) }
            )

            if (posts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Agriculture,
                            contentDescription = null,
                            tint = AgriTextSecondary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No farming posts found under $selectedFilter",
                            color = AgriTextSecondary,
                            fontSize = 15.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(posts, key = { it.id }) { post ->
                        PostCard(
                            post = post,
                            currentLang = currentLang,
                            onLike = { viewModel.toggleLike(post.id) },
                            onBookmark = { viewModel.toggleBookmark(post.id) },
                            onOpenComments = { viewModel.setActiveCommentPost(post) },
                            onReport = { viewModel.setReportTarget(Pair("POST", post.id)) },
                            onShare = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "🌾 Farming Advice from ${post.authorName} on Agribook:\n\n\"${post.content}\"\n\nCrop: ${post.cropTag}\nJoin the conversation on Agribook: https://ais-pre-do77zlohazs6k3h2gfqtvp-180384412792.asia-east1.run.app"
                                    )
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Share Farm Advice")
                                context.startActivity(shareIntent)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showCreatePostDialog) {
        CreatePostDialog(
            currentLang = currentLang,
            onDismiss = { showCreatePostDialog = false },
            onSubmit = { content, cropTag, mediaRes ->
                val mediaType = if (mediaRes.isNotEmpty()) "IMAGE" else "NONE"
                viewModel.createPost(content, cropTag, mediaType, mediaRes)
                showCreatePostDialog = false
            }
        )
    }

    if (activeCommentPost != null) {
        CommentsSheet(
            post = activeCommentPost!!,
            viewModel = viewModel,
            currentLang = currentLang,
            onDismiss = { viewModel.setActiveCommentPost(null) }
        )
    }
}

@Composable
fun PostCard(
    post: PostEntity,
    currentLang: AppLanguage,
    onLike: () -> Unit,
    onBookmark: () -> Unit,
    onOpenComments: () -> Unit,
    onReport: () -> Unit,
    onShare: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("post_card_${post.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Author row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            when (post.authorRole) {
                                "ADMIN" -> AgriGoldContainer
                                "EXPERT" -> AgriGreenContainer
                                else -> AgriSurfaceDark
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (post.authorRole) {
                            "ADMIN" -> Icons.Default.Shield
                            "EXPERT" -> Icons.Default.School
                            else -> Icons.Default.Person
                        },
                        contentDescription = null,
                        tint = when (post.authorRole) {
                            "ADMIN" -> AgriGold
                            "EXPERT" -> AgriGreenPrimary
                            else -> AgriLime
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.authorName,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary,
                            fontSize = 15.sp
                        )
                        if (post.authorRole != "FARMER") {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (post.authorRole == "ADMIN") AgriGoldContainer else AgriGreenContainer
                            ) {
                                Text(
                                    text = post.authorRole,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (post.authorRole == "ADMIN") AgriGold else AgriGreenPrimary,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "${post.authorLocation} • ${post.authorCrop}",
                        fontSize = 11.sp,
                        color = AgriTextSecondary
                    )
                }

                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = AgriTextSecondary)
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        containerColor = AgriSurfaceCardElevated
                    ) {
                        DropdownMenuItem(
                            text = { Text(AgriStrings.get("report", currentLang), color = AgriRedAlert) },
                            leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null, tint = AgriRedAlert) },
                            onClick = {
                                menuExpanded = false
                                onReport()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Post content
            Text(
                text = post.content,
                color = AgriTextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            // Crop Tag pill
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = AgriGreenContainer,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Eco, contentDescription = null, tint = AgriLime, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(post.cropTag, fontSize = 11.sp, color = AgriOnGreenContainer, fontWeight = FontWeight.SemiBold)
                }
            }

            // Attached Media image
            if (post.mediaType == "IMAGE") {
                val imageRes = if (post.mediaResName == "crop_sample_paddy") {
                    R.drawable.crop_sample_paddy
                } else {
                    R.drawable.agribook_banner
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Farm image attachment",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Divider(color = AgriBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 8.dp))

            // Action row: Like, Comment, Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onLike,
                    colors = ButtonDefaults.textButtonColors(contentColor = AgriGreenPrimary)
                ) {
                    Icon(Icons.Default.ThumbUp, contentDescription = "Helpful", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("${post.likesCount} ${AgriStrings.get("like", currentLang)}", fontSize = 12.sp)
                }

                TextButton(
                    onClick = onOpenComments,
                    colors = ButtonDefaults.textButtonColors(contentColor = AgriLime)
                ) {
                    Icon(Icons.Default.Comment, contentDescription = "Advice", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("${post.commentsCount} ${AgriStrings.get("comment", currentLang)}", fontSize = 12.sp)
                }

                IconButton(onClick = onBookmark) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Save Tip",
                        tint = AgriGold
                    )
                }

                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Advice",
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CreatePostDialog(
    currentLang: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (content: String, cropTag: String, mediaRes: String) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var selectedCrop by remember { mutableStateOf("Paddy") }
    var selectedMedia by remember { mutableStateOf("") } // "" or "crop_sample_paddy" or "agribook_banner"

    val crops = listOf("Paddy", "Wheat", "Cotton", "Chili", "Sugarcane", "Organic", "Soil & Fertilizer", "Pest Control")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AgriSurfaceCard,
        title = {
            Text(AgriStrings.get("create_post", currentLang), color = AgriTextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Describe pest symptoms, crop progress, fertilizer dosages, or ask questions...") },
                    minLines = 4,
                    maxLines = 6,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AgriGreenPrimary,
                        unfocusedBorderColor = AgriBorder,
                        focusedTextColor = AgriTextPrimary,
                        unfocusedTextColor = AgriTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(AgriStrings.get("crop_category", currentLang), fontSize = 12.sp, color = AgriLime, fontWeight = FontWeight.Bold)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    crops.forEach { crop ->
                        FilterChip(
                            selected = selectedCrop == crop,
                            onClick = { selectedCrop = crop },
                            label = { Text(crop, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AgriGreenPrimary,
                                selectedLabelColor = AgriBlack
                            )
                        )
                    }
                }

                Text("Attach Farm Photo:", fontSize = 12.sp, color = AgriTextSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedMedia == "crop_sample_paddy",
                        onClick = { selectedMedia = if (selectedMedia == "crop_sample_paddy") "" else "crop_sample_paddy" },
                        label = { Text("Paddy Crop Photo", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = selectedMedia == "agribook_banner",
                        onClick = { selectedMedia = if (selectedMedia == "agribook_banner") "" else "agribook_banner" },
                        label = { Text("Farmland Scenic", fontSize = 11.sp) }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(content, selectedCrop, selectedMedia) },
                enabled = content.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
            ) {
                Text(AgriStrings.get("post_btn", currentLang), color = AgriBlack, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = AgriTextSecondary)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsSheet(
    post: PostEntity,
    viewModel: AgriBookViewModel,
    currentLang: AppLanguage,
    onDismiss: () -> Unit
) {
    val comments by viewModel.currentComments.collectAsState()
    var commentText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AgriSurfaceCard,
        contentColor = AgriTextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(max = 500.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Agricultural Advice & Comments",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AgriGreenPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = AgriTextSecondary)
                }
            }

            Divider(color = AgriBorder, modifier = Modifier.padding(vertical = 8.dp))

            if (comments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No comments yet. Share your experience or advice!",
                        color = AgriTextSecondary,
                        fontSize = 13.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(comments, key = { it.id }) { comment ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = AgriSurfaceDark,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = comment.authorName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = AgriTextPrimary
                                    )
                                    if (comment.authorRole != "FARMER") {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "(${comment.authorRole})",
                                            fontSize = 11.sp,
                                            color = AgriLime
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = comment.text,
                                    fontSize = 13.sp,
                                    color = AgriTextSecondary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Write agricultural advice...", fontSize = 13.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AgriGreenPrimary,
                        unfocusedBorderColor = AgriBorder,
                        focusedTextColor = AgriTextPrimary,
                        unfocusedTextColor = AgriTextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            viewModel.addComment(post.id, commentText)
                            commentText = ""
                        }
                    },
                    modifier = Modifier
                        .background(AgriGreenPrimary, CircleShape)
                        .size(44.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send Comment", tint = AgriBlack)
                }
            }
        }
    }
}
