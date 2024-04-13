package com.example.task.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.task.R
import com.example.task.ui.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun StartScreen(
    taskViewModel: TaskViewModel,
    onNavigateToAddTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Observe the tasks state in the ViewModel.
    val tasks by taskViewModel.getAllTasks().collectAsState(initial = emptyList())

    Box(modifier = modifier.fillMaxSize()) {
        // Tasks list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal =  16.dp),
            contentPadding = PaddingValues(vertical =  16.dp)
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onTaskClick = {
                        // Launch a coroutine to call the ViewModel's suspending function
                        CoroutineScope(Dispatchers.Main).launch {
                            taskViewModel.setTaskCompleted(task.id)
                        }
                    },
                    onDeleteClick = {
                        // Launch a coroutine to call the ViewModel's suspending function
                        CoroutineScope(Dispatchers.Main).launch {
                            taskViewModel.deleteTask(task)
                        }
                    }
                )
            }
        }

        // FloatingActionButton
        FloatingActionButton(
            onClick = onNavigateToAddTask,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_large))
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Adicionar Tarefa"
            )
        }
    }
}
