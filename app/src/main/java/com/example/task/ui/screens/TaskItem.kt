package com.example.task.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task.R
import com.example.task.data.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskItem(
    task: Task, onTaskClick: (Task) -> Unit, onDeleteClick: (Task) -> Unit
) {
    Card(/*colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),*/
        elevation = CardDefaults.cardElevation(8.dp)){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.profile_picture),
                contentDescription = "Profile Picture", // Updated content description
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                // Task creation date
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                val formattedCreationDate = sdf.format(Date(task.creationDate))
                Text(
                    text = formattedCreationDate,
                    fontSize =  12.sp
                )

                // Task title
                Text(
                    text = "Jane",
                    fontWeight = FontWeight.Bold,
                    fontSize =  14.sp,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox for marking task as completed
            Checkbox(
                checked = task.completed,
                onCheckedChange = { isChecked -> onTaskClick(task.copy(completed = isChecked)) },
                colors = CheckboxDefaults.colors(
                    checkedColor = if (task.completed) Color(0,  200,  0) else Color.Unspecified
                )
            )

            // Column for task title, description, and creation date
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                // Task title
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize =  20.sp,
                    style = MaterialTheme.typography.titleLarge
                )

                // Task description
                if (task.description.isNotEmpty()) {
                    Text(
                        text = task.description,
                        fontSize =  16.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Delete button
            IconButton(
                onClick = { onDeleteClick(task) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Task",
                    tint = Color.Red
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview
@Composable
fun TaskItemPreview() {
    // Crie uma tarefa de exemplo para usar no preview
    val task = Task(
        id = 1,
        title = "Example Task",
        description = "This is an example task",
        creationDate = System.currentTimeMillis(),
        completed = false
    )

    // Chame TaskItem com a tarefa de exemplo e funções de clique vazias
    TaskItem(
        task = task,
        onTaskClick = {},
        onDeleteClick = {}
    )
}