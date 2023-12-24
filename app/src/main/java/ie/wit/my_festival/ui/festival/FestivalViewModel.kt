package ie.wit.my_festival.ui.festival

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ie.wit.my_festival.firebase.FirebaseDBManager
import ie.wit.my_festival.firebase.FirebaseImageManager
import ie.wit.my_festival.models.FestivalModel

class FestivalViewModel : ViewModel() {

    private val _festival = MutableLiveData<FestivalModel>()
    val festival: LiveData<FestivalModel> = _festival

    private val _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser: LiveData<FirebaseUser> = _currentUser

    init {
        _currentUser.value = FirebaseAuth.getInstance().currentUser
    }

    fun setFestival(festivalModel: FestivalModel) {
        _festival.value = festivalModel
    }

    fun createFestival(festival: FestivalModel) {
        val user = currentUser.value
        if (user != null) {
            FirebaseDBManager.create(MutableLiveData(user), festival)
        }
    }

    fun updateFestival(festival: FestivalModel) {
        val user = currentUser.value
        if (user != null) {
            FirebaseDBManager.update(user.uid, festival.uid ?: "", festival)
        }
    }
}
