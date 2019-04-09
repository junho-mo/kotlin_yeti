package com.study.cube.yeti

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
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
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import kotlinx.android.synthetic.main._activity_main.*
import kotlin.reflect.KClass

class _MainActivity : AppCompatActivity() {

    private lateinit var result: Drawer
    private lateinit var headerResult: AccountHeader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._activity_main)

        try {
            setSupportActionBar(_toolbar)
            _toolbar.setBackgroundColor(Color.TRANSPARENT)
            _toolbar.setTitleTextColor(Color.TRANSPARENT)
//            Toast.makeText(this,toolbar.title, Toast.LENGTH_LONG).show()

            result = drawer {
                toolbar = this@_MainActivity._toolbar
                hasStableIds = true
                savedInstance = savedInstanceState
                showOnFirstLaunch = true

                headerResult = accountHeader {
                    background = R.drawable.header1
                    savedInstance = savedInstanceState
                    translucentStatusBar = true

                    profile("juno", "nanfunylife@gmail.com") {
//                        iconUrl = "https://avatars3.githubusercontent.com/u/1476232?v=3&s=460"
                        icon = R.drawable.profile4
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
                    }
                    secondaryItem("진행 프로젝트") {
                        level = 2
                        icon = R.drawable.icons8_mind_map
                        identifier = 2001
                        badge("2") {
                            textColor = Color.WHITE.toLong()
                            colorRes = R.color.md_red_800
                        }
                    }
                }
                // divider : co.zsmb.materialdrawerkt.draweritems.divider
                divider {}
                primaryItem("이용안내") {
                    icon = R.drawable.icons8_help

                }
                primaryItem("공지사항") {
                    icon = R.drawable.icons8_commercial
//                    selectable = false
                }
            }
        } catch(e : Exception) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    private fun <T : Activity> openActivity(activity: KClass<T>): (View?) -> Boolean = {
        startActivity(Intent(this@_MainActivity, activity.java))
        false
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (::result.isInitialized) result.saveInstanceState(outState)
        if (::headerResult.isInitialized) headerResult.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (result.isDrawerOpen)
            result.closeDrawer()
        else {
            // 메인화면 백버튼 막기 (시작화면 이동방지)
//            super.onBackPressed()
        }

    }
}
