package dev.toufikforyou.colormatching.main.utils

/**
 * Example usage of the memory-based color challenge game functions
 * This file demonstrates how to use all the implemented components together
 */
object MemoryGameExample {
    
    /**
     * Complete example of setting up and playing a memory-based color challenge game
     */
    fun demonstrateFullGameFlow() {
        println("Memory-Based Color Challenge Game - Complete Flow")
        println("===============================================")
        
        // Step 1: Choose difficulty and get appropriate colors
        val difficulty = 2 // Medium difficulty
        val colorSet = ColorSets.getColorSetForDifficulty(difficulty)
        println("1. Selected difficulty $difficulty with colors: ${colorSet.joinToString(", ")}")
        
        // Step 2: Generate the game grid
        val gridSize = 4 // 4x4 grid for medium difficulty
        val originalGrid = MemoryGameUtils.generateBalancedColorGrid(gridSize, colorSet)
        println("\n2. Generated $gridSize×$gridSize grid:")
        originalGrid.to2D().forEachIndexed { row, colors ->
            println("   Row $row: ${colors.joinToString(" | ")}")
        }
        
        // Step 3: Create memory game session
        val session = GameModeIntegration.createMemoryGameSession(
            gridSize = gridSize,
            colorSet = colorSet,
            memorizeTime = 5
        )
        println("\n3. Created memory game session (${session.memorizeTimeSeconds}s memorize time)")
        
        // Step 4: Show available colors for user selection
        val availableColors = GameModeIntegration.getColorOptions(session)
        println("4. Available colors for selection: ${availableColors.joinToString(", ")}")
        
        // Step 5: Simulate memorize phase ending
        val playSession = GameModeIntegration.startPlayPhase(session)
        println("\n5. Memorize phase complete - starting play phase")
        
        // Step 6: Simulate user making some selections
        var currentSession = playSession
        
        // User tries to recreate the first row
        val firstRow = originalGrid.to2D()[0]
        println("\n6. User attempting to recreate first row: ${firstRow.joinToString(", ")}")
        
        firstRow.forEachIndexed { col, color ->
            currentSession = GameModeIntegration.processUserSelection(
                currentSession, 0, col, color
            )
        }
        
        // Show partial progress
        println("   Current user grid after first row:")
        currentSession.userGrid.to2D().forEachIndexed { row, colors ->
            println("   Row $row: ${colors.joinToString(" | ")}")
        }
        
        println("   Accuracy so far: ${(currentSession.getAccuracy() * 100).toInt()}%")
        println("   Correct cells: ${currentSession.getCorrectCells()}/${gridSize * gridSize}")
        
        // Step 7: Fill remaining cells (simulate user guessing)
        for (row in 1 until gridSize) {
            for (col in 0 until gridSize) {
                // Simulate user guessing (50% correct, 50% random)
                val correctColor = originalGrid.getColor(row, col)
                val guessColor = if (kotlin.random.Random.nextBoolean()) {
                    correctColor // Correct guess
                } else {
                    availableColors.random() // Random guess
                }
                
                currentSession = GameModeIntegration.processUserSelection(
                    currentSession, row, col, guessColor
                )
            }
        }
        
        // Step 8: Show final results
        println("\n7. Game completed!")
        println("   Final user grid:")
        currentSession.userGrid.to2D().forEachIndexed { row, colors ->
            println("   Row $row: ${colors.joinToString(" | ")}")
        }
        
        println("\n8. Final Results:")
        println("   Perfect match: ${currentSession.isPerfect()}")
        println("   Final accuracy: ${(currentSession.getAccuracy() * 100).toInt()}%")
        println("   Correct cells: ${currentSession.getCorrectCells()}/${gridSize * gridSize}")
        println("   Final score: ${currentSession.score}")
        
        // Step 9: Show detailed comparison
        val detailedComparison = MemoryGameUtils.compareGridsDetailed(
            currentSession.originalGrid, 
            currentSession.userGrid
        )
        println("\n9. Detailed Analysis:")
        println("   Exact match: ${detailedComparison.isExactMatch}")
        println("   Accuracy: ${(detailedComparison.accuracy * 100).toInt()}%")
        println("   Incorrect positions: ${detailedComparison.incorrectPositions}")
        
        if (detailedComparison.incorrectPositions.isNotEmpty()) {
            println("   Mistakes:")
            detailedComparison.incorrectPositions.forEach { index ->
                val row = index / gridSize
                val col = index % gridSize
                val expected = originalGrid.getColor(index)
                val actual = currentSession.userGrid.getColor(index)
                println("     Position ($row,$col): Expected '$expected', Got '$actual'")
            }
        }
    }
    
    /**
     * Example showing different grid sizes and color sets
     */
    fun demonstrateDifferentConfigurations() {
        println("\nDifferent Grid Configurations")
        println("============================")
        
        // Test all supported grid sizes
        listOf(3, 4, 5).forEach { size ->
            val colors = ColorSets.getColorSetForGridSize(size)
            val grid = MemoryGameUtils.generateBalancedColorGrid(size, colors)
            
            println("\n${size}×${size} Grid (${colors.size} colors):")
            grid.to2D().forEach { row ->
                println("  ${row.joinToString(" ")}")
            }
            
            // Show color distribution
            val distribution = colors.associateWith { color ->
                grid.cells.count { it == color }
            }
            println("  Distribution: $distribution")
        }
        
        // Test different color sets
        println("\nColor Set Examples:")
        ColorSets.getAllColorSets().forEach { (name, colorSet) ->
            println("$name: ${colorSet.take(5).joinToString(", ")}... (${colorSet.size} total)")
        }
    }
    
    /**
     * Example showing integration with existing ColorBox system
     */
    fun demonstrateColorBoxIntegration() {
        println("\nColorBox Integration Example")
        println("===========================")
        
        // Create a grid with string colors
        val stringGrid = MemoryGameUtils.generateColorGrid(3, ColorSets.BASIC_COLORS)
        println("\nOriginal string-based grid:")
        stringGrid.to2D().forEach { row ->
            println("  ${row.joinToString(", ")}")
        }
        
        // Convert to ColorBox objects for UI
        val colorBoxes = GameModeIntegration.convertToColorBoxes(stringGrid)
        println("\nConverted to ColorBox objects:")
        colorBoxes.chunked(3).forEachIndexed { rowIndex, row ->
            val colorNames = row.map { box ->
                GameModeIntegration.colorNameMap.entries
                    .find { it.value == box.color }?.key ?: "Unknown"
            }
            println("  Row $rowIndex: ${colorNames.joinToString(", ")}")
        }
        
        // Convert back to string grid
        val convertedBack = GameModeIntegration.convertFromColorBoxes(colorBoxes, 3)
        println("\nConverted back to string grid:")
        println("Original == Converted: ${stringGrid.cells == convertedBack.cells}")
    }
}

/**
 * Extension property for easier access to the color name map in integration class
 */
val GameModeIntegration.colorNameMap: Map<String, androidx.compose.ui.graphics.Color>
    get() = mapOf(
        "Red" to androidx.compose.ui.graphics.Color.Red,
        "Blue" to androidx.compose.ui.graphics.Color.Blue,
        "Green" to androidx.compose.ui.graphics.Color.Green,
        "Yellow" to androidx.compose.ui.graphics.Color.Yellow,
        "Orange" to androidx.compose.ui.graphics.Color(0xFFFFA500),
        "Purple" to androidx.compose.ui.graphics.Color(0xFF800080)
    )