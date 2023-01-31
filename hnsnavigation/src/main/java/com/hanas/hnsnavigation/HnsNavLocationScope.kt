package com.hanas.hnsnavigation

import android.os.Parcelable

interface HnsNavLocationScope<L : HnsNavLocation<*>> {
    fun <A : Parcelable> HnsNavigator.scopedNavigateTo(destination: HnsNavLocation<A>, navArgs: A)

    fun HnsNavigator.scopedNavigateTo(destination: HnsNavLocation<HnsNavNoData>)
}

class HnsNavLocationScopeImpl<L : HnsNavLocation<*>>(
    private val currentLocation: L
) : HnsNavLocationScope<L> {
    override fun <A : Parcelable> HnsNavigator.scopedNavigateTo(
        destination: HnsNavLocation<A>, navArgs: A
    ) {
        this.navigateFromTo(currentLocation, destination, navArgs)
    }

    override fun HnsNavigator.scopedNavigateTo(destination: HnsNavLocation<HnsNavNoData>) {
        this.navigateFromTo(currentLocation, destination)
    }
}