package com.example

import com.example.data.local.SampleData
import com.example.data.model.CandidateProfile
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testCandidateProfileGrounded() {
        val candidate = CandidateProfile()
        assertEquals("Michel DONGMO", candidate.name)
        assertEquals("michel.dongmoza@gmail.com", candidate.email)
        assertTrue(candidate.location.contains("Ottawa"))
        assertTrue(candidate.verifiedSkills.contains("Python"))
        assertTrue(candidate.verifiedSkills.contains("PyTorch"))
        assertTrue(candidate.verifiedSkills.contains("YOLOv8"))
        assertEquals(2, candidate.projects.size)
        assertEquals("PRJ-SIACP", candidate.projects[0].id)
    }

    @Test
    fun testMandatoryGatesCompliance() {
        val jobs = SampleData.initialJobs
        assertTrue(jobs.isNotEmpty())
        jobs.forEach { job ->
            assertTrue("Gate A (Remote) failed for ${job.title}", job.gateA_Remote)
            assertTrue("Gate B (Salary > $80K) failed for ${job.title}", job.salaryMax > 80000.0)
            assertTrue("Gate C (Canada) failed for ${job.title}", job.gateC_Canada)
            assertTrue("Gate D (Relevance) failed for ${job.title}", job.gateD_Relevance)
            assertTrue("Job should qualify", job.isQualifying)
        }
    }

    @Test
    fun testTarget5ApplicationsSubmitted() {
        val apps = SampleData.initialApplications
        assertEquals(5, apps.size)
        apps.forEach { app ->
            assertEquals("SUBMITTED", app.status)
            assertTrue(app.confirmationId.isNotBlank())
            assertTrue(app.cvVersion.endsWith(".pdf"))
            assertTrue(app.submissionEvidence.isNotBlank())
        }
    }
}
