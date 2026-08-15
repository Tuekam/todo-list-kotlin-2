package com.example.presentation.screens.tasklist

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.components.ConfirmationModal
import com.example.presentation.components.TaskCard
import com.example.presentation.components.TaskFiltersSection
import com.example.presentation.components.TaskForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    navigation: TaskListNavigation
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    // Infinite Scroll réactif
    LaunchedEffect(listState, state.hasMore, state.isLoadingMore) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastIndex ->
                if (lastIndex != null && 
                    lastIndex >= state.tasks.size - 1 && 
                    state.hasMore && 
                    !state.isLoadingMore && 
                    !state.isLoading
                ) {
                    viewModel.loadMore()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Tâches", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Rafraîchir")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                
                TaskFiltersSection(
                    searchQuery = state.searchQuery,
                    onSearchChange = viewModel::onSearchQueryChange,
                    completedFilter = state.completedFilter,
                    onFilterChange = viewModel::onFilterChange,
                    sortBy = state.sortBy,
                    sortDirection = state.sortDirection,
                    onSortChange = viewModel::onSortChange,
                    limit = state.limit,
                    onLimitChange = viewModel::onLimitChange
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                
                TaskForm(
                    title = state.newTaskTitle,
                    onTitleChange = viewModel::onNewTaskTitleChange,
                    onAddClick = viewModel::onCreateTask
                )

                Box(modifier = Modifier.weight(1f)) {
                    if (state.isLoading && state.tasks.isEmpty()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    if (state.error.isNotEmpty()) {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center).padding(16.dp)
                        )
                    }

                    LazyColumn(
                        state = listState,
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
                                onCancelEdit = viewModel::onCancelEdit,
                                onTitleChange = viewModel::onEditingTitleChange,
                                onSaveEdit = viewModel::onSaveEdit,
                                onDelete = { viewModel.requestDelete(task) }
                            )
                        }

                        if (state.isLoadingMore) {
                            item {
                                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Notification Flash
            AnimatedVisibility(
                visible = state.successMessage.isNotEmpty(),
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.extraLarge,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = state.successMessage,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        if (state.taskToDelete != null) {
            ConfirmationModal(
                title = "Supprimer la tâche",
                message = "Êtes-vous sûr ?",
                onConfirm = viewModel::confirmDelete,
                onDismiss = viewModel::cancelDelete
            )
        }
    }
}
