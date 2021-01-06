package com.najib.ecommerce.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.najib.ecommerce.R
import com.najib.ecommerce.util.Functions
import com.najib.ecommerce.view.fragment.CartFragment
import com.najib.ecommerce.view.fragment.FeedFragment
import com.najib.ecommerce.view.fragment.HomeFragment
import com.najib.ecommerce.view.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mFragmentHome: HomeFragment? = null
    private var mFragmentFeed: FeedFragment? = null
    private var mFragmentCart: CartFragment? = null
    private var mFragmentProfile: ProfileFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    override fun onBackPressed() {
        try {
            val fragment = supportFragmentManager.findFragmentById(R.id.frame_container)
            when {
                fragment is HomeFragment -> finishAffinity()
                else -> super.onBackPressed()
            }
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }

    private fun initView() {
        loadFragment(getFragmentHome())
        bottomNavigation()
        try {
            supportFragmentManager.addOnBackStackChangedListener(getListener())
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }

    private fun bottomNavigation() {
        bottom_nav.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.menu_home -> {
                    loadFragment(getFragmentHome())
                    true
                }
                R.id.menu_feed -> {
                    loadFragment(getFragmentFeed())
                    true
                }
                R.id.menu_cart -> {
                    loadFragment(getFragmentCart())
                    true
                }
                R.id.menu_profile -> {
//                    loadFragment(getFragmentProfile())
                    PurchaseHistoryActivity.launchIntent(this)
                    false
                }
                else -> {
                    loadFragment(getFragmentHome())
                    true
                }
            }
        }
    }

    private fun getListener(): FragmentManager.OnBackStackChangedListener {
        return FragmentManager.OnBackStackChangedListener {
            val currentFragment: Fragment? = getCurrentFragment(supportFragmentManager)
            if (currentFragment != null) {
                when (currentFragment) {
                    is HomeFragment -> {
                        currentFragment.pageState()
                    }
                    is FeedFragment -> {
                        currentFragment.pageState()
                    }
                    is CartFragment -> {
                        currentFragment.pageState()
                    }
                    is ProfileFragment -> {
                        currentFragment.pageState()
                    }
                }
            }
        }
    }

    private fun getCurrentFragment(fm: FragmentManager): Fragment? {
        val stackCount = fm.backStackEntryCount
        if (stackCount > 0) {
            val backEntry =
                fm.getBackStackEntryAt(stackCount - 1)
            return fm.findFragmentByTag(backEntry.name)
        } else {
            val fragments =
                fm.fragments
            if (fragments.size > 0) {
                for (f in fragments) {
                    if (f != null && !f.isHidden) {
                        return f
                    }
                }
            }
        }
        return null
    }

    private fun loadFragment(fragment: Fragment?) {
        if (fragment != null) {
            supportFragmentManager.beginTransaction().apply {
                if (getFragmentHome()?.isAdded == true)
                    hide(getFragmentHome() ?: fragment)
                if (getFragmentFeed()?.isAdded == true)
                    hide(getFragmentFeed() ?: fragment)
                if (getFragmentCart()?.isAdded == true)
                    hide(getFragmentCart() ?: fragment)
                if (getFragmentProfile()?.isAdded == true)
                    hide(getFragmentProfile() ?: fragment)

                if (fragment.isAdded) {
                    show(fragment)
                } else {
                    val tag = fragment.toString()
                    add(frame_container.id, fragment, tag)
                    addToBackStack(tag)
                }
            }.commit()
        }
    }

    fun getFragmentHome(): HomeFragment? {
        if (mFragmentHome == null) {
            mFragmentHome = HomeFragment()
        }
        return mFragmentHome
    }

    fun getFragmentFeed(): FeedFragment? {
        if (mFragmentFeed == null) {
            mFragmentFeed = FeedFragment()
        }
        return mFragmentFeed
    }

    fun getFragmentCart(): CartFragment? {
        if (mFragmentCart == null) {
            mFragmentCart = CartFragment()
        }
        return mFragmentCart
    }

    fun getFragmentProfile(): ProfileFragment? {
        if (mFragmentProfile == null) {
            mFragmentProfile = ProfileFragment()
        }
        return mFragmentProfile
    }

    fun navSelected(menu: Int) {
        bottom_nav.menu.findItem(menu).isChecked = true
    }

    companion object {

        fun launchIntent(context: Context?) {
            context?.let {
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
            }
        }
    }
}