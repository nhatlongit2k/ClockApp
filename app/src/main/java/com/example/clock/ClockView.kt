package com.example.clock

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.lang.Integer.min
import java.lang.Math.cos
import java.util.*
import kotlin.math.sin

class ClockView(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attributeSet, defStyleAttr) {
    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet, 0)
    constructor(context: Context): this(context, null)

    private var mHeight: Int = 0
    private var mWidth: Int = 0

    private val mClockHours = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    public var calendar: Calendar = Calendar.getInstance()

    private var mHandTruncation: Int = 0
    private var mHourHandTruncation: Int = 0

    private var mRadius: Int = 0

    private var mPaint: Paint = Paint()

    private var isInitialized: Boolean = false

    //Drawable
    private var dayPeriodIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_dark_mode_24)
    //Khai bao mau

    private var lightBackgroundColor = ContextCompat.getColor(context, R.color.defaultLightBackgroundColor)
    private var darkBackgroundColor = ContextCompat.getColor(context, R.color.defaultDarkBackgroundColor)
    private var lightShadowColor = ContextCompat.getColor(context, R.color.defaultLightShadowColor)
    private var darkShadowColor = ContextCompat.getColor(context, R.color.defaultDarkShadowColor)
    private var borderColor = ContextCompat.getColor(context, R.color.defaultBoderColor)
    private var minHourHandsColor = ContextCompat.getColor(context, R.color.defaultMinHourHandsColor)
    private var sencondsHandColor = ContextCompat.getColor(context, R.color.defaultSecondHandsColor)
    private var textColor = ContextCompat.getColor(context, R.color.defaultTextColor)
    private var iconColor = ContextCompat.getColor(context, R.color.defaultIconColor)


    // Decalre size variables

    private var iconSize: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32f, context.resources.displayMetrics)
    private var defaultMargin: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, context.resources.displayMetrics)
    private val textFontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, context.resources.displayMetrics)
    private val handSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, context.resources.displayMetrics)

    //Rect for hour text
    private val mRect = Rect()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(!isInitialized)
            initialize()

        canvas?.scale(0.9f, 0.9f, (mWidth/2).toFloat(), (mHeight/2).toFloat())
//        canvas!!.drawColor(Color.RED)
        drawClockShape(canvas)

        //Draw clock icon
