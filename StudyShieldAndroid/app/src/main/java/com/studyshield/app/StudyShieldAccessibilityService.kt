package com.studyshield.app

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Detects when the user is on a reels/shorts surface inside TikTok or Instagram
 * and launches the blocker overlay. Chat inboxes (DMs) and static feeds are
 * whitelisted via view-id heuristics.
 */
class StudyShieldAccessibilityService : AccessibilityService() {

    private val prefs by lazy { getSharedPreferences("studyshield", MODE_PRIVATE) }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!prefs.getBoolean("active", true) || event == null) return

        val pkg = event.packageName?.toString() ?: return
        if (pkg != "com.zhiliaoapp.musically" && pkg != "com.instagram.android") return

        val root = rootInActiveWindow ?: return
        when {
            containsAny(root, "direct", "inbox", "thread") -> { /* DM screen: allow */ }
            containsAny(root, "reel", "clips", "for_you", "short_video") -> block()
            else -> { /* static feed: allow */ }
        }
        root.recycle()
    }

    private fun containsAny(node: AccessibilityNodeInfo, vararg needles: String): Boolean {
        fun walk(n: AccessibilityNodeInfo?): Boolean {
            if (n == null) return false
            val id = n.viewIdResourceName?.lowercase() ?: ""
            val text = n.text?.toString()?.lowercase() ?: ""
            val desc = n.contentDescription?.toString()?.lowercase() ?: ""
            if (needles.any { id.contains(it) || text.contains(it) || desc.contains(it) }) return true
            for (i in 0 until n.childCount) if (walk(n.getChild(i))) return true
            return false
        }
        return walk(node)
    }

    private fun block() {
        prefs.edit().putInt("count", prefs.getInt("count", 0) + 1).apply()
        startService(Intent(this, BlockerOverlayService::class.java))
    }

    override fun onInterrupt() {}
}
