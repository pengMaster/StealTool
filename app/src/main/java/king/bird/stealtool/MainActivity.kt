package king.bird.stealtool

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import king.bird.tool.StealUtils
import kotlinx.android.synthetic.main.activity_main.*

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/09/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //获取图片
        mBtnGetPic.setOnClickListener {
            StealUtils.getAllLocalPhotos(this@MainActivity)
                    .map { it }.forEach { Log.e("mBtnGetPic", it.toString()) }
        }
        //获取视频
        mBtnGetVideo.setOnClickListener {
            StealUtils.getAllLocalVideos(this@MainActivity)
                    .map { it }.forEach { Log.e("mBtnGetVideo", it.toString()) }
        }
        //获取联系人
        mBtnGetContacts.setOnClickListener {
            StealUtils.getAllContactInfo(this@MainActivity)
                    .map { it }.forEach { Log.e("mBtnGetContacts", it.toString()) }
        }
        //获取短信
        mBtnGetSMS.setOnClickListener {
            Log.e("mBtnGetSMS", StealUtils.getSmsInPhone(this@MainActivity)
            )
        }

        //获取通话记录
        mBtnGetCallLog.setOnClickListener {
            StealUtils.getCallInfos(this@MainActivity)
                    .map { it }.forEach { Log.e("mBtnGetCallLog", it.toString()) }
        }

        // 获取安装所有App
        mBtnGetApp.setOnClickListener {
            StealUtils.getInstallApp(packageManager)
                    .map { it }.forEach { Log.e("mBtnGetApp", it.toString()) }
        }


        mBtnGotoPermission.setOnClickListener {
            startActivity(Intent(applicationContext,PermissionActivity::class.java)) }
        }

}