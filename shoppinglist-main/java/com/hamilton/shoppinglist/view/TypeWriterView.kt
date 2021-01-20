package com.hamilton.shoppinglist.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.TextView

class TypeWriter : TextView {
    private var mText: CharSequence? = null
    private var mIndex: Int = 0
    private var mDelay: Long = 150 // in ms
    private val mHandler = Handler()
    private val characterAdder = object : Runnable {
        override fun run() {
            text = mText!!.subSequence(0, mIndex++)
            if (mIndex <= mText!!.length) {
                mHandler.postDelayed(this, mDelay)
            }
        }
    }

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    fun animateText(txt: CharSequence) {
        mText = txt
        mIndex = 0
        text = ""
        mHandler.removeCallbacks(characterAdder)
        mHandler.postDelayed(characterAdder, mDelay)
    }

    fun setCharacterDelay(m: Long) {
        mDelay = m
    }
}
