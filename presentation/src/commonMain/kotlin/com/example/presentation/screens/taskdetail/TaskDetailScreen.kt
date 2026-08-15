package com.example.presentation.screens.taskdetail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel,
    navigation: TaskDetailNavigation
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détail de la tâche") },
                navigationIcon = {
                    IconButton(onClick = { navigation.goBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    if (state.isEditing) {
                        IconButton(onClick = { viewModel.onCancelEdit() }) {
                            Icon(Icons.Default.Close, contentDescription = "Annuler")
                        }
                        IconButton(onClick = { viewModel.onSaveClick() }) {
                            Icon(Icons.Default.Check, contentDescription = "Enregistrer")
                        }
                    } else {
                        IconButton(onClick = { viewModel.onEditClick() }) {
                            Icon(Icons.Default.Edit, contentDescription = "Modifier")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.task?.let { task ->
                Column(modifier = Modifier.padding(16.dp)) {
                    if (state.isEditing) {
                        OutlinedTextField(
                            value = state.editedTitle,
                            onValueChange = { viewModel.onTitleChange(it) },
                            label = { Text("Titre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(text = task.title, style = MaterialTheme.typography.headlineMedium)
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    DetailItem(label = "Statut", value = if (task.completed) "Terminée" else "En cours")
                    
                    task.createdAt?.let {
                        Spacer(modifier = Modifier.height(12.dp))
                        DetailItem(label = "Date de création", value = it.substringBefore("T"))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (!state.isEditing) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = task.completed, onCheckedChange = { viewModel.onToggleComplete() })
                            Text(text = "Marquer comme terminée")
                        }
                    }
                }
            }

            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
