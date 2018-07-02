package net.gahfy.mvvmposts

import android.app.Application
import net.gahfy.mvvmposts.utils.postsModule
import org.koin.android.ext.android.startKoin

class PostsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(postsModule(this)))
    }
}