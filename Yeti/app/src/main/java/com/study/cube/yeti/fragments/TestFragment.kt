package com.study.cube.yeti.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.study.cube.yeti.R
import kotlinx.android.synthetic.main.fragment_test.*

class TestFragment : Fragment() {
    var fragmentTitle : String? = null
    var tabIndex : Int? = null
    var className : String = TestFragment::class.java.name
    private var view1: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view1 = inflater?.inflate(R.layout.fragment_test, container, false)

        //Log.e(className,fragmentTitle + " tabIndex : " + tabIndex)

        // 툴바 좌측 메뉴 노출
        // 이 부분을 설정해줘야 onCreateOptionsMenu 함수가 호출 됩니다.
        setHasOptionsMenu(true)     // OptionMenu를 사용할지 결정

        return view1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViewPager(pager)
        tabs.setupWithViewPager(pager)
    }

    private fun setupViewPager(pager: ViewPager?) {
        val adapter = Adapter(activity!!.supportFragmentManager)

        val f1 = NoticeFragment.newInstance("Home")
        adapter.addFragment(f1, "Home")

        val f2 = ProjectListFragment.newInstance("Dashboard", 0)
        adapter.addFragment(f2, "전체 프로젝트")

        val f3 = ProjectListFragment.newInstance("Dashboard", 1)
        adapter.addFragment(f3, "완료 프로젝트")

        pager?.adapter = adapter
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

    companion object {
        private const val KEY_TITLE = "TestFragment"

        fun newInstance(title: String, _tabIndex: Int?) =
                TestFragment().apply {
                    fragmentTitle = title
                    tabIndex = _tabIndex
                    val args = Bundle()
                    args.putString(KEY_TITLE, title)
                    arguments = args
                }
    }
}
