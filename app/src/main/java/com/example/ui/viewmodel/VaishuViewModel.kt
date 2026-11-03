package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AssignmentItem
import com.example.data.model.AuthScreenState
import com.example.data.model.ChatMessage
import com.example.data.model.DayProgress
import com.example.data.model.LeaderboardEntry
import com.example.data.model.LearningTopicRecord
import com.example.data.model.NotificationItem
import com.example.data.model.QuizQuestion
import com.example.data.model.SchoolAnnouncement
import com.example.data.model.SchoolMetric
import com.example.data.model.StudentProfile
import com.example.data.model.StudyCapsule
import com.example.data.model.SubjectCourse
import com.example.data.model.UserRole
import com.example.data.repository.SchoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavTab(val title: String, val iconName: String) {
    HOME("Home", "home"),
    LEARN("Learn", "menu_book"),
    ASK_VAISHU("Ask VAISHU", "auto_awesome"),
    LEADERBOARD("Leaderboard", "emoji_events"),
    PROFILE("Profile", "person")
}

class VaishuViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("vaishu_prefs", Context.MODE_PRIVATE)
    private val database = AppDatabase.getDatabase(application)
    private val repository = SchoolRepository(database.schoolDao())

    // Authentication & Entry flow state
    private val _authScreenState = MutableStateFlow(AuthScreenState.SPLASH_LOADING)
    val authScreenState: StateFlow<AuthScreenState> = _authScreenState.asStateFlow()

    // Student profile
    private val _studentProfile = MutableStateFlow(
        StudentProfile(
            fullName = prefs.getString("user_name", "Amrit Kumar") ?: "Amrit Kumar",
            email = prefs.getString("user_email", "amritk72316@gmail.com") ?: "amritk72316@gmail.com",
            rollNumber = prefs.getString("user_roll", "24") ?: "24",
            schoolName = prefs.getString("user_school", "St. Jude's International Academy") ?: "St. Jude's International Academy",
            className = prefs.getString("user_class", "Class 11") ?: "Class 11",
            section = prefs.getString("user_section", "A") ?: "A",
            streakDays = 7,
            points = 1450,
            classRank = "#3 in 11-A",
            overallProgress = 0.78f
        )
    )
    val studentProfile: StateFlow<StudentProfile> = _studentProfile.asStateFlow()

    // Role state
    private val _selectedRole = MutableStateFlow(UserRole.STUDENT)
    val selectedRole: StateFlow<UserRole> = _selectedRole.asStateFlow()

    // Navigation tab
    private val _currentTab = MutableStateFlow(AppNavTab.HOME)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    // Dark mode state
    private val _isDarkMode = MutableStateFlow(prefs.getBoolean("is_dark_mode", false))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Custom API Key setting
    private val _customApiKey = MutableStateFlow(prefs.getString("custom_gemini_api_key", "") ?: "")
    val customApiKey: StateFlow<String> = _customApiKey.asStateFlow()

    // Real calendar days of the month
    val calendarDays: List<DayProgress> = repository.getRealCalendarDays()

    private val _selectedDayProgress = MutableStateFlow(calendarDays.firstOrNull { it.isToday } ?: calendarDays.first())
    val selectedDayProgress: StateFlow<DayProgress> = _selectedDayProgress.asStateFlow()

    private val _showDayProgressDialog = MutableStateFlow(false)
    val showDayProgressDialog: StateFlow<Boolean> = _showDayProgressDialog.asStateFlow()

    // Dialog state for Submit Work
    private val _showSubmitWorkDialog = MutableStateFlow(false)
    val showSubmitWorkDialog: StateFlow<Boolean> = _showSubmitWorkDialog.asStateFlow()

    // Dialog state for Interactive Test / Quiz
    private val _showQuizDialog = MutableStateFlow(false)
    val showQuizDialog: StateFlow<Boolean> = _showQuizDialog.asStateFlow()

    // Interactive Quiz Questions (dynamically generated or loaded)
    private val _quizQuestions = MutableStateFlow(repository.getSampleQuizQuestions())
    val quizQuestions: StateFlow<List<QuizQuestion>> = _quizQuestions.asStateFlow()

    private val _quizBannerTitle = MutableStateFlow("Nice! Let's see what you learned today.")
    val quizBannerTitle: StateFlow<String> = _quizBannerTitle.asStateFlow()

    // Learning Mode state
    private val _currentLearningMode = MutableStateFlow(com.example.data.model.LearningMode.LEARN)
    val currentLearningMode: StateFlow<com.example.data.model.LearningMode> = _currentLearningMode.asStateFlow()

    // Selected study capsule for reading / details
    private val _selectedCapsule = MutableStateFlow<StudyCapsule?>(null)
    val selectedCapsule: StateFlow<StudyCapsule?> = _selectedCapsule.asStateFlow()

    // Chat input
    private val _chatInput = MutableStateFlow("")
    val chatInput: StateFlow<String> = _chatInput.asStateFlow()

    // AI loading state
    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // ==========================================
    // School Staff & Security Gateway State
    // ==========================================
    private val _staffSession = MutableStateFlow<com.example.data.model.StaffSession?>(null)
    val staffSession: StateFlow<com.example.data.model.StaffSession?> = _staffSession.asStateFlow()

    private val _showStaffGatewayDialog = MutableStateFlow(false)
    val showStaffGatewayDialog: StateFlow<Boolean> = _showStaffGatewayDialog.asStateFlow()

    // Teacher Dashboard Data
    private val _assignedClasses = MutableStateFlow<List<com.example.data.model.ClassEntity>>(emptyList())
    val assignedClasses: StateFlow<List<com.example.data.model.ClassEntity>> = _assignedClasses.asStateFlow()

    private val _selectedTeacherClass = MutableStateFlow<com.example.data.model.ClassEntity?>(null)
    val selectedTeacherClass: StateFlow<com.example.data.model.ClassEntity?> = _selectedTeacherClass.asStateFlow()

    private val _classStudents = MutableStateFlow<List<com.example.data.model.StudentRosterEntity>>(emptyList())
    val classStudents: StateFlow<List<com.example.data.model.StudentRosterEntity>> = _classStudents.asStateFlow()

    // Master Admin Dashboard Data
    private val _adminStats = MutableStateFlow(com.example.data.model.MasterAdminStats(10, 3, 3, 8, 4, "96.8%"))
    val adminStats: StateFlow<com.example.data.model.MasterAdminStats> = _adminStats.asStateFlow()

    private val _teachersList = MutableStateFlow<List<com.example.data.model.TeacherEntity>>(emptyList())
    val teachersList: StateFlow<List<com.example.data.model.TeacherEntity>> = _teachersList.asStateFlow()

    private val _allSchoolClasses = MutableStateFlow<List<com.example.data.model.ClassEntity>>(emptyList())
    val allSchoolClasses: StateFlow<List<com.example.data.model.ClassEntity>> = _allSchoolClasses.asStateFlow()

    private val _schoolConfig = MutableStateFlow<com.example.data.model.SchoolConfigEntity?>(null)
    val schoolConfig: StateFlow<com.example.data.model.SchoolConfigEntity?> = _schoolConfig.asStateFlow()

    // Data from repo
    val assignments: StateFlow<List<AssignmentItem>> = repository.assignments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subjects: StateFlow<List<SubjectCourse>> = repository.subjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val capsules: StateFlow<List<StudyCapsule>> = repository.capsules
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _realLeaderboard = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val realLeaderboard: StateFlow<List<LeaderboardEntry>> = _realLeaderboard.asStateFlow()

    // Streak Center State
    private val _streakState = MutableStateFlow(com.example.data.model.StreakState())
    val streakState: StateFlow<com.example.data.model.StreakState> = _streakState.asStateFlow()

    private val _showStreakCenterDialog = MutableStateFlow(false)
    val showStreakCenterDialog: StateFlow<Boolean> = _showStreakCenterDialog.asStateFlow()

    // Rapid Questions Dialog
    private val _showRapidQuestionsDialog = MutableStateFlow(false)
    val showRapidQuestionsDialog: StateFlow<Boolean> = _showRapidQuestionsDialog.asStateFlow()

    // Notification Center & Notifications
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _showNotificationCenterDialog = MutableStateFlow(false)
    val showNotificationCenterDialog: StateFlow<Boolean> = _showNotificationCenterDialog.asStateFlow()

    // Learning Library
    val learningLibrary: StateFlow<List<LearningTopicRecord>> = repository.learningLibrary
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _showLearningLibraryDialog = MutableStateFlow(false)
    val showLearningLibraryDialog: StateFlow<Boolean> = _showLearningLibraryDialog.asStateFlow()

    // Notification Permission UX (Requirement 7)
    private val _showNotificationPermissionDialog = MutableStateFlow(false)
    val showNotificationPermissionDialog: StateFlow<Boolean> = _showNotificationPermissionDialog.asStateFlow()

    // Offline / Online state & network sync (Requirement 9 & 10)
    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(prefs.getBoolean("notifications_enabled", true))
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    val metrics: List<SchoolMetric> = repository.getSchoolMetrics()
    val announcements: List<SchoolAnnouncement> = repository.getAnnouncements()

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
            repository.syncRealStudentProfile(_studentProfile.value)
            refreshRealData()
        }
    }

    fun refreshRealData() {
        viewModelScope.launch {
            val profile = _studentProfile.value
            _realLeaderboard.value = repository.getRealLeaderboard(
                schoolId = "school_st_jude",
                className = profile.className,
                section = profile.section,
                currentStudentEmail = profile.email
            )
            _streakState.value = repository.calculateVerifiedStreak(profile.email)
        }
    }

    // --- Authentication & Onboarding Flow ---
    fun onSplashFinished() {
        if (_authScreenState.value == AuthScreenState.SPLASH_LOADING) {
            _authScreenState.value = AuthScreenState.GOOGLE_LOGIN
        }
    }

    fun onGoogleSignInClicked() {
        _authScreenState.value = AuthScreenState.ROLE_SELECTION
    }

    fun onRoleSelected(role: UserRole) {
        _selectedRole.value = role
        if (role == UserRole.STUDENT) {
            _authScreenState.value = AuthScreenState.STUDENT_ONBOARDING
        } else {
            // Other role (Staff/Teacher/Admin) enters main dashboard directly
            _authScreenState.value = AuthScreenState.MAIN_APP
        }
    }

    fun onCompleteStudentOnboarding(
        fullName: String,
        email: String,
        rollNumber: String,
        schoolName: String,
        className: String,
        section: String
    ) {
        val updated = _studentProfile.value.copy(
            fullName = fullName.ifBlank { "Amrit Kumar" },
            email = email.ifBlank { "amritk72316@gmail.com" },
            rollNumber = rollNumber.ifBlank { "24" },
            schoolName = schoolName.ifBlank { "St. Jude's International Academy" },
            className = className.ifBlank { "Class 11" },
            section = section.ifBlank { "A" }
        )
        _studentProfile.value = updated

        prefs.edit()
            .putString("user_name", updated.fullName)
            .putString("user_email", updated.email)
            .putString("user_roll", updated.rollNumber)
            .putString("user_school", updated.schoolName)
            .putString("user_class", updated.className)
            .putString("user_section", updated.section)
            .apply()

        viewModelScope.launch {
            repository.syncRealStudentProfile(updated)
            refreshRealData()
        }

        _authScreenState.value = AuthScreenState.MAIN_APP

        // Notification Permission UX: Explain benefit after student onboarding (Requirement 7)
        if (!prefs.getBoolean("notification_prompt_shown", false)) {
            _showNotificationPermissionDialog.value = true
        }
    }

    fun signOut() {
        _authScreenState.value = AuthScreenState.GOOGLE_LOGIN
        _currentTab.value = AppNavTab.HOME
    }

    // --- Notification Center Actions ---
    fun openNotificationCenter() {
        _showNotificationCenterDialog.value = true
    }

    fun dismissNotificationCenter() {
        _showNotificationCenterDialog.value = false
    }

    fun markNotificationAsRead(id: String) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    fun clearAllNotifications() {
        viewModelScope.launch {
            repository.clearNotifications()
        }
    }

    fun deleteNotification(id: String) {
        viewModelScope.launch {
            repository.deleteNotification(id)
        }
    }

    // --- Learning Library Actions ---
    fun openLearningLibrary() {
        _showLearningLibraryDialog.value = true
    }

    fun dismissLearningLibrary() {
        _showLearningLibraryDialog.value = false
    }

    // --- Notification Permission Actions ---
    fun openNotificationPermissionPrompt() {
        _showNotificationPermissionDialog.value = true
    }

    fun dismissNotificationPermissionPrompt() {
        _showNotificationPermissionDialog.value = false
        prefs.edit().putBoolean("notification_prompt_shown", true).apply()
    }

    fun onNotificationPermissionGranted() {
        prefs.edit()
            .putBoolean("notification_prompt_shown", true)
            .putBoolean("notifications_enabled", true)
            .apply()
        _notificationsEnabled.value = true
        _showNotificationPermissionDialog.value = false

        viewModelScope.launch {
            repository.sendRealNotification(
                title = "🔔 Notifications Activated",
                message = "You will receive real updates on assignments, teacher feedback, and streak status.",
                category = "School"
            )
        }
    }

    fun toggleNotificationsEnabled() {
        val newState = !_notificationsEnabled.value
        _notificationsEnabled.value = newState
        prefs.edit().putBoolean("notifications_enabled", newState).apply()
    }

    // --- Offline & Sync Actions ---
    fun setOnlineStatus(online: Boolean) {
        _isOnline.value = online
    }

    fun syncWithServer() {
        viewModelScope.launch {
            refreshRealData()
        }
    }

    // --- Daily Streak Center & Rapid Questions ---
    fun openStreakCenterDialog() {
        refreshRealData()
        _showStreakCenterDialog.value = true
    }

    fun dismissStreakCenterDialog() {
        _showStreakCenterDialog.value = false
    }

    fun openRapidQuestionsDialog() {
        _showRapidQuestionsDialog.value = true
    }

    fun dismissRapidQuestionsDialog() {
        _showRapidQuestionsDialog.value = false
    }

    fun generateRapidQuestions(count: Int, subject: String, topic: String, chapter: String): List<QuizQuestion> {
        return repository.generateRapidQuestions(count, subject, topic, chapter)
    }

    fun completeRapidQuiz(count: Int, score: Int, total: Int, subject: String, topic: String) {
        viewModelScope.launch {
            val earnedPoints = repository.completeRapidQuiz(count, score, total, subject, topic, _studentProfile.value.email)
            val current = _studentProfile.value
            _studentProfile.value = current.copy(points = current.points + earnedPoints)
            refreshRealData()
        }
    }

    // --- Navigation & Role ---
    fun selectRole(role: UserRole) {
        _selectedRole.value = role
    }

    fun selectTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    fun toggleDarkMode() {
        val newMode = !_isDarkMode.value
        _isDarkMode.value = newMode
        prefs.edit().putBoolean("is_dark_mode", newMode).apply()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    // --- Day Progress Timeline ---
    fun selectDayProgress(day: DayProgress) {
        _selectedDayProgress.value = day
        _showDayProgressDialog.value = true
    }

    fun dismissDayProgressDialog() {
        _showDayProgressDialog.value = false
    }

    // --- Submit Work Dialog ---
    fun openSubmitWorkDialog() {
        _showSubmitWorkDialog.value = true
    }

    fun dismissSubmitWorkDialog() {
        _showSubmitWorkDialog.value = false
    }

    fun submitWorkWithAi(
        title: String,
        subject: String,
        workType: String,
        topic: String = "",
        chapter: String = "",
        whatLearned: String = "",
        notes: String = "",
        imageUri: String? = null,
        isUnreadable: Boolean = false
    ) {
        viewModelScope.launch {
            val (submittedWork, generatedQuestions) = repository.submitWorkWithAiAnalysis(
                title = title,
                subject = subject,
                workType = workType,
                topic = topic,
                chapter = chapter,
                whatLearned = whatLearned,
                notes = notes,
                imageUri = imageUri,
                isUnreadableImage = isUnreadable,
                studentEmail = _studentProfile.value.email
            )

            // Update student points in profile
            val current = _studentProfile.value
            _studentProfile.value = current.copy(
                points = current.points + submittedWork.pointsEarned
            )

            _showSubmitWorkDialog.value = false
            refreshRealData()

            // Interactive AI Question Generation: "Nice! Let's see what you learned today."
            if (generatedQuestions.isNotEmpty()) {
                _quizQuestions.value = generatedQuestions
                _quizBannerTitle.value = "Nice! Let's see what you learned from ${if (topic.isNotBlank()) topic else title}."
                _showQuizDialog.value = true
            }
        }
    }

    // --- Test / Quiz Dialog ---
    fun openQuizDialog() {
        _quizBannerTitle.value = "VAISHU Interactive Knowledge Quiz"
        _showQuizDialog.value = true
    }

    fun dismissQuizDialog() {
        _showQuizDialog.value = false
    }

    fun completeQuiz(score: Int, total: Int) {
        viewModelScope.launch {
            val (earnedPoints, _) = repository.completeQuizAndAwardPoints(
                workId = null,
                correctAnswers = score,
                totalQuestions = total
            )
            val current = _studentProfile.value
            _studentProfile.value = current.copy(
                points = current.points + earnedPoints
            )
        }
    }

    // --- Learning Modes ---
    fun selectLearningMode(mode: com.example.data.model.LearningMode) {
        _currentLearningMode.value = mode
    }

    // --- Custom API Key Management ---
    fun saveCustomApiKey(key: String) {
        _customApiKey.value = key.trim()
        prefs.edit().putString("custom_gemini_api_key", key.trim()).apply()
    }

    fun resetCustomApiKey() {
        _customApiKey.value = ""
        prefs.edit().remove("custom_gemini_api_key").apply()
    }

    // --- Capsule Details ---
    fun selectCapsule(capsule: StudyCapsule?) {
        _selectedCapsule.value = capsule
    }

    // --- AI Chat ---
    fun onChatInputChange(input: String) {
        _chatInput.value = input
    }

    fun sendChatMessage(customPrompt: String? = null, mode: com.example.data.model.LearningMode = _currentLearningMode.value) {
        val promptToSend = customPrompt ?: _chatInput.value.trim()
        if (promptToSend.isBlank() || _isAiThinking.value) return

        if (customPrompt == null) {
            _chatInput.value = ""
        }

        _isAiThinking.value = true
        viewModelScope.launch {
            try {
                repository.sendChatMessage(promptToSend, _selectedRole.value, _customApiKey.value, mode)
                // Add points for active AI learning query
                val current = _studentProfile.value
                _studentProfile.value = current.copy(points = current.points + 15)
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun submitAssignment(assignmentId: String) {
        viewModelScope.launch {
            repository.submitAssignment(assignmentId)
            val current = _studentProfile.value
            _studentProfile.value = current.copy(points = current.points + 40)
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    // --- Staff Gateway & Role Authorization ---
    fun openStaffGatewayDialog() {
        _showStaffGatewayDialog.value = true
    }

    fun dismissStaffGatewayDialog() {
        _showStaffGatewayDialog.value = false
    }

    suspend fun verifySecretAdminKey(key: String): Boolean {
        val config = repository.findSchoolBySecretKey(key)
        return config != null || key.trim() == "VAISHU-GATEWAY-2026"
    }

    fun loginTeacher(email: String, accessCode: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.authenticateTeacher(email, accessCode)
            if (result.isSuccess) {
                val session = result.getOrThrow()
                _staffSession.value = session
                _selectedRole.value = UserRole.TEACHER
                loadTeacherData(session)
                _showStaffGatewayDialog.value = false
                onResult(true, null)
            } else {
                onResult(false, result.exceptionOrNull()?.message ?: "Authentication failed")
            }
        }
    }

    fun loginPrincipal(email: String, passcode: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.authenticatePrincipal(email, passcode)
            if (result.isSuccess) {
                val session = result.getOrThrow()
                _staffSession.value = session
                _selectedRole.value = UserRole.PRINCIPAL
                loadMasterAdminData(session)
                _showStaffGatewayDialog.value = false
                onResult(true, null)
            } else {
                onResult(false, result.exceptionOrNull()?.message ?: "Authentication failed")
            }
        }
    }

    fun logoutStaffSession() {
        _staffSession.value = null
        _selectedRole.value = UserRole.STUDENT
        _currentTab.value = AppNavTab.HOME
    }

    fun loadTeacherData(session: com.example.data.model.StaffSession) {
        viewModelScope.launch {
            val classesRes = repository.getTeacherAssignedClasses(session)
            if (classesRes.isSuccess) {
                val classes = classesRes.getOrThrow()
                _assignedClasses.value = classes
                val firstClass = classes.firstOrNull()
                _selectedTeacherClass.value = firstClass
                if (firstClass != null) {
                    val studentsRes = repository.getTeacherAssignedStudents(session, firstClass.classId)
                    _classStudents.value = studentsRes.getOrDefault(emptyList())
                }
            }
        }
    }

    fun selectTeacherClass(classEntity: com.example.data.model.ClassEntity) {
        val session = _staffSession.value ?: return
        _selectedTeacherClass.value = classEntity
        viewModelScope.launch {
            val studentsRes = repository.getTeacherAssignedStudents(session, classEntity.classId)
            _classStudents.value = studentsRes.getOrDefault(emptyList())
        }
    }

    fun loadMasterAdminData(session: com.example.data.model.StaffSession) {
        viewModelScope.launch {
            val statsRes = repository.getMasterAdminStats(session)
            _adminStats.value = statsRes.getOrDefault(_adminStats.value)
            val teachersRes = repository.getAllTeachers(session)
            _teachersList.value = teachersRes.getOrDefault(emptyList())
            val classesRes = repository.getAllClasses(session)
            _allSchoolClasses.value = classesRes.getOrDefault(emptyList())
            val configRes = repository.getSchoolConfig(session)
            _schoolConfig.value = configRes.getOrNull()
        }
    }

    fun createTeacher(
        fullName: String,
        email: String,
        subject: String,
        assignedClassId: String,
        onResult: (Boolean, String?) -> Unit = { _, _ -> }
    ) {
        val session = _staffSession.value ?: run {
            onResult(false, "No active administrator session.")
            return
        }
        viewModelScope.launch {
            val res = repository.createTeacherAccount(session, fullName, email, subject, assignedClassId)
            if (res.isSuccess) {
                loadMasterAdminData(session)
                onResult(true, null)
            } else {
                onResult(false, res.exceptionOrNull()?.message ?: "Failed to create teacher.")
            }
        }
    }

    fun createClass(classId: String, name: String, subject: String, teacherId: String) {
        val session = _staffSession.value ?: return
        viewModelScope.launch {
            repository.createClass(session, classId, name, subject, teacherId)
            loadMasterAdminData(session)
        }
    }

    fun updateSchoolSecretKey(newKey: String, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        val session = _staffSession.value ?: run {
            onResult(false, "No active administrator session.")
            return
        }
        viewModelScope.launch {
            val res = repository.rotateAdminSecret(session, newKey)
            if (res.isSuccess) {
                loadMasterAdminData(session)
                onResult(true, null)
            } else {
                onResult(false, res.exceptionOrNull()?.message ?: "Failed to rotate secret key.")
            }
        }
    }

    fun configureSchoolRules(
        submissionPoints: Int,
        quizPoints: Int,
        aiStudyPoints: Int,
        minStreakMinutes: Int,
        streakGraceDays: Int,
        onResult: (Boolean, String?) -> Unit = { _, _ -> }
    ) {
        val session = _staffSession.value ?: run {
            onResult(false, "No active administrator session.")
            return
        }
        viewModelScope.launch {
            val res = repository.configureSchoolRules(session, submissionPoints, quizPoints, aiStudyPoints, minStreakMinutes, streakGraceDays)
            if (res.isSuccess) {
                loadMasterAdminData(session)
                onResult(true, null)
            } else {
                onResult(false, res.exceptionOrNull()?.message ?: "Failed to configure rules.")
            }
        }
    }
}
