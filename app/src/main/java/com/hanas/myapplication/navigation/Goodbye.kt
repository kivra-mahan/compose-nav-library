package com.hanas.myapplication.navigation

import com.hanas.hnsnavigation.Behaviour
import com.hanas.hnsnavigation.HnsNavConfigurationPair
import com.hanas.hnsnavigation.HnsNavLocation
import com.hanas.hnsnavigation.HnsNavNoData
import com.hanas.hnsnavigation.SameOnTopBehaviour
import com.hanas.hnsnavigation.lazyMapOf

object Goodbye : HnsNavLocation<HnsNavNoData>(
    name = "goodbye",
    dataClass = HnsNavNoData::class,
    configurations = lazyMapOf(
        HnsNavConfigurationPair(Greeting, Behaviour.ADD_ON_TOP, SameOnTopBehaviour.REPLACE)
    )
)