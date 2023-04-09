package ru.bonbon.libellus.ui.theme

enum class Themes(
    val id: Int,
    val label: String,
) {
    System(id = 0, label = "Системная"),
    Light(id = 1, label = "Светлая"),
    Dark(id = 2, label = "Тёмная"),
}