package com.example.task.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.task.data.Task
import com.example.task.ui.TaskViewModel
import kotlinx.coroutines.launch


@Composable
fun AddTaskScreen(
    navController: NavController,
    taskViewModel: TaskViewModel
) {
    // Estado para lidar com a entrada do usuário no campo de texto
    var newTaskName by rememberSaveable { mutableStateOf("") }
    var newTaskDescription by rememberSaveable { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val onDoneClick: () -> Unit = {
        // Verificar se os campos de nome e descrição estão preenchidos
        if (newTaskName.isEmpty() || newTaskDescription.isEmpty()) {
            // Ativar os estados de erro
            nameError = newTaskName.isEmpty()
            descriptionError = newTaskDescription.isEmpty()
        } else {
            // Resetar os estados de erro
            nameError = false
            descriptionError = false

            // Adicionar a nova tarefa usando o ViewModel.
            coroutineScope.launch {
                try {
                    taskViewModel.insertTask(Task(title = newTaskName, description = newTaskDescription))
                    newTaskName = ""
                    newTaskDescription = ""
                    // Navegar de volta para a tela inicial
                    navController.popBackStack()
                } catch (exception: Exception) {
                    // Handle the exception, perhaps show a message to the user
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo de texto para adicionar um novo nome de tarefa
        TextField(
            value = newTaskName,
            onValueChange = {
                newTaskName = it
                // Resetar o estado de erro ao digitar
                nameError = false
            },
            label = {
                if (nameError) {
                    Text(
                        text = "Titulo ausente",
                        color = Color.Red
                    )
                } else {
                    Text(
                        text = "Titulo",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        // Campo de texto para adicionar uma nova descrição de tarefa
        TextField(
            value = newTaskDescription,
            onValueChange = {
                newTaskDescription = it
                // Resetar o estado de erro ao digitar
                descriptionError = false
            },
            label = {
                if (descriptionError) {
                    Text(
                        text = "Descrição ausente",
                        color = Color.Red
                    )
                } else {
                    Text(
                        text = "Descrição",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .heightIn(min = 100.dp)
        )

        // Adicionando um botão Done
        Button(
            onClick = onDoneClick,
            enabled = newTaskName.isNotEmpty() && newTaskDescription.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Done")
        }
    }
}

