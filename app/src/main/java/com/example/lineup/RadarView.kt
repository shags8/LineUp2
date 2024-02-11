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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
        val sortedUsers = users.sortedBy { it.distance }

        // Draw users

        for (user in sortedUsers) {
            val directionInDegrees =
                directionToDegrees(user.direction) // Direction of the user in degrees

            val separationFactor = 0.35 // Adjust this factor as needed
            var distance = maxDistanceInKm * (0.7 + (sortedUsers.indexOf(user) * separationFactor))

            // Calculate position of the user based on distance and direction
            var x =
                centerX + distance * scalingFactor * cos(Math.toRadians(directionInDegrees)).toFloat()
            var y =
                centerY + distance * scalingFactor * sin(Math.toRadians(directionInDegrees)).toFloat()
            Log.e("id1235","$x,$y")





            // Draw the user
            //canvas.drawCircle(x.toFloat(), y.toFloat(), 20f, userPaint)
            val userLayout = createUserLayout(user,maxRadarDimension)
            userLayout.measure(width, height)
            userLayout.layout(0, 0, width, height)
            canvas.save()
            canvas.translate((x - userLayout.measuredWidth / 2f).toFloat(),
                (y - userLayout.measuredHeight / 2f).toFloat()
            )
            userLayout.draw(canvas)
            canvas.restore()

        }
    }
    private fun createUserLayout(user: location, maxRadarDimension: Int): LinearLayout {
        val context = context
        val userLayout = LinearLayout(context)
        userLayout.orientation = LinearLayout.VERTICAL

        val radarSize = min(width, height) // Assuming radar view is square
        val maxDrawableDimension = (radarSize * 0.010).toInt()

        // Distance TextView
        val distanceTextView = TextView(context)
        val formattedDistance = String.format("%.3f", user.distance)
        distanceTextView.text = "Distance: ${formattedDistance} km"
        distanceTextView.textSize = maxDrawableDimension.toFloat()
        userLayout.addView(distanceTextView)

        // Name TextView
        val nameTextView = TextView(context)
        nameTextView.text = "Name: ${user.name}"
        nameTextView.textSize = maxDrawableDimension.toFloat()// Change to user's actual name
        userLayout.addView(nameTextView)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        userLayout.layoutParams = layoutParams

        // Icon ImageView (Assuming you have a URL to fetch the icon from the backend)
        val iconImageView = ImageView(context)
        // Load icon from the backend and set it to the ImageView
        // You can use any image loading library like Picasso, Glide, etc.
        // Example: Glide.with(context).load(user.iconUrl).into(iconImageView)
       // userLayout.addView(iconImageView)

        return userLayout
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
