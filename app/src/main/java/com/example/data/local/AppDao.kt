package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ActivityLogItem
import com.example.data.model.AssignmentItem
import com.example.data.model.ChatMessage
import com.example.data.model.StudyCapsule
import com.example.data.model.SubjectCourse
import com.example.data.model.SubmittedWork
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {
    // Assignments
    @Query("SELECT * FROM assignments ORDER BY dueDate ASC")
    fun getAllAssignments(): Flow<List<AssignmentItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignments(items: List<AssignmentItem>)

    @Update
    suspend fun updateAssignment(item: AssignmentItem)

    // Subjects
    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): Flow<List<SubjectCourse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(items: List<SubjectCourse>)

    // Capsules
    @Query("SELECT * FROM study_capsules")
    fun getAllCapsules(): Flow<List<StudyCapsule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCapsules(items: List<StudyCapsule>)

    // AI Chat
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage): Long

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatMessages()

    // Submitted School Work
    @Query("SELECT * FROM submitted_work ORDER BY submissionTimestamp DESC")
    fun getAllSubmittedWork(): Flow<List<SubmittedWork>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmittedWork(item: SubmittedWork)

    @Update
    suspend fun updateSubmittedWork(item: SubmittedWork)

    // Activity Logs
    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC")
    fun getAllActivityLogs(): Flow<List<ActivityLogItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLog(item: ActivityLogItem)

    // ==========================================
    // Staff & School Management Operations
    // ==========================================

    // Teachers
    @Query("SELECT * FROM teachers WHERE schoolId = :schoolId")
    fun getTeachersBySchool(schoolId: String): Flow<List<com.example.data.model.TeacherEntity>>

    @Query("SELECT * FROM teachers WHERE email = :email AND accessCode = :accessCode LIMIT 1")
    suspend fun findTeacherByCredentials(email: String, accessCode: String): com.example.data.model.TeacherEntity?

    @Query("SELECT * FROM teachers WHERE teacherId = :teacherId LIMIT 1")
    suspend fun getTeacherById(teacherId: String): com.example.data.model.TeacherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: com.example.data.model.TeacherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeachers(teachers: List<com.example.data.model.TeacherEntity>)

    // Classes
    @Query("SELECT * FROM school_classes WHERE schoolId = :schoolId")
    fun getClassesBySchool(schoolId: String): Flow<List<com.example.data.model.ClassEntity>>

    @Query("SELECT * FROM school_classes WHERE classId IN (:classIds)")
    suspend fun getClassesByIds(classIds: List<String>): List<com.example.data.model.ClassEntity>

    @Query("SELECT * FROM school_classes WHERE classId = :classId LIMIT 1")
    suspend fun getClassById(classId: String): com.example.data.model.ClassEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClasses(classes: List<com.example.data.model.ClassEntity>)

    // Class Students Roster
    @Query("SELECT * FROM class_students WHERE schoolId = :schoolId AND classId = :classId ORDER BY rollNumber ASC")
    suspend fun getStudentsForClass(schoolId: String, classId: String): List<com.example.data.model.StudentRosterEntity>

    @Query("SELECT * FROM class_students WHERE schoolId = :schoolId AND classId IN (:classIds) ORDER BY rollNumber ASC")
    suspend fun getStudentsForClasses(schoolId: String, classIds: List<String>): List<com.example.data.model.StudentRosterEntity>

    @Query("SELECT * FROM class_students WHERE schoolId = :schoolId")
    suspend fun getAllStudentsBySchool(schoolId: String): List<com.example.data.model.StudentRosterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<com.example.data.model.StudentRosterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: com.example.data.model.StudentRosterEntity)

    @Query("SELECT * FROM class_students WHERE studentId = :studentId LIMIT 1")
    suspend fun getStudentById(studentId: String): com.example.data.model.StudentRosterEntity?

    @Query("SELECT * FROM class_students WHERE schoolId = :schoolId AND classId = :classId AND (points > 0 OR streakDays > 0) ORDER BY points DESC, streakDays DESC")
    suspend fun getLeaderboardStudents(schoolId: String, classId: String): List<com.example.data.model.StudentRosterEntity>

    // School Config & Security
    @Query("SELECT * FROM school_config WHERE schoolId = :schoolId LIMIT 1")
    suspend fun getSchoolConfig(schoolId: String): com.example.data.model.SchoolConfigEntity?

    @Query("SELECT * FROM school_config WHERE secretAdminKey = :secretKey LIMIT 1")
    suspend fun findSchoolBySecretKey(secretKey: String): com.example.data.model.SchoolConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchoolConfig(config: com.example.data.model.SchoolConfigEntity)

    @Update
    suspend fun updateSchoolConfig(config: com.example.data.model.SchoolConfigEntity)

    // Notifications
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<com.example.data.model.NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: com.example.data.model.NotificationItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<com.example.data.model.NotificationItem>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: String)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllNotificationsAsRead()

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotification(id: String)

    @Query("DELETE FROM notifications")
    suspend fun clearAllNotifications()

    // Learning Library
    @Query("SELECT * FROM learning_library ORDER BY lastStudiedTimestamp DESC")
    fun getLearningLibrary(): Flow<List<com.example.data.model.LearningTopicRecord>>

    @Query("SELECT * FROM learning_library WHERE id = :id LIMIT 1")
    suspend fun getLearningTopicById(id: String): com.example.data.model.LearningTopicRecord?

    @Query("SELECT * FROM learning_library WHERE subject = :subject AND topic = :topic LIMIT 1")
    suspend fun findTopic(subject: String, topic: String): com.example.data.model.LearningTopicRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLearningTopic(topic: com.example.data.model.LearningTopicRecord)
}
