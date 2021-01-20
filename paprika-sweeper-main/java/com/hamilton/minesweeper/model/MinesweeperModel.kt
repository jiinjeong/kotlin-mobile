package com.hamilton.minesweeper.model

import com.hamilton.minesweeper.Field
import com.hamilton.minesweeper.GameActivity
import java.util.*

object MinesweeperModel {

    val level = GameActivity.LEVEL
    var minesToAdd = level

    private val fieldMatrix: Array<Array<Field>> = arrayOf(
        arrayOf(
            Field(0, 0, 0, false, false),
            Field(1, 0, 0, false, false),
            Field(2, 0, 0, false, false),
            Field(3, 0, 0, false, false),
            Field(4, 0, 0, false, false)
        ),
        arrayOf(
            Field(5, 0, 0, false, false),
            Field(6, 0, 0, false, false),
            Field(7, 0, 0, false, false),
            Field(8, 0, 0, false, false),
            Field(9, 0, 0, false, false)
        ),
        arrayOf(
            Field(10, 0, 0, false, false),
            Field(11, 0, 0, false, false),
            Field(12, 0, 0, false, false),
            Field(13, 0, 0, false, false),
            Field(14, 0, 0, false, false)
        ),
        arrayOf(
            Field(15, 0, 0, false, false),
            Field(16, 0, 0, false, false),
            Field(17, 0, 0, false, false),
            Field(18, 0, 0, false, false),
            Field(19, 0, 0, false, false)
        ),
        arrayOf(
            Field(20, 0, 0, false, false),
            Field(21, 0, 0, false, false),
            Field(22, 0, 0, false, false),
            Field(23, 0, 0, false, false),
            Field(24, 0, 0, false, false)
        )
    )

    fun generateField() {
        while (minesToAdd > 0) {
            makeMine()
        }
        countAdjacentMines()
    }

    private fun makeMine() {
        val random = Random(System.currentTimeMillis())

        val randomVal = random.nextInt(5)
        val randomVal2 = random.nextInt(5)

        if (!isMine(randomVal, randomVal2)) {
            fieldMatrix[randomVal][randomVal2].status = 1
            minesToAdd -= 1
        }
    }

    private fun countAdjacentMines() {
        for (i in 0..4) {
            for (j in 0..4) {
                if (!isMine(i, j)) {
                    var count = 0
                    for (p in i - 1..i + 1) {
                        for (q in j - 1..j + 1) {
                            if (p in 0..4 && q in 0..4) {
                                if (isMine(p, q)) {
                                    count += 1
                                }
                            }
                        }
                    }
                    fieldMatrix[i][j].minesAround = count
                }
            }
        }
    }

    public fun initializeModel() {
        for (i in 0..4) {
            for (j in 0..4) {
                fieldMatrix[i][j] = Field(i + j, 0, 0, false, false)
            }
        }
    }

    public fun flag(x: Int, y: Int) {
        fieldMatrix[x][y].isFlagged = true
    }

    public fun click(x: Int, y: Int) {
        fieldMatrix[x][y].wasClicked = true
    }

    public fun isMine(x: Int, y: Int) = fieldMatrix[x][y].status == 1
    public fun getMinesAround(x: Int, y: Int) = fieldMatrix[x][y].minesAround
    public fun getStat(x: Int, y: Int) = fieldMatrix[x][y].status
    public fun getFlaggedStat(x: Int, y: Int) = fieldMatrix[x][y].isFlagged
    public fun getClickedStat(x: Int, y: Int) = fieldMatrix[x][y].wasClicked

    public fun resetModel() {
        initializeModel()
        minesToAdd = level
        generateField()
    }
}
