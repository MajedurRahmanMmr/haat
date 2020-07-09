package com.ss.haat.Interface

import com.ss.haat.Data.Cart
import com.ss.haat.Data.WishList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface Cart_Repository:JpaRepository<Cart, Long>
{
    @Query("select  p from Cart p where p.animal_id  = :animal and p.user_id = :user")
    fun getCart(@Param("animal") animal: Long, @Param("user") user: Long): Cart

    @Query("select  p from Cart p where p.animal_id  = :animal and p.user_id = :user")
    fun getCarts(@Param("animal") animal: Long, @Param("user") user: Long): MutableList<Cart>

    @Query("select  p from Cart p where p.user_id  = :user")
    fun getCartlist(@Param("user") user: Long): MutableList<Cart>

    @Query("select  p from Cart p where p.animal_id  = :id")
    fun getCartlists(@Param("id") id: Long): MutableList<Cart>


    @Query("select  p from Cart p where p.animal_id  = :animal and p.user_id = :user")
    fun getCartAvail(@Param("animal") animal: Long, @Param("user") user: Long): MutableList<Cart>
}