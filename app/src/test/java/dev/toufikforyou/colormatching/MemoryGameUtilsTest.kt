package dev.toufikforyou.colormatching

import dev.toufikforyou.colormatching.main.domain.model.ColorGrid
import dev.toufikforyou.colormatching.main.utils.MemoryGameUtils
import dev.toufikforyou.colormatching.main.utils.ColorSets
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for MemoryGameUtils functions
 */
class MemoryGameUtilsTest {

    @Test
    fun generateColorGrid_validInputs_createsCorrectGrid() {
        val colors = listOf("Red", "Blue", "Green")
        val grid = MemoryGameUtils.generateColorGrid(3, colors)
        
        assertEquals(3, grid.size)
        assertEquals(9, grid.cells.size)
        
        // All cells should contain colors from the provided list
        grid.cells.forEach { cell ->
            assertTrue("Cell color '$cell' not in provided colors", colors.contains(cell))
        }
    }

    @Test
    fun generateColorGrid_invalidGridSize_throwsException() {
        val colors = listOf("Red", "Blue")
        
        try {
            MemoryGameUtils.generateColorGrid(2, colors)
            fail("Should throw exception for invalid grid size")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Grid size must be 3, 4, or 5"))
        }
    }

    @Test
    fun generateColorGrid_emptyColorList_throwsException() {
        try {
            MemoryGameUtils.generateColorGrid(3, emptyList())
            fail("Should throw exception for empty color list")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Color list cannot be empty"))
        }
    }

    @Test
    fun generateBalancedColorGrid_createsEvenDistribution() {
        val colors = listOf("Red", "Blue", "Green")
        val grid = MemoryGameUtils.generateBalancedColorGrid(3, colors)
        
        assertEquals(3, grid.size)
        assertEquals(9, grid.cells.size)
        
        // Count occurrences of each color - should be balanced (3, 3, 3)
        val colorCounts = colors.associateWith { color ->
            grid.cells.count { it == color }
        }
        
        // For 9 cells and 3 colors, each should appear 3 times
        colorCounts.values.forEach { count ->
            assertEquals(3, count)
        }
    }

    @Test
    fun generateBalancedColorGrid_unevenDistribution_handlesRemainder() {
        val colors = listOf("Red", "Blue", "Green", "Yellow")
        val grid = MemoryGameUtils.generateBalancedColorGrid(3, colors)
        
        assertEquals(9, grid.cells.size)
        
        val colorCounts = colors.associateWith { color ->
            grid.cells.count { it == color }
        }
        
        // For 9 cells and 4 colors: should be distributed as 3,2,2,2 or similar
        val totalCells = colorCounts.values.sum()
        assertEquals(9, totalCells)
        
        // No color should appear more than 3 times or less than 2 times
        colorCounts.values.forEach { count ->
            assertTrue("Color count $count should be between 2 and 3", count in 2..3)
        }
    }

    @Test
    fun convertToStaticMode_returnsIdenticalGrid() {
        val originalGrid = ColorGrid(3, listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green"))
        val staticGrid = MemoryGameUtils.convertToStaticMode(originalGrid)
        
        assertEquals(originalGrid.size, staticGrid.size)
        assertEquals(originalGrid.cells, staticGrid.cells)
        // Ensure it's a copy, not the same reference
        assertNotSame(originalGrid, staticGrid)
    }

    @Test
    fun compareGrids_identicalGrids_returnsTrue() {
        val grid1 = ColorGrid(3, listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green"))
        val grid2 = ColorGrid(3, listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green"))
        
        assertTrue(MemoryGameUtils.compareGrids(grid1, grid2))
    }

    @Test
    fun compareGrids_differentGrids_returnsFalse() {
        val grid1 = ColorGrid(3, listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green"))
        val grid2 = ColorGrid(3, listOf("Blue", "Red", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green"))
        
        assertFalse(MemoryGameUtils.compareGrids(grid1, grid2))
    }

    @Test
    fun compareGrids_differentSizes_returnsFalse() {
        val grid1 = ColorGrid(3, listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green"))
        val grid2 = ColorGrid(4, List(16) { "Red" })
        
        assertFalse(MemoryGameUtils.compareGrids(grid1, grid2))
    }

    @Test
    fun compareGridsDetailed_identicalGrids_returnsCorrectResult() {
        val grid1 = ColorGrid(3, listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green"))
        val grid2 = ColorGrid(3, listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green"))
        
        val result = MemoryGameUtils.compareGridsDetailed(grid1, grid2)
        
        assertTrue(result.isExactMatch)
        assertEquals(9, result.correctCells)
        assertEquals(9, result.totalCells)
        assertEquals(1.0, result.accuracy, 0.001)
        assertTrue(result.incorrectPositions.isEmpty())
    }

    @Test
    fun compareGridsDetailed_partiallyCorrect_returnsCorrectResult() {
        val grid1 = ColorGrid(3, listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green"))
        val grid2 = ColorGrid(3, listOf("Red", "Yellow", "Green", "Red", "Blue", "Purple", "Red", "Blue", "Green"))
        
        val result = MemoryGameUtils.compareGridsDetailed(grid1, grid2)
        
        assertFalse(result.isExactMatch)
        assertEquals(6, result.correctCells)
        assertEquals(9, result.totalCells)
        assertEquals(6.0/9.0, result.accuracy, 0.001)
        assertEquals(listOf(1, 5), result.incorrectPositions)
    }

    @Test
    fun createEmptyGrid_createsCorrectGrid() {
        val grid = MemoryGameUtils.createEmptyGrid(4, "Empty")
        
        assertEquals(4, grid.size)
        assertEquals(16, grid.cells.size)
        grid.cells.forEach { cell ->
            assertEquals("Empty", cell)
        }
    }

    @Test
    fun createEmptyGrid_defaultPlaceholder_usesDefault() {
        val grid = MemoryGameUtils.createEmptyGrid(3)
        
        assertEquals(3, grid.size)
        assertEquals(9, grid.cells.size)
        grid.cells.forEach { cell ->
            assertEquals("Empty", cell)
        }
    }

    @Test
    fun updateGridCell_byRowCol_updatesCorrectCell() {
        val originalGrid = ColorGrid(3, listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green"))
        val updatedGrid = MemoryGameUtils.updateGridCell(originalGrid, 1, 1, "Yellow")
        
        assertEquals("Yellow", updatedGrid.getColor(1, 1))
        assertEquals("Red", updatedGrid.getColor(0, 0)) // Other cells unchanged
        assertEquals(originalGrid.size, updatedGrid.size)
    }

    @Test
    fun updateGridCell_byIndex_updatesCorrectCell() {
        val originalGrid = ColorGrid(3, listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green"))
        val updatedGrid = MemoryGameUtils.updateGridCell(originalGrid, 4, "Yellow")
        
        assertEquals("Yellow", updatedGrid.getColor(4))
        assertEquals("Red", updatedGrid.getColor(0)) // Other cells unchanged
        assertEquals(originalGrid.size, updatedGrid.size)
    }

    @Test
    fun updateGridCell_invalidRowCol_throwsException() {
        val grid = ColorGrid(3, List(9) { "Red" })
        
        try {
            MemoryGameUtils.updateGridCell(grid, 3, 0, "Blue")
            fail("Should throw exception for invalid row")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Row must be between 0 and 2"))
        }
    }

    @Test
    fun updateGridCell_invalidIndex_throwsException() {
        val grid = ColorGrid(3, List(9) { "Red" })
        
        try {
            MemoryGameUtils.updateGridCell(grid, 9, "Blue")
            fail("Should throw exception for invalid index")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Index must be between 0 and 8"))
        }
    }
}