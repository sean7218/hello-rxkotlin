package com.seanzhang.hellorxkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.operators.observable.ObservableError
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        example()
//        hello()
//        starwar()
//        eps()
//        never()
        create()
    }
    fun empty() {
        exampleOf("emptyObs") {
            val observable = Observable.empty<Unit>()

            observable.subscribeBy (
                onNext = { println(it) },
                onComplete = { println("Completed") }
            )
        }
    }

    fun never() {
        exampleOf("never") {
            val observable = Observable.never<Any>()


            observable.subscribeBy (
                    onNext = { println(it) },
                    onComplete = { println("Completed") }
            )
        }

        exampleOf("dispose") {
            val mostPopular: Observable<String> = Observable.just(episodeV, episodeIV, episodeVI)

            val subscription = mostPopular.subscribe {
                println(it)
            }

            // until a stop event like error event occur
            // to avoid leaking memory
            subscription.dispose()
        }

        exampleOf("compositeDesposable") {
            val subscriptions = CompositeDisposable()

            subscriptions.add(listOf(episodeV, episodeIV, episodeVI).
                                toObservable()
                                .subscribe{ println(it) }
            )
        }
    }

    fun eps() {
        val originalTrilogy = Observable.just(episodeIV, episodeV, episodeVI)

        originalTrilogy.subscribeBy( onError = { println(it)}, onComplete = { println("completed")}, onNext = { println(it)})
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

    fun create() {

        exampleOf("create") {
            val subscriptions = CompositeDisposable()
            val droids = Observable.create<String> { emitter ->
                emitter.onNext("R2-D2")
                //emitter.onError(Droid.OU812())
                emitter.onNext("C-3PO")
                emitter.onNext("K-2SO")

            }
            val observer = droids.subscribeBy (
                    onNext = { println(it) },
                    onComplete = { println( "completed") },
                    onError = { println("Error, $it") }
            )

            subscriptions.add(observer)
        }

        exampleOf("single") {
            val subscriptions = CompositeDisposable()

            fun loadText(filename: String): Single<String> {
                return Single.create create@{ emitter ->
                    val file = File(baseContext.filesDir,filename)
                    val afile = applicationContext.filesDir
                    println(afile)

                    if (!file.exists()) {
                        emitter.onError(FileReadError.FileNotFound())
                        return@create
                    }

                    val contents = file.readText(UTF_8)
                    emitter.onSuccess(contents)
                }
            }

            val observer = loadText("ANewHope.txt")
                    .subscribe( { println(it)}, {println("Error, $it")})

            subscriptions.add(observer)
        }

        exampleOf("PublishSubject") {

            val quotes = PublishSubject.create<String>()

            quotes.onNext(itsNotMyFault)

            val subscriptionOne = quotes.subscribeBy(
                    onNext = { printWithLabel("1)", it) },
                    onComplete = { printWithLabel("1)", "Complete") }
            )

            quotes.onNext(doOrDoNot)

            val subscriptionTwo =  quotes.subscribeBy(
                    onNext = { printWithLabel("2)", it) },
                    onComplete = { printWithLabel("2)", "Complete") }
            )

            quotes.onNext(lackOfFaith)

            subscriptionOne.dispose()

            quotes.onNext(eyesCanDeceive)

            quotes.onComplete()

            val subscriptionThree = quotes.subscribeBy(
                    onNext = { printWithLabel("3)", it) },
                    onComplete = { printWithLabel("3)", "Complete") }
            )

            quotes.onNext(stayOnTarget)

            subscriptionTwo.dispose()
            subscriptionThree.dispose()
        }

        exampleOf("skipWhile") {
            val subscriptions = CompositeDisposable()

            subscriptions.add(
                    Observable.fromIterable(tomatometerRatings)
                            .skipWhile { movie ->
                                movie.rating < 90
                            }
                            .subscribe {
                                println(it)
                            })
        }

        exampleOf("skipUntil") {
            val subscriptions =  CompositeDisposable()

            val subject = PublishSubject.create<String>()
            val trigger = PublishSubject.create<Unit>()

            subscriptions.add(
                    subject.skipUntil(trigger)
                            .subscribe{
                                println(it)
                            })

            subject.onNext(repisodeI.title)
            subject.onNext(repisodeII.title)
            subject.onNext(repisodeIII.title)

            trigger.onNext(Unit)

            subject.onNext(repisodeIV.title)
        }
    }


}
