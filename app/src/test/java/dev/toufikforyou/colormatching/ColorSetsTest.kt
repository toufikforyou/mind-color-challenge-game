package dev.toufikforyou.colormatching

import dev.toufikforyou.colormatching.main.utils.ColorSets
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for ColorSets utility
 */
class ColorSetsTest {

    @Test
    fun basicColors_containsExpectedColors() {
        val basicColors = ColorSets.BASIC_COLORS
        
        assertEquals(6, basicColors.size)
        assertTrue(basicColors.contains("Red"))
        assertTrue(basicColors.contains("Blue"))
        assertTrue(basicColors.contains("Green"))
        assertTrue(basicColors.contains("Yellow"))
        assertTrue(basicColors.contains("Orange"))
        assertTrue(basicColors.contains("Purple"))
    }

    @Test
    fun extendedColors_containsBasicAndMore() {
        val extendedColors = ColorSets.EXTENDED_COLORS
        val basicColors = ColorSets.BASIC_COLORS
        
        assertTrue(extendedColors.size > basicColors.size)
        basicColors.forEach { color ->
            assertTrue("Extended colors should contain basic color: $color", 
                extendedColors.contains(color))
        }
    }

    @Test
    fun pastelColors_containsExpectedColors() {
        val pastelColors = ColorSets.PASTEL_COLORS
        
        assertTrue(pastelColors.isNotEmpty())
        // Pastel colors should contain "Light" prefix or soft color names
        val hasLightColors = pastelColors.any { it.startsWith("Light") }
        val hasSoftColors = pastelColors.any { 
            it in listOf("Mint", "Peach", "Lavender", "Cream") 
        }
        assertTrue("Pastel colors should contain light or soft colors", 
            hasLightColors || hasSoftColors)
    }

    @Test
    fun vibrantColors_containsExpectedColors() {
        val vibrantColors = ColorSets.VIBRANT_COLORS
        
        assertTrue(vibrantColors.isNotEmpty())
        // Vibrant colors should contain intensity words
        val hasIntenseColors = vibrantColors.any { color ->
            color.contains("Bright") || color.contains("Electric") || 
            color.contains("Neon") || color.contains("Hot") || 
            color.contains("Deep")
        }
        assertTrue("Vibrant colors should contain intense color names", hasIntenseColors)
    }

    @Test
    fun earthTones_containsNaturalColors() {
        val earthTones = ColorSets.EARTH_TONES
        
        assertTrue(earthTones.isNotEmpty())
        // Earth tones should contain nature-related words
        val hasNaturalColors = earthTones.any { color ->
            color.contains("Forest") || color.contains("Sky") || 
            color.contains("Sunset") || color.contains("Desert") ||
            color.contains("Ocean") || color.contains("Mountain") ||
            color.contains("Autumn") || color.contains("Grass")
        }
        assertTrue("Earth tones should contain nature-related colors", hasNaturalColors)
    }

    @Test
    fun getColorSetForDifficulty_returnsCorrectSets() {
        val easy = ColorSets.getColorSetForDifficulty(1)
        val medium = ColorSets.getColorSetForDifficulty(2)
        val hard = ColorSets.getColorSetForDifficulty(3)
        val invalid = ColorSets.getColorSetForDifficulty(0)
        
        assertEquals(ColorSets.BASIC_COLORS, easy)
        assertEquals(ColorSets.EXTENDED_COLORS.take(9), medium)
        assertEquals(ColorSets.EXTENDED_COLORS, hard)
        assertEquals(ColorSets.BASIC_COLORS, invalid) // Default to basic for invalid input
        
        // Verify progression: easy <= medium <= hard
        assertTrue(easy.size <= medium.size)
        assertTrue(medium.size <= hard.size)
    }

    @Test
    fun getColorSetForGridSize_returnsAppropriateSize() {
        val colors3x3 = ColorSets.getColorSetForGridSize(3)
        val colors4x4 = ColorSets.getColorSetForGridSize(4)
        val colors5x5 = ColorSets.getColorSetForGridSize(5)
        
        assertEquals(4, colors3x3.size)
        assertEquals(6, colors4x4.size)
        assertEquals(8, colors5x5.size)
        
        // All should be subsets of EXTENDED_COLORS
        colors3x3.forEach { color ->
            assertTrue("Color $color should be in extended colors", 
                ColorSets.EXTENDED_COLORS.contains(color))
        }
        colors4x4.forEach { color ->
            assertTrue("Color $color should be in extended colors", 
                ColorSets.EXTENDED_COLORS.contains(color))
        }
        colors5x5.forEach { color ->
            assertTrue("Color $color should be in extended colors", 
                ColorSets.EXTENDED_COLORS.contains(color))
        }
    }

    @Test
    fun getColorSetForGridSize_invalidSize_returnsDefault() {
        val colorsInvalid = ColorSets.getColorSetForGridSize(2)
        assertEquals(6, colorsInvalid.size)
    }

    @Test
    fun getAllColorSets_returnsAllSets() {
        val allSets = ColorSets.getAllColorSets()
        
        assertEquals(5, allSets.size)
        assertTrue(allSets.containsKey("Basic"))
        assertTrue(allSets.containsKey("Extended"))
        assertTrue(allSets.containsKey("Pastel"))
        assertTrue(allSets.containsKey("Vibrant"))
        assertTrue(allSets.containsKey("Earth Tones"))
        
        assertEquals(ColorSets.BASIC_COLORS, allSets["Basic"])
        assertEquals(ColorSets.EXTENDED_COLORS, allSets["Extended"])
        assertEquals(ColorSets.PASTEL_COLORS, allSets["Pastel"])
        assertEquals(ColorSets.VIBRANT_COLORS, allSets["Vibrant"])
        assertEquals(ColorSets.EARTH_TONES, allSets["Earth Tones"])
    }

    @Test
    fun colorSets_noEmptySets() {
        val allSets = ColorSets.getAllColorSets()
        
        allSets.values.forEach { colorSet ->
            assertTrue("Color set should not be empty", colorSet.isNotEmpty())
        }
    }

    @Test
    fun colorSets_noDuplicatesWithinSets() {
        val allSets = ColorSets.getAllColorSets()
        
        allSets.forEach { (name, colorSet) ->
            val uniqueColors = colorSet.toSet()
            assertEquals("Color set $name should not have duplicates", 
                colorSet.size, uniqueColors.size)
        }
    }

    @Test
    fun colorSets_allColorsAreStrings() {
        val allSets = ColorSets.getAllColorSets()
        
        allSets.values.forEach { colorSet ->
            colorSet.forEach { color ->
                assertTrue("Color should be non-empty string", color.isNotBlank())
                assertTrue("Color should be properly formatted", 
                    color.trim() == color) // No leading/trailing whitespace
            }
        }
    }
}