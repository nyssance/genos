package com.example.genos.model

data class Examination(
        val id: Long,
        val title: String,
        val questions: List<Question>
)

data class Question(
        val id: Long,
        val title: String,
        val type: String,
        val options: List<String>,
        val answers: List<String>,
        val analysis: String
)
