package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AuthScreenState {
    SPLASH_LOADING,
    GOOGLE_LOGIN,
    ROLE_SELECTION,
    STUDENT_ONBOARDING,
    MAIN_APP
}

enum class UserRole(val displayName: String, val subtitle: String, val defaultBadge: String) {
    STUDENT("Student", "Personalized AI Study & Homework Companion", "Grade 11-A"),
    OTHER("Other (Staff & Admin)", "Teacher & School Leadership Hub", "Staff Desk"),
    TEACHER("Teacher", "AI Lesson Planner & Classroom Insights", "Faculty - Science"),
    PRINCIPAL("Principal / Admin", "School-wide AI Analytics & Operations", "Leadership Desk")
}

enum class LearningMode(val title: String, val promptPrefix: String, val description: String) {
    LEARN("Learn", "Explain the concept clearly and intuitively: ", "Explains core concepts with friendly analogies"),
    TUTORIAL("Tutorial", "Teach me step-by-step with structured milestones: ", "Teaches step-by-step guidance"),
    PRACTICE("Practice", "Generate 3 guided practice questions on: ", "Generates interactive problem sets"),
    TEST("Test", "Generate a structured test with multiple choice options on: ", "Generates a complete knowledge test"),
    EXPLAIN("Explain", "Explain and break down this educational material: ", "Explains uploaded educational notes"),
    HINT("Hint", "Do not reveal the direct answer yet! Give me a thoughtful hint to solve: ", "Provides Socratic hints without spoiling the answer")
}

data class StudentProfile(
    val fullName: String = "Amrit Kumar",
    val email: String = "amritk72316@gmail.com",
    val rollNumber: String = "24",
    val schoolName: String = "St. Jude's International Academy",
    val className: String = "Class 11",
    val section: String = "A",
    val streakDays: Int = 7,
    val points: Int = 1450,
    val classRank: String = "#3 in 11-A",
    val overallProgress: Float = 0.78f,
    val isVerified: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val streakNotifications: Boolean = true,
    val assignmentNotifications: Boolean = true,
    val teacherFeedbackNotifications: Boolean = true,
    val dailyChallengeNotifications: Boolean = true
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val category: String, // "Learning", "Streak", "Teacher", "School", "Assignment", "Achievement", "Report", "Security"
    val timestamp: Long = System.currentTimeMillis(),
    val timeAgoFormatted: String = "Just now",
    val isRead: Boolean = false,
    val actionRoute: String? = null // e.g. "STREAK", "LEARN", "RAPID", "SUBMIT", "LIBRARY"
)

@Entity(tableName = "learning_library")
data class LearningTopicRecord(
    @PrimaryKey val id: String,
    val subject: String,
    val topic: String,
    val chapter: String,
    val lastStudiedTimestamp: Long = System.currentTimeMillis(),
    val lastStudiedFormatted: String,
    val questionsCompleted: Int,
    val accuracyPercentage: Int,
    val progress: Float,
    val sessionsCount: Int = 1,
    val notesSummary: String = ""
)

data class DayProgress(
    val dayNumber: String,
    val dayOfWeek: String,
    val fullDate: String,
    val isToday: Boolean,
    val studyMinutes: Int,
    val tasksCompleted: Int,
    val quizScore: String,
    val topicsExplored: List<String>
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val className: String,
    val points: Int,
    val streak: Int,
    val isCurrentUser: Boolean = false
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

@Entity(tableName = "submitted_work")
data class SubmittedWork(
    @PrimaryKey val id: String,
    val title: String,
    val subject: String,
    val workType: String, // Notes, Homework, Question answers, Worksheets, Assignments
    val topic: String = "",
    val chapter: String = "",
    val whatLearned: String = "",
    val hasPhoto: Boolean = false,
    val imageUri: String? = null,
    val submissionTimestamp: Long = System.currentTimeMillis(),
    val submissionDateStr: String,
    val notes: String = "",
    val aiAnalysisSummary: String,
    val questionsCount: Int = 3,
    val isQuizCompleted: Boolean = false,
    val score: String = "Pending Quiz",
    val pointsEarned: Int = 50
)

@Entity(tableName = "activity_logs")
data class ActivityLogItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // "Submission", "Quiz", "AI Study", "Streak"
    val pointsAwarded: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val dateFormatted: String
)

data class DayStreakStatus(
    val dayLabel: String,
    val dateStr: String,
    val isVerifiedActive: Boolean,
    val pointsEarned: Int
)

