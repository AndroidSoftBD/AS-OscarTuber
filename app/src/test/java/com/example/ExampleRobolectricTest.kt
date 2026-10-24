package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.OscarRepository
import kotlinx.coroutines.runBlocking
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
    fun `read string from context verifies OscarTuber name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("OscarTuber", appName)
    }

    @Test
    fun `repository provides seeded videos and handles offline toggle`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = OscarRepository(context)

        // Ensure database seed is initialized
        repository.ensureInitialized()

        // Seeded list verification
        val initialVideo = repository.getVideoById("v1")
        assertNotNull("Sample video v1 should exist", initialVideo)
        assertTrue("Sample video v1 should be saved offline", initialVideo?.isOfflineSaved == true)

        // Toggle offline status synchronously
        repository.setOfflineSaveStatus("v1", false)
        val updatedVideo = repository.getVideoById("v1")
        assertEquals(false, updatedVideo?.isOfflineSaved)
    }
}
