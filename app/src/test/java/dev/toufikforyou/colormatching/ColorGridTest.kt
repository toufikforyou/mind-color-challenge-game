package dev.toufikforyou.colormatching

import dev.toufikforyou.colormatching.main.domain.model.ColorGrid
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for ColorGrid data class
 */
class ColorGridTest {

    @Test
    fun colorGrid_validInputs_createsCorrectly() {
        val colors = listOf("Red", "Blue", "Green", "Yellow", "Orange", "Purple", "Pink", "Brown", "Gray")
        val grid = ColorGrid(3, colors)
        
        assertEquals(3, grid.size)
        assertEquals(9, grid.cells.size)
        assertEquals(colors, grid.cells)
    }

    @Test
    fun colorGrid_invalidSize_throwsException() {
        val colors = List(9) { "Red" }
        
        try {
            ColorGrid(2, colors)
            fail("Should throw exception for invalid size")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Grid size must be 3, 4, or 5"))
        }
    }

    @Test
    fun colorGrid_wrongCellCount_throwsException() {
        val colors = listOf("Red", "Blue") // Only 2 colors for 3x3 grid
        
        try {
            ColorGrid(3, colors)
            fail("Should throw exception for wrong cell count")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Cell count must match grid size (9)"))
        }
    }

    @Test
    fun getColor_byRowCol_returnsCorrectColor() {
        val colors = listOf("Red", "Blue", "Green", "Yellow", "Orange", "Purple", "Pink", "Brown", "Gray")
        val grid = ColorGrid(3, colors)
        
        assertEquals("Red", grid.getColor(0, 0))
        assertEquals("Blue", grid.getColor(0, 1))
        assertEquals("Green", grid.getColor(0, 2))
        assertEquals("Yellow", grid.getColor(1, 0))
        assertEquals("Orange", grid.getColor(1, 1))
        assertEquals("Purple", grid.getColor(1, 2))
        assertEquals("Pink", grid.getColor(2, 0))
        assertEquals("Brown", grid.getColor(2, 1))
        assertEquals("Gray", grid.getColor(2, 2))
    }

    @Test
    fun getColor_byIndex_returnsCorrectColor() {
        val colors = listOf("Red", "Blue", "Green", "Yellow", "Orange", "Purple", "Pink", "Brown", "Gray")
        val grid = ColorGrid(3, colors)
        
        for (i in colors.indices) {
            assertEquals(colors[i], grid.getColor(i))
        }
    }

    @Test
    fun getColor_invalidRowCol_throwsException() {
        val grid = ColorGrid(3, List(9) { "Red" })
        
        try {
            grid.getColor(3, 0)
            fail("Should throw exception for invalid row")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Row must be between 0 and 2"))
        }
        
        try {
            grid.getColor(0, 3)
            fail("Should throw exception for invalid column")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Column must be between 0 and 2"))
        }
    }

    @Test
    fun getColor_invalidIndex_throwsException() {
        val grid = ColorGrid(3, List(9) { "Red" })
        
        try {
            grid.getColor(9)
            fail("Should throw exception for invalid index")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Index must be between 0 and 8"))
        }
        
        try {
            grid.getColor(-1)
            fail("Should throw exception for negative index")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Index must be between 0 and 8"))
        }
    }

    @Test
    fun to2D_convertsCorrectly() {
        val colors = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I")
        val grid = ColorGrid(3, colors)
        
        val expected = listOf(
            listOf("A", "B", "C"),
            listOf("D", "E", "F"),
            listOf("G", "H", "I")
        )
        
        assertEquals(expected, grid.to2D())
    }

    @Test
    fun to2D_4x4Grid_convertsCorrectly() {
        val colors = (1..16).map { it.toString() }
        val grid = ColorGrid(4, colors)
        
        val expected = listOf(
            listOf("1", "2", "3", "4"),
            listOf("5", "6", "7", "8"),
            listOf("9", "10", "11", "12"),
            listOf("13", "14", "15", "16")
        )
        
        assertEquals(expected, grid.to2D())
    }

    @Test
    fun to2D_5x5Grid_convertsCorrectly() {
        val colors = (1..25).map { it.toString() }
        val grid = ColorGrid(5, colors)
        
        val result = grid.to2D()
        assertEquals(5, result.size) // 5 rows
        result.forEach { row ->
            assertEquals(5, row.size) // 5 columns each
        }
        
        // Check first and last rows
        assertEquals(listOf("1", "2", "3", "4", "5"), result[0])
        assertEquals(listOf("21", "22", "23", "24", "25"), result[4])
    }

    @Test
    fun colorGrid_equality_worksCorrectly() {
        val colors1 = listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green")
        val colors2 = listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green")
        val colors3 = listOf("Blue", "Red", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green")
        
        val grid1 = ColorGrid(3, colors1)
        val grid2 = ColorGrid(3, colors2)
        val grid3 = ColorGrid(3, colors3)
        
        assertEquals(grid1, grid2)
        assertNotEquals(grid1, grid3)
    }

    @Test
    fun colorGrid_copy_worksCorrectly() {
        val colors = listOf("Red", "Blue", "Green", "Red", "Blue", "Green", "Red", "Blue", "Green")
        val grid1 = ColorGrid(3, colors)
        val grid2 = grid1.copy()
        
        assertEquals(grid1, grid2)
        assertNotSame(grid1, grid2)
    }
}