data class StreakState(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val graceFreezesLeft: Int = 2,
    val lastActiveDay: String = "None",
    val streakProtected: Boolean = true,
    val todayPoints: Int = 0,
    val todayGoalPoints: Int = 100,
    val todayTasksCompleted: Int = 0,
    val dailyChallengeTitle: String = "Complete 5 Rapid Questions",
    val isDailyChallengeDone: Boolean = false,
    val recent7Days: List<DayStreakStatus> = emptyList()
)

data class StudentProgressSummary(
    val totalXp: Int,
    val currentLevel: Int,
    val levelProgress: Float,
    val currentStreak: Int,
    val accuracyRate: Int,
    val totalQuizzesCompleted: Int,
    val totalWorkSubmissions: Int,
    val hoursThisWeek: Float,
    val topicsMastered: List<String>
)

enum class AssignmentStatus {
    PENDING,
    SUBMITTED,
    GRADED
}

@Entity(tableName = "assignments")
data class AssignmentItem(
    @PrimaryKey val id: String,
    val title: String,
    val subject: String,
    val dueDate: String,
    val status: AssignmentStatus,
    val grade: String? = null,
    val aiSummaryFeedback: String? = null,
    val description: String = ""
)

@Entity(tableName = "subjects")
data class SubjectCourse(
    @PrimaryKey val id: String,
    val title: String,
    val code: String,
    val instructor: String,
    val room: String,
    val colorHex: Long,
    val progressPercent: Float,
    val nextTopic: String
)

@Entity(tableName = "study_capsules")
data class StudyCapsule(
    @PrimaryKey val id: String,
    val title: String,
    val subject: String,
    val readMinutes: Int,
    val difficulty: String,
    val summary: String,
    val aiKeyPoint: String
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderName: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val suggestions: String = "",
    val learningMode: String = "LEARN"
)

data class SchoolMetric(
    val id: String,
    val label: String,
    val value: String,
    val changeDescription: String,
    val isPositive: Boolean,
    val iconName: String
)

data class SchoolAnnouncement(
    val id: String,
    val title: String,
    val author: String,
    val date: String,
    val content: String,
    val isUrgent: Boolean = false,
    val targetRole: String = "ALL"
)

// ==========================================
// School Staff & Role Management Entities
// ==========================================

@Entity(tableName = "teachers")
data class TeacherEntity(
    @PrimaryKey val teacherId: String,
    val schoolId: String,
    val fullName: String,
    val email: String,
    val accessCode: String, // Secure access code generated by Master Admin (e.g. TCH-7421)
    val assignedClassIds: String, // Comma-separated class IDs, e.g. "class_11_a,class_11_b"
    val subject: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "school_classes")
data class ClassEntity(
    @PrimaryKey val classId: String,
    val schoolId: String,
    val name: String, // e.g. "Class 11-A"
    val subject: String, // e.g. "Physics"
    val teacherId: String
)

@Entity(tableName = "class_students")
data class StudentRosterEntity(
    @PrimaryKey val studentId: String,
    val schoolId: String,
    val classId: String,
    val fullName: String,
    val rollNumber: String,
    val streakDays: Int,
    val points: Int,
    val submissionStatus: String, // "Submitted Today", "Pending Review", "Up to Date"
    val overallProgress: Float,
    val className: String = "Class 11",
    val section: String = "A"
)

@Entity(tableName = "school_config")
data class SchoolConfigEntity(
    @PrimaryKey val schoolId: String, // e.g. "school_st_jude"
    val schoolName: String,
    val secretAdminKey: String, // Rotatable secret admin key, e.g. "VAISHU-GATEWAY-2026"
    val principalEmail: String,
    val principalPasscode: String,
    val submissionPoints: Int = 50,
    val quizPoints: Int = 150,
    val aiStudyPoints: Int = 15,
    val minStreakMinutes: Int = 20,
    val streakGraceDays: Int = 2
)

data class StaffSession(
    val sessionToken: String,
    val userId: String,
    val schoolId: String,
    val schoolName: String,
    val role: UserRole, // TEACHER or PRINCIPAL
    val teacherId: String? = null,
    val displayName: String,
    val email: String,
    val assignedClassIds: List<String> = emptyList(),
    val authenticatedAt: Long = System.currentTimeMillis()
)

data class MasterAdminStats(
    val totalStudents: Int,
    val activeTeachers: Int,
    val totalClasses: Int,
    val averageStreak: Int,
    val dailySubmissionsCount: Int,
    val schoolAttendanceRate: String
)

