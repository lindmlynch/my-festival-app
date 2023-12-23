package ie.wit.my_festival.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize



@IgnoreExtraProperties
@Parcelize
data class FestivalModel(
    var uid: String? = "",
    var title: String = "",
    var description: String = "",
    var date: String = "",
    var valueForMoney: Float = 0.0f,
    var accessibility: Float = 0.0f,
    var familyFriendliness: Float = 0.0f,
    var image: String = "",
    var email: String = ""
) : Parcelable {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "description" to description,
            "date" to date,
            "valueForMoney" to valueForMoney,
            "accessibility" to accessibility,
            "familyFriendliness" to familyFriendliness,
            "image" to image,
            "email" to email
        )
    }
}
