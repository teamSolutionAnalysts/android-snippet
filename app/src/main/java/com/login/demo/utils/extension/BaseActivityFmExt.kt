package com.login.demo.utils.extension

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.login.base.base.BaseActivity
import com.login.base.base.BaseFragment
import com.login.demo.R
import com.login.demo.utils.FragmentState
import java.lang.Exception

// called when fragment back pressed
internal fun BaseActivity.notifyFragment(isAnimation: Boolean) {
    if (stack.size > 1) {
        popFragment(isAnimation)
    } else {
        this.finish()
    }
}


fun <T> BaseActivity.replaceFragment(
    containerId: Int,
    fragmentEnum: FragmentState,
    keys: T?,
    isAnimation: Boolean
) {
    ft = this.supportFragmentManager.beginTransaction()
    for (i in 0..stack.size) {
        if (!stack.isEmpty()) {
            stack.lastElement().onPause()
            ft?.remove(stack.pop())
        }
    }
    if (isAnimation) {
        ft?.setCustomAnimations(
            R.anim.anim_enter_from_right,
            R.anim.anim_exit_to_left,
            R.anim.anim_enter_from_left,
            R.anim.anim_exit_to_right
        )
    }
    val fragment = Fragment.instantiate(this, fragmentEnum.fragment.name)

    if (keys != null && keys is Bundle) {
        fragment.arguments = keys
    }

    ft?.add(containerId, fragment, fragmentEnum.fragment.name)
    if (!stack.isEmpty()) {
        stack.lastElement().onPause()
        ft?.hide(stack.lastElement())
    }
    stack.push(fragment)
    ft?.commitAllowingStateLoss()
    setUpFragmentConfig(fragmentEnum, keys)
}

// Call For Fragment Switch
fun <T> BaseActivity.addFragmentInStack(
    containerId: Int,
    fragmentEnum: FragmentState,
    keys: T?,
    isAnimation: Boolean
) {
    ft = this.supportFragmentManager.beginTransaction()
    if (isAnimation) {
        ft?.setCustomAnimations(
            R.anim.anim_enter_from_right,
            R.anim.anim_exit_to_left,
            R.anim.anim_enter_from_left,
            R.anim.anim_exit_to_right
        )
    }
    val fragment = Fragment.instantiate(this, fragmentEnum.fragment.name)
    if (keys != null && keys is Bundle) {
        fragment.arguments = keys
    }
    ft?.add(containerId, fragment, fragmentEnum.fragment.name)
    if (!stack.isEmpty()) {
        stack.lastElement().onPause()
        ft?.hide(stack.lastElement())
    }
    stack.push(fragment)
    ft?.commitAllowingStateLoss()
    setUpFragmentConfig(fragmentEnum, keys)
}

// When to resume last fragment and to pop only one fragment
fun BaseActivity.popFragment(isAnimation: Boolean) {
    ft = this.supportFragmentManager.beginTransaction()

    if (isAnimation) {
        ft?.setCustomAnimations(
            R.anim.anim_enter_from_left,
            R.anim.anim_exit_to_right,
            R.anim.anim_enter_from_right,
            R.anim.anim_exit_to_left
        )
    }
    stack.lastElement().onPause()
    ft?.remove(stack.pop())
    stack.lastElement().onResume()
    ft?.show(stack.lastElement())
    ft?.commit()

    (stack.lastElement() as BaseFragment).setUpFragmentConfig<Any>(
        FragmentState.getValue(stack.lastElement().javaClass),
        null
    )
    setUpFragmentConfig<Any>(FragmentState.getValue(stack.lastElement().javaClass), null)
}

fun <T> BaseActivity.addFragmentAlwaysNew(
    containerId: Int,
    fragmentEnum: FragmentState,
    keys: Any?,
    isAnimation: Boolean
) {
    addFragmentInStack(containerId, fragmentEnum, keys, isAnimation)
}



