package br.com.spaceinformatica.spacevendas.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ItemMenu(

    val id: Int,
    @DrawableRes val imageID: Int,
    @StringRes val label: Int,

    )
