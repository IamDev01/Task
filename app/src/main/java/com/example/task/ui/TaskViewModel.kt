package com.example.task.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.data.Sample.taskSample
import com.example.task.data.Task
import com.example.task.data.TaskDao
import com.example.task.utils.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val settingsManager: SettingsManager
) : ViewModel() {
    // Utilize settingsManager para obter e definir o estado do tema
    val darkTheme: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            darkTheme.value = settingsManager.isDarkThemeEnabled()
        }
    }

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun setTaskCompleted(id: Int) = taskDao.setTaskCompleted(id)

    suspend fun insertSampleTasks() {
        val rowCount = taskDao.getRowCount()
        if (rowCount == 0) {
            taskSample.forEach { task ->
                taskDao.insertTask(task)
            }
        }
    }
    fun toggleDarkTheme() {
        val newThemeState = !darkTheme.value
        viewModelScope.launch {
            settingsManager.setDarkThemeEnabled(newThemeState)
            darkTheme.value = newThemeState
        }
    }
}



