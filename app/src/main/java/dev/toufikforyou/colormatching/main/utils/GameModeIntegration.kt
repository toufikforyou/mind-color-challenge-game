package dev.toufikforyou.colormatching.main.utils

import androidx.compose.ui.graphics.Color
import dev.toufikforyou.colormatching.main.domain.model.ColorBox
import dev.toufikforyou.colormatching.main.domain.model.ColorGrid

/**
 * Integration utilities to bridge the new memory-based color challenge game
 * with the existing ColorBox-based system
 */
object GameModeIntegration {
    
    /**
     * Mapping of string color names to Compose Color objects
     * This allows the memory game to use string names while maintaining
     * compatibility with the existing UI system
     */
    private val colorNameMap = mapOf(
        "Red" to Color.Red,
        "Blue" to Color.Blue,
        "Green" to Color.Green,
        "Yellow" to Color.Yellow,
        "Orange" to Color(0xFFFFA500),
        "Purple" to Color(0xFF800080),
        "Pink" to Color(0xFFFFC0CB),
        "Brown" to Color(0xFFA52A2A),
        "Gray" to Color.Gray,
        "Cyan" to Color.Cyan,
        "Magenta" to Color.Magenta,
        "Lime" to Color(0xFF00FF00),
        "Light Pink" to Color(0xFFFFB6C1),
        "Light Blue" to Color(0xFFADD8E6),
        "Light Green" to Color(0xFF90EE90),
        "Light Yellow" to Color(0xFFFFFFE0),
        "Light Orange" to Color(0xFFFFE4B5),
        "Light Purple" to Color(0xFFDDA0DD),
        "Mint" to Color(0xFFF5FFFA),
        "Peach" to Color(0xFFFFDAB9),
        "Lavender" to Color(0xFFE6E6FA),
        "Cream" to Color(0xFFFFFFCC),
        "Bright Red" to Color(0xFFFF0000),
        "Electric Blue" to Color(0xFF0080FF),
        "Neon Green" to Color(0xFF39FF14),
        "Golden Yellow" to Color(0xFFFFD700),
        "Hot Pink" to Color(0xFFFF69B4),
        "Deep Purple" to Color(0xFF9400D3),
        "Turquoise" to Color(0xFF40E0D0),
        "Coral" to Color(0xFFFF7F50),
        "Emerald" to Color(0xFF50C878),
        "Ruby" to Color(0xFFE0115F),
        "Forest Green" to Color(0xFF228B22),
        "Sky Blue" to Color(0xFF87CEEB),
        "Sunset Orange" to Color(0xFFFF4500),
        "Desert Sand" to Color(0xFFF4A460),
        "Ocean Blue" to Color(0xFF006994),
        "Mountain Gray" to Color(0xFF708090),
        "Autumn Red" to Color(0xFF8B0000),
        "Grass Green" to Color(0xFF7CFC00),
        "Empty" to Color.Transparent
    )
    
    /**
     * Convert a ColorGrid (string-based) to a list of ColorBox objects
     * This enables using the memory game grid with existing UI components
     * @param colorGrid The string-based color grid
     * @return List of ColorBox objects compatible with existing UI
     */
    fun convertToColorBoxes(colorGrid: ColorGrid): List<ColorBox> {
        return colorGrid.cells.map { colorName ->
            ColorBox(
                color = colorNameMap[colorName] ?: Color.Gray,
                isSelected = false,
                isMatched = false
            )
        }
    }
    
    /**
     * Convert a list of ColorBox objects to a ColorGrid
     * This allows converting existing game state back to the memory game format
     * @param colorBoxes List of ColorBox objects
     * @param gridSize Grid dimensions
     * @return ColorGrid with string-based color names
     */
    fun convertFromColorBoxes(colorBoxes: List<ColorBox>, gridSize: Int): ColorGrid {
        val colorNames = colorBoxes.map { colorBox ->
            colorNameMap.entries.find { it.value == colorBox.color }?.key ?: "Unknown"
        }
        return ColorGrid(gridSize, colorNames)
    }
    
