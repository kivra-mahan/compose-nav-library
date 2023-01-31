package com.hanas.myapplication.navigation

import android.os.Parcelable
import com.hanas.hnsnavigation.Behaviour
import com.hanas.hnsnavigation.HnsNavConfigurationPair
import com.hanas.hnsnavigation.HnsNavLocation
import com.hanas.hnsnavigation.SameOnTopBehaviour
import com.hanas.hnsnavigation.lazyMapOf
import kotlinx.parcelize.Parcelize

object Greeting : HnsNavLocation<Greeting.Data>(
    name = "greeting",
    dataClass = Data::class,
    configurations = lazyMapOf(
        HnsNavConfigurationPair(
            destination = Goodbye,
            behaviour = Behaviour.BACK_TO_LAST_INSTANCE_OF_DESTINATION,
            sameOnTopBehaviour = SameOnTopBehaviour.OPEN_EXISTING
        )
    )
) {
    @Parcelize
    data class Data(
        val nickname: String
    ) : Parcelable
}