//        drawClockPeriod(canvas)

        //Draw hours
        drawNumerals(canvas)

        //Draw hands
        drawHands(canvas)

        //Draw center cicler
        drawCenterCircle(canvas)
    }

    private fun drawCenterCircle(canvas: Canvas?) {
        mPaint.color = sencondsHandColor
        canvas!!.drawCircle(mWidth/2f, mHeight/2f, handSize, mPaint)
    }

    private fun drawHands(canvas: Canvas?) {

//        calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 1)
        val hour = calendar.get(Calendar.HOUR)
        drawHandLine(canvas!!, (hour + calendar.get(Calendar.MINUTE)/60f)*5f, HandType.HOUR)
        drawHandLine(canvas!!, calendar.get(Calendar.MINUTE).toFloat(), HandType.MINUTE)
        drawHandLine(canvas!!, calendar.get(Calendar.SECOND).toFloat(), HandType.SECONDS)

//        postInvalidateDelayed(50000)
        invalidate()
        Thread.sleep(1000)
        mPaint.reset()
    }
    private fun drawHandLine(canvas: Canvas, value: Float, handType: HandType) {
        val angle = Math.PI * value / 30 - Math.PI / 2

        val handRadius = when (handType) {
            HandType.HOUR -> mRadius - mRadius / 3
            HandType.MINUTE -> mRadius - mRadius / 6
            HandType.SECONDS -> mRadius - mRadius / 9
        }

        mPaint.color = if (handType == HandType.SECONDS) sencondsHandColor else minHourHandsColor
        mPaint.strokeWidth = if (handType == HandType.SECONDS) handSize else handSize * 2
        mPaint.strokeCap = Paint.Cap.ROUND

        canvas.drawLine(
            (mWidth / 2).toFloat(),
            (mHeight / 2).toFloat(),
            (mWidth / 2 + kotlin.math.cos(angle) * handRadius).toFloat(),
            (mHeight / 2 + sin(angle) * handRadius).toFloat(),
            mPaint
        )
    }
    enum class HandType{HOUR, MINUTE, SECONDS}

    private fun drawNumerals(canvas: Canvas?) {
        mPaint.textSize = textFontSize
        mPaint.isFakeBoldText = true
        mPaint.color = textColor

        for(hour in mClockHours){
            var tmp = hour.toString()
            mPaint.getTextBounds(tmp, 0, tmp.length, mRect)
            val angle = Math.PI/6 * (hour - 3)
            val x = (mWidth/2+cos(angle) * mRadius - mRect.width()/2).toFloat()
            val y = ((mHeight/2).toDouble() + sin(angle) * mRadius + (mRect.height()/2)).toFloat()

            if(listOf(12, 3, 6, 9).contains(hour)){
                canvas!!.drawText(tmp, x, y, mPaint)
            }else{
                canvas!!.drawText("·", x, y, mPaint)
            }
        }
    }

    private fun drawClockPeriod(canvas: Canvas?) {
        val xPosition: Float = (mWidth/2).toFloat()
        val yPosition: Float = ((mHeight/2) - (mRadius/1.5)).toFloat()

        //draw top
        mPaint.setShadowLayer(4f, 4f, 4f, darkShadowColor)
        canvas!!.drawCircle(xPosition, yPosition, iconSize/2, mPaint)

        // draw bottom
        mPaint.color = darkBackgroundColor
        mPaint.setShadowLayer(4f, -4f, -4f, lightShadowColor)
        canvas!!.drawCircle(xPosition, yPosition, iconSize+defaultMargin/2, mPaint)

        //Draw border
        mPaint.strokeWidth = 4f
        mPaint.style = Paint.Style.STROKE
        mPaint.color = borderColor
        canvas!!.drawCircle(xPosition, yPosition, iconSize+defaultMargin/2, mPaint)

        //Add center icon
        dayPeriodIcon?.setTint(iconColor)
        dayPeriodIcon?.setBounds(
            (xPosition-(iconSize/2)+defaultMargin/2).toInt(),
            (yPosition-(iconSize/2)+defaultMargin/2).toInt(),
            (xPosition+(iconSize/2)-defaultMargin/2).toInt(),
            (yPosition+(iconSize/2)-defaultMargin/2).toInt()

        )
        dayPeriodIcon?.draw(canvas)
    }

    private fun drawClockShape(canvas: Canvas?) {
        //Dra bottom
        mPaint.setShadowLayer(30f, 15f, 15f, darkShadowColor)
        canvas!!.drawCircle((mWidth/2).toFloat(), (mHeight/2).toFloat(), mRadius.toFloat(), mPaint)

        //Draw top
        mPaint.color = lightBackgroundColor
        mPaint.setShadowLayer(30f, -15f, -15f, lightShadowColor)
        canvas!!.drawCircle((mWidth/2).toFloat(), (mHeight/2).toFloat(), mRadius.toFloat(), mPaint)

//        //Draw clock circle
//        mPaint.color = lightBackgroundColor
//        canvas!!.drawCircle((mWidth/2).toFloat(), (mHeight/2).toFloat(), mRadius.toFloat(), mPaint)

        //Draw boder
        mPaint.strokeWidth = 4f
        mPaint.style = Paint.Style.STROKE
        mPaint.color = borderColor
        canvas.drawCircle((mWidth/2).toFloat(), (mHeight/2).toFloat(), mRadius.toFloat(), mPaint)

        mPaint.reset()
    }

    private fun initialize() {
        mHeight = height
        mWidth = width

        val minHeightWidthValue: Int = min(mHeight, mWidth)
        mRadius = minHeightWidthValue / 2
        mPaint.isAntiAlias = true
        isInitialized=true
    }
}