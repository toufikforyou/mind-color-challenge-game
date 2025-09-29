# Memory-Based Color Challenge Game API

This document describes the new memory-based color challenge game functions implemented for the Mind Color Challenge Game.

## Overview

The implementation provides a complete memory-based color challenge game system with:
- 2D grid generation with customizable sizes (3x3, 4x4, 5x5)
- Custom color lists using simple string names
- Grid comparison and validation functions
- Modular, extensible design for future enhancements

## Core Components

### 1. ColorGrid Data Model

```kotlin
data class ColorGrid(
    val size: Int,        // Grid dimensions (3, 4, or 5)
    val cells: List<String> // Color names in row-major order
)
```

**Methods:**
- `getColor(row: Int, col: Int): String` - Get color by coordinates
- `getColor(index: Int): String` - Get color by linear index
- `to2D(): List<List<String>>` - Convert to 2D representation

### 2. MemoryGameUtils

Main utility object with core game functions:

#### Grid Generation
```kotlin
// Generate random color grid
fun generateColorGrid(gridSize: Int, colors: List<String>): ColorGrid

// Generate balanced color distribution
fun generateBalancedColorGrid(gridSize: Int, colors: List<String>): ColorGrid

// Create empty grid for user input
fun createEmptyGrid(gridSize: Int, placeholderColor: String = "Empty"): ColorGrid
```

#### Grid Operations
```kotlin
// Update specific cell
fun updateGridCell(grid: ColorGrid, row: Int, col: Int, newColor: String): ColorGrid
fun updateGridCell(grid: ColorGrid, index: Int, newColor: String): ColorGrid

// Convert dynamic to static mode
fun convertToStaticMode(dynamicGrid: ColorGrid): ColorGrid
```

#### Grid Comparison
```kotlin
// Simple comparison
fun compareGrids(originalGrid: ColorGrid, userAttempt: ColorGrid): Boolean

// Detailed comparison with metrics
fun compareGridsDetailed(originalGrid: ColorGrid, userAttempt: ColorGrid): GridComparisonResult
```

### 3. ColorSets

Predefined color collections for different game modes:

```kotlin
object ColorSets {
    val BASIC_COLORS: List<String>      // Primary colors (6 colors)
    val EXTENDED_COLORS: List<String>   // More variety (12 colors)
    val PASTEL_COLORS: List<String>     // Soft colors
    val VIBRANT_COLORS: List<String>    // High contrast colors
    val EARTH_TONES: List<String>       // Natural colors
    
    // Smart color selection
    fun getColorSetForDifficulty(difficulty: Int): List<String>
    fun getColorSetForGridSize(gridSize: Int): List<String>
    fun getAllColorSets(): Map<String, List<String>>
}
```

### 4. GameModeIntegration

Bridge between new memory game and existing ColorBox system:

```kotlin
object GameModeIntegration {
    // Convert between systems
    fun convertToColorBoxes(colorGrid: ColorGrid): List<ColorBox>
    fun convertFromColorBoxes(colorBoxes: List<ColorBox>, gridSize: Int): ColorGrid
    
    // Game session management
    fun createMemoryGameSession(gridSize: Int, colorSet: List<String>, memorizeTime: Int): MemoryGameSession
    fun processUserSelection(session: MemoryGameSession, row: Int, col: Int, selectedColor: String): MemoryGameSession
    fun startPlayPhase(session: MemoryGameSession): MemoryGameSession
    
    // Utility functions
    fun getColorOptions(session: MemoryGameSession): List<String>
    fun isValidColor(colorName: String): Boolean
    fun getColorFromName(colorName: String): Color
}
```

## Usage Examples

### Basic Grid Generation

```kotlin
// Create a 3x3 grid with custom colors
val colors = listOf("Red", "Blue", "Green", "Yellow")
val grid = MemoryGameUtils.generateColorGrid(3, colors)

println("Generated grid:")
grid.to2D().forEach { row ->
    println(row.joinToString(" | "))
}
```

### Balanced Color Distribution

```kotlin
// Create 4x4 grid with even color distribution
val colors = ColorSets.getColorSetForGridSize(4)
val balancedGrid = MemoryGameUtils.generateBalancedColorGrid(4, colors)

// Check distribution
val distribution = colors.associateWith { color ->
    balancedGrid.cells.count { it == color }
}
println("Color distribution: $distribution")
```

### Memory Game Session

```kotlin
// Create complete memory game session
val session = GameModeIntegration.createMemoryGameSession(
    gridSize = 3,
    colorSet = ColorSets.BASIC_COLORS,
    memorizeTime = 5
)

// Start play phase after memorization
val playSession = GameModeIntegration.startPlayPhase(session)

// Process user selection
val updatedSession = GameModeIntegration.processUserSelection(
    playSession, row = 0, col = 0, selectedColor = "Red"
)

// Check results
println("Accuracy: ${(updatedSession.getAccuracy() * 100).toInt()}%")
println("Perfect match: ${updatedSession.isPerfect()}")
```

### Grid Comparison

```kotlin
val originalGrid = ColorGrid(3, listOf("Red", "Blue", "Green", /*...*/ ))
val userAttempt = ColorGrid(3, listOf("Red", "Yellow", "Green", /*...*/ ))

// Simple comparison
val isExactMatch = MemoryGameUtils.compareGrids(originalGrid, userAttempt)

// Detailed comparison
val result = MemoryGameUtils.compareGridsDetailed(originalGrid, userAttempt)
println("Accuracy: ${result.accuracy}")
println("Correct cells: ${result.correctCells}/${result.totalCells}")
println("Incorrect positions: ${result.incorrectPositions}")
```

## Color System

The implementation uses simple string names instead of hex codes:

**Basic Colors:** Red, Blue, Green, Yellow, Orange, Purple
**Extended Colors:** All basic plus Pink, Brown, Gray, Cyan, Magenta, Lime
**Pastel Colors:** Light Pink, Light Blue, Mint, Peach, Lavender, Cream
**Vibrant Colors:** Bright Red, Electric Blue, Neon Green, Hot Pink
**Earth Tones:** Forest Green, Sky Blue, Desert Sand, Ocean Blue

## Integration with Existing Game

The new memory game functions integrate seamlessly with the existing ColorBox-based system:

1. **ColorGrid** objects can be converted to **ColorBox** lists for UI rendering
2. **GameModeIntegration** provides conversion utilities
3. **MemoryGameSession** manages complete game state
4. All functions are compatible with existing grid sizes and UI components

## Error Handling

All functions include proper validation:
- Grid sizes must be 3, 4, or 5
- Cell counts must match grid dimensions
- Color lists cannot be empty
- Row/column indices must be within bounds
- Invalid inputs throw `IllegalArgumentException` with descriptive messages

## Testing

Comprehensive unit tests are provided for all functions:
- `ColorGridTest` - Tests the ColorGrid data model
- `MemoryGameUtilsTest` - Tests all utility functions
- `ColorSetsTest` - Tests color set functionality
- All edge cases and error conditions are covered

## Future Extensibility

The modular design supports easy extension:
- Add new color sets in `ColorSets`
- Extend grid sizes by modifying validation
- Add difficulty modes with different rules
- Implement timers and scoring systems
- Add new comparison metrics in `GridComparisonResult`

## Performance

- All operations are O(n) where n is grid size
- Memory usage is minimal (string-based colors)
- No heavy computations or allocations
- Suitable for real-time game interactions