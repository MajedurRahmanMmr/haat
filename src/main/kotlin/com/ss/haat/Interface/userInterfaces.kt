package com.ss.haat.Interface

import com.ss.haat.Data.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface User_Repository:JpaRepository<User,Long>
{
    @Query("select  p from User p where p.phone  = :phone")
    fun getByPhone(@Param("phone") phone: String?): User?

    @Query("select  p from User  p where p.token  = :token")
    fun getByToken(@Param("token") token: String): User?
}