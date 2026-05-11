package com.example.powertrack_app.data.remote.entity

data class ExerciseDbResponse(
    val id: String,
    val name: String,
    val gifUrl: String,
    val bodyPart: String,
    val equipment: String,
    val target: String
)