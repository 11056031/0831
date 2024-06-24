package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    @PrimaryKey val tel: String,
    val name: String
): Serializable
