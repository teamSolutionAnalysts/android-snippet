package com.login.demo.utils.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.login.base.base.BaseFragment
import com.login.demo.R
import com.login.demo.utils.FragmentState
import java.util.regex.Matcher
import java.util.regex.Pattern

fun <T> BaseFragment.replaceFragment(
    parentFragment: Fragment,
    containerId: Int,
    fragmentEnum: FragmentState,
    keys: T?,
    isAnimation: Boolean
) {
    ft = this.childFragmentManager.beginTransaction()
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
    val fragment =
        Fragment.instantiate(parentFragment.requireActivity(), fragmentEnum.fragment.name)

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
fun <T> BaseFragment.addFragmentInStack(
    parentFragment: Fragment,
    containerId: Int,
    fragmentEnum: FragmentState,
    keys: T?,
    isAnimation: Boolean
) {
    ft = this.childFragmentManager.beginTransaction()
    if (isAnimation) {
        ft?.setCustomAnimations(
            R.anim.anim_enter_from_right,
            R.anim.anim_exit_to_left,
            R.anim.anim_enter_from_left,
            R.anim.anim_exit_to_right
        )
    }
    val fragment =
        Fragment.instantiate(parentFragment.requireActivity(), fragmentEnum.fragment.name)
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
fun BaseFragment.popFragment(isAnimation: Boolean) {
    ft = this.childFragmentManager.beginTransaction()

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

    setUpFragmentConfig<Any>(FragmentState.getValue(stack.lastElement().javaClass), null)
}

// When not to resume last fragment
fun BaseFragment.popFragment(numberOfFragment: Int) {
    val ft = this.childFragmentManager.beginTransaction()
    for (i in 0 until numberOfFragment) {
        if (!stack.isEmpty()) {
            stack.lastElement().onPause()
            ft.remove(stack.pop())
        }
    }

    if (!stack.isEmpty())
        ft.show(stack.lastElement())

    ft.commit()
    setUpFragmentConfig<Any>(FragmentState.getValue(stack.lastElement().javaClass), null)
}

// When not to resume last fragment
fun <T> BaseFragment.popFragment(
    numberOfFragment: Int,
    appFragmentState: FragmentState,
    bundle: T
) {
    ft = this.childFragmentManager.beginTransaction()
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
    if (!stack.isEmpty())
        ft?.show(fragment)
    ft?.commit()
    setUpFragmentConfig<Any>(FragmentState.getValue(stack.lastElement().javaClass), null)
}


fun BaseFragment.getLastFragment(): Fragment? {
    if (!stack.empty())
        return stack.lastElement()
    else return null
}

fun getFragment(parentFragment: Fragment, appFragmentState: FragmentState): Fragment? {
    return parentFragment.childFragmentManager.findFragmentByTag(appFragmentState.fragment.name)
}


// To bring already fragment in stack to top
fun <T> BaseFragment.moveFragmentToTop(
    parentFragment: Fragment,
    appFragmentState: FragmentState,
    `object`: T,
    isAnimation: Boolean
) {

    ft = parentFragment.childFragmentManager.beginTransaction()
    if (isAnimation) {
        ft?.setCustomAnimations(
            R.anim.anim_enter_from_right,
            R.anim.anim_exit_to_left,
            R.anim.anim_enter_from_left,
            R.anim.anim_exit_to_right
        )
    }

    val fragment = getFragment(parentFragment, appFragmentState)
    val position = stack.indexOf(fragment)

    if (position > -1) {
        stack.removeAt(position)
        ft?.hide(stack.lastElement())//todo handle java.util.NoSuchElementException
        stack.push(fragment)
        if (!stack.isEmpty()) {
            stack.lastElement().onResume()
            ft?.show(stack.lastElement())
        }
        ft?.commit()
    }
    setUpFragmentConfig<Any>(appFragmentState, null)
}

fun <T> BaseFragment.addFragment(
    parentFragment: Fragment,
    containerId: Int,
    fragmentEnum: FragmentState,
    keys: Any?,
    isAnimation: Boolean
) {
    val availableFragment = getFragment(parentFragment, fragmentEnum)
    if (availableFragment != null) {
        moveFragmentToTop(parentFragment, fragmentEnum, keys, isAnimation)
    } else {
        addFragmentInStack(parentFragment, containerId, fragmentEnum, keys, isAnimation)
    }
}

fun BaseFragment.clearAllFragment() {
    val supportFragmentManager = childFragmentManager
    for (entry in 0 until supportFragmentManager.backStackEntryCount) {
        val tag = supportFragmentManager.getBackStackEntryAt(entry).name
        val showFragment = supportFragmentManager.findFragmentByTag(tag)
        if (showFragment != null && entry != 0) {
            showFragment.userVisibleHint = false
        }
    }
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun findNumericValue(data: String): Int {
    var value = 0
    val p: Pattern = Pattern.compile("-?\\d+")
    val m: Matcher = p.matcher(data)
    while (m.find()) {
        value = m.group().toInt()
    }
    return value
}