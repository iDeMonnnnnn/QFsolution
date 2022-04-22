package com.demon.qf_app

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author DeMon
 * Created on 2022/2/28.
 * E-mail idemon_liu@qq.com
 * Desc:携程相关扩展
 */

/**
 * io线程
 */
fun CoroutineScope.launchIO(block: suspend () -> Unit): Job {
    return this.launch(Dispatchers.IO) { block() }
}

/**
 * 主线程
 */
fun CoroutineScope.launchUI(block: suspend () -> Unit): Job {
    return this.launch(Dispatchers.Main) { block() }
}

fun LifecycleOwner.launch(block: suspend () -> Unit): Job {
    return lifecycleScope.launch { block() }
}

fun LifecycleOwner.launchUI(block: suspend () -> Unit): Job {
    return lifecycleScope.launch(Dispatchers.Main) { block() }
}

fun LifecycleOwner.launchIO(block: suspend () -> Unit): Job {
    return lifecycleScope.launch(Dispatchers.IO) { block() }
}
