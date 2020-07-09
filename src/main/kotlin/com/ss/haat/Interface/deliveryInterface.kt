package com.ss.haat.Interface

import com.ss.haat.Data.Animal
import com.ss.haat.Data.Cart
import com.ss.haat.Data.Order
import com.ss.haat.Data.Order_Delivery
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface Delivery_Repository:JpaRepository<Order_Delivery,Long>
{
    @Query("select  p from Order_Delivery  p where p.order_id  = :id ")
    fun getByOrderId(@Param("id") id: Long): MutableList<Order_Delivery>

    @Query("select  p from Order_Delivery  p where p.on_the_way  = :id ")
    fun getByOrderWay(@Param("id") id: String): MutableList<Order_Delivery>

    @Query("select  p from Order_Delivery p where p.on_the_way  = :name and p.delivered = :names")
    fun getByOrderMsg(@Param("name") name: String, @Param("names") names: String): MutableList<Order_Delivery>

    @Query("select  p from Order_Delivery p where p.animal_id  = :animal and p.user_id = :user")
    fun getDeliveryAvail(@Param("animal") animal: Long, @Param("user") user: Long): MutableList<Order_Delivery>

    @Query("select  p from Order_Delivery p where p.user_id = :user")
    fun getDeliverylist(@Param("user") user: Long): MutableList<Order_Delivery>
}