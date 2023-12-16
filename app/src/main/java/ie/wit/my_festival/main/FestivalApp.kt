package ie.wit.my_festival.main

import android.app.Application
import ie.wit.my_festival.models.FestivalMemStore
import ie.wit.my_festival.models.FestivalStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class FestivalApp : Application() {

    lateinit var festivals: FestivalStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        festivals = FestivalMemStore()
        i("Festival started")

        // festivals.add(FestivalModel("One", "About one..."))
        // festivals.add(FestivalModel("Two", "About two..."))
        // festivals.add(FestivalModel("Three", "About three..."))
    }
}