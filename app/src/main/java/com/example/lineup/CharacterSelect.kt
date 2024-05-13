package com.example.lineup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.lineup.RetrofitApi.apiInterface
import com.example.lineup.adapters.AvatarAdapter
import com.example.lineup.databinding.ActivityCharacterSelectBinding
import com.example.lineup.models.Avatar
import com.example.lineup.models.Avatar2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs


class CharacterSelect : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterSelectBinding
    private lateinit var characterAdapter: AvatarAdapter
    private var pageTranslationX: Float = 0f
    private lateinit var viewPager: ViewPager2
    private var currentVisiblePosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue")
        val editor = sharedPreferences.edit()
        Log.e("id1234", "$retrievedValue")
        var visibleImage: Int = 0
        val characters: IntArray = intArrayOf(
            R.drawable.red_avatar,
            R.drawable.pink_avatar,
            R.drawable.yellow_avatar,
            R.drawable.small_avatar,
            R.drawable.grey_avatar,
            R.drawable.blue_avatar,
            R.drawable.brown_avatar,
            R.drawable.green_avatar
        )

        binding.VP.adapter = AvatarAdapter(characters)
        binding.VP.clipToPadding = false
        binding.VP.clipChildren = false
        binding.VP.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val transformation = CompositePageTransformer()
        transformation.addTransformer(MarginPageTransformer(40))
        transformation.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.40f + r * 0.60f
            page.scaleX = 0.40f + r * 0.60f
            page.translationX = -pageTranslationX * position
            val scaleFactor = 0.5f + r * 0.5f  // Adjust the scaling factor for a larger portion
            page.scaleY = scaleFactor
            page.scaleX = scaleFactor
        }

        val offsetPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin).toInt()
                .dpToPx(resources.displayMetrics)
        binding.VP.setPadding(offsetPx, 0, offsetPx, 0)
        binding.VP.setPageTransformer(transformation)
        val itemDecoration = HorizontalMarginItemDecoration(
            this, R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.VP.addItemDecoration(itemDecoration)
        binding.VP.offscreenPageLimit = 2

        val header = "Bearer $retrievedValue"
        binding.characterBtn.setOnClickListener {
            Log.e("imageNumber", "$visibleImage")
            editor.putString("Character Token" , visibleImage.toString())
            editor.apply()
            //Log.e("id12344","$number")
            val call = apiInterface.storeAvatar(header,Avatar(visibleImage))
            call.enqueue(object : Callback<Avatar2> {

                override fun onResponse(call: Call<Avatar2>, response: Response<Avatar2>) {
                    Log.e("id1234", "${response.body()}")

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Log.e("id1234", "${response.body()}")
                            if(response.body()!!.message=="Avatar stored successfully") {
                                val intent = Intent(this@CharacterSelect, CountDownActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                        } else {
                            Toast.makeText(this@CharacterSelect, "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<Avatar2>, t: Throwable) {
                    Toast.makeText(this@CharacterSelect, "Error!", Toast.LENGTH_SHORT).show()
                }

            })

        }
        viewPager = binding.VP
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // This method is called when a new page becomes selected
                // Update the currentVisiblePosition
                currentVisiblePosition = position
                // You can now use this position to determine the mapped image number
                visibleImage = getVisibleMappedImage()
            }
        })
    }

    private fun getVisibleMappedImage(): Int {
        // Calculate the position of the currently visible item
        return currentVisiblePosition + 1
    }

    private fun Int.dpToPx(displayMetrics: DisplayMetrics): Int =
        (this * displayMetrics.density).toInt()

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { dialog, which ->
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, which ->
                // Handle "no" button click or remove this block if not needed
            }
            .show()
    }

}