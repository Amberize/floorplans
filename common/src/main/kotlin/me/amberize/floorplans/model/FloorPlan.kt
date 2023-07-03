package me.amberize.floorplans.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FloorPlan(
    val hash: String,
    val thumbnail: String?,
    val rooms: Int?,
)
