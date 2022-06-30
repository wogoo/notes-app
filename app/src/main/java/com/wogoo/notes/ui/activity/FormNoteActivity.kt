package com.wogoo.notes.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.lifecycleScope
import com.wogoo.notes.R
import com.wogoo.notes.database.AppDatabase
import com.wogoo.notes.databinding.ActivityFormNoteBinding
import com.wogoo.notes.extensions.tryLoadImage
import com.wogoo.notes.model.Note
import com.wogoo.notes.repository.NoteRepository
import com.wogoo.notes.ui.dialog.FormImageDialog
import com.wogoo.notes.webClient.NoteWebClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class FormNoteActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormNoteBinding.inflate(layoutInflater)
    }

    private var image: MutableStateFlow<String?> = MutableStateFlow(null)
    private val repository by lazy {
        NoteRepository(
            AppDatabase.instance(this).noteDao(),
            NoteWebClient()
        )
    }

    private var noteId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.activityFormNoteToolbar)
        imageConfig()
        tryLoadNoteId()
        lifecycleScope.launch{
            launch {
                tryFindNote()
            }
            launch {
                configLoadedImage()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.form_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.form_nota_menu_salvar -> {
                save()
            }
            R.id.form_nota_menu_remover -> {
                remove()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        val note = createNote()
        lifecycleScope.launch {
            repository.save(note)
            finish()
        }
    }

    private fun remove() {
        lifecycleScope.launch {
            noteId?.let { id ->
                repository.remove(id)
            }
            finish()
        }
    }

    private fun createNote(): Note {
        val title = binding.activityFormNoteTitle.text.toString()
        val desc = binding.activityFormNoteDesc.text.toString()
        return noteId?.let { id ->
            Note(
                id = id,
                title = title,
                description = desc,
                image = image.value
            )
        } ?: Note(
            title = title,
            description = desc,
            image = image.value
        )
    }



    private suspend fun configLoadedImage() {
        val noteImage = binding.activityFormNoteImage
        image.collect { imagemNova ->
            noteImage.visibility =
                if (imagemNova.isNullOrBlank())
                    GONE
                else {
                    noteImage.tryLoadImage(imagemNova)
                    VISIBLE
                }
        }
    }

    private suspend fun tryFindNote() {
        noteId?.let { id ->
            repository.findById(id)
                .filterNotNull()
                .collect { note ->
                    noteId = note.id
                    image.value = note.image
                    binding.activityFormNoteTitle.setText(note.title)
                    binding.activityFormNoteDesc.setText(note.description)
                }
        }
    }

    private fun tryLoadNoteId() {
        noteId = intent.getStringExtra(NOTE_ID)
    }

    private fun imageConfig() {
        binding.activityFormNoteAddImage.setOnClickListener {
            FormImageDialog(this)
                .show(image.value) { loadedImage ->
                    binding.activityFormNoteImage
                        .tryLoadImage(loadedImage)
                    image.value = loadedImage
                }
        }
    }
}