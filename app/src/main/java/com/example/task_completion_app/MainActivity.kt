package com.example.task_completion_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskApp()
        }
    }
}

@Composable
fun TaskApp() {
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<Task>()) }

    Column(modifier = Modifier.padding(16.dp)) {
        TaskInputField(
            taskName = taskName,
            taskDescription = taskDescription,
            onTaskNameChange = { taskName = it },
            onTaskDescriptionChange = { taskDescription = it },
            onAddTask = {
                if (taskName.isNotEmpty() && taskDescription.isNotEmpty()) {
                    tasks = tasks + Task(taskName, taskDescription, false)
                    taskName = ""
                    taskDescription = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))


        TaskList(
            tasks = tasks,
            onTaskCheckedChange = { index, isChecked ->
                tasks = tasks.mapIndexed { i, task ->
                    if (i == index) task.copy(isComplete = isChecked) else task
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            tasks = tasks.filterNot { it.isComplete }
        }) {
            Text("Clear Completed")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInputField(
    taskName: String,
    taskDescription: String,
    onTaskNameChange: (String) -> Unit,
    onTaskDescriptionChange: (String) -> Unit,
    onAddTask: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = taskName,
            onValueChange = onTaskNameChange,
            placeholder = { Text("Enter task name...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Blue,
                containerColor = Color.LightGray,
                focusedIndicatorColor = Color.Blue,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = taskDescription,
            onValueChange = onTaskDescriptionChange,
            placeholder = { Text("Enter task description...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Blue,
                containerColor = Color.LightGray,
                focusedIndicatorColor = Color.Blue,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onAddTask, modifier = Modifier.align(Alignment.End)) {
            Text("Add Task")
        }
    }
}

@Composable
fun TaskList(tasks: List<Task>, onTaskCheckedChange: (Int, Boolean) -> Unit) {
    Column {
        tasks.forEachIndexed { index, task ->
            TaskRow(
                task = task,
                onCheckedChange = { isChecked ->
                    onTaskCheckedChange(index, isChecked)
                }
            )
        }
    }
}

@Composable
fun TaskRow(task: Task, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.name,
                style = TextStyle(
                    fontSize = 18.sp,
                    textDecoration = if (task.isComplete) TextDecoration.LineThrough else null,
                    color = if (task.isComplete) Color.Gray else Color.Black
                )
            )
        }
        Text(
            text = task.description,
            style = TextStyle(
                fontSize = 14.sp,
                textDecoration = if (task.isComplete) TextDecoration.LineThrough else null,
                color = if (task.isComplete) Color.Gray else Color.Black
            )
        )
    }
    Checkbox(
        checked = task.isComplete,
        onCheckedChange = onCheckedChange
    )
}


data class Task(val name: String, val description: String, val isComplete: Boolean)

@Preview(showBackground = true)
@Composable
fun TaskAppPreview() {
    TaskApp()
}
