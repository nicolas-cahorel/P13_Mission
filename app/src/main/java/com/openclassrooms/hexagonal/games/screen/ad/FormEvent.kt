package com.openclassrooms.hexagonal.games.screen.ad

import com.openclassrooms.hexagonal.games.R

sealed class FormEvent {
  data class TitleChanged(val title: String) : FormEvent()
  
  data class DescriptionChanged(val description: String) : FormEvent()
}

sealed class FormError(val messageRes: Int) {
  data object TitleError : FormError(R.string.error_title)
}
