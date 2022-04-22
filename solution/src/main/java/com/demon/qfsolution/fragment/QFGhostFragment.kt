package com.demon.qfsolution.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.random.Random

class QFGhostFragment : Fragment() {

    private var requestCode = 0x1024
    private var intent: Intent? = null
    private var callback: ((result: Intent?) -> Unit)? = null

    fun init(intent: Intent, callback: ((result: Intent?) -> Unit)) {
        this.requestCode = Random.nextInt(1, 1000)
        this.intent = intent
        this.callback = callback
    }

    //https://github.com/wuyr/ActivityMessenger/issues/6
    private var activityStarted = false

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (!activityStarted) {
            activityStarted = true
            intent?.let { startActivityForResult(it, requestCode) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!activityStarted) {
            activityStarted = true
            intent?.let { startActivityForResult(it, requestCode) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == this.requestCode) {
            callback?.let { it(data) }
        }
    }

    override fun onDetach() {
        super.onDetach()
        intent = null
        callback = null
    }
}


inline fun Fragment.qfActivityForResult(
    intent: Intent,
    requestCode: Int = Random.nextInt(1, 1000),
    crossinline callback: ((result: Intent?) -> Unit)
) {
    val fragment = QFGhostFragment()
    fragment.init(intent) { result ->
        callback(result)
        childFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
    childFragmentManager.beginTransaction().add(fragment, QFGhostFragment::class.java.simpleName)
        .commitAllowingStateLoss()
}

inline fun FragmentActivity.qfActivityForResult(
    intent: Intent,
    crossinline callback: ((result: Intent?) -> Unit)
) {
    val fragment = QFGhostFragment()
    fragment.init(intent) { result ->
        callback(result)
        supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
    supportFragmentManager.beginTransaction().add(fragment, QFGhostFragment::class.java.simpleName)
        .commitAllowingStateLoss()
}
