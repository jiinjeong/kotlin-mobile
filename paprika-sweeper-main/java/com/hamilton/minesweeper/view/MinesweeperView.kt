package com.hamilton.minesweeper.view

import android.content.Context
import android.graphics.*
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.hamilton.minesweeper.GameActivity
import com.hamilton.minesweeper.R
import com.hamilton.minesweeper.model.MinesweeperModel

class MinesweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paintBackground = Paint()
    private val paintLine = Paint()
    private val numSquare = 5

    private val mineCircle = Paint()
    private val mineText = Paint()
    private val flagCross = Paint()
    var minesFound : Int = 0

    init {
        paintBackground.color = Color.rgb(110, 212, 130)
        paintBackground.style = Paint.Style.FILL

        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 4f

        mineCircle.color = Color.RED
        mineCircle.strokeWidth = 3f

        mineText.color = Color.RED
        flagCross.color = Color.RED
        flagCross.strokeWidth = 5f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mineText.textSize = height / numSquare.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)
        drawGameBoard(canvas)
        drawCurGameStat(canvas)
    }

    private fun drawGameBoard(canvas: Canvas?) {
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        drawHorizontalLines(canvas)
        drawVerticalLines(canvas)
    }

    private fun drawVerticalLines(canvas: Canvas?) {
        for (i in 1..numSquare) {
            canvas?.drawLine(
                (i * width / numSquare).toFloat(), 0f,
                (i * width / numSquare).toFloat(), height.toFloat(),
                paintLine
            )
        }
    }

    private fun drawHorizontalLines(canvas: Canvas?) {
        for (i in 1..numSquare) {
            canvas?.drawLine(
                0f, (i * height / numSquare).toFloat(),
                width.toFloat(), (i * height / numSquare).toFloat(),
                paintLine
            )
        }
    }

    private fun drawCurGameStat(canvas: Canvas?) {
        var end = numSquare - 1
        for (i in 0..end) {
            for (j in 0..end) {
                if (MinesweeperModel.getFlaggedStat(i, j)) {
                    drawAllFlags(i, j, canvas)
                } else if (MinesweeperModel.getClickedStat(i, j)) {
                    drawAllClicks(i, j, canvas)
                }
            }
        }
    }

    private fun drawAllClicks(i: Int, j: Int, canvas: Canvas?) {
        if (MinesweeperModel.getStat(i, j) == 0) {
            drawAdjMines(i, j, canvas)
        } else if (MinesweeperModel.getStat(i, j) == 1) {
            drawMine(i, j, canvas)
        }
    }

    private fun drawAllFlags(i: Int, j: Int, canvas: Canvas?) {
        if (MinesweeperModel.getStat(i, j) == 0) {
            drawFlag(canvas, i, j)
            drawAdjMines(i, j, canvas)
        } else if (MinesweeperModel.getStat(i, j) == 1) {
            drawFlag(canvas, i, j)
        }
    }

    private fun drawMine(i: Int, j: Int, canvas: Canvas?) {
        val centerX = (i * width / numSquare + width / (numSquare * 2)).toFloat()
        val centerY = (j * height / numSquare + height / (numSquare * 2)).toFloat()
        val radius = height / (numSquare * 2)
        canvas?.drawCircle(centerX, centerY, radius.toFloat(), mineCircle)
    }

    private fun drawAdjMines(i: Int, j: Int, canvas: Canvas?) {
        val textX = (i * width / numSquare).toFloat()
        val textY = ((j + 1) * height / numSquare).toFloat()
        canvas?.drawText(MinesweeperModel.getMinesAround(i, j).toString(), textX, textY, mineText)
    }

    private fun drawFlag(canvas: Canvas?, i: Int, j: Int) {
        canvas?.drawLine(
            (i * width / numSquare).toFloat(), (j * height / numSquare).toFloat(),
            ((i + 1) * width / numSquare).toFloat(),
            ((j + 1) * height / numSquare).toFloat(), flagCross
        )
        canvas?.drawLine(
            ((i + 1) * width / numSquare).toFloat(), (j * height / numSquare).toFloat(),
            (i * width / numSquare).toFloat(), ((j + 1) * height / numSquare).toFloat(), flagCross
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_DOWN && !(context as GameActivity).isEnd()) {
            val tX = event.x.toInt() / (width / numSquare)
            val tY = event.y.toInt() / (height / numSquare)

            if ((context as GameActivity).isFlagMode()) {
                flagResult(tX, tY)
            } else if ((context as GameActivity).isTryMode()) {
                tryResult(tX, tY)
            }
            invalidate()
        }
        return true
    }

    private fun tryResult(tX: Int, tY: Int) {
        MinesweeperModel.click(tX, tY)
        invalidate()
        if (tX < numSquare && tY < numSquare && MinesweeperModel.getStat(tX, tY) == 1
        ) {
            Snackbar.make(this, context.getString(R.string.click_paprika), Snackbar.LENGTH_LONG).show()
            (context as GameActivity).endGame()
        } else if (tX < numSquare && tY < numSquare && MinesweeperModel.getStat(tX, tY) == 0) {
            Snackbar.make(this, context.getString(R.string.click_safe), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun flagResult(tX: Int, tY: Int) {
        MinesweeperModel.flag(tX, tY)
        invalidate()
        if (tX < numSquare && tY < numSquare && MinesweeperModel.getStat(tX, tY) == 1
        ) {
            Snackbar.make(this, context.getString(R.string.flag_paprika), Snackbar.LENGTH_LONG).show()
            minesFound += 1
        } else if (tX < numSquare && tY < numSquare && MinesweeperModel.getStat(tX, tY) == 0) {
            Snackbar.make(this, context.getString(R.string.flag_safe), Snackbar.LENGTH_LONG).show()
            (context as GameActivity).endGame()
        }
        if (minesFound == MinesweeperModel.level) {
            winGame()
        }
    }

    private fun winGame() {
        Snackbar.make(this, context.getString(R.string.win), Snackbar.LENGTH_LONG).show()
        (context as GameActivity).endGame()
    }

    public fun resetGame() {
        MinesweeperModel.resetModel()
        invalidate()
    }
}
