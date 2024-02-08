package com.example.lineup

// RadarView.kt
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.lineup.models.location
import com.google.android.play.integrity.internal.t
import java.util.Locale
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class RadarView : View {
    private val radarPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }
    private val userPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private var users: List<location> = emptyList()
    private var selfDrawable: Bitmap? = null

    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    {
        init()
    }

    fun setUsers(users: List<location>) {
        this.users = users
        invalidate() // Trigger redraw
    }

    private var radarSize: Int = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Calculate the size of the radar view, assuming it is square
        radarSize = min(w, h)
        // Initialize the drawable when the size is available
        init()
    }

    private fun init() {
        if (radarSize == 0) return
        // Load the drawable for yourself
        val originalDrawable = BitmapFactory.decodeResource(resources, R.drawable.pink_avatar)
        val radarSize = min(width, height) // Assuming radar view is square
       // val drawableSize = (radarSize * 0.1).toInt()
        val maxDrawableDimension = (radarSize * 0.15).toInt()
        val scaleFactor = maxDrawableDimension.toFloat() / max(originalDrawable.width, originalDrawable.height)
        selfDrawable = Bitmap.createScaledBitmap(originalDrawable, (originalDrawable.width * scaleFactor).toInt(), (originalDrawable.height * scaleFactor).toInt(), true)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw radar circle
        val centerX = width / 2f
        val centerY = height / 2f
        // Calculate maxDistanceInKm dynamically based on the actual distance values of users
        val maxDistanceInKm = users.maxByOrNull { it.distance }?.distance ?: 1.0 // Default to 100 km if no users are present
        //  val maxDistanceInKm = 1 // Maximum distance in kilometers (adjust as needed)
        val maxRadarDimension = min(width, height) // Assuming radar view is square
        val maxRadarRadius = maxRadarDimension / 2
        val scalingFactor = maxRadarRadius / maxDistanceInKm

       // canvas.drawCircle(centerX, centerY, maxRadarRadius.toFloat(), radarPaint)
        val drawableWidth = selfDrawable?.width ?: 0
        val drawableHeight = selfDrawable?.height ?: 0
        val x = centerX - drawableWidth / 2f
        val y = centerY - drawableHeight / 2f

        selfDrawable?.let {
            canvas.drawBitmap(it, x, y, null)
        }

        // Draw users
        val distance = maxDistanceInKm*0.7// Distance of users from the center (same for all)
        for (user in users) {
            val directionInDegrees =
                directionToDegrees(user.direction) // Direction of the user in degrees


            // Calculate position of the user based on distance and direction
            val x =
                centerX + distance * scalingFactor * cos(Math.toRadians(directionInDegrees)).toFloat()
            val y =
                centerY + distance * scalingFactor * sin(Math.toRadians(directionInDegrees)).toFloat()
            Log.e("id1235","$x,$y")

            // Draw the user
            canvas.drawCircle(x.toFloat(), y.toFloat(), 20f, userPaint)

//            // Optionally, draw a line indicating the direction
//            val lineLength = 40f
//            val lineEndX =
//                x + lineLength * cos(Math.toRadians(directionInDegrees.toDouble())).toFloat()
//            val lineEndY =
//                y + lineLength * sin(Math.toRadians(directionInDegrees.toDouble())).toFloat()
//            canvas.drawLine(x.toFloat(),
//                y.toFloat(), lineEndX.toFloat(), lineEndY.toFloat(), userPaint)
        }
    }
    fun directionToDegrees(direction: String): Double {
        return when (direction.uppercase(Locale.ROOT)) {
            "N" -> 270.0
            "NE" -> 315.0
            "E" -> 0.0
            "SE" -> 45.0
            "S" -> 90.0
            "SW" -> 135.0
            "W" -> 180.0
            "NW" -> 225.0
            else -> 0.0 // Default to North if direction is not recognized
        }
    }

}
