package com.example.task.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // Método para obter todas as tarefas ordenadas por ID em ordem decrescente
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<Task>>

    // Método para inserir uma nova tarefa
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    // Método para excluir uma tarefa
    @Delete
    suspend fun deleteTask(task: Task)

    // Método para marcar uma tarefa como concluída com base no ID
    @Query("UPDATE tasks SET completed =  1 WHERE id = :id")
    suspend fun setTaskCompleted(id: Int)

    // Método para obter o número total de linhas na tabela de tarefas
    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getRowCount(): Int
}


