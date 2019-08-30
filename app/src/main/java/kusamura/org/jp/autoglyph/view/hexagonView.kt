package kusamura.org.jp.autoglyph.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class HexagonView : View{

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val paint = Paint()
    private val path = Path()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.TRANSPARENT)
        paint.color = Color.RED
        paint.strokeWidth = 3.0f
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE

        val center = (width/2).toFloat()
        path.moveTo((center * sin(Math.toDegrees(0.0)) + center).toFloat(), (center * cos(Math.toRadians(0.0))).toFloat()+ center)
        for(rad in 1..6){
            path.lineTo((center * sin(Math.toRadians(60.0*rad)) + center).toFloat(), (center * cos(Math.toRadians(60.0*rad)) + center).toFloat())
        }
        canvas?.drawPath(path, paint)
        canvas?.drawCircle(center, center, center, paint)
    }

}