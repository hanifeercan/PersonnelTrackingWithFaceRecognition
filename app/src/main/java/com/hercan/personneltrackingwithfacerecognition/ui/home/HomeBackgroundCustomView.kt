package com.hercan.personneltrackingwithfacerecognition.ui.home

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.hercan.personneltrackingwithfacerecognition.R

class HomeBackgroundCustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = rootView.context.getColor(R.color.green_hornet)
        val width = width.toFloat()
        val height = height.toFloat()
        val oneThirdWidth = width / 4.5
        canvas.drawRect(0f, 0f, oneThirdWidth.toFloat(), height, paint)
    }
}
