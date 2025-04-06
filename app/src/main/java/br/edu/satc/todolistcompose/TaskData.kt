package br.edu.satc.todolistcompose

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity
data class TaskData(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "task_title") val taskName: String,
    @ColumnInfo(name = "task_description") val taskDescription: String
)

@Dao
interface TaskDao {
    @Query("SELECT * FROM taskdata")
    fun getAll(): List<TaskData>

    @Insert
    fun insertAll(vararg tasks: TaskData)

    @Query("DELETE FROM taskdata")
    fun deleteAll()
}

@Database(entities = [TaskData::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
