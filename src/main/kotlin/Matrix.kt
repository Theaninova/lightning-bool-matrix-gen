import java.time.LocalDateTime

/**
 * Prints the header to the command line
 */
@kotlin.ExperimentalUnsignedTypes
@kotlin.ExperimentalStdlibApi
fun main() {
    // 8x8 matrix with zeros
    val positions = Array(8) { Array(8) { 0 } }
    var pos = 0
    for (y in 0..4) {
        for (x in y until (7 - y)) {
            positions[y][x] = pos
            pos++
        }
    }
    for (y in 0..4) {
        for (x in y until (7 - y)) {
            positions[x][7 - y] = pos
            pos++
        }
    }
    for (y in 0..4) {
        for (x in y until (7 - y)) {
            positions[7 - y][7 - x] = pos
            pos++
        }
    }
    for (y in 0..4) {
        for (x in y until (7 - y)) {
            positions[7 - x][y] = pos
            pos++
        }
    }

    val header = """
//
// Generated on ${LocalDateTime.now()}
//

#ifndef UNTITLED4_LIGHTNING_MATRIX_H
#define UNTITLED4_LIGHTNING_MATRIX_H

/**
 * x86_64 64 bit bitwise rotate right
 */
static inline unsigned long long rorq(unsigned long long var, unsigned short amount) {
    __asm__("rorq %%cl,%0" : "=r" (var) : "0" (var), "c" (amount));
    return var;
}

/**
 * x86_64 64 bit bitwise rotate left
 */
static inline unsigned long long rolq(unsigned long long var, unsigned short amount) {
    __asm__("rolq %%cl,%0" : "=r" (var) : "0" (var), "c" (amount));
    return var;
}

${printMatrixQuadrants(positions)}

${printMatrix(positions)}

${printMatrixBitwiseOpsDocs(positions)}

${printMatrixBitwiseOpsSet(positions)}

${printMatrixBitwiseOpsUnset(positions)}

#define set_true(matrix, x, y) (matrix | mat_set_masks[x][y])
#define set_false(matrix, x, y) (matrix & mat_unset_masks[x][y])
#define get(matrix, x, y) (matrix & mat_set_masks[x][y])

#endif //UNTITLED4_LIGHTNING_MATRIX_H
    """

    println(header)
}

fun printMatrix(matrix: Array<Array<Int>>): String {
    return """
// to get positions at runtime"
${matrix.joinToString(
    prefix = "static unsigned short mat_positions[8][8] = {\n",
    separator = ",\n",
    postfix = "\n};") {
        it.joinToString(separator = ", ", prefix = "    {", postfix = "}") { number -> number.toString().padStart(2, ' ')}
}}
    """
}

fun printMatrixQuadrants(matrix: Array<Array<Int>>): String {
    return """
// Quadrants:
${matrix.joinToString(separator = "\n") {
    it.joinToString(separator = " ", prefix = "// ") { number ->
        when (number / 16) {
            0 -> "\\/"
            1 -> "< "
            2 -> "/\\"
            3 -> " >"
            else -> " " // never
        }
    }
}}
    """
}

@kotlin.ExperimentalUnsignedTypes
@kotlin.ExperimentalStdlibApi
fun printMatrixBitwiseOpsSet(matrix: Array<Array<Int>>): String {
    return """
// for setting a bit to true using `matrix | mat_set_masks[x][y]`
${matrix.joinToString(
        prefix = "static unsigned long long mat_set_masks[8][8] = {\n",
        separator = ",\n",
        postfix = "\n};") {
        it.joinToString(separator = ", ", prefix = "    {", postfix = "}") { number -> "0x${(1u.toULong().rotateLeft(number)).toString(16).padEnd(16, ' ')}"}
    }}
    """
}

@kotlin.ExperimentalUnsignedTypes
@kotlin.ExperimentalStdlibApi
fun printMatrixBitwiseOpsUnset(matrix: Array<Array<Int>>): String {
    return """
// for setting a bit to false using `matrix & mat_unset_masks[x][y]`
${matrix.joinToString(
        prefix = "static unsigned long long mat_unset_masks[8][8] = {\n",
        separator = ",\n",
        postfix = "\n};") {
        it.joinToString(separator = ", ", prefix = "    {", postfix = "}") { number -> "0x${(1u.toULong().rotateLeft(number).inv()).toString(16).padEnd(16, ' ')}"}
    }}
    """
}

@kotlin.ExperimentalUnsignedTypes
@kotlin.ExperimentalStdlibApi
fun printMatrixBitwiseOpsDocs(matrix: Array<Array<Int>>): String {
    return """
// mask positions
${matrix.joinToString(
        separator = "\n") {
        it.joinToString(separator = " ", prefix = "// ") { number -> (1u.toULong().rotateLeft(number)).toString(2).padStart(64, '0') }
    }}
    """
}
