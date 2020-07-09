package com.ss.haat.Interface

import com.ss.haat.Data.Animal
import com.ss.haat.Data.Animal_Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface Animal_Image_Repository:JpaRepository<Animal_Image,Long>
{

    @Query("select  p from Animal_Image  p where p.animal_id  = :id ")
    fun getByImage(@Param("id") id: Long): MutableList<Animal_Image>

}