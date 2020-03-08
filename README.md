# Genos

[![Download](https://api.bintray.com/packages/nyssance/maven/genos/images/download.svg)](https://bintray.com/nyssance/maven/genos/_latestVersion)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue)](https://opensource.org/licenses/Apache-2.0)

👊 Genos makes it very easy to build better mobile apps more quickly and with less code.
For more information please see [the website][genos].

[Genos for iOS](https://github.com/nyssance/GenosSwift)

- [Genos Samples](https://github.com/nyssance/genos-samples)

---

## Installation

### build.gradle

```groovy
implementation 'com.nyssance.genos:genos:2.0.0'
```

### build.gradle.kts

```kotlin
implementation("com.nyssance.genos:genos:2.0.0")
```

## Features

Genos integrate google architecture. just use. if your need learn more info about how genos work, and mvvm, repository , viewmodel etc., see [link](https://developer.android.com/topic/libraries/architecture)

1.Rules

- Activity just as an container, include app bar and drawer/bottom navigation, and one fragment or more.
- Fragment have two type: list and detail.
  - list for REST list api, like <https://www.yourdomain.com/api/v1/users/,> list include default `listView`, `adapter`
  - detail for REST detail api, like <https://www.yourdomain,com/api/v1/users/{:user_id}/>
  - call in fragment is a call of it, it's a [Retrofit][retrofit] call
- Repository is for load data.
- ViewModel is for bind data and view.

2.How to use

Create a list fragment, override three methods, 15 lines code, that's all you need to do.

```kotlin
import genos.ui.fragment.generic.List
import genos.ui.viewholder.Holder

class UserList : List<User, Holder>() {
    override fun onCreate() {
        call = API.userList(page) // A retrofit call of this fragment.
    }

    override fun onDisplayItem(item: User, view: Holder, viewType: Int) {
        view.icon?.setImage(item.avatarUrl)
        view.title?.text = item.username
    }

    override fun onOpenItem(item: User) {
        // StartActivity or do anything when click item.
    }
}
```

Create a bottom navigation with three buttons, 10 lines.

```kotlin
// If you need a drawer navigation, use `MainActivity : DrawerActivity`
class MainActivity : TabBarActivity(mapOf(
        R.id.navigation_1 to UserList(),
        R.id.navigation_2 to PlaceholderFragment.instance("2"),
        R.id.navigation_3 to PlaceholderFragment.instance("3"),
        R.id.navigation_4 to PlaceholderFragment.instance("4")
)) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(Global) { // Config
            APP_SCHEME = "genos-sample"
        }
        router() // Set router
    }
}
```

## Tutorial

[Develop an app in 10 minutes][genos].

## Architecture

```txt
genos
├── Global.kt                               Global config.
├── Helper.kt
├── extension
│   ├── Fragment+Extension.kt
│   ├── ImageView+Extension.kt
│   └── String+Extension.kt
├── model
│   ├── BaseItem.kt
│   └── Item.kt
├── repository
│   ├── HttpRepository.kt                   Default http repository.
│   ├── HttpUtil.kt
│   ├── IRepository.kt
│   ├── NetworkState.kt
│   └── Status.kt
├── ui
│   ├── BaseAdapter.kt                      Default Adapter for list fragment.
│   ├── BaseViewModel.kt                    Default ViewModel for list and detail fragment.
│   ├── activity
│   │   ├── AppBarActivity.kt               Activity with an app bar.
│   │   ├── CollapsingActivity.kt           Activity with a collapsing app bar.
│   │   ├── DrawerActivity.kt               Activity with drawer.
│   │   ├── TabBarActivity.kt               Activity with bottom navigation.
│   │   ├── WebActivity.kt
│   │   ├── base                            Design your activity by extends activity in base.
│   │   │   ├── BaseActivity.kt
│   │   │   ├── ContainerActivity.kt
│   │   │   └── NavigationActivity.kt
│   ├── fragment
│   │   ├── ActionSheet.kt
│   │   ├── Dialog.kt
│   │   ├── PlaceholderFragment.kt
│   │   ├── ViewPagerFragment.kt            Fragment with a view pager.
│   │   ├── base                            Design your fragment by extends fragment in base.
│   │   │   ├── BaseFragment.kt
│   │   │   ├── ListFragment.kt
│   │   │   ├── LoaderFragment.kt
│   │   │   ├── ObjectFragment.kt
│   │   │   └── RecyclerViewFragment.kt
│   │   └── generic
│   │       ├── Detail.kt                   Fragment for detail.
│   │       ├── GridViewList.kt             Fragment with a grid layout, user for grid list.
│   │       ├── List.kt                     Fragment with a linear layout, use for stand list, one item per line.
│   │       ├── StaggeredGridViewList.kt    Fragment with a staggered grid layout, use for waterfall list.
│   │       └── TableViewDetail.kt
│   └── viewholder
│       ├── BaseHolder.kt                   Base holder.
│       └── Holder.kt                       A holder with icon, title, subtitle, accessory.
└── vendor
    └── Retrofit.kt
```

## Syntactic sugar

`ifBlank`, `orEmpty`

## Vendor

- [Android Jetpack](https://developer.android.com/jetpack)
- [Glide](https://github.com/bumptech/glide)
- [Logger](https://github.com/orhanobut/logger)
- [Retrofit][retrofit]
- [AgentWeb](https://github.com/Justson/AgentWeb)

Special thanks [bintray-release](https://github.com/novoda/bintray-release), who save my life.

## License

Genos is released under the Apache license. [See LICENSE](https://github.com/nyssance/genos/blob/master/LICENSE) for details.

[genos]: https://nyssance.github.io/genos/
[retrofit]: https://square.github.io/retrofit/
[2]: https://search.maven.org/remote_content?g=com.nyssance.genos&a=genos&v=LATEST
[10]: https://developer.android.com/studio/projects/create-project
[synthetic exported]: https://youtrack.jetbrains.com/issue/KT-22430
