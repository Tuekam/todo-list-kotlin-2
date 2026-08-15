package com.example.presentation.screens.tasklist

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.models.Task
import com.example.presentation.components.ConfirmationModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    navigation: TaskListNavigation
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Tâches", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                            Text("${state.tasks.size}")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                
                // 1. Section Filtres (Miroir Web)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 0.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Recherche
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Rechercher une tâche...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (state.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Effacer")
                                    }
                                }
                            },
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Statut, Tri et Limite
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Statut
                            TaskFilterDropdown(
                                label = when(state.completedFilter) {
                                    true -> "Terminées"
                                    false -> "En cours"
                                    else -> "Toutes"
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                DropdownMenuItem(text = { Text("Toutes") }, onClick = { viewModel.onFilterChange(null) })
                                DropdownMenuItem(text = { Text("Terminées") }, onClick = { viewModel.onFilterChange(true) })
                                DropdownMenuItem(text = { Text("En cours") }, onClick = { viewModel.onFilterChange(false) })
                            }

                            // Tri
                            TaskFilterDropdown(
                                label = when(state.sortBy) {
                                    "title" -> "Titre"
                                    else -> "Date"
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                DropdownMenuItem(text = { Text("Date de création") }, onClick = { viewModel.onSortChange("createdAt") })
                                DropdownMenuItem(text = { Text("Titre") }, onClick = { viewModel.onSortChange("title") })
                            }

                            // Limite
                            TaskFilterDropdown(
                                label = state.limit?.toString() ?: "Toutes",
                                modifier = Modifier.width(80.dp)
                            ) {
                                DropdownMenuItem(text = { Text("5") }, onClick = { viewModel.onLimitChange(5) })
                                DropdownMenuItem(text = { Text("10") }, onClick = { viewModel.onLimitChange(10) })
                                DropdownMenuItem(text = { Text("20") }, onClick = { viewModel.onLimitChange(20) })
                                DropdownMenuItem(text = { Text("Toutes") }, onClick = { viewModel.onLimitChange(null) })
                            }
                        }
                    }
                }

                // 2. Formulaire de création (Séparateur)
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.newTaskTitle,
                        onValueChange = { viewModel.onNewTaskTitleChange(it) },
                        placeholder = { Text("Ajouter une nouvelle tâche...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { viewModel.onCreateTask() }, shape = MaterialTheme.shapes.medium) {
                        Text("Ajouter")
                    }
                }

                // 3. Liste
                Box(modifier = Modifier.weight(1f)) {
                    if (state.isLoading && state.tasks.isEmpty()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.tasks) { task ->
                            TaskCard(
                                task = task,
                                isEditing = state.editingTaskId == task.id,
                                editingTitle = state.editingTitle,
                                onToggle = { viewModel.onToggleTask(task) },
                                onClick = { navigation.goToDetail(task) },
                                onStartEdit = { viewModel.onStartEdit(task) },
                                onCancelEdit = { viewModel.onCancelEdit() },
                                onTitleChange = { viewModel.onEditingTitleChange(it) },
                                onSaveEdit = { viewModel.onSaveEdit() },
                                onDelete = { viewModel.requestDelete(task) }
                            )
                        }
                    }
                }
            }

            // 4. Notification Flash (Top Popup)
            AnimatedVisibility(
                visible = state.successMessage != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp)
            ) {
                state.successMessage?.let { msg ->
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.extraLarge,
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = msg,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }

        // Modale de confirmation
        state.taskToDelete?.let { task ->
            ConfirmationModal(
                title = "Supprimer la tâche",
                message = "Êtes-vous sûr de vouloir supprimer \"${task.title}\" ?",
                onConfirm = { viewModel.confirmDelete() },
                onDismiss = { viewModel.cancelDelete() }
            )
        }
    }
}

@Composable
private fun TaskFilterDropdown(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedCard(
            onClick = { expanded = true },
            shape = MaterialTheme.shapes.small,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = label, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            content { expanded = false }
        }
    }
}

@Composable
private fun TaskCard(
    task: Task,
    isEditing: Boolean,
    editingTitle: String,
    onToggle: () -> Unit,
    onClick: () -> Unit,
    onStartEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onTitleChange: (String) -> Unit,
    onSaveEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().let { 
            if (!isEditing) it.clickable { onClick() } else it 
        },
        shape = MaterialTheme.shapes.medium,
        border = androidx.compose.foundation.BorderStroke(
            1.dp, 
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.completed) 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) 
                else MaterialTheme.colorScheme.surface
        )
    ) {
        if (isEditing) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = editingTitle,
                    onValueChange = onTitleChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = MaterialTheme.shapes.small
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onSaveEdit) {
                    Icon(Icons.Default.Check, contentDescription = "Enregistrer", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onCancelEdit) {
                    Icon(Icons.Default.Close, contentDescription = "Annuler")
                }
            }
        } else {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = task.completed, onCheckedChange = { onToggle() })
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (task.completed) 
                            MaterialTheme.colorScheme.onSurfaceVariant 
                            else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (task.completed) FontWeight.Normal else FontWeight.Medium
                    )
                    task.createdAt?.let {
                        Text(
                            text = it.substringBefore("T"),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onStartEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Modifier", tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
