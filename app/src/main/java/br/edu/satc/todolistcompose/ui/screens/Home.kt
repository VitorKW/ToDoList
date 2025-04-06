@file:OptIn(ExperimentalMaterial3Api::class)

package br.edu.satc.todolistcompose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.satc.todolistcompose.Database
import br.edu.satc.todolistcompose.TaskData
import br.edu.satc.todolistcompose.ui.components.TaskCard
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    var showBottomSheet by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = "ToDoList UniSATC") },
                actions = {
                    val scope = rememberCoroutineScope()
                    IconButton(onClick = {
                        scope.launch {
                            Database.db.taskDao().deleteAll()
                            refreshTrigger++ // força atualização da tela
                        }
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Limpar tudo")
                    }

                    IconButton(onClick = { /* Configurações */ }) {
                        Icon(Icons.Rounded.Settings, contentDescription = "Configurações")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Nova tarefa") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = { showBottomSheet = true }
            )
        }
    ) { innerPadding ->
        HomeContent(innerPadding = innerPadding, refreshTrigger = refreshTrigger)
        NewTask(showBottomSheet = showBottomSheet) {
            showBottomSheet = false
            refreshTrigger++ // atualiza a tela
        }
    }
}

@Composable
fun HomeContent(innerPadding: PaddingValues, refreshTrigger: Int) {
    val tasks = remember(refreshTrigger) {
        Database.db.taskDao().getAll()
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .padding(top = innerPadding.calculateTopPadding())
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        for (task in tasks) {
            TaskCard(task.taskName, task.taskDescription)
        }
    }
}

@Composable
fun NewTask(showBottomSheet: Boolean, onComplete: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onComplete() },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text(text = "Título da tarefa") }
                )
                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text(text = "Descrição da tarefa") }
                )

                Button(
                    modifier = Modifier.padding(top = 4.dp),
                    onClick = {
                        scope.launch {
                            val novaTask = TaskData(
                                uid = 0,
                                taskName = taskTitle,
                                taskDescription = taskDescription
                            )

                            Database.db.taskDao().insertAll(novaTask)

                            sheetState.hide()
                            onComplete()
                            taskTitle = ""
                            taskDescription = ""
                        }
                    }
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}
