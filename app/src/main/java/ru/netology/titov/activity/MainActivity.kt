package ru.netology.titov.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.titov.R
import ru.netology.titov.adapter.PostAdapter
import ru.netology.titov.adapter.PostEventListener
import ru.netology.titov.databinding.ActivityMainBinding
import ru.netology.titov.dto.Post
import ru.netology.titov.util.AndroidUtils
import ru.netology.titov.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private var isEditing: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        binding.group.visibility = View.GONE

        val adapter = PostAdapter(
            object : PostEventListener {
                override fun onEdit(post: Post) {
                    if (!isEditing) {
                        isEditing = true
                        viewModel.editContent(post)
                        binding.group.visibility = View.VISIBLE
                    }
                }

                override fun onRemove(post: Post) {
                    if (!isEditing) {
                    viewModel.removeById(post.id)
                }
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    viewModel.shareById(post.id)
                }
            }
        )

        binding.container.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.itemCount < posts.size
            adapter.submitList(posts) {
                if (newPost) binding.container.smoothScrollToPosition(0)
            }
        }

        viewModel.edited.observe(this) {
            with(binding.contentCheck) {
                text = it.content
            }
            binding.content.setText(it.content)

        }

        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.empty_post_error),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.saveContent()
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                binding.group.visibility = View.GONE
                isEditing = false // Clear the edit state
            }
        }

        binding.cancel.setOnClickListener {
            with(binding.content) {
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                binding.group.visibility = View.GONE
                viewModel.saveContent()
                isEditing = false // Clear the edit state
            }
        }
    }
}