    /**
     * Create a memory challenge game mode using the existing ColorBox system
     * @param gridSize Grid dimensions (3, 4, or 5)
     * @param colorSet List of color names to use
     * @param memorizeTime Time in seconds to show colors initially
     * @return MemoryGameSession containing all game data
     */
    fun createMemoryGameSession(
        gridSize: Int,
        colorSet: List<String>,
        memorizeTime: Int = 5
    ): MemoryGameSession {
        // Generate the original grid for the player to memorize
        val originalGrid = MemoryGameUtils.generateBalancedColorGrid(gridSize, colorSet)
        
        // Create an empty grid for user input
        val userGrid = MemoryGameUtils.createEmptyGrid(gridSize, "Empty")
        
        return MemoryGameSession(
            originalGrid = originalGrid,
            userGrid = userGrid,
            memorizeTimeSeconds = memorizeTime,
            isMemorizePhase = true,
            isComplete = false,
            score = 0
        )
    }
    
    /**
     * Process a user's cell selection in memory game mode
     * @param session Current game session
     * @param row Row of selected cell
     * @param col Column of selected cell  
     * @param selectedColor Color name chosen by user
     * @return Updated MemoryGameSession
     */
    fun processUserSelection(
        session: MemoryGameSession,
        row: Int,
        col: Int,
        selectedColor: String
    ): MemoryGameSession {
        if (session.isMemorizePhase || session.isComplete) {
            return session // No changes during memorize phase or after completion
        }
        
        val updatedUserGrid = MemoryGameUtils.updateGridCell(
            session.userGrid, row, col, selectedColor
        )
        
        // Check if grid is complete (no empty cells)
        val isComplete = !updatedUserGrid.cells.contains("Empty")
        
        // Calculate score if complete
        val score = if (isComplete) {
            val comparison = MemoryGameUtils.compareGridsDetailed(session.originalGrid, updatedUserGrid)
            (comparison.accuracy * 1000).toInt() // Score based on accuracy
        } else {
            session.score
        }
        
        return session.copy(
            userGrid = updatedUserGrid,
            isComplete = isComplete,
            score = score
        )
    }
    
    /**
     * Transition from memorize phase to play phase
     * @param session Current game session
     * @return Updated session in play mode
     */
    fun startPlayPhase(session: MemoryGameSession): MemoryGameSession {
        return session.copy(isMemorizePhase = false)
    }
    
    /**
     * Get available color options for user selection
     * Based on the colors used in the original grid
     * @param session Current game session
     * @return List of color names that might be in the grid
     */
    fun getColorOptions(session: MemoryGameSession): List<String> {
        return session.originalGrid.cells.distinct().sorted()
    }
    
    /**
     * Check if a color name is valid and can be converted to a Color
     * @param colorName Name of the color to check
     * @return true if the color is supported
     */
    fun isValidColor(colorName: String): Boolean {
        return colorNameMap.containsKey(colorName)
    }
    
    /**
     * Get the Compose Color for a color name
     * @param colorName Name of the color
     * @return Compose Color object, or Gray if not found
     */
    fun getColorFromName(colorName: String): Color {
        return colorNameMap[colorName] ?: Color.Gray
    }
    
    /**
     * Add a new color mapping (for extensibility)
     * @param colorName Name of the new color
     * @param color Compose Color object
     */
    fun addColorMapping(colorName: String, color: Color) {
        (colorNameMap as MutableMap)[colorName] = color
    }
}

/**
 * Data class representing a complete memory game session
 */
data class MemoryGameSession(
    val originalGrid: ColorGrid,
    val userGrid: ColorGrid,
    val memorizeTimeSeconds: Int,
    val isMemorizePhase: Boolean,
    val isComplete: Boolean,
    val score: Int
) {
    /**
     * Get the accuracy of the current user attempt
     * @return Accuracy percentage (0.0 to 1.0)
     */
    fun getAccuracy(): Double {
        return MemoryGameUtils.compareGridsDetailed(originalGrid, userGrid).accuracy
    }
    
    /**
     * Check if the user's attempt is perfect
     * @return true if all cells match exactly
     */
    fun isPerfect(): Boolean {
        return MemoryGameUtils.compareGrids(originalGrid, userGrid)
    }
    
    /**
     * Get the number of correct cells
     * @return Count of correctly placed colors
     */
    fun getCorrectCells(): Int {
        return MemoryGameUtils.compareGridsDetailed(originalGrid, userGrid).correctCells
    }
}