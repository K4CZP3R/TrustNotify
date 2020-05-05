package xyz.k4czp3r.trustnotify


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import xyz.k4czp3r.trustnotify.models.Page
import xyz.k4czp3r.trustnotify.user_interface.FragmentAbout
import xyz.k4czp3r.trustnotify.user_interface.FragmentSettings
import xyz.k4czp3r.trustnotify.user_interface.FragmentSummary

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentPagerAdapter: FragmentPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private val summaryPage = Page(
        "Summary",
        R.drawable.ic_lock_screen,
        0
    )
    private val settingsPage = Page(
        "Settings",
        R.drawable.ic_settings,
        1
    )
    private val aboutPage = Page(
        "About",
        R.drawable.ic_about,
        2
    )

    private val pages = listOf(summaryPage, settingsPage, aboutPage)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.am_view_pager)
        fragmentPagerAdapter = KspPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentPagerAdapter

        tabLayout = findViewById(R.id.am_tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        for (page in pages) {
            tabLayout.getTabAt(page.getPagePosition())?.setIcon(page.getPageIcon())
            tabLayout.getTabAt(page.getPagePosition())?.text = page.getPageName()
        }

    }


    class KspPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(
        fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        private val tagName = KspPagerAdapter::class.qualifiedName

        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            Log.v(tagName, String.format("Getting item no.%s", position))
            return when (position) {
                0 -> FragmentSummary.newInstance()
                1 -> FragmentSettings.newInstance()
                2 -> FragmentAbout.newInstance()
                else -> {
                    throw IllegalStateException("This item does not exist!")
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            Log.v(tagName, String.format("Getting page title of no.%s", position))
            return position.toString()
        }
    }


}
