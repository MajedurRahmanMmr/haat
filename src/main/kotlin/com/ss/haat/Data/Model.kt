package com.ss.haat.Data

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class User(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id_user: Long? = null,
        var name:String? = null,
        val phone:String? = null,
        var password:String? = null,
        var address:String? = null,
        var email:String? = null,
        val order_state_one:Long = 0,
        var order_state_two:Long = 0,
        var second_number:String = "0",
        var token:String? = null
)

@Entity
class Animal(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id_animal: Long? = null,
        val animal_name:String? = null,
        val animal_des:String? = null,
        val animal_price_new:String? = null,
        val animal_price_old:String? = null,
        val animal_type:String? = null,
        val animal_pic:String? = null,
        val animal_owner:Long? = null,
        val animal_contact:String? = null,
        val animal_rating:Double? = null,
        val animal_location:String? = null
)

@Entity
class Animal_Image(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id_animal_image: Long? = null,
        val animal_id:Long? = null,
        val image_url:String? = null
)


@Entity
class WishList(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id_wish: Long? = null,
        val id_animal: Long? = null,
        val animal_name:String? = null,
        val animal_des:String? = null,
        val animal_price_new:String? = null,
        val animal_price_old:String? = null,
        val animal_pic:String? = null,
        val id_user:Long? = null,
        val animal_rating:Double? = null,
        val animal_location:String? = null
)

@Entity
class Order(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id_order: Long? = null,
        val user_id:Long? = null,
        val animal_id:Long? = null,
        val user_name:String? = null,
        val user_address:String? = null,
        val user_number:String? = null,
        val user_contact:String? = null,
        val animal_name:String? = null,
        val animal_des: String? = null,
        val animal_price:String? = null,
        var order_status:String? = null,
        val animal_rating:Double? = null,
        val animal_pic:String? = null,
        val order_time:String? = null,
        var order_id_tracking:String?= null,
        var order_location:String? = null
)

@Entity
class Order_Delivery(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id_delivery: Long? = null,
        val user_id:Long? = null,
        val animal_id:Long? = null,
        val order_id:Long? = null,
        val user_name:String? = null,
        val user_address:String? = null,
        val user_number:String? = null,
        val user_contact:String? = null,
        val animal_name:String? = null,
        val animal_price:String? = null,
        var order_status:String? = null,
        val animal_rating:Double? = null,
        val animal_pic:String? = null,
        val order_time:String? = null,
        val order_location:String? = null,
        val order_recived:String? = null,
        val order_recived_message: String?= null,
        var on_the_way:String? = null,
        var on_the_way_message:String? = null,
        var delivered:String? = null,
        val order_id_tracking:String?= null,
        val customer_receive:String? = null,
        var estimated_time:LocalDateTime? = null

)


@Entity
class Cart(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id_cart: Long? = null,
        val user_id:Long? = null,
        val animal_id:Long? = null,
        val user_name:String? = null,
        val user_address:String? = null,
        val user_number:String? = null,
        val user_contact:String? = null,
        val animal_name:String? = null,
        val animal_des: String? = null,
        val animal_price:String? = null,
        val animal_location: String? = null,
        //var order_status:String? = null,
        val animal_rating:Double? = null,
        val animal_pic:String? = null
        //val order_time:String? = null//,
        // val order_location:String? = null
)