// When not to resume last fragment
fun BaseActivity.popFragment(numberOfFragment: Int, isAnimation: Boolean) {
    val ft = this.supportFragmentManager.beginTransaction()
    if (isAnimation) {
        ft.setCustomAnimations(
            R.anim.anim_enter_from_left,
            R.anim.anim_exit_to_right,
            R.anim.anim_enter_from_right,
            R.anim.anim_exit_to_left
        )
    }

    for (i in 0 until numberOfFragment) {
        if (!stack.isEmpty()) {
            stack.lastElement().onPause()
            ft.remove(stack.pop())
        }
    }

    if (!stack.isEmpty())
        stack.lastElement().onResume()
    ft.show(stack.lastElement())
    ft.commit()
    setUpFragmentConfig<Any>(FragmentState.getValue(stack.lastElement().javaClass), null)
}

// When not to resume last fragment
fun <T> BaseActivity.popFragment(
    numberOfFragment: Int,
    bundle: T,
    isAnimation: Boolean
) {
    try {
        ft = this.supportFragmentManager.beginTransaction()

        if (isAnimation)
            ft?.setCustomAnimations(
                R.anim.anim_enter_from_left,
                R.anim.anim_exit_to_right,
                R.anim.anim_enter_from_right,
                R.anim.anim_exit_to_left
            )

        for (i in 0 until numberOfFragment) {
            if (!stack.isEmpty()) {
                stack.lastElement().onPause()
                ft?.remove(stack.pop())
            }
        }

        val fragment = stack.lastElement()
        if (!stack.isEmpty()) {
            ft?.show(fragment)
            stack.lastElement().onResume()
        }
        ft?.commit()
        (stack.lastElement() as BaseFragment).setUpFragmentConfig<Any>(
            FragmentState.getValue(stack.lastElement().javaClass),
            bundle
        )
        setUpFragmentConfig<Any>(FragmentState.getValue(stack.lastElement().javaClass), bundle)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun BaseActivity.getLastFragment(): Fragment? {
    if (!stack.empty())
        return stack.lastElement()
    else return null
}

fun getFragment(activity: BaseActivity, appFragmentState: FragmentState): Fragment? {
    return activity.supportFragmentManager.findFragmentByTag(appFragmentState.fragment.name)
}


// To bring already fragment in stack to top
fun <T> BaseActivity.moveFragmentToTop(
    activity: BaseActivity, appFragmentState: FragmentState, `object`: T, isAnimation: Boolean
) {

    ft = activity.supportFragmentManager.beginTransaction()
    if (isAnimation) {
        ft?.setCustomAnimations(
            R.anim.anim_enter_from_right,
            R.anim.anim_exit_to_left,
            R.anim.anim_enter_from_left,
            R.anim.anim_exit_to_right
        )
    }

    val fragment = getFragment(activity, appFragmentState)
    val position = stack.indexOf(fragment)

    if (position > -1) {
        stack.removeAt(position)
        ft?.hide(stack.lastElement())
        stack.push(fragment)
        if (!stack.isEmpty()) {
            stack.lastElement().onResume()
            ft?.show(stack.lastElement())
        }
        ft?.commit()
    }
    activity.setUpFragmentConfig<Any>(appFragmentState, `object`)
}

fun <T> BaseActivity.addFragment(
    containerId: Int,
    fragmentEnum: FragmentState,
    keys: Any?, isAnimation: Boolean
) {
    val availableFragment = getFragment(this, fragmentEnum)
    if (availableFragment != null) {
        moveFragmentToTop(this, fragmentEnum, keys, isAnimation)
    } else {
        addFragmentInStack(containerId, fragmentEnum, keys, isAnimation)
    }
}

fun BaseActivity.clearAllFragment() {
    val supportFragmentManager = supportFragmentManager
    for (entry in 0 until supportFragmentManager.backStackEntryCount) {
        val tag = supportFragmentManager.getBackStackEntryAt(entry).name
        val showFragment = supportFragmentManager.findFragmentByTag(tag)
        if (showFragment != null && entry != 0) {
            showFragment.userVisibleHint = false
        }
    }
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun isAvailable(activity: Activity?): Boolean {
    if (activity != null && !activity.isFinishing) {
        return true
    }
    return false
}

fun BaseActivity.removeFragment(fragment: Fragment) {
    ft = this.supportFragmentManager.beginTransaction()

    val position = stack.indexOf(fragment)
    if (position > -1) {
        ft?.remove(stack.removeAt(position))
        ft?.commit()
    }
}