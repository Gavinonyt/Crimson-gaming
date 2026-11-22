package com.ultra.driveconnect

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    private val background = Color.rgb(8, 9, 16)
    private val surface = Color.rgb(19, 22, 34)
    private val surfaceAlt = Color.rgb(25, 29, 44)
    private val accent = Color.rgb(153, 112, 255)
    private val accentSoft = Color.rgb(45, 35, 75)
    private val primaryText = Color.rgb(245, 245, 252)
    private val secondaryText = Color.rgb(164, 169, 188)

    private lateinit var pageContent: LinearLayout
    private var selectedPage = "HOME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = background
        window.navigationBarColor = background
        window.decorView.systemUiVisibility = 0
        render(selectedPage)
    }

    private fun render(page: String) {
        selectedPage = page

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(background)
        }

        val scroll = ScrollView(this).apply {
            isFillViewport = true
            clipToPadding = false
            setPadding(dp(20), dp(22), dp(20), dp(18))
        }

        pageContent = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        scroll.addView(
            pageContent,
            ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT
            )
        )
        root.addView(scroll, LinearLayout.LayoutParams(0, 0, 1f))

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(9), dp(8), dp(9))
            background = rounded(surface, 0, 0, 0, 0)
        }

        listOf("HOME", "AUTO", "KEYS", "GARAGE").forEach { item ->
            val active = item == page
            val button = TextView(this).apply {
                text = item
                textSize = 10f
                letterSpacing = 0.06f
                gravity = Gravity.CENTER
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(if (active) primaryText else secondaryText)
                setPadding(dp(5), dp(13), dp(5), dp(13))
                background = rounded(
                    if (active) accentSoft else Color.TRANSPARENT,
                    dp(12)
                )
                setOnClickListener { render(item) }
            }
            nav.addView(
                button,
                LinearLayout.LayoutParams(0, dp(46), 1f).apply {
                    setMargins(dp(3), 0, dp(3), 0)
                }
            )
        }

        root.addView(
            nav,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(root)
        when (page) {
            "AUTO" -> showAuto()
            "KEYS" -> showKeys()
            "GARAGE" -> showGarage()
            else -> showHome()
        }
    }

    private fun showHome() {
        header("ULTRA", "DRIVE CONNECT", "YOUR DRIVE. ONE COMPANION.")
        statusPill("COMPANION APP • VERSION 2.0")
        gap(20)
        sectionTitle("Your dashboard", "Quick access to your connected-drive tools.")
        actionCard(
            "01  CONNECTION CENTER",
            "Setup guides and compatibility information for your phone and vehicle.",
            "EXPLORE CONNECTIONS"
        ) { render("AUTO") }
        actionCard(
            "02  DIGITAL VEHICLE KEY",
            "Check what is needed before setting up a supported manufacturer's digital key.",
            "VIEW KEY STATUS"
        ) { render("KEYS") }
        actionCard(
            "03  MY GARAGE",
            "Your space for vehicle profiles and saved vehicle information.",
            "OPEN GARAGE"
        ) { render("GARAGE") }
        note("Safety first", "Set up your phone before driving. Never interact with the app while the vehicle is moving.")
        footer()
    }

    private fun showAuto() {
        header("AUTO", "CONNECT", "PHONE-TO-CAR COMPANION")
        statusPill("SETUP & SUPPORT")
        gap(18)
        sectionTitle("Choose your platform", "Official setup information for supported systems.")
        actionCard(
            "ANDROID AUTO",
            "Android Auto is provided by Google and requires a compatible Android phone, vehicle or receiver, and supported connection. Ultra Drive Connect does not replace or project the Android Auto interface.",
            "OPEN GOOGLE SETUP GUIDE"
        ) {
            openExternal("https://support.google.com/androidauto/answer/6348029")
        }
        actionCard(
            "APPLE CARPLAY",
            "CarPlay is an Apple platform for compatible Apple devices and vehicles. This Android app can show setup information, but it cannot act as a CarPlay receiver or project CarPlay.",
            "OPEN APPLE SETUP GUIDE"
        ) {
            openExternal("https://support.apple.com/en-us/108415")
        }
        actionCard(
            "CONNECTION STATUS",
            "No live vehicle connection is configured in this release. Live vehicle data requires a supported vehicle interface and authorized integration.",
            null
        )
        footer()
    }

    private fun showKeys() {
        header("DIGITAL", "CAR KEY", "SECURE ACCESS READINESS")
        statusPill("NOT PAIRED")
        gap(18)
        actionCard(
            "KEY SETUP REQUIREMENTS",
            "A real digital key depends on a compatible vehicle, supported phone hardware, and the vehicle maker's approved digital-key service. Availability varies by model, region, and device.",
            null
        )
        actionCard(
            "CURRENT APP STATUS",
            "No vehicle key is stored or connected. This app does not send lock, unlock, or engine-start commands.",
            null
        )
        actionCard(
            "SAFE NEXT STEP",
            "For an eligible vehicle, use the official automaker app and follow the instructions supplied by the vehicle manufacturer.",
            null
        )
        footer()
    }

    private fun showGarage() {
        header("MY", "GARAGE", "YOUR VEHICLE SPACE")
        statusPill("GARAGE PROFILE")
        gap(18)
        actionCard(
            "NO VEHICLES ADDED",
            "Your garage is ready for vehicle profiles. This starter version does not yet save vehicle profiles or connect to vehicle systems.",
            null
        )
        actionCard(
            "PLANNED GARAGE TOOLS",
            "Vehicle profile details, supported-device information, and authorized integrations can be added in future releases.",
            null
        )
        footer()
    }

    private fun header(first: String, second: String, subtitle: String) {
        val brand = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val mark = TextView(this).apply {
            text = "U"
            textSize = 25f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(primaryText)
            background = rounded(accent, dp(15))
        }
        brand.addView(mark, LinearLayout.LayoutParams(dp(52), dp(52)))

        val names = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), 0, 0, 0)
        }
        names.addView(label(first, 13, accent, true))
        names.addView(label(second, 22, primaryText, true))
        brand.addView(names)
        pageContent.addView(brand)

        gap(18)
        pageContent.addView(label(subtitle, 11, secondaryText, true))
        gap(8)
        pageContent.addView(label("A smarter companion for the road.", 14, primaryText, false))
    }

    private fun statusPill(value: String) {
        val pill = TextView(this).apply {
            text = value
            textSize = 10f
            letterSpacing = 0.08f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.rgb(205, 185, 255))
            setPadding(dp(12), dp(9), dp(12), dp(9))
            background = rounded(accentSoft, dp(30))
        }
        pageContent.addView(
            pill,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun sectionTitle(title: String, subtitle: String) {
        pageContent.addView(label(title, 21, primaryText, true))
        pageContent.addView(label(subtitle, 13, secondaryText, false))
        gap(14)
    }

    private fun actionCard(
        title: String,
        body: String,
        action: String?,
        onClick: (() -> Unit)? = null
    ) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(17), dp(17), dp(17), dp(16))
            background = rounded(surface, dp(19), Color.rgb(42, 45, 62))
        }

        card.addView(label(title, 15, Color.rgb(197, 176, 255), true))
        gapIn(card, 7)
        card.addView(label(body, 13, secondaryText, false))

        if (action != null) {
            val button = Button(this).apply {
                text = action
                textSize = 10f
                isAllCaps = false
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(primaryText)
                background = rounded(accent, dp(12))
                setPadding(dp(10), dp(4), dp(10), dp(4))
                setOnClickListener { onClick?.invoke() }
            }
            card.addView(
                button,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(48)
                ).apply { topMargin = dp(13) }
            )
        }

        pageContent.addView(
            card,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(12)
            }
        )
    }

    private fun note(title: String, message: String) {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(15), dp(14), dp(15), dp(14))
            background = rounded(surfaceAlt, dp(16))
        }
        box.addView(label(title, 13, primaryText, true))
        box.addView(label(message, 12, secondaryText, false))
        pageContent.addView(box)
    }

    private fun footer() {
        gap(10)
        pageContent.addView(
            label(
                "ULTRA DRIVE CONNECT  •  COMPANION APP",
                10,
                secondaryText,
                true
            )
        )
        gap(14)
    }

    private fun label(
        value: String,
        size: Int,
        color: Int,
        bold: Boolean
    ): TextView = TextView(this).apply {
        text = value
        textSize = size.toFloat()
        setTextColor(color)
        if (bold) typeface = Typeface.DEFAULT_BOLD
        setLineSpacing(dp(3).toFloat(), 1f)
    }

    private fun gap(height: Int) {
        pageContent.addView(View(this), LinearLayout.LayoutParams(1, dp(height)))
    }

    private fun gapIn(parent: LinearLayout, height: Int) {
        parent.addView(View(this), LinearLayout.LayoutParams(1, dp(height)))
    }

    private fun rounded(
        color: Int,
        radius: Int,
        stroke: Int = Color.TRANSPARENT
    ): GradientDrawable = GradientDrawable().apply {
        setColor(color)
        cornerRadius = radius.toFloat()
        if (stroke != Color.TRANSPARENT) {
            setStroke(dp(1), stroke)
        }
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    private fun openExternal(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (_: Exception) {
            // The app remains usable if no browser is available.
        }
    }
}
