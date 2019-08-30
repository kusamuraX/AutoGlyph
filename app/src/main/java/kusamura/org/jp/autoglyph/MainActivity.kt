package kusamura.org.jp.autoglyph

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import kusamura.org.jp.autoglyph.service.OverlayService

class MainActivity : Activity() {

    private val REQUEST_SYSTEM_OVERLAY : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("overlay", "on create")
    }

    override fun onStart() {
        super.onStart()
        if(!Settings.canDrawOverlays(this)){
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            this.startActivityForResult(intent, REQUEST_SYSTEM_OVERLAY)
        } else {
            startService(Intent(applicationContext, OverlayService::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_SYSTEM_OVERLAY -> if (Settings.canDrawOverlays(this)){
                startService(Intent(applicationContext, OverlayService::class.java))
            } else {
                android.os.Process.killProcess(android.os.Process.myPid())
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
