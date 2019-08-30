package kusamura.org.jp.autoglyph.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.PointF
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import kusamura.org.jp.autoglyph.R

class OverlayService : Service() {

    private val overlayView: ViewGroup by lazy {
        LayoutInflater.from(this).inflate(
            R.layout.overlay,
            null
        ) as ViewGroup
    }

    private val windowManager: WindowManager by lazy { applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    private var isLongClick = false
    private var is2Finger = false

    private var startPoint = PointF()

    private val displaySize: Point by lazy {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        size
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("overlay", "overlay service start")

        val wmParam = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )

        overlayView.setOnLongClickListener {
            it.setBackgroundResource(R.color.overlayDragging)
            isLongClick = true
            false
        }


        overlayView.setOnTouchListener { view, motionEvent ->
            val x = motionEvent.rawX.toInt()
            val y = motionEvent.rawY.toInt()
            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_MOVE -> {
                    if (isLongClick) {
                        val centerX = x - (displaySize.x / 2)
                        val centerY = y - (displaySize.y / 2)
                        wmParam.x = centerX
                        wmParam.y = centerY
                        windowManager.updateViewLayout(overlayView, wmParam)
                    }
                    else if(is2Finger){
                        Log.d("over", "move o  2 finger")
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (isLongClick) {
                        view.setBackgroundResource(android.R.color.transparent)
                    }
                    isLongClick = false
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    is2Finger = true
                    startPoint.set(motionEvent.rawX, motionEvent.rawY)
                }

                MotionEvent.ACTION_POINTER_UP -> {
                    is2Finger = false
                    startPoint.set(0f, 0f)
                }
            }

            false
        }



        windowManager.addView(overlayView, wmParam)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? = null

}
