package com.ss.haat.Interface

import com.ss.haat.Data.Cart
import com.ss.haat.Data.Order
import com.ss.haat.Data.WishList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface Order_Repository:JpaRepository<Order,Long>
{
    @Query("select  p from Order p where p.user_id  = :user_id and p.animal_id = :animal_id")
    fun getOrder(@Param("user_id") user_id: Long, @Param("animal_id") animal_id: Long): Order

    @Query("select  p from Order p where p.user_id  = :user_id")
    fun getOrderList(@Param("user_id") user_id: Long): MutableList<Order>

    @Query("select  p from Order p where p.order_status  = :pending")
    fun getPendingOrder(@Param("pending") pending: String): MutableList<Order>

    @Query("select  p from Order p where p.animal_id  = :animal and p.user_id = :user")
    fun getOrderAvail(@Param("animal") animal: Long, @Param("user") user: Long): MutableList<Cart>
}