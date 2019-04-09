package com.study.cube.yeti.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import com.study.cube.yeti.R
import com.study.cube.yeti.eventBus.BusProvider
import com.study.cube.yeti.eventBus.EventKind
import kotlinx.android.synthetic.main.fragment_project_new.*
import org.greenrobot.eventbus.EventBus

class ProjectNewStep1Fragment : Fragment() {
    var fragmentTitle : String? = null
    var className : String = ProjectNewStep1Fragment::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_project_new, container, false)

        // 툴바 좌측 메뉴 노출
        // 이 부분을 설정해줘야 onCreateOptionsMenu 함수가 호출 됩니다.
        setHasOptionsMenu(true)     // OptionMenu를 사용할지 결정

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        projectNewStep1_back.setOnClickListener {
//            EventBus.getDefault().post(BusProvider(className, EventKind.GO_MAIN, null))
//        }

        // setting editlayout
        projectNewStep1_editTextLayout_projectName.isCounterEnabled = true
        projectNewStep1_editTextLayout_projectName.counterMaxLength = 50
        projectNewStep1_editTextLayout_projectDec.isCounterEnabled = true
        projectNewStep1_editTextLayout_projectDec.counterMaxLength = 100

        projectNewStep1_button_new.setOnClickListener {
            if (validationInput()) {
                // 다음 단계로 이동
                EventBus.getDefault().post(BusProvider(ProjectListTabFragment::class.java.name, EventKind.BIND_FRAGMENT, "projectid", 0))
            }
        }

        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    fun validationInput() : Boolean {
        val project_name : String? = projectNewStep1_editText_projectName.text.toString().trim()
        val project_dec : String? = projectNewStep1_editText_projectDec.text.toString().trim()

        project_name?.let {
            if (it.length == 0) {
                val errorMsg : String? = getString(R.string.new_project_name_error)
                //projectNewStep1_editText_projectName.setError(errorMsg)
                EventBus.getDefault().post(BusProvider(className, EventKind.SHOW_SNACKBAR, errorMsg))
                projectNewStep1_editTextLayout_projectName.error = errorMsg
                return false
            } else {
                return true
            }
        }

        return false
    }

    // setHasOptionsMenu() 호출해야 실행됨
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        Log.d("=======", "onCreateOptionsMenu")

//        inflater?.inflate(R.menu.menu_notice,menu)
//        menu?.let {
//        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    // 좌측 아이콘 선택시만 동작 : activity에 동일 메서드 있을 경우 activity 우선 실행 후 아래 메서드 실행
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        Log.d(className, "============= onOptionsItemSelected() : " + item?.itemId)


        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val KEY_TITLE = "ProjectNewStep1Fragment"

        fun newInstance(title: String) =
                ProjectNewStep1Fragment().apply {
                    fragmentTitle = title

                    val args = Bundle()
                    args.putString(KEY_TITLE, title)
                    arguments = args
                }
    }

}