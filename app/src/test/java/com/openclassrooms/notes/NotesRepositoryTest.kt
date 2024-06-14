package com.openclassrooms.notes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.openclassrooms.notes.model.Note
import com.openclassrooms.notes.repository.NotesRepository
import com.openclassrooms.notes.service.NotesApiService
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.concurrent.Executors

@RunWith(MockitoJUnitRunner::class)
class NotesRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: NotesApiService

    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        notesRepository = NotesRepository(apiService)
    }

    @Test
    fun testFetchNotes() {
        val notes = listOf(
            Note("Title1", "Description1"),
            Note("Title2", "Description2")
        )

        whenever(apiService.getAllNotes()).thenReturn(notes)

        val observer = mock<Observer<List<Note>>>()
        notesRepository.noteLiveData.observeForever(observer)

        Executors.newSingleThreadExecutor().execute {
            notesRepository.fetchNotes()
        }

        Thread.sleep(1000)  // Allow some time for the background task to complete

        Mockito.verify(observer).onChanged(notes)
        Assert.assertEquals(notes, notesRepository.noteLiveData.value)
    }

    @Test
    fun testUpdateNotes() {
        val notes = listOf(
            Note("Title1", "Description1"),
            Note("Title2", "Description2")
        )

        val observer = mock<Observer<List<Note>>>()
        notesRepository.noteLiveData.observeForever(observer)

        notesRepository.updateNotes(notes)

        Mockito.verify(observer).onChanged(notes)
        Assert.assertEquals(notes, notesRepository.noteLiveData.value)
    }
}
