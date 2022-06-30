package com.wogoo.notes.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.wogoo.notes.database.AppDatabase
import com.wogoo.notes.databinding.ActivityListNotesBinding
import com.wogoo.notes.extensions.goTo
import com.wogoo.notes.repository.NoteRepository
import com.wogoo.notes.ui.recyclerView.adapter.ListNotesAdapter
import com.wogoo.notes.webClient.NoteWebClient
import kotlinx.coroutines.launch

class ListNotesActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityListNotesBinding.inflate(layoutInflater)
    }
    private val adapter by lazy {
        ListNotesAdapter(this)
    }
    private val repository by lazy {
        NoteRepository(
            AppDatabase.instance(this).noteDao(),
            NoteWebClient()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configFab()
        configRecyclerView()
        configSwipeRefresh()
        lifecycleScope.launch {
            launch {
                sincroniza()
            }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                buscaNotas()
            }
        } }

        private suspend fun sincroniza() {
            repository.synchronize()
        }

        private fun configSwipeRefresh() {
            binding.activityListaNotasSwipe.setOnRefreshListener {
                lifecycleScope.launch {
                    sincroniza()
                    binding.activityListaNotasSwipe.isRefreshing = false
                }
            }
        }

        private fun configFab() {
            binding.activityListNotesFab.setOnClickListener {
                Intent(this, FormNoteActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }

        private fun configRecyclerView() {
            binding.activityListNotesRecyclerview.adapter = adapter
            adapter.whenClickItem = { note ->
                goTo(FormNoteActivity::class.java) {
                    putExtra(NOTE_ID, note.id)
                }
            }
        }

        private suspend fun buscaNotas() {
            repository.findAll()
                .collect { notasEncontradas ->
                    binding.activityListaNotasMensagemSemNotas.visibility =
                        if (notasEncontradas.isEmpty()) {
                            binding.activityListNotesRecyclerview.visibility = GONE
                            VISIBLE
                        } else {
                            binding.activityListNotesRecyclerview.visibility = VISIBLE
                            adapter.update(notasEncontradas)
                            GONE
                        }
                }
        }

    }

