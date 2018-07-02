package net.gahfy.mvvmposts.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import net.gahfy.mvvmposts.model.PostDao
import net.gahfy.mvvmposts.network.PostApi
import net.gahfy.mvvmposts.ui.post.PostListViewModel

class ViewModelFactory(private val postDao: PostDao, private val postApi: PostApi): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostListViewModel(postDao, postApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}