package org.fuusio.api.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class FlowLayout : ViewGroup {
    private var lineHeightSpace = 0

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        require(MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED)
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        var height = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom
        val count = childCount
        var lineHeightSpace = 0
        var x = paddingLeft
        var y = paddingTop
        val childHeightMeasureSpec =
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST)
            } else {
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            }

        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val params = child.layoutParams as LayoutParams
                child.measure(
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                    childHeightMeasureSpec
                )
                val childWidth = child.measuredWidth
                lineHeightSpace =
                    lineHeightSpace.coerceAtLeast(child.measuredHeight + params.vertical_spacing)
                if (x + childWidth > width) {
                    x = paddingLeft
                    y += lineHeightSpace
                }
                x += childWidth + params.horizontal_spacing
            }
        }
        this.lineHeightSpace = lineHeightSpace

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = y + lineHeightSpace
        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            if (y + lineHeightSpace < height) height = y + lineHeightSpace
        }
        setMeasuredDimension(width, height)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(1, 1) // default of 1px spacing
    }

    override fun checkLayoutParams(params: ViewGroup.LayoutParams) = params is LayoutParams

    override fun onLayout(
        isChanged: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        val count = childCount
        val width = right - left
        var x = paddingLeft
        var y = paddingTop

        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                val params = child.layoutParams as LayoutParams
                if (x + childWidth > width) {
                    x = paddingLeft
                    y += lineHeightSpace
                }
                child.layout(x, y, x + childWidth, y + childHeight)
                x += childWidth + params.horizontal_spacing
            }
        }
    }

    class LayoutParams(var horizontal_spacing: Int, var vertical_spacing: Int) :
        ViewGroup.LayoutParams(0, 0)
}