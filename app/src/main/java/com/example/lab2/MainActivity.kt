package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

/* -----------------------------
   Task Model
-------------------------------- */
data class Task(
    val description: String,
    val isCompleted: Boolean = false
)

/* -----------------------------
   Main Screen
-------------------------------- */
@Composable
fun MainScreen() {

    var taskText by remember { mutableStateOf("") }
    val taskList = remember { mutableStateListOf<Task>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Task Manager",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskInputField(
            taskText = taskText,
            onTextChange = { taskText = it },
            onAddClick = {
                if (taskText.isNotBlank()) {
                    taskList.add(Task(taskText))
                    taskText = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        TaskList(
            tasks = taskList,
            onToggle = { index ->
                val task = taskList[index]
                taskList[index] = task.copy(isCompleted = !task.isCompleted)
            },
            onDelete = { index ->
                taskList.removeAt(index)
            }
        )
    }
}

/* -----------------------------
   Input Field + Button
-------------------------------- */
@Composable
fun TaskInputField(
    taskText: String,
    onTextChange: (String) -> Unit,
    onAddClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        OutlinedTextField(
            value = taskText,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Enter task") },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7B1FA2)
            )
        ) {
            Text("Add Task")
        }
    }
}

/* -----------------------------
   Task List
-------------------------------- */
@Composable
fun TaskList(
    tasks: List<Task>,
    onToggle: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(tasks) { index, task ->
            TaskItem(
                task = task,
                onToggle = { onToggle(index) },
                onDelete = { onDelete(index) }
            )
        }
    }
}

/* -----------------------------
   Individual Task Item
-------------------------------- */
@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF7B1FA2),
                checkmarkColor = Color.White
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = task.description,
            modifier = Modifier.weight(1f),
            textDecoration = if (task.isCompleted)
                TextDecoration.LineThrough
            else
                TextDecoration.None,
            color = if (task.isCompleted)
                Color.Gray
            else
                MaterialTheme.colorScheme.onBackground
        )

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}
