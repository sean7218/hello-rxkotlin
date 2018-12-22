package com.seanzhang.hellorxkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableError
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        example()
        hello()
        starwar()
    }

    fun starwar() {
        exampleOf("creating observables") {
            val mostPopular: Observable<String> = Observable.just(episodeV)
            val originalTrilogy = Observable.just(episodeIV, episodeV, episodeVI)
            val prequelTrilogy = Observable.just(listOf(episodeI, episodeII, episodeIII))
            val sequelTrilogy = Observable.fromIterable(listOf(episodeVII, episodeVIII, episodeIX))
            val stories = listOf(solo, rogueOne).toObservable()
        }
    }
    fun hello() {
        Observable.just("Hello, RxKotlin").subscribe{
            println(it)
        }
    }

    fun example() {
        val list = listOf("Alpha", "Beta", "Gamma", "Delta", "Epsilon")

        list.toObservable() // extension function for Iterables
                .filter { it.length >= 5 }
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = { println(it) },
                        onError =  { it.printStackTrace() },
                        onComplete = { println("Done!") }
                )
    }


}
