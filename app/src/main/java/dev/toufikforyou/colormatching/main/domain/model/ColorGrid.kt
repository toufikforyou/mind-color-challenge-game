package dev.toufikforyou.colormatching.main.domain.model

/**
 * Represents a 2D color grid for the memory-based color challenge game
 * @param size Grid dimensions (3x3, 4x4, or 5x5)
 * @param cells List of color names arranged in row-major order
 */
data class ColorGrid(
    val size: Int,
    val cells: List<String>
) {
    init {
        require(size in listOf(3, 4, 5)) { "Grid size must be 3, 4, or 5" }
        require(cells.size == size * size) { "Cell count must match grid size (${size * size})" }
    }
    
    /**
     * Get color at specific row and column
     * @param row Row index (0-based)
     * @param col Column index (0-based)
     * @return Color name at the specified position
     */
    fun getColor(row: Int, col: Int): String {
        require(row in 0 until size) { "Row must be between 0 and ${size - 1}" }
        require(col in 0 until size) { "Column must be between 0 and ${size - 1}" }
        return cells[row * size + col]
    }
    
    /**
     * Get color at specific index (row-major order)
     * @param index Linear index
     * @return Color name at the specified index
     */
    fun getColor(index: Int): String {
        require(index in cells.indices) { "Index must be between 0 and ${cells.size - 1}" }
        return cells[index]
    }
    
    /**
     * Convert grid to 2D representation for easier visualization
     * @return List of rows, each containing a list of color names
     */
    fun to2D(): List<List<String>> {
        return cells.chunked(size)
    }
}