package com.openclassrooms.hexagonal.games.screen.ad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.hexagonal.games.data.repository.PostRepository
import com.openclassrooms.hexagonal.games.domain.model.Post
import com.openclassrooms.hexagonal.games.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * This ViewModel manages data and interactions related to adding new posts in the AddScreen.
 * It utilizes dependency injection to retrieve a PostRepository instance for interacting with post data.
 */
@HiltViewModel
class AddViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {
  
  private var _post = MutableStateFlow(
    Post(
      id = UUID.randomUUID().toString(),
      title = "",
      description = "",
      photoUrl = null,
      timestamp = System.currentTimeMillis(),
      author = null
    )
  )
  
  val post: StateFlow<Post>
    get() = _post
  
  private var _error = MutableSharedFlow<FormError?>()
  val error: SharedFlow<FormError?>
    get() = _error
  
  fun onAction(formEvent: FormEvent) {
    when (formEvent) {
      is FormEvent.DescriptionChanged -> {
        _post.value = _post.value.copy(
          description = formEvent.description
        )
      }
      
      is FormEvent.TitleChanged -> {
        _post.value = _post.value.copy(
          title = formEvent.title
        )
      }
    }
  }
  
  fun addPost(): Boolean {
    val isPostValid = verifyPost()
    
    if (isPostValid) {
      //TODO : retrieve the current user
      postRepository.addPost(
        _post.value.copy(
          author = User("1", "Gerry", "Ariella")
        )
      )
    }
    
    return isPostValid
  }
  
  private fun verifyPost(): Boolean {
    return if (_post.value.title.isEmpty()) {
      viewModelScope.launch {
        _error.emit(FormError.TitleError)
      }
      
      false
    } else {
      true
    }
  }
  
}
