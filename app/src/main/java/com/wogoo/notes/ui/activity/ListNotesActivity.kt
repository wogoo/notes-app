package com.wogoo.notes.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wogoo.notes.R

class ListNotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_notes)
    }
}