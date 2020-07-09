package com.ss.haat.Data

data class register(
        val name:String? = null,
        val phone:String? = null,
        val password:String? = null,
        val address:String? = null,
        val email:String? = null
)

data class ret(
        val name:String? = null
)

data class login(
        val phone: String?,
        val password: String?
)

data class change(
        val token:String,
        val pass_old:String,
        val pass_new:String
)

data class update(
        val token:String,
        val fullname:String,
        val second_number:String,
        var address:String
)

data class purchase(
        val token:String,
        val name:String,
        val phone:String,
        val email:String,
        val address: String,
        val product_id:String
)

data class orderdetail(
        val order_id:String,
        val animal_name:String,
        val animal_pic:String,
        val animal_price:String,
        val animal_age:String,
        val animal_location:String,
        val order_tracking_id:String,
        val order_location:String,
        val order_status:String,
        val order_time:String,
        val advance_amount:String
)