/**
 * Prints the header to the command line
 */
fun main() {
    // 8x8 matrix with zeros
    val positions = Array(8) { Array(8) { 0 } }
    var pos = 0
    for (y in 0..4) {
        for (x in y until (7 - y)) {
            positions[y][x] = pos
            println("#define pos${x}_$y $pos")
            pos++
        }
    }
    for (y in 0..4) {
        for (x in y until (7 - y)) {
            positions[x][7 - y] = pos
            println("#define pos${7 - y}_$x $pos")
            pos++
        }
    }
    for (y in 0..4) {
        for (x in y until (7 - y)) {
            positions[7 - y][7 - x] = pos
            println("#define pos${7 - x}_${7 - y} $pos")
            pos++
        }
    }
    for (y in 0..4) {
        for (x in y until (7 - y)) {
            positions[7 - x][y] = pos
            println("#define pos${y}_${7 - x} $pos")
            pos++
        }
    }
    println()
    printMatrix(positions)
    println()
    printMatrixQuadrants(positions)
}

fun printMatrix(matrix: Array<Array<Int>>) {
    println("// to get positions at runtime")
    println(
        matrix.joinToString(prefix = "static unsigned short[8][8] mat_positions = {\n", separator = ",\n", postfix = "\n}") {
            it.joinToString(separator = ", ", prefix = "    {", postfix = "}") { number -> String.format("%02d", number) }
        }
    )
}

fun printMatrixQuadrants(matrix: Array<Array<Int>>) {
    println("// Quadrants:")
    println(
        matrix.joinToString(separator = "\n") {
            it.joinToString(separator = " ", prefix = "// ") { number ->
                when (number / 16) {
                    0 -> "\\/"
                    1 -> "< "
                    2 -> "/\\"
                    3 -> " >"
                    else -> " " // never
                }
            }
        }
    )
}

fun printMatrixBitwiseOps(matrix: Array<Array<Int>>) {
    println("// set bits")
    println(
        matrix.joinToString(separator = "\n") {
            it.joinToString(separator = " ", prefix = "// ") { number ->
                when (number / 16) {
                    0 -> "\\/"
                    1 -> "< "
                    2 -> "/\\"
                    3 -> " >"
                    else -> " " // never
                }
            }
        }
    )
}
