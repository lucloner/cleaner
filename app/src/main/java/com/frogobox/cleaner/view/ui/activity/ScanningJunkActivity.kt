package com.frogobox.cleaner.view.ui.activity

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import com.frogobox.cleaner.R
import com.frogobox.cleaner.base.BaseActivity
import com.frogobox.cleaner.databinding.ActivityScanningJunkBinding
import com.frogobox.cleaner.model.Apps
import com.frogobox.cleaner.utils.Constant
import com.frogobox.cleaner.view.adapter.JunkAppsViewAdapter
import com.github.ybq.android.spinkit.style.ThreeBounce
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import java.util.*

class ScanningJunkActivity : BaseActivity() {

    private var check = 0
    private var prog = 0
    private lateinit var packages: List<ApplicationInfo>
    private lateinit var activityScanningJunkBinding: ActivityScanningJunkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityScanningJunkBinding = ActivityScanningJunkBinding.inflate(baseLayoutInflater())
        setContentView(activityScanningJunkBinding.root)
        packages = packageManager.getInstalledApplications(0)
        setupAnimationProcess()
        setupRecyclerViewApps()
    }

    private fun setupAnimationProcess() {
        val timer = Timer()
        val mActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val rotate = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 1500
        rotate.repeatCount = 4
        rotate.interpolator = LinearInterpolator()

        rotate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                timer.cancel()
                timer.purge()

                activityScanningJunkBinding.apply {
                    ivBallIndicator1.hide()
                    ivBallIndicator2.hide()
                    ivBallIndicator3.hide()
                    ivBallIndicator4.hide()
                    ivBallIndicator5.hide()
                    ivBallIndicator6.hide()
                    tvFilesData.text = ""
                }

            }

            override fun onAnimationRepeat(animation: Animation) {
                check++
                startAnim(check)
            }
        })
        activityScanningJunkBinding.ivScanningMain.startAnimation(rotate)

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (prog < packages.size) {
                        activityScanningJunkBinding.tvFilesData.text = packages[prog].sourceDir
                        prog++
                    } else {
                        timer.cancel()
                        timer.purge()
                    }
                }
            }
        }, 80, 80)
    }

    private fun setupRecyclerViewApps() {
        val apps = mutableListOf<Apps>()
        val junkAppsViewAdapter = JunkAppsViewAdapter(apps)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        activityScanningJunkBinding.recyclerView.apply {
            itemAnimator = SlideInLeftAnimator()
            layoutManager = linearLayoutManager
            itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
            computeHorizontalScrollExtent()
            adapter = junkAppsViewAdapter
        }


        junkAppsViewAdapter.notifyDataSetChanged()
        setupContentRecyclerView(apps, junkAppsViewAdapter)
    }

    private fun setupContentRecyclerView(apps: MutableList<Apps>, adapter: JunkAppsViewAdapter) {
        Handler().postDelayed({ addArrayApps(apps, adapter, 0) }, 1000)
        Handler().postDelayed({ addArrayApps(apps, adapter, 1) }, 2000)
        Handler().postDelayed({ addArrayApps(apps, adapter, 2) }, 3000)
        Handler().postDelayed({ addArrayApps(apps, adapter, 3) }, 4000)
        Handler().postDelayed({ removeArrayApps(apps, adapter, 0) }, 5000)
        Handler().postDelayed({ removeArrayApps(apps, adapter, 0) }, 6000)
        Handler().postDelayed({ removeArrayApps(apps, adapter, 0) }, 7000)

        Handler().postDelayed({
            removeArrayApps(apps, adapter, 0)

            activityScanningJunkBinding.apply {

                rippleBackground.startRippleAnimation()
                ivScanningBackground.visibility = View.INVISIBLE
                ivScanningMain.visibility = View.INVISIBLE
                ivImageDone.setImageResource(R.drawable.ic_task_done_main)

                loadingIndicator.setIndeterminateDrawable(ThreeBounce())
                loadingIndicator.visibility = View.GONE

                val sumJunk = intent.extras?.getString(Constant.Variable.SHARED_PREF_JUNK)
                tvScanning.text = "$sumJunk MB of Junk Files Are Cleared"

                val anim = AnimatorInflater.loadAnimator(this@ScanningJunkActivity, R.animator.flipping) as ObjectAnimator
                anim.target = ivImageDone
                anim.duration = 3000
                anim.start()

                anim.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                        val cache = intent.extras?.getString(Constant.Variable.SHARED_PREF_JUNK)
                        tvScanning.text = "Cleared $cache MB"
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        setupFinishCleaningJunk()
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })

                tvFilesData.text = ""
            }

        }, 8000)
    }

    private fun setupFinishCleaningJunk() {
        activityScanningJunkBinding.rippleBackground.stopRippleAnimation()
        Handler().postDelayed({ finish() }, 1000)
    }

    private fun startAnim(timePosition: Int) {
        when (timePosition) {
            1 -> {
                activityScanningJunkBinding.apply {
                    ivBallIndicator1.show()
                    ivBallIndicator3.show()
                    ivBallIndicator5.show()

                    ivBallIndicator2.hide()
                    ivBallIndicator4.hide()
                    ivBallIndicator6.hide()
                }
            }
            2 -> {
                activityScanningJunkBinding.apply {
                    ivBallIndicator2.show()
                    ivBallIndicator4.show()
                    ivBallIndicator6.show()

                    ivBallIndicator1.hide()
                    ivBallIndicator3.hide()
                    ivBallIndicator5.hide()
                }

            }
            3 -> {
                activityScanningJunkBinding.apply {
                    ivBallIndicator2.show()
                    ivBallIndicator4.show()
                    ivBallIndicator6.show()

                    ivBallIndicator1.hide()
                    ivBallIndicator3.hide()
                    ivBallIndicator5.hide()
                }
            }
            4 -> {
                activityScanningJunkBinding.apply {
                    ivBallIndicator2.show()
                    ivBallIndicator3.show()
                    ivBallIndicator5.show()

                    ivBallIndicator1.hide()
                    ivBallIndicator2.hide()
                    ivBallIndicator6.hide()
                }

            }
        }
    }

    private fun addArrayApps(apps: MutableList<Apps>, adapter: JunkAppsViewAdapter, position: Int) {

        val positionRandom = 0 + (Math.random() * (packages.size - 1 - 0 + 1)).toInt()
        val packageName = packages[positionRandom].packageName
        val pName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)) as String
        val a = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val ico = packageManager.getApplicationIcon(packages[positionRandom].packageName)

        apps.add(Apps(packages[positionRandom].dataDir, ico))
        adapter.notifyItemInserted(position)
    }

    private fun removeArrayApps(apps: MutableList<Apps>, adapter: JunkAppsViewAdapter, position: Int) {
        adapter.notifyItemRemoved(position)
        apps.removeAt(position)
    }
}