package com.ss.haat.Interface

import com.ss.haat.Data.Animal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface Animal_Repository:JpaRepository<Animal,Long>
{
    @Query("select  p from Animal  p where p.animal_type  = :type ")
    fun getAnimalType(@Param("type") type: String): MutableList<Animal>

    @Query("select  p from Animal  p where p.id_animal  = :animal ")
    fun getByAnimal(@Param("animal") type: Long): MutableList<Animal>

    @Query("select  p from Animal  p where p.id_animal  = :id ")
    fun getByOne(@Param("id") id: Long): Animal

}