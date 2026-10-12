package ru.mtuci.drivenext

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var btnSkip: Button
    private lateinit var layoutIndicators: LinearLayout

    private val onboardingList = listOf(
        OnboardingItem(
            R.string.onboarding_title_1,
            R.string.onboarding_desc_1,
            R.drawable.ic_splash_logo
        ),
        OnboardingItem(
            R.string.onboarding_title_2,
            R.string.onboarding_desc_2,
            R.drawable.ic_splash_logo
        ),
        OnboardingItem(
            R.string.onboarding_title_3,
            R.string.onboarding_desc_3,
            R.drawable.ic_splash_logo
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)
        layoutIndicators = findViewById(R.id.layoutIndicators)

        val adapter = OnboardingAdapter(onboardingList)
        viewPager.adapter = adapter

        setupIndicators()
        setCurrentIndicator(0)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                if (position == onboardingList.size - 1) {
                    btnNext.setText(R.string.onboarding_start)
                } else {
                    btnNext.setText(R.string.onboarding_next)
                }
            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem + 1 < onboardingList.size) {
                viewPager.currentItem += 1
            } else {
                finishOnboarding()
            }
        }

        btnSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(onboardingList.size)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(applicationContext, R.drawable.indicator_dot_inactive)
            )
            indicators[i]?.layoutParams = layoutParams
            layoutIndicators.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = layoutIndicators.childCount
        for (i in 0 until childCount) {
            val imageView = layoutIndicators.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.indicator_dot_active)
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.indicator_dot_inactive)
                )
            }
        }
    }

    private fun finishOnboarding() {
        val sharedPref = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("is_onboarding_completed", true)
            apply()
        }

        // Открываем экран выбора входа/регистрации
        val intent = Intent(this, AuthChoiceActivity::class.java)
        startActivity(intent)
        finish()
    }
}