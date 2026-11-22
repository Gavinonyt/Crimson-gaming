package com.ultra.driveconnect

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private val bg = Color.rgb(10, 8, 18)
    private val panel = Color.rgb(27, 21, 39)
    private val accent = Color.rgb(185, 139, 255)
    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        showHome()
    }

    private fun base(title: String, subtitle: String) {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            setPadding(22, 28, 22, 24)
        }
        val scroll = ScrollView(this)
        content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(0, 12, 0, 0)
        }
        listOf("HOME", "AUTO", "KEYS", "GARAGE").forEach { label ->
            val b = Button(this).apply {
                text = label
                textSize = 10f
                setTextColor(Color.WHITE)
                setBackgroundColor(panel)
                setOnClickListener { when(label) {
                    "HOME" -> showHome()
                    "AUTO" -> showAuto()
                    "KEYS" -> showKeys()
                    else -> showGarage()
                } }
            }
            nav.addView(b, LinearLayout.LayoutParams(0, 52, 1f).apply { setMargins(3,0,3,0) })
        }
        root.addView(nav)
        setContentView(root)
        text(title, 28, true)
        text(subtitle, 13, false, Color.LTGRAY)
        space(18)
    }

    private fun text(value: String, size: Int, bold: Boolean = false, color: Int = Color.WHITE) {
        content.addView(TextView(this).apply {
            text = value
            textSize = size.toFloat()
            setTextColor(color)
            if (bold) typeface = Typeface.DEFAULT_BOLD
            setPadding(0, 5, 0, 7)
        })
    }
    private fun space(h: Int) { content.addView(View(this), LinearLayout.LayoutParams(1,h)) }
    private fun card(title: String, body: String, action: String? = null, click: (() -> Unit)? = null) {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(17, 15, 17, 15)
            setBackgroundColor(panel)
        }
        box.addView(TextView(this).apply { text=title; textSize=17f; setTextColor(accent); typeface=Typeface.DEFAULT_BOLD })
        box.addView(TextView(this).apply { text=body; textSize=13f; setTextColor(Color.LTGRAY); setPadding(0,8,0,8) })
        if (action != null) box.addView(Button(this).apply {
            text=action; setOnClickListener { click?.invoke() }
        })
        content.addView(box, LinearLayout.LayoutParams(-1,-2).apply { setMargins(0,0,0,13) })
    }

    private fun showHome() {
        base("ULTRA DRIVE CONNECT", "YOUR DRIVE. ONE COMPANION APP.")
        card("CONNECTION CENTER", "Your vehicle connection status and supported companion features.", "VIEW CONNECTIONS") { showAuto() }
        card("DIGITAL VEHICLE KEY", "Pairing is unavailable until an approved vehicle-maker digital-key integration is configured.", "VIEW KEY STATUS") { showKeys() }
        card("MY GARAGE", "Keep a local list of vehicles you want to manage in the app.", "OPEN GARAGE") { showGarage() }
        text("Drive safely. Set up your phone before the vehicle is moving.", 12, false, Color.LTGRAY)
    }

    private fun showAuto() {
        base("AUTO CONNECT", "PHONE-TO-CAR COMPANION")
        card("ANDROID AUTO", "Android Auto projection is provided by Google's Android Auto app and compatible vehicle systems. This app is a companion hub; it does not replace Google's projection service.", "OPEN ANDROID AUTO HELP") {
            openUrl("https://support.google.com/androidauto/answer/6348029")
        }
        card("APPLE CARPLAY", "CarPlay is an Apple platform for compatible Apple devices. An Android APK cannot act as an Apple CarPlay receiver or provide CarPlay projection.", "OPEN CARPLAY GUIDE") {
            openUrl("https://support.apple.com/en-us/108415")
        }
        card("CONNECTION STATUS", "No vehicle connection detected. Pairing and live telemetry are not enabled in this starter release.")
    }

    private fun showKeys() {
        base("DIGITAL CAR KEY", "SECURE VEHICLE ACCESS")
        card("NOT PAIRED", "Real digital keys require a compatible vehicle, supported phone hardware, and an authorized automaker/OEM digital-key implementation. No unlock, lock, or start commands are available in this build.")
        card("SECURITY", "Never share a vehicle key credential. Use your vehicle maker's official app and setup process for supported digital keys.")
    }

    private fun showGarage() {
        base("MY GARAGE", "YOUR VEHICLE PROFILES")
        card("NO VEHICLES ADDED", "Vehicle profiles and saved preferences will live here. Live vehicle data is not connected in this starter release.")
        card("COMING NEXT", "Vehicle profile editor, supported-device checks, and authorized digital-key provider integration.")
    }

    private fun openUrl(url: String) {
        try { startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))) } catch (_: Exception) { }
    }
}
