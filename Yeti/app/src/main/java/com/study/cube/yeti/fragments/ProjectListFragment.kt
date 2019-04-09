package com.study.cube.yeti.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.study.cube.yeti.R
import com.study.cube.yeti.eventBus.BusProvider
import com.study.cube.yeti.eventBus.EventKind
import com.study.cube.yeti.utils.CircleTransform
import kotlinx.android.synthetic.main.fragment_project_list.*
import org.greenrobot.eventbus.EventBus

class ProjectListFragment : Fragment() {
    var fragmentTitle : String? = null
    var tabIndex : Int? = null
    var className : String = ProjectListFragment::class.java.name
    private var view1: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view1 = inflater?.inflate(R.layout.fragment_project_list, container, false)

        //Log.e(className,fragmentTitle + " tabIndex : " + tabIndex)

//        // 툴바 좌측 메뉴 노출
//        // 이 부분을 설정해줘야 onCreateOptionsMenu 함수가 호출 됩니다.
//        setHasOptionsMenu(true)     // OptionMenu를 사용할지 결정

        return view1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // 키보드 오픈시 숨김
        EventBus.getDefault().post(BusProvider(className, EventKind.HIDE_KEYBOARD, null))

        //
        //EventBus.getDefault().post(BusProvider(className, EventKind.SHOW_SNACKBAR, tabIndex.toString()))

        // 리스트
        projectList_recyclerview.adapter = ProjectRecyclerViewAdapter()
        projectList_recyclerview.layoutManager = LinearLayoutManager(context)

//        // 탭레이아웃 선택
//        tabIndex?.let {
//            projectList_tabLayout.setScrollPosition(tabIndex!!,0f,true)
//        }
//
//        // 탭레이아웃
//        projectList_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//            }
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//            }
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                Log.d(className,tab?.position.toString())
//            }
//        })

        super.onActivityCreated(savedInstanceState)
    }

    // 현재 보고있는 Fragment를 알기위해 (viewPager 에 바인딩된 fragment 해당)
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            // 보임
            Log.e(className, "setUserVisibleHint $tabIndex")
        } else {
            // 안보임
        }

        super.setUserVisibleHint(isVisibleToUser)
    }

    class ProjectRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var className : String = ProjectListFragment::class.java.name
        var titles = arrayOf("연구아이템은 선정중입니다구아이템은 선정중입니다구아이템은 선정중입니다구아이템은 선정중입니다.", "조직을 재구성중입니다.", "프로젝트를 시작했습니다.", "테스트진행중입니다.","큐브랩 연구소가 오픈했습니다."
                ,"연구아이템은 선정중입니다.", "조직을 재구성중입니다.", "프로젝트를 시작했습니다.", "테스트진행중입니다.","큐브랩 연구소가 오픈했습니다.")
        var dates = arrayOf("12시간전", "1주일전", "1일전", "10분전", "2시간전"
                ,"3일전", "2주일전", "30분전", "10초전", "9일전")

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_project_list, parent, false)
            return ProjectListFragment.ProjectRecyclerViewAdapter.CustomViewHolder(view)
        }

        class CustomViewHolder(view:View?) : RecyclerView.ViewHolder(view) {
            var textView_title : TextView? = null
            var textView_date : TextView? = null
            var imageView_ownerProfile : ImageView? = null
            var imageView_btnPopup : ImageView? = null
            init {
                textView_title = view?.findViewById(R.id.projectList_textView_title)
                textView_date = view?.findViewById(R.id.projectList_textView_date)
                imageView_ownerProfile = view?.findViewById(R.id.productList_imageView_ownerProfile)
                imageView_btnPopup = view?.findViewById(R.id.productList_imageView_btnPopup)
            }
        }

        override fun getItemCount(): Int {
            return titles.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var view = holder as ProjectListFragment.ProjectRecyclerViewAdapter.CustomViewHolder
            var title : String? = titles[position]
            title?.let {
                //if (it.length > 16) { title = it.substring(0, 15) + "..." }
            }

            view.textView_title!!.text = title
            view.textView_date!!.text = dates[position]

            // 이미지 바인딩
            var profileUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTvIMLwvLcK1kmabi1dC6ATGCMqsZXaZHfP-ZSLqBy33p2LtF1Z"
            Picasso.get().load(profileUrl)
                    .transform(CircleTransform())
                    .error(R.drawable.profile2)
                    .into(view.imageView_ownerProfile as ImageView)

            // 클릭이벤트
            view!!.textView_title!!.setOnClickListener {
                Log.d(className, title)
                EventBus.getDefault().post(BusProvider(ProjectTaskListFragment::class.java.name, EventKind.BIND_FRAGMENT,null,null
                                                        , BusProvider.Project(title, "")))
            }

            // 클릭이벤트 - ...
            view!!.imageView_btnPopup!!.setOnClickListener {
                EventBus.getDefault().post(BusProvider(null, EventKind.SHOW_SNACKBAR, title))
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        Log.d(className, "onCreateOptionsMenu")
//
//        inflater?.inflate(R.menu.menu_project_list,menu)
//        menu?.let {
//            var searchItem : MenuItem = menu.findItem(R.id.projectListMenuItem_searchView)
//            var searchView = searchItem.actionView as SearchView
//            searchView?.let {
//                it.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//                    override fun onQueryTextSubmit(query: String?): Boolean {
//                        Toast.makeText(view?.context,query, Toast.LENGTH_SHORT).show()
//                        return true
//                    }
//
//                    override fun onQueryTextChange(newText: String?): Boolean {
//                        Log.d(className, "onQueryTextChange : " + newText)
//                        return true
//                    }
//                })
//            }
//        }
//
//        super.onCreateOptionsMenu(menu, inflater)
//    }

    companion object {
        private const val KEY_TITLE = "ProjectListFragment"

        fun newInstance(title: String, _tabIndex: Int?) =
                ProjectListFragment().apply {
                    fragmentTitle = title
                    tabIndex = _tabIndex
                    val args = Bundle()
                    //args.putString(KEY_TITLE, title)
                    args.putString(title, _tabIndex.toString())
                    arguments = args
                }
    }
}
