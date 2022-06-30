package com.wogoo.notes.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.wogoo.notes.databinding.ImageFormBinding
import com.wogoo.notes.extensions.tryLoadImage

class FormImageDialog(private val context: Context) {
    fun show(
        urlStandard: String? = null,
        whenImageLoaded: (image: String) -> Unit
    ) {
        ImageFormBinding.inflate(LayoutInflater.from(context)).apply {

            urlStandard?.let {
                formImageImageview.tryLoadImage(it)
                formImageUrl.setText(it)
            }

            formImageButtonLoad.setOnClickListener {
                val url = formImageUrl.text.toString()
                whenImageLoaded(url)
            }

            AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton("Confirmar") { _, _ ->
                    val url = formImageUrl.text.toString()
                    whenImageLoaded(url)
                }
                .setNegativeButton("Cancelar") { _, _ ->

                }
                .show()
        }
    }
}



