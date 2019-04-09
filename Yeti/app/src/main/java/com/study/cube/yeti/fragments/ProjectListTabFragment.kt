package com.study.cube.yeti.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.Toast
import com.study.cube.yeti.R
import com.study.cube.yeti.eventBus.BusProvider
import com.study.cube.yeti.eventBus.EventKind
import kotlinx.android.synthetic.main.fragment_project_list_tab.*
import org.greenrobot.eventbus.EventBus

class ProjectListTabFragment : Fragment() {
    var fragmentTitle : String? = null
    var tabIndex : Int? = null
    var className : String = ProjectListTabFragment::class.java.name
    private var view1: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view1 = inflater?.inflate(R.layout.fragment_project_list_tab, container, false)

        //Log.e(className,fragmentTitle + " tabIndex : " + tabIndex)
        setHasOptionsMenu(true)     // OptionMenu를 사용할지 결정

        return view1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // -------------------------------------------------------------------------- 탭레이아웃 셋팅 시작
        setupViewPager(projectListTab_viewPager)
        projectListTab_tabLayout.setupWithViewPager(projectListTab_viewPager)

        tabIndex?.let {
            // 완료(2), 진행(1) 넘어오지만 탭포지션은 다르기 때문에 별도 변수지정
            var _index : Int = 0
            when(it) {
                1 -> { _index = 2 }
                2 -> { _index = 1 }
            }

            projectListTab_viewPager.setCurrentItem(_index, true)
            Log.e(className, tabIndex.toString())
        }
        // -------------------------------------------------------------------------- 탭레이아웃 셋팅 종료
    }

    private fun setupViewPager(pager: ViewPager?) {
        val adapter = Adapter2(activity!!.supportFragmentManager)

        val f1 = ProjectListFragment.newInstance("프로젝트리스트-전체", 0)
        adapter.addFragment(f1, "전체 99+")

        val f2 = ProjectListFragment.newInstance("프로젝트리스트-완료", 2)
        adapter.addFragment(f2, "완료 5")

        val f3 = ProjectListFragment.newInstance("프로젝트리스트-진행", 1)
        adapter.addFragment(f3, "진행 94")

        pager?.adapter = adapter
    }

    class Adapter2(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        private val fragments = ArrayList<Fragment>()
        private val titles = ArrayList<String>()

        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size

        override fun getPageTitle(position: Int): CharSequence? = titles[position]

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }
    }

    class Adapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

        private val fragments = ArrayList<Fragment>()
        private val titles = ArrayList<String>()

        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size

        override fun getPageTitle(position: Int): CharSequence? = titles[position]

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        Log.d(className, "onCreateOptionsMenu")

        inflater?.inflate(R.menu.menu_project_list,menu)
        menu?.let {
            var searchItem : MenuItem = menu.findItem(R.id.projectListMenuItem_searchView)
            var searchView = searchItem.actionView as SearchView
            searchView?.let {
                it.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        Toast.makeText(view?.context,query, Toast.LENGTH_SHORT).show()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        Log.d(className, "onQueryTextChange : " + newText)
                        return true
                    }
                })
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            R.id.projectListMenuItem_add -> {
                EventBus.getDefault().post(BusProvider(ProjectNewStep1Fragment::class.java.name, EventKind.BIND_FRAGMENT, null))
                return true
            }
            else -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val KEY_TITLE = "ProjectListTabFragment"

        fun newInstance(title: String, _tabIndex: Int?) =
                ProjectListTabFragment().apply {
                    fragmentTitle = title
                    tabIndex = _tabIndex
                    val args = Bundle()
                    args.putString(KEY_TITLE, title)
                    arguments = args
                }
    }
}