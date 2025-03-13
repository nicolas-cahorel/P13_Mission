package com.openclassrooms.hexagonal.games.screen.addPostScreen

import androidx.annotation.StringRes
import com.openclassrooms.hexagonal.games.R

/**
 * A sealed class representing different events that can occur on a form.
 * These events correspond to user interactions with the form fields, such as changing the title, description, or photo.
 */
sealed class FormEvent {

    /**
     * Event triggered when the title of the form is changed.
     *
     * @property title The new title of the form.
     */
    data class TitleChanged(val title: String) : FormEvent()

    /**
     * Event triggered when the description of the form is changed.
     *
     * @property description The new description of the form.
     */
    data class DescriptionChanged(val description: String) : FormEvent()

    /**
     * Event triggered when the photo URL of the form is changed.
     *
     * @property photoUrl The new photo URL of the form.
     */
    data class PhotoChanged(val photoUrl: String) : FormEvent()

}

/**
 * A sealed class representing different errors that can occur on a form.
 *
 * Each error holds a resource ID for the corresponding error message string.
 * These errors are triggered when user input fails validation.
 */
sealed class FormError(@StringRes val messageRes: Int) {

    /**
     * Error indicating an issue with the form title.
     *
     * The actual error message can be retrieved using the provided resource ID (`R.string.error_title`).
     */
    data object TitleError : FormError(R.string.error_title)

    /**
     * Error indicating an issue with the form description.
     *
     * The actual error message can be retrieved using the provided resource ID (`R.string.error_description`).
     */
    data object DescriptionError : FormError(R.string.error_description)


    /**
     * Error indicating an issue with the form photo.
     *
     * The actual error message can be retrieved using the provided resource ID (`R.string.error_photo`).
     */
    data object PhotoError : FormError(R.string.error_photo)

}