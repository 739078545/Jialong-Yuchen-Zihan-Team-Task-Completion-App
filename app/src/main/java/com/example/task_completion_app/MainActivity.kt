package com.example.task_completion_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var taskText by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<Task>()) }

    Column(modifier = Modifier.padding(16.dp)) {
        TaskInputField(
            taskText = taskText,
            onTaskTextChange = { taskText = it },
            onAddTask = {
                if (taskText.isNotEmpty()) {
                    tasks = tasks + Task(taskText, false)
                    taskText = ""
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
    taskText: String,
    onTaskTextChange: (String) -> Unit,
    onAddTask: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = taskText,
            onValueChange = onTaskTextChange,
            placeholder = { Text("Enter task...") },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.LightGray,
                focusedIndicatorColor = Color.Blue,
                unfocusedIndicatorColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onAddTask) {
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
        Text(
            text = task.description,
            style = TextStyle(
                fontSize = 18.sp,
                textDecoration = if (task.isComplete) TextDecoration.LineThrough else null,
                color = if (task.isComplete) Color.Gray else Color.Black
            )
        )
        Checkbox(
            checked = task.isComplete,
            onCheckedChange = onCheckedChange
        )
    }
}

data class Task(val description: String, val isComplete: Boolean)

@Preview(showBackground = true)
@Composable
fun TaskAppPreview() {
    TaskApp()
}
