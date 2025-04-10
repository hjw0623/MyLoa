package com.hjw0623.lostark

sealed class NavigationRoutes(val route: String) {
    data object CharacterManager : NavigationRoutes("character_manager")
    data object CharacterSearch : NavigationRoutes("character_search")
    data object Event: NavigationRoutes("event")
    data object CharacterAdd : NavigationRoutes("character_add") {
        fun createRoute() = "character_add"
    }
    data object CharacterOverview : NavigationRoutes("character_overview/{characterName}") {
        fun createRoute(characterName: String) = "character_overview/$characterName"
    }

    data object CharacterSetting : NavigationRoutes("character_setting/{characterName}") {
        fun createRoute(characterName: String) = "character_setting/$characterName"
    }
}
