package br.edu.satc.todolistcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import br.edu.satc.todolistcompose.ui.screens.HomeScreen
import br.edu.satc.todolistcompose.ui.theme.ToDoListComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Database.db = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "task-db"
        ).allowMainThreadQueries().build()

        setContent {
            ToDoListComposeTheme{
                HomeScreen()
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDoListComposeTheme { }
}}

