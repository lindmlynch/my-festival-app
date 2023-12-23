package ie.wit.my_festival.main

import android.app.Application
import ie.wit.my_festival.firebase.FirebaseDBManager
import ie.wit.my_festival.models.FestivalStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class FestivalApp : Application() {

    lateinit var festivals: FestivalStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        festivals = FirebaseDBManager
        i("Festival started")

    }
}