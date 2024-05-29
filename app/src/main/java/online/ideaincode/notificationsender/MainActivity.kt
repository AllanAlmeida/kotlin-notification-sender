package online.ideaincode.notificationsender

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

const val CHANNEL_ID = "primary_channel_id"

class MainActivity : AppCompatActivity() {

    private val simpleNotification: Button by lazy { findViewById(R.id.simpleNotification) }
    private val longNotification: Button by lazy { findViewById(R.id.longNotification) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        simpleNotification.setOnClickListener {
            sendNotification(1, "Simple Notification", "This is a test of simple notification", "")
        }

        longNotification.setOnClickListener {
            sendNotification(2, "Long Notification", "", "This is a test of long notification: Lorem ipsum dolor sit amet. Qui dolorem saepe At dolorum deserunt ab harum quos ut laudantium galisum et cupiditate voluptas At quae consequatur. Sed odit deserunt est obcaecati unde eum minus libero quo autem voluptatem non quas quisquam ut minus omnis.")
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val descriptionText = "Channel for notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(id: Int, title: String, simpleText: String, longText: String) {

        var longNotificationText: NotificationCompat.BigTextStyle? = null

        if (longText.isNotEmpty()) {
            longNotificationText = NotificationCompat.BigTextStyle().bigText(longText)
        }


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(simpleText)
            .setStyle(longNotificationText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(id, builder.build())
        }
    }
}
