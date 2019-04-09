package com.study.cube.yeti.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.study.cube.yeti.R
import com.study.cube.yeti.eventBus.BusProvider
import com.study.cube.yeti.eventBus.EventKind
import kotlinx.android.synthetic.main.fragment_notice.*
import org.greenrobot.eventbus.EventBus

class NoticeFragment : Fragment() {
    var fragmentTitle : String? = null
    var className : String = NoticeFragment::class.java.name
    private var view1: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view1 = inflater?.inflate(R.layout.fragment_notice, container, false)

//        // 닫기버튼
//        val imgClose = view1!!.findViewById<ImageView>(R.id.notice_back)
//        imgClose.setOnClickListener {
//            EventBus.getDefault().post(BusProvider(className, EventKind.GO_MAIN, null))
//        }

        // 툴바 좌측 메뉴 노출
        // 이 부분을 설정해줘야 onCreateOptionsMenu 함수가 호출 됩니다.
        setHasOptionsMenu(true)     // OptionMenu를 사용할지 결정

        return view1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {
            // 리스트
            notice_recyclerview.adapter = NoticeRecyclerViewAdapter()
            notice_recyclerview.layoutManager = LinearLayoutManager(context)

            // notice_recyclerview.addItemDecoration(DividerItemDecoration())
        } catch (e : Exception) {
            Log.e(className, e.toString())
        }
    }

    class NoticeRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var className : String = NoticeFragment::class.java.name
        var titles = arrayOf("연구아이템은 선정중입니다구아이템은 선정중입니다구아이템은 선정중입니다구아이템은 선정중입니다.", "조직을 재구성중입니다.", "프로젝트를 시작했습니다.", "테스트진행중입니다.","큐브랩 연구소가 오픈했습니다."
                            ,"연구아이템은 선정중입니다.", "조직을 재구성중입니다.", "프로젝트를 시작했습니다.", "테스트진행중입니다.","큐브랩 연구소가 오픈했습니다."
                            ,"연구아이템은 선정중입니다.", "조직을 재구성중입니다.", "프로젝트를 시작했습니다.", "테스트진행중입니다.","큐브랩 연구소가 오픈했습니다."
                            ,"연구아이템은 선정중입니다.", "조직을 재구성중입니다.", "프로젝트를 시작했습니다.", "테스트진행중입니다.","큐브랩 연구소가 오픈했습니다."
                            ,"연구아이템은 선정중입니다.", "조직을 재구성중입니다.", "프로젝트를 시작했습니다.", "테스트진행중입니다.","큐브랩 연구소가 오픈했습니다.")
        var dates = arrayOf("2018.09.05", "2018.08.01", "2018.05.01", "2018.04.01", "2018.01.10"
                            ,"2018.09.05", "2018.08.01", "2018.05.01", "2018.04.01", "2018.01.10"
                            ,"2018.09.05", "2018.08.01", "2018.05.01", "2018.04.01", "2018.01.10"
                            ,"2018.09.05", "2018.08.01", "2018.05.01", "2018.04.01", "2018.01.10"
                            ,"2018.09.05", "2018.08.01", "2018.05.01", "2018.04.01", "2018.01.10")

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_notice_list, parent, false)
            return CustomViewHolder(view)
        }

        class CustomViewHolder(view:View?) : RecyclerView.ViewHolder(view) {
            var textView_title : TextView? = null
            var textView_date : TextView? = null
            init {
                textView_title = view?.findViewById(R.id.notice_textView_title)
                textView_date = view?.findViewById(R.id.notice_textView_date)
            }
        }

        override fun getItemCount(): Int {
            return titles.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var view = holder as CustomViewHolder
            var title : String? = titles[position]

            view.textView_title!!.text = title
            view.textView_date!!.text = dates[position]

            view!!.textView_title!!.setOnClickListener {
                Log.d(NoticeFragment::class.java.name, title)
                EventBus.getDefault().post(BusProvider(NoticeItemFragment::class.java.name, EventKind.BIND_FRAGMENT, null))
            }
        }

    }

    override fun onStart() {
        super.onStart()
    }

    // setHasOptionsMenu() 호출해야 실행됨
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        Log.d(className, "onCreateOptionsMenu")

        inflater?.inflate(R.menu.menu_notice,menu)
        menu?.let {
            var searchItem : MenuItem = menu.findItem(R.id.noticeMenuItem_searchView)
            var searchView = searchItem.actionView as SearchView
            searchView?.let {
                it.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        Toast.makeText(view?.context,query,Toast.LENGTH_SHORT).show()
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

    // 좌측 아이콘 선택시만 동작 : activity에 동일 메서드 있을 경우 activity 우선 실행 후 아래 메서드 실행
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        Log.d(className, "============= onOptionsItemSelected() : " + item?.itemId)

        when (id) {
            R.id.noticeMenuItem_searchView -> {
                Toast.makeText(activity, "Message clicked!", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> {
                Toast.makeText(activity, "else", Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val KEY_TITLE = "NoticeFragment"

        fun newInstance(title: String) =
                NoticeFragment().apply {
                    fragmentTitle = title

                    val args = Bundle()
                    args.putString(KEY_TITLE, title)
                    arguments = args
                }
    }
}
