package ie.wit.my_festival.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser


interface FestivalStore {
    fun findAll(festivalsList:
                MutableLiveData<List<FestivalModel>>)
    fun findAll(userid:String,
                festivalsList:
                MutableLiveData<List<FestivalModel>>)
    fun findById(userid:String, festivalid: String,
                 festival: MutableLiveData<FestivalModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, festival: FestivalModel)
    fun delete(userid:String, festivalid: String)
    fun update(userid:String, festivalid: String, festival: FestivalModel)
}