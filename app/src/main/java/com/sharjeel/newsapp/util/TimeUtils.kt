package com.sharjeel.newsapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.TimeZone

object TimeUtils {

    fun formatRelativeTime(isoDate: String?): String {
        if (isoDate.isNullOrEmpty()) return "just now"

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatRelativeTimeApi26(isoDate)
        } else {
            formatRelativeTimeLegacy(isoDate)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatRelativeTimeApi26(isoDate: String): String {
        return try {
            // Currents API patterns aur ISO patterns dono ko parse karne ke liye formatters array
            val formatters = listOf(
                DateTimeFormatter.ISO_ZONED_DATE_TIME,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z", Locale.US),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US).withZone(java.time.ZoneId.of("UTC"))
            )

            var dateTime: ZonedDateTime? = null
            for (formatter in formatters) {
                try {
                    dateTime = ZonedDateTime.parse(isoDate, formatter)
                    break
                } catch (e: Exception) {
                    // Try next format
                }
            }

            val finalDateTime = dateTime ?: ZonedDateTime.parse(isoDate) // Default fallback
            val now = ZonedDateTime.now()

            val minutes = ChronoUnit.MINUTES.between(finalDateTime, now)
            val hours = ChronoUnit.HOURS.between(finalDateTime, now)
            val days = ChronoUnit.DAYS.between(finalDateTime, now)

            // Dynamic output labels mapping
            when {
                minutes < 1 -> "just now"
                minutes < 60 -> "$minutes minutes ago"
                hours < 24 -> "$hours hours ago"
                days < 7 -> "$days days ago"
                else -> {
                    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())
                    finalDateTime.format(formatter)
                }
            }
        } catch (e: Exception) {
            isoDate
        }
    }

    private fun formatRelativeTimeLegacy(isoDate: String): String {
        return try {
            // Multiple pattern fallbacks for older devices
            val patterns = listOf(
                "yyyy-MM-dd HH:mm:ss Z",
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd HH:mm:ss"
            )

            var date: java.util.Date? = null
            for (pattern in patterns) {
                try {
                    val sdf = SimpleDateFormat(pattern, Locale.US)
                    if (pattern.contains("Z") || pattern.contains("'Z'")) {
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                    }
                    date = sdf.parse(isoDate)
                    if (date != null) break
                } catch (e: Exception) {
                    // Try next format
                }
            }

            val finalDate = date ?: return isoDate
            val now = System.currentTimeMillis()
            val diff = now - finalDate.time

            val minutes = diff / (1000 * 60)
            val hours = diff / (1000 * 60 * 60)
            val days = diff / (1000 * 60 * 60 * 24)

            when {
                minutes < 1 -> "just now"
                minutes < 60 -> "$minutes minutes ago"
                hours < 24 -> "$hours hours ago"
                days < 7 -> "$days days ago"
                else -> {
                    val outSdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    outSdf.format(finalDate)
                }
            }
        } catch (e: Exception) {
            isoDate
        }
    }

    /**
     * Extracts the domain from a URL to help fetch channel logos
     */
    fun getDomain(url: String): String? {
        return try {
            val uri = java.net.URI(url)
            val domain = uri.host
            if (domain != null && domain.startsWith("www.")) {
                domain.substring(4)
            } else {
                domain
            }
        } catch (e: Exception) {
            null
        }
    }
}