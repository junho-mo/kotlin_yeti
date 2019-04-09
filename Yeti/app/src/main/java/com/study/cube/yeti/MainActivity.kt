package com.study.cube.yeti

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badge
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.expandable.expandableItem
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import co.zsmb.materialdrawerkt.draweritems.profile.profileSetting
import co.zsmb.materialdrawerkt.draweritems.sectionHeader
import co.zsmb.materialdrawerkt.imageloader.drawerImageLoader
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.squareup.picasso.Picasso
import com.study.cube.yeti.eventBus.BusProvider
import com.study.cube.yeti.eventBus.EventKind
import com.study.cube.yeti.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {
    private lateinit var result: Drawer
    private lateinit var headerResult: AccountHeader

    var className : String = MainActivity::class.java.name
    var bindFragmentNmPrevious : String? = null     // 이전 바인딩
    var bindFragmentNmPresent : String? = null      // 현재 바인
    var mainView_toolbar : Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(className, "onCreate()")

        // 툴바 셋팅
        mainView_toolbar = findViewById(R.id.mainView_toolbar) as Toolbar
        setSupportActionBar(mainView_toolbar)
        mainView_toolbar?.setTitleTextColor(getResources().getColor(R.color.colorLogo))
        supportActionBar?.title = ""
        // 좌측아이콘 버튼 노출
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   // ActionBar에 Home 버튼 표시 할 떄 "<" 부분을 표시 할 지 결정하는 함수, true(<), false(아이콘없음)
        //supportActionBar?.setDisplayShowHomeEnabled(true)  // ?
        //supportActionBar?.setHomeButtonEnabled(true)        // Home 버튼을 Enable /  Disable 할지를 결정
        //mainView_toolbar.setBackgroundColor(Color.TRANSPARENT)
        //mainView_toolbar.setTitleTextColor(Color.TRANSPARENT)

        // 메인프레그먼트 바인딩
        bindFragment(BusProvider(MainFragment::class.java.name, EventKind.HIDE_KEYBOARD, null))

//        // 툴바 바인딩
//        val mInflater : LayoutInflater = LayoutInflater.from(this@MainActivity)
//        val mCustomView : View = mInflater.inflate(R.layout.toolbar, null)
//        mainView_toolbar?.addView(mCustomView)

        try {
            // 피카소 이미지 uri 로드 설정
            drawerImageLoader {
                placeholder { ctx, tag ->
                    DrawerUIUtils.getPlaceHolder(ctx)
                }
                set { imageView, uri, placeholder, tag ->
                    Picasso.get()
                            .load(uri)
                            .into(imageView)
                }
                cancel { imageView ->
                    Picasso.get()
                            .cancelRequest(imageView)
                }
            }

            // 좌측메뉴 셋팅
            result = drawer {
                //toolbar = this@MainActivity.mainView_toolbar
                toolbar = findViewById(R.id.mainView_toolbar)
                hasStableIds = true
                savedInstance = savedInstanceState
                showOnFirstLaunch = true

                headerResult = accountHeader {
                    background = R.drawable.header1
                    savedInstance = savedInstanceState
                    translucentStatusBar = true

                    profile("juno", "nanfunylife@gmail.com") {
                        //iconUrl = "https://avatars3.githubusercontent.com/u/1476232?v=3&s=460"
                        //icon = R.drawable.profile4
                        iconUrl = "https://previews.123rf.com/images/markusgann/markusgann1404/markusgann140400035/27262574-ein-bild-von-einem-sch%C3%B6nen-blauen-auge-textur.jpg"
                        identifier = 100
                    }
                    profile("funny", "funny7103@gmail.com") {
                        //                        iconUrl = "https://avatars3.githubusercontent.com/u/887462?v=3&s=460"
                        icon = R.drawable.profile6
                        identifier = 101
                    }
                    profile("cubeLab", "cubelabio@gmail.com") {
                        icon = R.drawable.profile2
                        identifier = 102
                    }
//                    profileSetting("계정 추가", "Add new GitHub Account") {
//                        iicon = GoogleMaterial.Icon.gmd_add
//                        identifier = 100_000
//                    }
                    profileSetting("계정 추가") {
                        iicon = GoogleMaterial.Icon.gmd_add
                        identifier = 100_000
                    }
                    profileSetting("계정 관리") {
                        iicon = GoogleMaterial.Icon.gmd_settings
                        identifier = 100_001
                    }

                    onProfileChanged { _, profile, _ ->
                        if (profile.identifier == 100_000L) {
                            val size = headerResult.profiles.size
                            val newProfile = ProfileDrawerItem()
                                    .withName("New Batman ${size - 1}")
                                    .withNameShown(true)
                                    .withEmail("batman${size - 1}@gmail.com")
                                    .withIcon(R.drawable.profile5)
                                    .withIdentifier(100L + size + 1L)
                            headerResult.addProfile(newProfile, size - 2)
                        }
                        false
                    }
                }

                sectionHeader("협업관리 안드로이드앱") {
                    divider = false
                }

                primaryItem("메인") {
                    icon = R.drawable.icons8_home
                    onClick {
                        view, position, drawerItem ->
                        EventBus.getDefault().post(BusProvider(MainFragment::class.java.name, EventKind.BIND_FRAGMENT, null,null))
                        //EventBus.getDefault().post(BusProvider(TestFragment::class.java.name, EventKind.BIND_FRAGMENT, null, 2))
                        false
                    }
                }
                primaryItem("오늘일정") {
                    icon = R.drawable.icons8_calendar
                    badge("2") {
                        textColor = Color.WHITE.toLong()
                        colorRes = R.color.md_red_800
                    }
                }
//                primaryItem("프로젝트") {
//                    icon = R.drawable.icons8_worker_male
////                onClick(openActivity(MainDrawerFragment::class))
//                    onClick(openActivity(NoticeActivity::class))
//                    badge("4") {
//                        textColor = Color.WHITE.toLong()
//                        colorRes = R.color.md_red_700
//                    }
//                }
                expandableItem("프로젝트") {
                    icon = R.drawable.icons8_worker_male
                    identifier = 18
                    selectable = false
                    secondaryItem("완료 프로젝트") {
                        level = 2
                        icon = R.drawable.icons8_checked
                        identifier = 2000
                        badge("2") {
                            textColor = Color.WHITE.toLong()
                            colorRes = R.color.md_blue_800
                        }
                        onClick {
                            view, position, drawerItem ->
                            EventBus.getDefault().post(BusProvider(ProjectListTabFragment::class.java.name, EventKind.BIND_FRAGMENT, null, 2))
                            //EventBus.getDefault().post(BusProvider(TestFragment::class.java.name, EventKind.BIND_FRAGMENT, null, 2))
                            false
                        }
                    }
                    secondaryItem("진행 프로젝트") {
                        level = 2
                        icon = R.drawable.icons8_mind_map
                        identifier = 2001
                        badge("2") {
                            textColor = Color.WHITE.toLong()
                            colorRes = R.color.md_red_800
                        }
                        onClick {
                            view, position, drawerItem ->
                            EventBus.getDefault().post(BusProvider(ProjectListTabFragment::class.java.name, EventKind.BIND_FRAGMENT, null, 1))
                            false
                        }
                    }
                }
                // divider : co.zsmb.materialdrawerkt.draweritems.divider
                divider {}
                primaryItem("FAQ") {
                    icon = R.drawable.icons8_help
                    onClick {
                        view, position, drawerItem ->
                        EventBus.getDefault().post(BusProvider("", EventKind.BIND_FRAGMENT))

                        false
                    }
                }
                primaryItem("공지사항") {
                    icon = R.drawable.icons8_commercial
                    badge("1") {
                        textColor = Color.WHITE.toLong()
                        colorRes = R.color.md_red_800
                    }
                    onClick {
                        view, position, drawerItem ->
                        EventBus.getDefault().post(BusProvider(NoticeFragment::class.java.name, EventKind.BIND_FRAGMENT))
                        false
                    }
                }
                primaryItem("설정") {
                    icon = R.drawable.icons8_settings
                    onClick {
                        view, position, drawerItem ->
                        EventBus.getDefault().post(BusProvider(null, EventKind.BIND_FRAGMENT))

                        false
                    }
                }

                onOpened {
                    Log.d(className, "onOpened")
                }

                onClosed {
                    Log.d(className, "onClosed")
                }

                onSlide { drawerView, slideOffset ->
                    // Log.d(className, "onSlide")
                }

                onItemClick { _,position, _ ->
                    Log.d(className, "Item $position clicked")
                    false
                }

            }

            // 찾았다 !!!!!
            result.setOnDrawerNavigationListener {
                // true : 좌측메뉴 오픈되지 않음
                topLeftMenuIconAction()
            }

        } catch(e : Exception) {
            Log.e(className, e.toString())
            Toast.makeText(this,"ERROR : " + e.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    private fun <T : Activity> openActivity(activity: KClass<T>): (View?) -> Boolean = {
        startActivity(Intent(this@MainActivity, activity.java))
        false
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (::result.isInitialized) result.saveInstanceState(outState)
        if (::headerResult.isInitialized) headerResult.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    /* ====================================================================================
    *
    *                               EventBus Listener
    *
    ==================================================================================== */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: BusProvider) {/* Do something */
        // Toast.makeText(this,"onMessageEvent : " + event.fragmentName,Toast.LENGTH_SHORT).show()
        Log.d(className, "onMessageEvent()")

        when(event.eventKind){
            EventKind.SHOW_SNACKBAR -> {
                event.param1?.let {
                    showSnackBar(it)
                }
            }
            EventKind.HIDE_KEYBOARD -> {
                hideKeyboard()
            }
            EventKind.BIND_FRAGMENT -> {
                if (event.fragmentName == null) {
                    showSnackBar("준비중입니다.")
                } else {
                    bindFragment(event)
                }
            }
        }
    }

    // 키보드 숨김
    fun hideKeyboard() {
        currentFocus?.let {
            val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)
        }
    }

    /* ====================================================================================
    *
*                           메인화면 > 프레그먼트 바인딩 (full screen)
    *
    ==================================================================================== */
    fun bindFragment(event: BusProvider) {
        // 이전,현재 바인딩된 클래스명 셋팅
        bindFragmentNmPrevious = bindFragmentNmPresent
        bindFragmentNmPresent = event.fragmentName
        Log.d(className, "bindFragment() : " + event.fragmentName)

        val toolbar = findViewById<Toolbar>(R.id.mainView_toolbar)
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        showTopLeftMenuIcon("back")

        // [0] 불필요
        //supportFragmentManager.beginTransaction().remove(NoticeFragment()).commit()
        //supportFragmentManager.beginTransaction().addToBackStack(null)

//        // [1] 메뉴아이콘 visible
//        if (bindFragmentNmPresent == MainFragment::class.java.name) {
//            getSupportActionBar()?.show()
//        } else {
//            getSupportActionBar()?.hide()
//        }

        if (bindFragmentNmPrevious == bindFragmentNmPresent) {
            Log.d(className, "bindFragment() : " + event.fragmentName + " 동일페이지요청 !! ")
            return
        }

        // [2] fragment 바인딩
        when(bindFragmentNmPresent) {
            MainFragment::class.java.name -> {
                hideKeyboard()
                if (bindFragmentNmPrevious != null) {
                    fragmentTransaction.setCustomAnimations(R.anim.right_in,R.anim.right_out)
                }
                fragmentTransaction
                        .replace(R.id.frameLayout, MainFragment())
                        .commit()
                toolbar.title = ""
                showTopLeftMenuIcon("humberger")
            }
            ProjectNewStep1Fragment::class.java.name -> {
                fragmentTransaction
                        .setCustomAnimations(R.anim.left_in,R.anim.left_out)
                        .replace(R.id.frameLayout, ProjectNewStep1Fragment.newInstance("신규 프로젝트"))
                        .commit()
                toolbar.title = "프로젝트 생성"
                showTopLeftMenuIcon("close")
            }
            ProjectListTabFragment::class.java.name -> {
                if (bindFragmentNmPrevious == ProjectTaskListFragment::class.java.name) {
                    fragmentTransaction.setCustomAnimations(R.anim.right_in,R.anim.right_out)
                } else {
                    fragmentTransaction.setCustomAnimations(R.anim.left_in,R.anim.left_out)
                }
                fragmentTransaction
                        .replace(R.id.frameLayout, ProjectListTabFragment.newInstance("프로젝트 리스트", event.tabIndex))
                        .commit()
                toolbar.title = "프로젝트 리스트"
                showTopLeftMenuIcon("close")
            }
            ProjectTaskListFragment::class.java.name -> {
                fragmentTransaction
                        .setCustomAnimations(R.anim.left_in,R.anim.left_out)
                        .replace(R.id.frameLayout, ProjectTaskListFragment.newInstance(event.project as BusProvider.Project))
                        .commit()
                toolbar.title = "태스크 리스트"
                showTopLeftMenuIcon("back")
            }
            NoticeFragment::class.java.name -> {
                if (bindFragmentNmPrevious == NoticeItemFragment::class.java.name) {
                    fragmentTransaction.setCustomAnimations(R.anim.right_in,R.anim.right_out)
                } else {
                    fragmentTransaction.setCustomAnimations(R.anim.left_in,R.anim.left_out)
                }
                fragmentTransaction
                        .replace(R.id.frameLayout, NoticeFragment.newInstance("공지사항"))
                        .commit()
                toolbar.title = "공지사항"
                showTopLeftMenuIcon("close")
            }
            NoticeItemFragment::class.java.name -> {
                fragmentTransaction
                        .setCustomAnimations(R.anim.left_in,R.anim.left_out)
                        .replace(R.id.frameLayout, NoticeItemFragment.newInstance("공지사항 내용"))
                        .commit()
                toolbar.title = "공지내용"
                showTopLeftMenuIcon("back")
            }
            // test
            TestFragment::class.java.name -> {
                fragmentTransaction
                        .setCustomAnimations(R.anim.left_in,R.anim.left_out)
                        .replace(R.id.frameLayout, TestFragment.newInstance("공지사항 내용", event.tabIndex))
                        .commit()
                toolbar.title = "Test"
                showTopLeftMenuIcon("close")
            }
            else -> {
                Log.d(className, "No bindFragment() : " + event.fragmentName)
            }
        }
    }

    // 사용안함 : toolbar.xml 에서 단일테마 사용
    fun setToolbarTheme(iconKind : String) {
        Log.d(className, "setToolbarTheme")
        //TimeUnit.MICROSECONDS.sleep(10000L)   // 화면전환 애니메이션 완료시까지 대기
        val toolbar = findViewById<Toolbar>(R.id.mainView_toolbar)

//        when(iconKind) {
//            ItemIconKind.TRANSPARENT -> {
//                toolbar.setBackgroundColor(Color.TRANSPARENT)
//                toolbar.setTitleTextColor(Color.BLACK)
//                //actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
//
//            }
//            ItemIconKind.BACK_WHITE -> {
//                toolbar.setBackgroundColor(Color.WHITE)
//                toolbar.setTitleTextColor(Color.BLACK)
//                supportActionBar!!.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material) // <- 아이콘으로 변경
//
//                // 메뉴 아이콘 색 변경
//                val icon = AppCompatResources.getDrawable(this, R.drawable.abc_ic_ab_back_material)!!
//                DrawableCompat.setTint(icon, ContextCompat.getColor(this, R.color.colorBlack))
////                supportActionBar?.run {
////                    setHomeAsUpIndicator(icon)
////                    //setDisplayShowTitleEnabled(false)
////                    setDisplayHomeAsUpEnabled(true)
////                }
//            }
//            ItemIconKind.CLOSE_BLACK -> {
//                toolbar.setBackgroundColor(Color.WHITE)
//                toolbar.setTitleTextColor(Color.BLACK)
//                supportActionBar!!.setHomeAsUpIndicator(R.drawable.abc_ic_clear_material) // <- 아이콘으로 변경
//
//                // 메뉴 아이콘 색 변경
//                val icon = AppCompatResources.getDrawable(this, R.drawable.abc_ic_clear_material)!!
//                DrawableCompat.setTint(icon, ContextCompat.getColor(this, R.color.colorBlack))
//            }
//        }
    }

    fun showSnackBar(title : String) {
        title?.let {
            val mainSnackbar = Snackbar.make(mainView,it,Snackbar.LENGTH_LONG)
            mainSnackbar.view.setBackgroundColor(Color.parseColor("#2abaf8"))
            mainSnackbar.setActionTextColor(Color.parseColor("#ffffff"))
            mainSnackbar.show()
        }
    }

    fun showTopLeftMenuIcon(display : String) {
        Log.d(className, "showHambugerMenuIcon")

        if (::result.isInitialized) {
            when (display) {
                "humberger" -> {
                    // false -> true 해줄때 햄버거 아이콘으로 변경됨. ic 리스트에서 햄버거 아이콘 찾기 귀찮음.
                    result.actionBarDrawerToggle.isDrawerIndicatorEnabled = true    // 좌측메뉴아이콘 노출
                }
                "back" -> {
                    result.actionBarDrawerToggle.isDrawerIndicatorEnabled = false   // 좌측메뉴아이콘 숨김
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)
                }
                "close" -> {
                    result.actionBarDrawerToggle.isDrawerIndicatorEnabled = false   // 좌측메뉴아이콘 숨김
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.abc_ic_clear_material)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(className, "onCreateOptionsMenu()")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        Log.d(className, "onBackPressed()")

        if (result.isDrawerOpen)
            result.closeDrawer()
        else {
            backAction()
        }
    }

    // 좌측메뉴 클릭 이벤트 : false (좌측메뉴 오픈)
    fun topLeftMenuIconAction() :  Boolean {
        Log.d(className, "topLeftMenuIconAction()")
        var result : Boolean = true

        when(bindFragmentNmPresent) {
            MainFragment::class.java.name -> {
                result = false
            }
            NoticeFragment::class.java.name -> {
                EventBus.getDefault().post(BusProvider(MainFragment::class.java.name, EventKind.BIND_FRAGMENT, null))
            }
            NoticeItemFragment::class.java.name -> {
                EventBus.getDefault().post(BusProvider(NoticeFragment::class.java.name, EventKind.BIND_FRAGMENT, null))
            }
            ProjectNewStep1Fragment::class.java.name -> {
                if (bindFragmentNmPrevious == ProjectListTabFragment::class.java.name) {
                    // 프로젝트리스트 > 생성
                    EventBus.getDefault().post(BusProvider(ProjectListTabFragment::class.java.name, EventKind.BIND_FRAGMENT, null))

                } else {
                    // 메인 > 생성
                    EventBus.getDefault().post(BusProvider(MainFragment::class.java.name, EventKind.BIND_FRAGMENT, null))
                }
            }
            ProjectListTabFragment::class.java.name -> {
                EventBus.getDefault().post(BusProvider(MainFragment::class.java.name, EventKind.BIND_FRAGMENT, null))
            }
            ProjectTaskListFragment::class.java.name -> {
                EventBus.getDefault().post(BusProvider(ProjectListTabFragment::class.java.name, EventKind.BIND_FRAGMENT, null))
            }
            else -> {
                //bindFragment(MainFragment::class.java.name)
//                    super.onBackPressed()
            }
        }

        return result
    }

    fun backAction() {
        Log.d(className, "backAction()")

        when(bindFragmentNmPresent) {
            MainFragment::class.java.name -> {
                // 메인화면 백버튼 막기 (시작화면 이동방지)
                // super.onBackPressed()
            }
            NoticeFragment::class.java.name -> {
                EventBus.getDefault().post(BusProvider(MainFragment::class.java.name, EventKind.BIND_FRAGMENT, null))
            }
            NoticeItemFragment::class.java.name -> {
                EventBus.getDefault().post(BusProvider(NoticeFragment::class.java.name, EventKind.BIND_FRAGMENT, null))
            }
            ProjectNewStep1Fragment::class.java.name -> {
                EventBus.getDefault().post(BusProvider(MainFragment::class.java.name, EventKind.BIND_FRAGMENT, null))
            }
            ProjectListTabFragment::class.java.name -> {
                EventBus.getDefault().post(BusProvider(MainFragment::class.java.name, EventKind.BIND_FRAGMENT, null))
            }
            ProjectTaskListFragment::class.java.name -> {
                EventBus.getDefault().post(BusProvider(ProjectListTabFragment::class.java.name, EventKind.BIND_FRAGMENT, null))
            }
            else -> {
                //bindFragment(MainFragment::class.java.name)
//                    super.onBackPressed()
            }
        }
    }
}
