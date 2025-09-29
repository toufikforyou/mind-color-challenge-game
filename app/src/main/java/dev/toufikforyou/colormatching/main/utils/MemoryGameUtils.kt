package dev.toufikforyou.colormatching.main.utils

import dev.toufikforyou.colormatching.main.domain.model.ColorGrid
import kotlin.random.Random

/**
 * Utility functions for memory-based color challenge game
 */
object MemoryGameUtils {
    
    /**
     * Generate a 2D grid of dynamic colors with customizable size and color list
     * @param gridSize Grid dimensions (3, 4, or 5)
     * @param colors List of color names to choose from
     * @return ColorGrid with randomly assigned colors
     */
    fun generateColorGrid(gridSize: Int, colors: List<String>): ColorGrid {
        require(gridSize in listOf(3, 4, 5)) { "Grid size must be 3, 4, or 5" }
        require(colors.isNotEmpty()) { "Color list cannot be empty" }
        
        val totalCells = gridSize * gridSize
        val gridCells = mutableListOf<String>()
        
        // Randomly assign colors from the provided list to each cell
        repeat(totalCells) {
            gridCells.add(colors.random(Random.Default))
        }
        
        return ColorGrid(gridSize, gridCells)
    }
    
    /**
     * Generate a color grid with a balanced distribution of colors
     * This ensures better visual variety in the grid
     * @param gridSize Grid dimensions (3, 4, or 5)
     * @param colors List of color names to choose from
     * @return ColorGrid with evenly distributed colors
     */
    fun generateBalancedColorGrid(gridSize: Int, colors: List<String>): ColorGrid {
        require(gridSize in listOf(3, 4, 5)) { "Grid size must be 3, 4, or 5" }
        require(colors.isNotEmpty()) { "Color list cannot be empty" }
        
        val totalCells = gridSize * gridSize
        val gridCells = mutableListOf<String>()
        
        // Calculate how many times each color should appear
        val colorsPerCell = totalCells / colors.size
        val remainder = totalCells % colors.size
        
        // Add colors evenly
        colors.forEachIndexed { index, color ->
            val count = colorsPerCell + if (index < remainder) 1 else 0
            repeat(count) {
                gridCells.add(color)
            }
        }
        
        // Shuffle to randomize positions
        gridCells.shuffle(Random.Default)
        
        return ColorGrid(gridSize, gridCells)
    }
    
    /**
     * Simulate switching from dynamic to static mode
     * In a real implementation, this might involve stopping animations or color changes
     * For now, it returns the same grid as a "static" snapshot
     * @param dynamicGrid The dynamic color grid
     * @return ColorGrid representing the static state
     */
    fun convertToStaticMode(dynamicGrid: ColorGrid): ColorGrid {
        // In the future, this could:
        // - Stop color animations
        // - Fix color values that were changing
        // - Save the current state as immutable
        return dynamicGrid.copy()
    }
    
    /**
     * Compare two grids and return whether they match exactly
     * @param originalGrid The original/target grid
     * @param userAttempt The user's attempt to recreate the grid
     * @return true if grids match exactly, false otherwise
     */
    fun compareGrids(originalGrid: ColorGrid, userAttempt: ColorGrid): Boolean {
        if (originalGrid.size != userAttempt.size) return false
        return originalGrid.cells == userAttempt.cells
    }
    
    /**
     * Compare two grids and return detailed comparison results
     * @param originalGrid The original/target grid
     * @param userAttempt The user's attempt to recreate the grid
     * @return GridComparisonResult with detailed information
     */
    fun compareGridsDetailed(originalGrid: ColorGrid, userAttempt: ColorGrid): GridComparisonResult {
        if (originalGrid.size != userAttempt.size) {
            return GridComparisonResult(
                isExactMatch = false,
                correctCells = 0,
                totalCells = originalGrid.cells.size,
                accuracy = 0.0,
                incorrectPositions = emptyList()
            )
        }
        
        val incorrectPositions = mutableListOf<Int>()
        var correctCells = 0
        
        originalGrid.cells.forEachIndexed { index, originalColor ->
            if (originalColor == userAttempt.cells[index]) {
                correctCells++
            } else {
                incorrectPositions.add(index)
            }
        }
        
        return GridComparisonResult(
            isExactMatch = incorrectPositions.isEmpty(),
            correctCells = correctCells,
            totalCells = originalGrid.cells.size,
            accuracy = correctCells.toDouble() / originalGrid.cells.size,
            incorrectPositions = incorrectPositions
        )
    }
    
    /**
     * Create an empty grid filled with a placeholder color
     * Useful for initializing user input grids
     * @param gridSize Grid dimensions (3, 4, or 5)
     * @param placeholderColor Color name to fill the grid with
     * @return ColorGrid filled with placeholder color
     */
    fun createEmptyGrid(gridSize: Int, placeholderColor: String = "Empty"): ColorGrid {
        require(gridSize in listOf(3, 4, 5)) { "Grid size must be 3, 4, or 5" }
        
        val totalCells = gridSize * gridSize
        val cells = List(totalCells) { placeholderColor }
        
        return ColorGrid(gridSize, cells)
    }
    
    /**
     * Update a specific cell in the grid
     * @param grid Original grid
     * @param row Row index (0-based)  
     * @param col Column index (0-based)
     * @param newColor New color name for the cell
     * @return New ColorGrid with updated cell
     */
    fun updateGridCell(grid: ColorGrid, row: Int, col: Int, newColor: String): ColorGrid {
        require(row in 0 until grid.size) { "Row must be between 0 and ${grid.size - 1}" }
        require(col in 0 until grid.size) { "Column must be between 0 and ${grid.size - 1}" }
        
        val index = row * grid.size + col
        val newCells = grid.cells.toMutableList()
        newCells[index] = newColor
        
        return ColorGrid(grid.size, newCells)
    }
    
    /**
     * Update a specific cell in the grid by linear index
     * @param grid Original grid
     * @param index Linear index (row-major order)
     * @param newColor New color name for the cell
     * @return New ColorGrid with updated cell
     */
    fun updateGridCell(grid: ColorGrid, index: Int, newColor: String): ColorGrid {
        require(index in grid.cells.indices) { "Index must be between 0 and ${grid.cells.size - 1}" }
        
        val newCells = grid.cells.toMutableList()
        newCells[index] = newColor
        
        return ColorGrid(grid.size, newCells)
    }
}

/**
 * Data class representing the results of a grid comparison
 */
data class GridComparisonResult(
    val isExactMatch: Boolean,
    val correctCells: Int,
    val totalCells: Int,
    val accuracy: Double,
    val incorrectPositions: List<Int>
)