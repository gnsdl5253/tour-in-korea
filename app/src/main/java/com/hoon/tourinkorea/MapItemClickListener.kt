package com.hoon.tourinkorea

import com.hoon.tourinkorea.data.model.Place

interface MapItemClickListener {
    fun onClick(place: Place)
}