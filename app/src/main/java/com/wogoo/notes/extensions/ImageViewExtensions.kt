package com.wogoo.notes.extensions

import android.widget.ImageView
import coil.load
import com.wogoo.notes.R

fun ImageView.tryLoadImage(
    url: String? = null,
    fallback: Int = R.drawable.imagem_padrao
) {
    load(url) {
        placeholder(R.drawable.placeholder)
        error(R.drawable.erro)
        fallback(fallback)
    }
}