package ie.wit.my_festival.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FestivalModel(var id: Long = 0,
                         var title: String = "",
                         var description: String = "",
                         var date: String = "",
                         var valueForMoney: Float = 0.0f,
                         var accessibility: Float = 0.0f,
                         var familyFriendly: Float = 0.0f,
                         var image: Uri = Uri.EMPTY) : Parcelable