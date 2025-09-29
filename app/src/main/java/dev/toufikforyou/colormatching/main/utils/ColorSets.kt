package dev.toufikforyou.colormatching.main.utils

/**
 * Predefined color sets for the memory-based color challenge game
 * Uses simple string names instead of hex values for clean, modular design
 */
object ColorSets {
    
    /**
     * Basic color set with primary colors
     */
    val BASIC_COLORS = listOf(
        "Red",
        "Blue", 
        "Green",
        "Yellow",
        "Orange",
        "Purple"
    )
    
    /**
     * Extended color set with more variety
     */
    val EXTENDED_COLORS = listOf(
        "Red",
        "Blue",
        "Green", 
        "Yellow",
        "Orange",
        "Purple",
        "Pink",
        "Brown",
        "Gray",
        "Cyan",
        "Magenta",
        "Lime"
    )
    
    /**
     * Pastel color set for softer visuals
     */
    val PASTEL_COLORS = listOf(
        "Light Pink",
        "Light Blue",
        "Light Green",
        "Light Yellow",
        "Light Orange",
        "Light Purple",
        "Mint",
        "Peach",
        "Lavender",
        "Cream"
    )
    
    /**
     * Vibrant color set for high contrast
     */
    val VIBRANT_COLORS = listOf(
        "Bright Red",
        "Electric Blue",
        "Neon Green",
        "Golden Yellow",
        "Hot Pink",
        "Deep Purple",
        "Turquoise",
        "Coral",
        "Emerald",
        "Ruby"
    )
    
    /**
     * Earth tone color set for natural feel
     */
    val EARTH_TONES = listOf(
        "Forest Green",
        "Sky Blue",
        "Sunset Orange",
        "Desert Sand",
        "Ocean Blue",
        "Mountain Gray",
        "Autumn Red",
        "Grass Green"
    )
    
    /**
     * Get a color set based on difficulty level
     * @param difficulty 1 = Easy (6 colors), 2 = Medium (9 colors), 3 = Hard (12 colors)
     * @return List of color names appropriate for the difficulty
     */
    fun getColorSetForDifficulty(difficulty: Int): List<String> {
        return when (difficulty) {
            1 -> BASIC_COLORS
            2 -> EXTENDED_COLORS.take(9)
            3 -> EXTENDED_COLORS
            else -> BASIC_COLORS
        }
    }
    
    /**
     * Get a color set based on grid size (ensures adequate variety)
     * @param gridSize Grid dimensions (3, 4, or 5)
     * @return List of color names with enough variety for the grid size
     */
    fun getColorSetForGridSize(gridSize: Int): List<String> {
        val requiredColors = when (gridSize) {
            3 -> 4 // 3x3 = 9 cells, use 4 colors for some repetition
            4 -> 6 // 4x4 = 16 cells, use 6 colors for balanced distribution
            5 -> 8 // 5x5 = 25 cells, use 8 colors for good variety
            else -> 6
        }
        return EXTENDED_COLORS.take(requiredColors)
    }
    
    /**
     * Get all available color sets
     * @return Map of color set names to their color lists
     */
    fun getAllColorSets(): Map<String, List<String>> {
        return mapOf(
            "Basic" to BASIC_COLORS,
            "Extended" to EXTENDED_COLORS,
            "Pastel" to PASTEL_COLORS,
            "Vibrant" to VIBRANT_COLORS,
            "Earth Tones" to EARTH_TONES
        )
    }
}