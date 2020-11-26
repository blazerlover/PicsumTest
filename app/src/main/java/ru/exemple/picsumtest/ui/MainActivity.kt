package ru.exemple.picsumtest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.exemple.picsumtest.R
import ru.exemple.picsumtest.utils.AUTHORIZATION_FRAGMENT_TAG
import ru.exemple.picsumtest.utils.PICTURE_FRAGMENT_TAG

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var activeFragment: Fragment
    private var picturesFragment = PicturesFragment()
    private var authorizationFragment = AuthorizationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firstTransaction()
        bottomNavigationView = findViewById(R.id.main_activity__bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            navigate(it)
            true
        }
        bottomNavigationView.selectedItemId = R.id.bottom_nav_menu__pictures_fragment
    }

    private fun firstTransaction() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.main_activity__fragment_container, authorizationFragment, PICTURE_FRAGMENT_TAG)
                .hide(authorizationFragment)
                .commit()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.main_activity__fragment_container, picturesFragment, AUTHORIZATION_FRAGMENT_TAG)
                .commit()
        activeFragment = picturesFragment
        activeFragment = if (picturesFragment.isVisible) {
            picturesFragment
        } else {
            authorizationFragment
        }
    }

    private fun navigate(item: MenuItem) {
        when (item.itemId) {
            R.id.bottom_nav_menu__pictures_fragment -> {
                supportFragmentManager
                        .beginTransaction()
                        .hide(activeFragment)
                        .show(picturesFragment)
                        .commit()
                activeFragment = picturesFragment
            }
            R.id.bottom_nav_menu__authorization_fragment -> {
                supportFragmentManager
                        .beginTransaction()
                        .hide(activeFragment)
                        .show(authorizationFragment)
                        .commit()
                activeFragment = authorizationFragment
            }
        }
    }
}