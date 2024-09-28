package ru.netology.titov.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.titov.dto.Post
import ru.netology.titov.repository.PostRepositoryInMemory
import ru.netology.titov.repository.PostRepository

val emptyPost = Post(
    0,
    "",
    "",
    "",
    "",
    false,
    0,
    0
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemory()
    val data = repository.get()

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

    val edited = MutableLiveData(emptyPost)

    fun saveContent() {
        edited.value?.let {
            repository.save(it)
            edited.value = emptyPost
        }
    }

    fun editContent(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val trimmed = content.trim()
            if (trimmed == it.content) {
                return
            }
            edited.value = it.copy(content = trimmed)
        }
    }
}