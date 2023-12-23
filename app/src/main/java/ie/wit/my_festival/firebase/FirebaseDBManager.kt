package ie.wit.my_festival.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.my_festival.models.FestivalModel
import ie.wit.my_festival.models.FestivalStore
import timber.log.Timber



object FirebaseDBManager : FestivalStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(festivalsList: MutableLiveData<List<FestivalModel>>) {
        database.child("festivals")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Festival error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<FestivalModel>()
                    val children = snapshot.children
                    children.forEach {
                        val festival = it.getValue(FestivalModel::class.java)
                        localList.add(festival!!)
                    }
                    database.child("festivals")
                        .removeEventListener(this)

                    festivalsList.value = localList
                }
            })
    }

    override fun findAll(userid: String, festivalsList: MutableLiveData<List<FestivalModel>>) {

        database.child("user-festivals").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<FestivalModel>()
                    val children = snapshot.children
                    children.forEach {
                        val festival = it.getValue(FestivalModel::class.java)
                        localList.add(festival!!)
                    }
                    database.child("user-festivals").child(userid)
                        .removeEventListener(this)

                    festivalsList.value = localList
                }
            })
    }

    override fun findById(userid: String, festivalid: String, festival: MutableLiveData<FestivalModel>) {

        database.child("user-festivals").child(userid)
            .child(festivalid).get().addOnSuccessListener {
                festival.value = it.getValue(FestivalModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, festival: FestivalModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("festivals").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        festival.uid = key
        val festivalValues = festival.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/festivals/$key"] = festivalValues
        childAdd["/user-festivals/$uid/$key"] = festivalValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, festivalid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/festivals/$festivalid"] = null
        childDelete["/user-festivals/$userid/$festivalid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, festivalid: String, festival: FestivalModel) {

        val festivalValues = festival.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["festivals/$festivalid"] = festivalValues
        childUpdate["user-festivals/$userid/$festivalid"] = festivalValues

        database.updateChildren(childUpdate)
    }
}