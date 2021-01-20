package com.hamilton.minesweeper

data class Field(var type: Int, var status: Int, var minesAround: Int,
                 var isFlagged: Boolean, var wasClicked: Boolean)