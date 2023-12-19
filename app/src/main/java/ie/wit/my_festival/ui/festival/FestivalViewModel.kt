package ie.wit.my_festival.ui.festival

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.my_festival.models.FestivalManager
import ie.wit.my_festival.models.FestivalModel

class FestivalViewModel : ViewModel() {

    private val _festival = MutableLiveData<FestivalModel>()
    val festival: LiveData<FestivalModel> = _festival

    fun setFestival(festivalModel: FestivalModel) {
        _festival.value = festivalModel
    }

    fun createFestival(festival: FestivalModel) {
        FestivalManager.create(festival)
    }

    fun updateFestival(festival: FestivalModel) {

    }


}
