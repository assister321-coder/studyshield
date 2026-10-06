package com.studyshield.app

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

/** Minimalist glass-style "Focus Mode" overlay shown over reel feeds. */
class BlockerOverlayService : Service() {

    private var overlayView: View? = null
    private val wm by lazy { getSystemService(WINDOW_SERVICE) as WindowManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showOverlay()
        return START_NOT_STICKY
    }

    private fun showOverlay() {
        if (overlayView != null) return
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(64, 64, 64, 64)
            setBackgroundColor(Color.parseColor("#F0151723"))
            addView(TextView(this@BlockerOverlayService).apply {
                text = "Focus Mode 🌱"; setTextColor(Color.WHITE); textSize = 22f
            })
            addView(TextView(this@BlockerOverlayService).apply {
                text = "Reels are blocked by StudyShield. Back to studying."
                setTextColor(Color.parseColor("#9CA3AF")); textSize = 14f
            })
            addView(Button(this@BlockerOverlayService).apply {
                text = "Back to DMs"
                setOnClickListener {
                    startActivity(
                        Intent(Intent.ACTION_VIEW).setPackage("com.instagram.android")
                            .setData(android.net.Uri.parse("instagram://direct/inbox/"))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    stopSelf()
                }
            })
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT
        )
        overlayView = card
        wm.addView(card, params)
    }

    override fun onDestroy() {
        overlayView?.let { wm.removeView(it) }
        overlayView = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
