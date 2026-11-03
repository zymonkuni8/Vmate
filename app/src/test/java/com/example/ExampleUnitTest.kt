package com.example

import com.example.data.local.Converters
import com.example.data.model.AssignmentStatus
import com.example.data.model.UserRole
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testUserRolesDefined() {
    assertEquals(4, UserRole.values().size)
    assertEquals("Student", UserRole.STUDENT.displayName)
    assertEquals("Other (Staff & Admin)", UserRole.OTHER.displayName)
    assertEquals("Teacher", UserRole.TEACHER.displayName)
    assertEquals("Principal / Admin", UserRole.PRINCIPAL.displayName)
  }

  @Test
  fun testAssignmentStatusConverters() {
    val converters = Converters()
    val stringStatus = converters.fromAssignmentStatus(AssignmentStatus.SUBMITTED)
    assertEquals("SUBMITTED", stringStatus)

    val parsedStatus = converters.toAssignmentStatus(stringStatus)
    assertEquals(AssignmentStatus.SUBMITTED, parsedStatus)
  }
}

