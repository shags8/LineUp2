package com.example.lineup

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.lineup.databinding.ActivityCharacterSelectBinding
import com.gdsc.lineup.login.HorizontalMarginItemDecoration


class CharacterSelect : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterSelectBinding
    private lateinit var characterAdapter: AvatarAdapter
    private var pageTranslationX: Float = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue")
        Log.e("id1234", "$retrievedValue")

        var characters: IntArray = intArrayOf(
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


        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * Math.abs(position))

        }
        val offsetPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin).toInt()
                .dpToPx(resources.displayMetrics)
        binding.VP.setPadding(offsetPx, 0, offsetPx, 0)
//        binding.VP.setPageTransformer(pageTransformer)
//        val itemDecoration = HorizontalMarginItemDecoration(
//            this,
//            R.dimen.viewpager_current_item_horizontal_margin
//        )
//        binding.VP.addItemDecoration(itemDecoration)

        binding.VP.setPageTransformer { page, position ->
            val scale = if (position == 0f) 1f else 0.4f  // Adjust the scaling factor as needed
            page.scaleX = scale
            page.scaleY = scale
        }

// Set the offscreen page limit to ensure that enough pages are retained
        binding.VP.offscreenPageLimit = 2


        Log.d("AvatarAdapter3", "Inflating CharacterLayoutBinding")
    }

    private fun Int.dpToPx(displayMetrics: DisplayMetrics): Int =
        (this * displayMetrics.density).toInt()

}
