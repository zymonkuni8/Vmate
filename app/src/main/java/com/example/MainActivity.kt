package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AuthScreenState
import com.example.data.model.UserRole
import com.example.ui.components.ClayBottomBar
import com.example.ui.components.DailyStreakCenterDialog
import com.example.ui.components.DayProgressDialog
import com.example.ui.components.InteractiveQuizDialog
import com.example.ui.components.LearningLibraryDialog
import com.example.ui.components.NotificationCenterDialog
import com.example.ui.components.NotificationPermissionPromptDialog
import com.example.ui.components.OfflineStatusBanner
import com.example.ui.components.OrganicCurveBackdrop
import com.example.ui.components.RapidQuestionsDialog
import com.example.ui.components.RoleSwitcherDialog
import com.example.ui.components.SecretAdminGatewayDialog
import com.example.ui.components.StudyCapsuleDialog
import com.example.ui.components.SubmitWorkDialog
import com.example.ui.components.TopSchoolHeader
import com.example.ui.screens.academics.AcademicsScreen
import com.example.ui.screens.admin.MasterAdminDashboardScreen
import com.example.ui.screens.ai.VaishuAiScreen
import com.example.ui.screens.auth.GoogleLoginScreen
import com.example.ui.screens.auth.RoleSelectionScreen
import com.example.ui.screens.auth.SplashLoadingScreen
import com.example.ui.screens.auth.StudentOnboardingScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.leaderboard.LeaderboardScreen
import com.example.ui.screens.other.StaffLearningHubScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.teacher.TeacherDashboardScreen
import com.example.ui.theme.VaishAppTheme
import com.example.ui.viewmodel.AppNavTab
import com.example.ui.viewmodel.VaishuViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: VaishuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
            val authState by viewModel.authScreenState.collectAsStateWithLifecycle()
            val activeRole by viewModel.selectedRole.collectAsStateWithLifecycle()
            val studentProfile by viewModel.studentProfile.collectAsStateWithLifecycle()
            val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
            val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
            val customApiKey by viewModel.customApiKey.collectAsStateWithLifecycle()

            val selectedDayProgress by viewModel.selectedDayProgress.collectAsStateWithLifecycle()
            val showDayProgressDialog by viewModel.showDayProgressDialog.collectAsStateWithLifecycle()
            val showSubmitWorkDialog by viewModel.showSubmitWorkDialog.collectAsStateWithLifecycle()
            val showQuizDialog by viewModel.showQuizDialog.collectAsStateWithLifecycle()
            val quizQuestions by viewModel.quizQuestions.collectAsStateWithLifecycle()
            val quizBannerTitle by viewModel.quizBannerTitle.collectAsStateWithLifecycle()
            val currentLearningMode by viewModel.currentLearningMode.collectAsStateWithLifecycle()
            val selectedCapsule by viewModel.selectedCapsule.collectAsStateWithLifecycle()

            val subjects by viewModel.subjects.collectAsStateWithLifecycle()
            val assignments by viewModel.assignments.collectAsStateWithLifecycle()
            val capsules by viewModel.capsules.collectAsStateWithLifecycle()
            val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
            val chatInput by viewModel.chatInput.collectAsStateWithLifecycle()
            val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()

            val realLeaderboard by viewModel.realLeaderboard.collectAsStateWithLifecycle()
            val streakState by viewModel.streakState.collectAsStateWithLifecycle()
            val showStreakCenterDialog by viewModel.showStreakCenterDialog.collectAsStateWithLifecycle()
            val showRapidQuestionsDialog by viewModel.showRapidQuestionsDialog.collectAsStateWithLifecycle()

            val staffSession by viewModel.staffSession.collectAsStateWithLifecycle()
            val showStaffGatewayDialog by viewModel.showStaffGatewayDialog.collectAsStateWithLifecycle()
            val assignedClasses by viewModel.assignedClasses.collectAsStateWithLifecycle()
            val selectedTeacherClass by viewModel.selectedTeacherClass.collectAsStateWithLifecycle()
            val classStudents by viewModel.classStudents.collectAsStateWithLifecycle()
            val adminStats by viewModel.adminStats.collectAsStateWithLifecycle()
            val teachersList by viewModel.teachersList.collectAsStateWithLifecycle()
            val allSchoolClasses by viewModel.allSchoolClasses.collectAsStateWithLifecycle()
            val schoolConfig by viewModel.schoolConfig.collectAsStateWithLifecycle()

            var showRoleDialog by remember { mutableStateOf(false) }

            VaishAppTheme(darkTheme = isDarkMode) {
                when (authState) {
                    AuthScreenState.SPLASH_LOADING -> {
                        SplashLoadingScreen(
                            isDarkMode = isDarkMode,
                            onFinish = viewModel::onSplashFinished
                        )
                    }

                    AuthScreenState.GOOGLE_LOGIN -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            OrganicCurveBackdrop(isDarkMode = isDarkMode)
                            GoogleLoginScreen(
                                isDarkMode = isDarkMode,
                                onContinueWithGoogle = viewModel::onGoogleSignInClicked
                            )
                        }
                    }

                    AuthScreenState.ROLE_SELECTION -> {
                        BackHandler {
                            viewModel.signOut()
                        }
                        Box(modifier = Modifier.fillMaxSize()) {
                            OrganicCurveBackdrop(isDarkMode = isDarkMode)
                            RoleSelectionScreen(
                                isDarkMode = isDarkMode,
                                onRoleSelected = viewModel::onRoleSelected
                            )
                        }
                    }

                    AuthScreenState.STUDENT_ONBOARDING -> {
                        BackHandler {
                            viewModel.onGoogleSignInClicked()
                        }
                        Box(modifier = Modifier.fillMaxSize()) {
                            OrganicCurveBackdrop(isDarkMode = isDarkMode)
                            StudentOnboardingScreen(
                                isDarkMode = isDarkMode,
                                initialProfile = studentProfile,
                                onComplete = viewModel::onCompleteStudentOnboarding
                            )
                        }
                    }

                    AuthScreenState.MAIN_APP -> {
                        // Back handling: pop to Home screen if on another tab
                        if (currentTab != AppNavTab.HOME) {
                            BackHandler {
                                viewModel.selectTab(AppNavTab.HOME)
                            }
                        }

                        Box(modifier = Modifier.fillMaxSize()) {
                            // Signature curved organic backdrop from design reference
                            OrganicCurveBackdrop(isDarkMode = isDarkMode)

                            BoxWithConstraints(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                // Responsive constraint for tablets/foldables:
                                val contentModifier = if (maxWidth > 640.dp) {
                                    Modifier
                                        .widthIn(max = 640.dp)
                                        .fillMaxSize()
                                } else {
                                    Modifier.fillMaxSize()
                                }

                                Scaffold(
                                    modifier = contentModifier,
                                    containerColor = Color.Transparent,
                                    topBar = {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .windowInsetsPadding(WindowInsets.statusBars)
                                        ) {
                                            TopSchoolHeader(
                                                isDarkMode = isDarkMode,
                                                activeRole = activeRole,
                                                searchQuery = searchQuery,
                                                onSearchChange = viewModel::onSearchQueryChange,
                                                onToggleDarkMode = viewModel::toggleDarkMode,
                                                onRoleClick = { showRoleDialog = true }
                                            )
                                        }
                                    },
                                    bottomBar = {
                                        if (activeRole == UserRole.STUDENT) {
                                            ClayBottomBar(
                                                currentTab = currentTab,
                                                isDarkMode = isDarkMode,
                                                onTabSelected = viewModel::selectTab,
                                                onOpenStreakCenter = viewModel::openStreakCenterDialog
                                            )
                                        }
                                    }
                                ) { innerPadding ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .windowInsetsPadding(WindowInsets(top = innerPadding.calculateTopPadding()))
                                    ) {
                                        when (activeRole) {
                                            UserRole.TEACHER -> {
                                                val session = staffSession
                                                if (session != null && session.role == UserRole.TEACHER) {
                                                    TeacherDashboardScreen(
                                                        session = session,
                                                        assignedClasses = assignedClasses,
                                                        selectedClass = selectedTeacherClass,
                                                        students = classStudents,
                                                        isDarkMode = isDarkMode,
                                                        onSelectClass = viewModel::selectTeacherClass,
                                                        onAskAiGradeHelper = { studentName ->
                                                            viewModel.selectRole(UserRole.STUDENT)
                                                            viewModel.selectTab(AppNavTab.ASK_VAISHU)
                                                            viewModel.sendChatMessage("Provide learning feedback and study recommendations for student $studentName.")
                                                        },
                                                        onSignOutStaff = viewModel::logoutStaffSession
                                                    )
                                                } else {
                                                    StaffLearningHubScreen(
                                                        isDarkMode = isDarkMode,
                                                        onNavigateToAi = { customPrompt ->
                                                            viewModel.selectRole(UserRole.STUDENT)
                                                            viewModel.selectTab(AppNavTab.ASK_VAISHU)
                                                            if (customPrompt != null) viewModel.sendChatMessage(customPrompt)
                                                        },
                                                        onOpenStaffGateway = viewModel::openStaffGatewayDialog
                                                    )
                                                }
                                            }

                                            UserRole.PRINCIPAL -> {
                                                val session = staffSession
                                                if (session != null && session.role == UserRole.PRINCIPAL) {
                                                    MasterAdminDashboardScreen(
                                                        session = session,
                                                        stats = adminStats,
                                                        teachers = teachersList,
                                                        classes = allSchoolClasses,
                                                        schoolConfig = schoolConfig,
                                                        isDarkMode = isDarkMode,
                                                        onCreateTeacher = viewModel::createTeacher,
                                                        onRotateSecretKey = viewModel::updateSchoolSecretKey,
                                                        onSaveRules = viewModel::configureSchoolRules,
                                                        onSignOutStaff = viewModel::logoutStaffSession
                                                    )
                                                } else {
                                                    StaffLearningHubScreen(
                                                        isDarkMode = isDarkMode,
                                                        onNavigateToAi = { customPrompt ->
                                                            viewModel.selectRole(UserRole.STUDENT)
                                                            viewModel.selectTab(AppNavTab.ASK_VAISHU)
                                                            if (customPrompt != null) viewModel.sendChatMessage(customPrompt)
                                                        },
                                                        onOpenStaffGateway = viewModel::openStaffGatewayDialog
                                                    )
                                                }
                                            }

                                            UserRole.OTHER -> {
                                                StaffLearningHubScreen(
                                                    isDarkMode = isDarkMode,
                                                    onNavigateToAi = { customPrompt ->
                                                        viewModel.selectRole(UserRole.STUDENT)
                                                        viewModel.selectTab(AppNavTab.ASK_VAISHU)
                                                        if (customPrompt != null) viewModel.sendChatMessage(customPrompt)
                                                    },
                                                    onOpenStaffGateway = viewModel::openStaffGatewayDialog
                                                )
                                            }

                                            UserRole.STUDENT -> {
                                                when (currentTab) {
                                                    AppNavTab.HOME -> HomeScreen(
                                                        isDarkMode = isDarkMode,
                                                        activeRole = activeRole,
                                                        studentProfile = studentProfile,
                                                        calendarDays = viewModel.calendarDays,
                                                        subjects = subjects,
                                                        assignments = assignments,
                                                        capsules = capsules,
                                                        searchQuery = searchQuery,
                                                        onDaySelected = viewModel::selectDayProgress,
                                                        onNavigateToAi = { customPrompt ->
                                                            viewModel.selectTab(AppNavTab.ASK_VAISHU)
                                                            if (customPrompt != null) {
                                                                viewModel.sendChatMessage(customPrompt)
                                                            }
                                                        },
                                                        onNavigateToLearn = {
                                                            viewModel.selectTab(AppNavTab.LEARN)
                                                        },
                                                        onNavigateToLeaderboard = {
                                                            viewModel.selectTab(AppNavTab.LEADERBOARD)
                                                        },
                                                        onOpenSubmitWork = viewModel::openSubmitWorkDialog,
                                                        onOpenQuiz = viewModel::openQuizDialog,
                                                        onOpenStreakCenter = viewModel::openStreakCenterDialog,
                                                        onOpenRapidQuestions = viewModel::openRapidQuestionsDialog,
                                                        onSelectCapsule = viewModel::selectCapsule
                                                    )

                                                    AppNavTab.LEARN -> AcademicsScreen(
                                                        isDarkMode = isDarkMode,
                                                        activeRole = activeRole,
                                                        subjects = subjects,
                                                        assignments = assignments,
                                                        onSubmitAssignment = viewModel::submitAssignment,
                                                        onAskAiForTopic = { topicPrompt ->
                                                            viewModel.selectTab(AppNavTab.ASK_VAISHU)
                                                            viewModel.sendChatMessage(topicPrompt)
                                                        }
                                                    )

                                                    AppNavTab.ASK_VAISHU -> VaishuAiScreen(
                                                        isDarkMode = isDarkMode,
                                                        activeRole = activeRole,
                                                        chatMessages = chatMessages,
                                                        chatInput = chatInput,
                                                        isAiThinking = isAiThinking,
                                                        selectedLearningMode = currentLearningMode,
                                                        onSelectLearningMode = viewModel::selectLearningMode,
                                                        onChatInputChange = viewModel::onChatInputChange,
                                                        onSendMessage = { prompt ->
                                                            viewModel.sendChatMessage(prompt)
                                                        },
                                                        onClearChat = viewModel::clearChat
                                                    )

                                                    AppNavTab.LEADERBOARD -> LeaderboardScreen(
                                                        leaderboard = realLeaderboard,
                                                        isDarkMode = isDarkMode
                                                    )

                                                    AppNavTab.PROFILE -> ProfileScreen(
                                                        isDarkMode = isDarkMode,
                                                        activeRole = activeRole,
                                                        studentProfile = studentProfile,
                                                        customApiKey = customApiKey,
                                                        onSaveCustomApiKey = viewModel::saveCustomApiKey,
                                                        onResetCustomApiKey = viewModel::resetCustomApiKey,
                                                        onToggleDarkMode = viewModel::toggleDarkMode,
                                                        onChangeRole = { showRoleDialog = true },
                                                        onSignOut = viewModel::signOut
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Modals & Dialogs
                            if (showStreakCenterDialog) {
                                DailyStreakCenterDialog(
                                    streakState = streakState,
                                    isDarkMode = isDarkMode,
                                    onStartDailyChallenge = {
                                        viewModel.dismissStreakCenterDialog()
                                        viewModel.openRapidQuestionsDialog()
                                    },
                                    onDismiss = viewModel::dismissStreakCenterDialog
                                )
                            }

                            if (showRapidQuestionsDialog) {
                                RapidQuestionsDialog(
                                    isDarkMode = isDarkMode,
                                    onGenerateQuestions = viewModel::generateRapidQuestions,
                                    onCompleteRapidQuiz = viewModel::completeRapidQuiz,
                                    onDismiss = viewModel::dismissRapidQuestionsDialog
                                )
                            }

                            if (showStaffGatewayDialog) {
                                SecretAdminGatewayDialog(
                                    isDarkMode = isDarkMode,
                                    onVerifySecretKey = viewModel::verifySecretAdminKey,
                                    onTeacherLogin = viewModel::loginTeacher,
                                    onPrincipalLogin = viewModel::loginPrincipal,
                                    onDismiss = viewModel::dismissStaffGatewayDialog
                                )
                            }

                            if (showDayProgressDialog) {
                                DayProgressDialog(
                                    dayProgress = selectedDayProgress,
                                    isDarkMode = isDarkMode,
                                    onAskAiForReview = { topicPrompt ->
                                        viewModel.dismissDayProgressDialog()
                                        viewModel.selectTab(AppNavTab.ASK_VAISHU)
                                        viewModel.sendChatMessage(topicPrompt)
                                    },
                                    onDismiss = viewModel::dismissDayProgressDialog
                                )
                            }

                            if (showSubmitWorkDialog) {
                                SubmitWorkDialog(
                                    isDarkMode = isDarkMode,
                                    onSubmitWithAi = viewModel::submitWorkWithAi,
                                    onDismiss = viewModel::dismissSubmitWorkDialog
                                )
                            }

                            if (showQuizDialog) {
                                InteractiveQuizDialog(
                                    questions = quizQuestions,
                                    isDarkMode = isDarkMode,
                                    bannerTitle = quizBannerTitle,
                                    onCompleteQuiz = viewModel::completeQuiz,
                                    onDismiss = viewModel::dismissQuizDialog
                                )
                            }

                            if (showRoleDialog) {
                                RoleSwitcherDialog(
                                    currentRole = activeRole,
                                    activeStaffSession = staffSession,
                                    isDarkMode = isDarkMode,
                                    onRoleSelected = viewModel::selectRole,
                                    onRequireStaffAuth = { requiredRole ->
                                        showRoleDialog = false
                                        viewModel.openStaffGatewayDialog()
                                    },
                                    onDismiss = { showRoleDialog = false }
                                )
                            }

                            selectedCapsule?.let { capsule ->
                                StudyCapsuleDialog(
                                    capsule = capsule,
                                    isDarkMode = isDarkMode,
                                    onAskAiMore = { topicPrompt ->
                                        viewModel.selectCapsule(null)
                                        viewModel.selectTab(AppNavTab.ASK_VAISHU)
                                        viewModel.sendChatMessage(topicPrompt)
                                    },
                                    onDismiss = { viewModel.selectCapsule(null) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
