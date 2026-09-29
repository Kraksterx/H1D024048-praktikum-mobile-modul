package com.example.pengmob_jovan.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pengmob_jovan.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubungiKamiScreen(navController: NavController) {
    var problemType by rememberSaveable { mutableStateOf("Pilih Tipe Pesan") }
    var emailText by rememberSaveable { mutableStateOf("") }
    var messageText by rememberSaveable { mutableStateOf("") }
    var isAgreed by rememberSaveable { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val isEmailValid = emailText.contains("@") && emailText.isNotBlank()
    val isMessageValid = messageText.length >= 10
    val isFormValid = isEmailValid && isMessageValid && isAgreed && problemType != "Pilih Tipe Pesan"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hubungi Kami") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_icon),
                            contentDescription = "Back Icon"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            StatelessFormHubungiKami(
                email = emailText,
                onEmailChange = { emailText = it },
                message = messageText,
                onMessageChange = { messageText = it },
                problemType = problemType,
                onProblemTypeChange = { problemType = it },
                imageUri = imageUri,
                onImagePicked = { imageUri = it },
                isAgreed = isAgreed,
                onAgreedChange = { isAgreed = it },
                isFormValid = isFormValid,
                isEmailValid = isEmailValid,
                onSubmit = {
                    scope.launch {
                        snackbarHostState.showSnackbar(message = "Pesan Terkirim")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessFormHubungiKami(
    email: String,
    onEmailChange: (String) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    problemType: String,
    onProblemTypeChange: (String) -> Unit,
    imageUri: Uri?,
    onImagePicked: (Uri?) -> Unit,
    isAgreed: Boolean,
    onAgreedChange: (Boolean) -> Unit,
    isFormValid: Boolean,
    isEmailValid: Boolean,
    onSubmit: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onImagePicked(uri) }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hubungi Kami",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email Anda") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.mail_icon),
                    contentDescription = "Email"
                )
            },
            isError = !isEmailValid && email.isNotBlank(),
            supportingText = {
                if (!isEmailValid && email.isNotBlank()) {
                    Text("Format email salah")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
        val options = listOf("Pertanyaan", "Komplain", "Saran")

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = problemType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipe Pesan") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onProblemTypeChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            label = { Text("Pesan") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pilih Gambar Bukti")
        }

        if (imageUri != null) {
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.info_icon), contentDescription = "Image")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(imageUri.lastPathSegment ?: "Gambar Terpilih")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isAgreed, onCheckedChange = onAgreedChange)
            Text("Saya menyetujui syarat & ketentuan")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.send_icon),
                    contentDescription = "Send"
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text("Kirim Pesan", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}