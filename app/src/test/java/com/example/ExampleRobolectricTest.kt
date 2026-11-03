package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.LearningMode
import com.example.data.model.StudentProfile
import com.example.data.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("VAISHU", appName)
  }

  @Test
  fun `verify all 6 learning modes are properly configured`() {
    val modes = LearningMode.values()
    assertEquals(6, modes.size)
    assertNotNull(LearningMode.valueOf("LEARN"))
    assertNotNull(LearningMode.valueOf("TUTORIAL"))
    assertNotNull(LearningMode.valueOf("PRACTICE"))
    assertNotNull(LearningMode.valueOf("TEST"))
    assertNotNull(LearningMode.valueOf("EXPLAIN"))
    assertNotNull(LearningMode.valueOf("HINT"))

    assertTrue(LearningMode.HINT.promptPrefix.contains("hint", ignoreCase = true))
    assertTrue(LearningMode.TUTORIAL.promptPrefix.contains("step-by-step", ignoreCase = true))
  }

  @Test
  fun `verify student profile initial values and roles`() {
    val profile = StudentProfile()
    assertEquals(7, profile.streakDays)
    assertEquals(1450, profile.points)
    assertEquals(UserRole.STUDENT.displayName, "Student")
    assertEquals(UserRole.OTHER.displayName, "Other (Staff & Admin)")
  }
}
