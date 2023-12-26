package ie.wit.my_festival.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.my_festival.firebase.FirebaseDBManager
import ie.wit.my_festival.models.FestivalModel
import timber.log.Timber

class FestivalDetailViewModel : ViewModel() {
    private val festival = MutableLiveData<FestivalModel>()

    val observableFestival: LiveData<FestivalModel>
        get() = festival

    fun getFestival(userid:String, id: String) {
        try {
            FirebaseDBManager.findById(userid, id, festival)
            Timber.i("Detail getFestival() Success : ${
                festival.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getFestival() Error : $e.message")
        }
    }

    fun updateFestival(userid:String, id: String,festival: FestivalModel) {
        try {

            FirebaseDBManager.update(userid, id, festival)
            Timber.i("Detail update() Success : $festival")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}