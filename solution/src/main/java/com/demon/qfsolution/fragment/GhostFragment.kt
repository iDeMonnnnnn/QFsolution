package com.demon.qfsolution.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

class GhostFragment : Fragment() {

    private var requestCode = 0x1024
    private var intent: Intent? = null
    private var callback: ((result: Intent?) -> Unit)? = null

    fun init(intent: Intent, callback: ((result: Intent?) -> Unit)) {
        this.requestCode = requestCode
        this.intent = intent
        this.callback = callback
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        intent?.let { startActivityForResult(it, requestCode) }
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
