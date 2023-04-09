package ru.bonbon.libellus.navigation

enum class Screen (
    val route: String,
    val label: String,
){
    Schedule(route = "schedule", label = "Расписание"),
    Consultations(route = "consultations", label = "Консультации"),
    Settings(route = "settings", label = "Настройки")
}