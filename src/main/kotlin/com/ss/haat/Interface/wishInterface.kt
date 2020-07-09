package com.ss.haat.Interface

import com.ss.haat.Data.Animal
import com.ss.haat.Data.WishList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface Wish_Repository:JpaRepository<WishList, Long>
{
    @Query("select  p from WishList p where p.id_animal  = :wish and p.id_user = :user")
    fun getWishPresent(@Param("wish") wish: Long,@Param("user") user: Long): MutableList<WishList>

    @Query("select  p from WishList p where p.id_user = :user")
    fun getWishUser(@Param("user") user: Long): MutableList<WishList>

    @Query("select  p from WishList p where p.id_animal = :id")
    fun getWishList(@Param("id") id: Long): MutableList<WishList>

}