package ie.wit.my_festival.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.my_festival.firebase.FirebaseDBManager
import ie.wit.my_festival.models.FestivalModel
import timber.log.Timber
import java.lang.Exception

class ListViewModel : ViewModel() {

    private val festivalsList = MutableLiveData<List<FestivalModel>>()

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    val observableFestivalsList: LiveData<List<FestivalModel>>
        get() = festivalsList

    init {
        load()
    }

    fun load() {
        try {

            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,festivalsList)
            Timber.i("Report Load Success : ${festivalsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }
}
