package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitWorkDialog(
    isDarkMode: Boolean,
    onSubmitWithAi: (
        title: String,
        subject: String,
        workType: String,
        topic: String,
        chapter: String,
        whatLearned: String,
        notes: String,
        imageUri: String?,
        isUnreadable: Boolean
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("Science") }
    var selectedWorkType by remember { mutableStateOf("Notes") }
    var topic by remember { mutableStateOf("") }
    var chapter by remember { mutableStateOf("") }
    var whatLearned by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Image state: photo is NEVER mandatory
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUnreadableFlagged by remember { mutableStateOf(false) }
    var isPhotoSectionSkipped by remember { mutableStateOf(false) }

    // Zero-permission Android Photo Picker contract (Choose From Gallery)
    val galleryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            isPhotoSectionSkipped = false
            isUnreadableFlagged = false
        }
    }

    // Camera photo picker / simulation
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            isPhotoSectionSkipped = false
            isUnreadableFlagged = false
        }
    }

    val bgColor = if (isDarkMode) Color(0xFF14182E) else Color(0xFFFFFFFF)
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val fieldBg = if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF8FAFC)

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .testTag("submit_work_dialog")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clayEffect(
                    shape = RoundedCornerShape(32.dp),
                    backgroundColor = bgColor,
                    borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x80FFFFFF),
                    elevation = 14.dp
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📚 Submit School Work",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = "Notes, Homework, Question Answers & Worksheets",
                            fontSize = 11.sp,
                            color = Lavender500
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = textSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 1. Material Type
                Text(
                    text = "Learning Material Type",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = textSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val types = listOf(
                        "Notes",
                        "Homework",
                        "Question answers",
                        "Worksheets",
                        "Assignments",
                        "Classwork",
                        "Revision notes",
                        "Other school material"
                    )
                    items(types) { type ->
                        val isSelected = selectedWorkType == type
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Indigo500 else fieldBg)
                                .clickable { selectedWorkType = type }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = type,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else textSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Subject Selector
                Text("Subject", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val subjectsList = listOf("Science", "Advanced Mathematics", "Quantum Physics", "Cellular Biology", "AI & Computer Science", "English & Literature", "Social Studies")
                    items(subjectsList) { subj ->
                        val isSelected = selectedSubject == subj
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Indigo500 else fieldBg)
                                .clickable { selectedSubject = subj }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = subj,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else textSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3. Topic & Chapter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1.3f)) {
                        Text("Topic *", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        TextField(
                            value = topic,
                            onValueChange = { topic = it },
                            placeholder = { Text("e.g. Photosynthesis", fontSize = 11.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(fieldBg),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = fieldBg,
                                unfocusedContainerColor = fieldBg,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = textPrimary,
                                unfocusedTextColor = textPrimary
                            ),
                            singleLine = true
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Chapter", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        TextField(
                            value = chapter,
                            onValueChange = { chapter = it },
                            placeholder = { Text("e.g. Life Processes", fontSize = 11.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(fieldBg),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = fieldBg,
                                unfocusedContainerColor = fieldBg,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = textPrimary,
                                unfocusedTextColor = textPrimary
                            ),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // What they learned / Question
                Text("What you learned / Question solved", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = whatLearned,
                    onValueChange = { whatLearned = it },
                    placeholder = { Text("e.g. Light dependent reactions in thylakoid membranes", fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(fieldBg),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = fieldBg,
                        unfocusedContainerColor = fieldBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Description / Notes
                Text("Short Description / Study Notes", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("Optional notes or summary points...", fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(fieldBg),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = fieldBg,
                        unfocusedContainerColor = fieldBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(14.dp))

                // ==========================================
                // PHOTO SECTION (Optional with Preview, Replace, Remove)
                // ==========================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Work Photograph (Optional)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )

                    Text(
                        text = if (selectedImageUri != null) "Photo attached ✓" else "Photo is optional",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selectedImageUri != null) Color(0xFF10B981) else Lavender500
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (selectedImageUri == null && !isPhotoSectionSkipped) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 📷 Take Photo
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(fieldBg)
                                .border(1.dp, Color(0x306366F1), RoundedCornerShape(14.dp))
                                .clickable {
                                    cameraLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Indigo500, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("📷 Take Photo", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }
                        }

                        // 🖼️ Choose From Gallery
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(fieldBg)
                                .border(1.dp, Color(0x306366F1), RoundedCornerShape(14.dp))
                                .clickable {
                                    galleryPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = Lavender500, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("🖼️ From Gallery", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Explicit "Continue Without Photo" affordance
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "✍️ Continue Without Photo (Text Only)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo500,
                            modifier = Modifier
                                .clickable { isPhotoSectionSkipped = true }
                                .padding(4.dp)
                        )
                    }
                } else if (selectedImageUri != null) {
                    // Image Preview with Replace and Remove actions
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(fieldBg)
                            .border(1.dp, Color(0x306366F1), RoundedCornerShape(16.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Black.copy(alpha = 0.05f)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Uploaded material preview",
                                    modifier = Modifier.fillMaxWidth(),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    // Replace Image
                                    OutlinedButton(
                                        onClick = {
                                            galleryPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                        },
                                        modifier = Modifier.height(34.dp),
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Replace", fontSize = 10.sp)
                                    }

                                    // Remove Image
                                    OutlinedButton(
                                        onClick = { selectedImageUri = null },
                                        modifier = Modifier.height(34.dp),
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFFEF4444))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Remove", fontSize = 10.sp, color = Color(0xFFEF4444))
                                    }
                                }

                                // Toggle unreadable flag test
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable { isUnreadableFlagged = !isUnreadableFlagged }
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isUnreadableFlagged) Icons.Default.Warning else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (isUnreadableFlagged) Color(0xFFEF4444) else Color(0xFF10B981),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isUnreadableFlagged) "Flagged blurry" else "Legible scan",
                                        fontSize = 10.sp,
                                        color = if (isUnreadableFlagged) Color(0xFFEF4444) else textSecondary
                                    )
                                }
                            }
                        }
                    }

                    if (isUnreadableFlagged) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFEF4444).copy(alpha = 0.1f))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "⚠️ \"I couldn't clearly read this image. You can upload a clearer photo or continue by telling me the topic.\"",
                                fontSize = 11.sp,
                                color = Color(0xFFEF4444),
                                lineHeight = 15.sp
                            )
                        }
                    }
                } else {
                    // Photo section skipped - text only notice
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF10B981).copy(alpha = 0.1f))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✓ Submitting with topic & notes (No photo required)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF10B981)
                            )
                            Text(
                                text = "Add Photo",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Indigo500,
                                modifier = Modifier.clickable { isPhotoSectionSkipped = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                Button(
                    onClick = {
                        val finalTitle = title.ifBlank {
                            if (topic.isNotBlank()) "$topic $selectedWorkType" else "$selectedSubject $selectedWorkType"
                        }
                        onSubmitWithAi(
                            finalTitle,
                            selectedSubject,
                            selectedWorkType,
                            topic,
                            chapter,
                            whatLearned,
                            notes,
                            selectedImageUri?.toString(),
                            isUnreadableFlagged
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_work_action_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Indigo500,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Submit & Generate AI Questions ✨ (+50 XP)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
