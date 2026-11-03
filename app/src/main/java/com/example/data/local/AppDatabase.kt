package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.ActivityLogItem
import com.example.data.model.AssignmentItem
import com.example.data.model.AssignmentStatus
import com.example.data.model.ChatMessage
import com.example.data.model.ClassEntity
import com.example.data.model.LearningTopicRecord
import com.example.data.model.NotificationItem
import com.example.data.model.SchoolConfigEntity
import com.example.data.model.StudentRosterEntity
import com.example.data.model.StudyCapsule
import com.example.data.model.SubjectCourse
import com.example.data.model.SubmittedWork
import com.example.data.model.TeacherEntity

class Converters {
    @TypeConverter
    fun fromAssignmentStatus(status: AssignmentStatus?): String = status?.name ?: AssignmentStatus.PENDING.name

    @TypeConverter
    fun toAssignmentStatus(value: String?): AssignmentStatus = try {
        value?.let { AssignmentStatus.valueOf(it) } ?: AssignmentStatus.PENDING
    } catch (_: Exception) {
        AssignmentStatus.PENDING
    }
}

@Database(
    entities = [
        AssignmentItem::class,
        SubjectCourse::class,
        StudyCapsule::class,
        ChatMessage::class,
        SubmittedWork::class,
        ActivityLogItem::class,
        TeacherEntity::class,
        ClassEntity::class,
        StudentRosterEntity::class,
        SchoolConfigEntity::class,
        NotificationItem::class,
        LearningTopicRecord::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vaishu_school.db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
