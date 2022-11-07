package com.nvd.service

//import com.nvd.databases.appDatabase
import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.nvd.applocker.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class EndlessService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false
    private var currentApp : String? = null
    lateinit var sharedPreference : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    override fun onBind(intent: Intent): IBinder? {

        // We don't provide binding, so return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //log("onStartCommand executed with startId: $startId")
        //sharedPreference =  getSharedPreferences("AppLock", Context.MODE_PRIVATE)
        if (intent != null) {
            val action = intent.action
            //log("using an intent with action $action")
            when (action) {
                Actions.START.name -> {
                    startService()
                }
                Actions.STOP.name -> stopService()
                else -> Log.d("aa","This should never happen. No action in the received intent")
            }
        } else {
            Log.d( "ab",
                "with a null intent. It has been probably restarted by the system."
            )
        }



        val thread = Thread {

            while (true) {
                retriveNewApp()

                try {
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        thread.start()

        // by returning this we make sure the service is restarted if the system kills the service
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        //log("The service has been created".toUpperCase())
        val notification = createNotification()
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        //log("The service has been destroyed".toUpperCase())
        //Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, EndlessService::class.java).also {
            it.setPackage(packageName)
        };
        //PendingIntent.FLAG_UPDATE_CURRENT PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        val restartServicePendingIntent: PendingIntent = PendingIntent.getService(this, 1, restartServiceIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT);
        applicationContext.getSystemService(Context.ALARM_SERVICE);
        val alarmService: AlarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
    }

    private fun startService() {
        if (isServiceStarted) return

        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)
        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }

        // we're starting a loop in a coroutine
        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted) {
                launch(Dispatchers.IO) {
                    pingFakeServer()
                }
                delay(1 * 60 * 1000)
            }

        }
    }

    private fun stopService() {

        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            //log("Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)
    }

    private fun pingFakeServer() {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmmZ")
        val gmtTime = df.format(Date())

        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)

        val json =
            """
                {
                    "deviceId": "$deviceId",
                    "createdAt": "$gmtTime"
                }
            """
        try {
            Fuel.post("https://jsonplaceholder.typicode.com/posts")
                .jsonBody(json)
                .response { _, _, result ->
                    val (bytes, error) = result
                    if (bytes != null) {
                        Log.d("abs","[response bytes] ${String(bytes)}")
                    } else {
                        Log.d("abs","[response error] ${error?.message}")
                    }
                }
        } catch (e: Exception) {
            //log("Error making the request: ${e.message}")
        }
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        //FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT )
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)

        return builder
            .setContentTitle("App Locker")
            .setContentText("Your app is under the protection")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.icon_lock)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }


    private fun retriveNewApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            currentApp = null
            val usm = this.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()
            val applist =
                usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time)
            if (applist != null && applist.size > 0) {
                val mySortedMap: SortedMap<Long, UsageStats> = TreeMap()
                for (usageStats in applist) {
                    mySortedMap!![usageStats.lastTimeUsed] = usageStats
                }
                if (!mySortedMap.isEmpty()) {
                    currentApp = mySortedMap[mySortedMap.lastKey()]!!.packageName
                }
            }
            Log.d("BBB", "Current App in foreground is: $currentApp")
            //&& currentApp != "com.nvd.applocker"
            if (currentApp != null ) {
                checkMyApp(currentApp!!)
                //return currentApp as String
            }

        } else {
            val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val mm = manager.getRunningTasks(1)[0].topActivity!!.packageName
            val task = manager.runningAppProcesses
            currentApp = task[0].processName
            Log.d("AAA", "Current App in foreground is: $mm")
            if (mm != null )
            {
                checkMyApp(currentApp!!)

            }
        }
    }

    //  && appLocked!!.contains("com.nvd.applocker") com.sec.android.app.launcher
    private fun checkMyApp(currentApp : String) : Unit{
        sharedPreference =  getSharedPreferences("AppLock", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()

        val appLocked = sharedPreference.getString(currentApp, null)
        if (appLocked != null)
            Log.d("ccc", appLocked!!)

        val getLastApp = sharedPreference.getString("mLastApp", null)
        // currentApp == "com.sec.android.app.launcher" currentApp.contains("launcher") currentApp == "com.android.launcher3"
        if (currentApp.lowercase(Locale.getDefault()).contains("launcher") && getLastApp != null){
            editor.putString("mLastApp", null).apply()
        }
        //com.android.launcher3
        if ((getLastApp != appLocked && appLocked != null)){
            if (appLocked != "com.nvd.applocker")
                editor.putString("mLastApp", appLocked).apply()

            // mở màn hình khóa
            val intent = Intent(this, PasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}

