package com.example.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskFiltersSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    completedFilter: Boolean?,
    onFilterChange: (Boolean?) -> Unit,
    sortBy: String,
    sortDirection: String,
    onSortChange: (String) -> Unit,
    limit: Int,
    onLimitChange: (Int?) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Recherche
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Rechercher une tâche...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Effacer")
                        }
                    }
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Dropdowns
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Statut
                FilterDropdown(
                    label = when(completedFilter) {
                        true -> "Terminées"
                        false -> "En cours"
                        else -> "Toutes"
                    },
                    modifier = Modifier.weight(1f)
                ) { close ->
                    DropdownMenuItem(text = { Text("Toutes") }, onClick = { onFilterChange(null); close() })
                    DropdownMenuItem(text = { Text("Terminées") }, onClick = { onFilterChange(true); close() })
                    DropdownMenuItem(text = { Text("En cours") }, onClick = { onFilterChange(false); close() })
                }

                // Tri
                FilterDropdown(
                    label = (if(sortBy == "title") "Titre" else "Date") + (if(sortDirection == "asc") " asc" else " desc"),
                    modifier = Modifier.weight(1f)
                ) { close ->
                    DropdownMenuItem(text = { Text("Date") }, onClick = { onSortChange("createdAt"); close() })
                    DropdownMenuItem(text = { Text("Titre") }, onClick = { onSortChange("title"); close() })
                }

                // Limite
                FilterDropdown(
                    label = limit.toString(),
                    modifier = Modifier.width(80.dp)
                ) { close ->
                    DropdownMenuItem(text = { Text("5") }, onClick = { onLimitChange(5); close() })
                    DropdownMenuItem(text = { Text("10") }, onClick = { onLimitChange(10); close() })
                    DropdownMenuItem(text = { Text("20") }, onClick = { onLimitChange(20); close() })
                }
            }
        }
    }
}

@Composable
private fun FilterDropdown(
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
