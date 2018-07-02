package net.gahfy.mvvmposts.utils

import android.arch.persistence.room.Room
import android.content.Context
import io.reactivex.schedulers.Schedulers
import net.gahfy.mvvmposts.model.database.AppDatabase
import net.gahfy.mvvmposts.network.PostApi
import net.gahfy.mvvmposts.ui.post.PostListViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

fun postsModule(applicationContext: Context): Module = org.koin.dsl.module.applicationContext {
    viewModel { PostListViewModel(get("dao"), get("network")) }
    bean("dao") {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "posts").build().postDao()
    }
    bean("network") {
        (get("retrofit") as Retrofit).create(PostApi::class.java) as PostApi
    }
    bean("retrofit") {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

}
