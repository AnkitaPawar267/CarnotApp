package com.example.carnot.ResponsePojoClass

import com.google.gson.annotations.SerializedName

data class VillageWise (

    @SerializedName("created") var created : Int,
    @SerializedName("updated") var updated : Int,
    @SerializedName("created_date") var createdDate : String,
    @SerializedName("updated_date") var updatedDate : String,
    @SerializedName("active") var active : String,
    @SerializedName("index_name") var indexName : String,
    @SerializedName("org") var org : List<String>,
    @SerializedName("org_type") var orgType : String,
    @SerializedName("source") var source : String,
    @SerializedName("title") var title : String,
    @SerializedName("external_ws_url") var externalWsUrl : String,
    @SerializedName("visualizable") var visualizable : String,
    @SerializedName("field") var field : List<Field>,
    @SerializedName("external_ws") var externalWs : Int,
    @SerializedName("catalog_uuid") var catalogUuid : String,
    @SerializedName("sector") var sector : List<String>,
    @SerializedName("target_bucket") var targetBucket : TargetBucket,
    @SerializedName("desc") var desc : String,
    @SerializedName("message") var message : String,
    @SerializedName("version") var version : String,
    @SerializedName("status") var status : String,
    @SerializedName("total") var total : Int,
    @SerializedName("count") var count : Int,
    @SerializedName("limit") var limit : String,
    @SerializedName("offset") var offset : String,
    @SerializedName("records") var records : List<Records>

)

data class Field (

    @SerializedName("name") var name : String,
    @SerializedName("id") var id : String,
    @SerializedName("type") var type : String

)

data class TargetBucket (

    @SerializedName("field") var field : String,
    @SerializedName("index") var index : String,
    @SerializedName("type") var type : String

)

data class Records (

    @SerializedName("state") var state : String,
    @SerializedName("district") var district : String,
    @SerializedName("market") var market : String,
    @SerializedName("commodity") var commodity : String,
    @SerializedName("variety") var variety : String,
    @SerializedName("arrival_date") var arrivalDate : String,
    @SerializedName("min_price") var minPrice : String,
    @SerializedName("max_price") var maxPrice : String,
    @SerializedName("modal_price") var modalPrice : String

)

