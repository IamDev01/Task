package com.example.task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.task.ui.TaskViewModel
import com.example.task.ui.screens.AddTaskScreen
import com.example.task.ui.screens.StartScreen
import com.example.task.ui.theme.TaskTheme
import com.example.task.utils.SettingsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val viewModel: TaskViewModel = viewModel()
            // Observe o estado do tema no viewModel
            val darkTheme by viewModel.darkTheme.collectAsState()

            TaskTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    TaskApp(viewModel)
                }
            }
        }
    }
}

enum class Screen(@StringRes val title: Int, val route: String) {
    START_SCREEN(title = R.string.taskscreen, route = "taskScreen"),
    ADD_TASK_SCREEN(title = R.string.addtask, route = "addTask")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    isDarkMode: Boolean,
    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onToggleTheme: () -> Unit, // Função de callback para alternar o tema
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(currentScreen.title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
        },
        actions = {
            Switch(
                checked = isDarkMode,
                onCheckedChange = { onToggleTheme() }, // Chama a função de callback para alternar o tema
                modifier = Modifier.padding(8.dp)
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
fun TaskApp(
    viewModel: TaskViewModel
) {
    val isDarkMode by viewModel.darkTheme.collectAsState() // Observa as alterações no tema escuro

    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.insertSampleTasks()
        }
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.entries.firstOrNull {
        it.route == backStackEntry?.destination?.route
    } ?: Screen.START_SCREEN

    Scaffold(
        topBar = {
            AppBar(
                isDarkMode = isDarkMode, // Usa o valor do tema escuro observado
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onToggleTheme = { viewModel.toggleDarkTheme() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.START_SCREEN.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.START_SCREEN.route) {
                StartScreen(
                    onNavigateToAddTask = {
                        navController.navigate(Screen.ADD_TASK_SCREEN.route)
                    },
                    taskViewModel = viewModel
                )
            }
            composable(route = Screen.ADD_TASK_SCREEN.route) {
                AddTaskScreen(
                    navController = navController,
                    taskViewModel = viewModel
                )
            }
        }
    }
}









