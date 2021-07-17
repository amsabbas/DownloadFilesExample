package com.ams.downloadfiles.data.source.model

import com.squareup.moshi.Json

data class File(
    @field:Json(name = "id") var id: Int,
    @field:Json(name = "type") var type: String,
    @field:Json(name = "url") var url: String,
    @field:Json(name = "name") var name: String
)