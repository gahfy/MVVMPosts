package net.gahfy.mvvmposts.utils

import io.reactivex.schedulers.Schedulers
import net.gahfy.mvvmposts.network.PostApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()

val retrofitPostApi = retrofit.create(PostApi::class.java)