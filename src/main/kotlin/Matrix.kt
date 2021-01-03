import java.time.LocalDateTime

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
    postfix = "\n}") {
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

fun printMatrixBitwiseOps(matrix: Array<Array<Int>>): String {
    return """
// set bits
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
