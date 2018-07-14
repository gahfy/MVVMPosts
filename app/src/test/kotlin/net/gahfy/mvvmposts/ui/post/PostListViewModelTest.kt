package net.gahfy.mvvmposts.ui.post

import android.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import net.gahfy.mvvmposts.model.Post
import net.gahfy.mvvmposts.model.PostDao
import net.gahfy.mvvmposts.network.PostApi
import net.gahfy.mvvmposts.utils.ApiUtils
import net.gahfy.mvvmposts.utils.POST_MOCK_PATH
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


class PostListViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testRetrofitPostApi = object : PostApi {
        override fun getPosts(): Observable<List<Post>> {
            return Observable.fromCallable { ApiUtils.getUrl<List<Post>>(POST_MOCK_PATH) }
        }
    }

    @Before
    fun setUpRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Scheduler.Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

    @Test
    fun loadPosts_success() {
        POST_MOCK_PATH = "posts.json"
        val postDao = PostDaoImpl()
        val viewModel = PostListViewModel(postDao, testRetrofitPostApi)
        assertEquals("Check that posts are inserted in database", 50, postDao.all.size)
        assertEquals("Check that adapter has correct number of rows", 50, viewModel.postListAdapter.itemCount)
    }

}

private class PostDaoImpl : PostDao {
    var posts = mutableListOf<Post>()

    override val all: List<Post>
        get() = posts

    override fun insertAll(vararg posts: Post) {
        this.posts.addAll(posts)
    }
}