# Genos
[ ![Download](https://api.bintray.com/packages/nyssance/maven/genos/images/download.svg) ](https://bintray.com/nyssance/maven/genos/_latestVersion)

Genos makes it very easy to build better Android apps more quickly and with less code.
For more information please see [the website][1].

### Download
Gradle:
```gradle
implementation 'com.nyssance.genos:genos:1.1.0rc1'
```
### Featured
Genos integrate google architecture. just use. if your need learn more info about how genos work, and mvvm, repository , viewmodel etc., see [link](https://developer.android.com/topic/libraries/architecture/index.html)

1. Rules

- Activity just as an container, include app bar and drawer/bottom navigation, and one fragment or more.
- Fragment have two type: list and detail.
  - list for REST list api, like https://www.yourdomain.com/api/v1/users/, list include default `mListView`, `mAdapter`
  - detail for REST detail api, like https://www.yourdomain,com/api/v1/users/{:user_id}/
  - call in fragment is a call of it, it's a [Retrofit](http://square.github.io/retrofit/) call 
- Repository is for load data.
- ViewModel is for bind data and view.

2. How to use

Create a list fragment, override three methods, 20 lines code, that's all you need to do.
```java
public class UserList extends TableList<User, SubtitleHolder> {
    @Override
    protected void onPrepare() {
        call = API.userList(page);  // a retrofit call of this fragment.
        tileId = R.layout.list_item_subtitle;  // the layout res id of list item
    }

    @Override
    protected void onDisplayItem(@NotNull User item, @NotNull SubtitleHolder holder, int viewType) {
        holder.title.setText(item.login);
        holder.subtitle.setText("id: " + item.id);
        holder.setImage(holder.icon, item.avatarUrl);
    }

    @Override
    protected void onOpenItem(@NotNull User item) {
        // startActivity or do anything when click item
    }
}
```

Create a bottom navigation with three buttons, 10 lines
```java
public class MainActivity extends TabBarActivity { // If you need a drawer navigation, just use DrawerActivity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments.append(R.id.navigation_home, new UserList());
        fragments.append(R.id.navigation_discover, PlaceholderFragment.newInstance(2));
        fragments.append(R.id.navigation_me, PlaceholderFragment.newInstance(3));
        onNavigationItemSelected(R.id.navigation_home);
    }
}
```

### Tutorial
[Develop an app in 10 minutes][1].

### Architecture
```
genos
├── BaseAppManager.kt                 extends it for config your app.
├── libs
│   └── MessageEvent.kt               util for EventBus.
├── repository
│   └── HttpRepository.kt             Default Http repository.
└─── ui
    ├── BaseAdapter.kt                Default Adapter for list fragment.
    ├── BaseViewModel.kt              Default ViewModel for list and detail fragment.
    ├── activity
    │   ├── AppBarActivity.kt         Activity with an app bar.
    │   ├── CollapsingActivity.kt     Activity with a collapsing app bar.
    │   ├── DrawerActivity.kt         Activity with drawer.
    │   ├── TabBarActivity.kt         Activity with bottom navigation.
    │   └── base                      (design your activity by extends activities in base.)
    ├── fragment
    │   ├── CollectionList.kt         Fragment with a StaggeredGrid layout, use for waterfall list.
    │   ├── DetailFragment.kt         Fragment for detail.
    │   ├── GridList.kt               Fragment with a grid layout, user for grid list.
    │   ├── PagerFragment.kt          Fragment with a pager.
    │   ├── TableList.kt              Fragment with a Linear layout, use for stand list, one item per line.
    │   └── base                      (design your fragemnt by extends fragments in base.)
    └── viewholder
        ├── BaseHolder.kt             Base holder.
        ├── DefaultHolder.kt          A holder with icon / title
        └── SubtitleHolder.kt         A holder with icon / title / subtitle
```

### Vendor
* Android
  * [Support Library](https://developer.android.com/topic/libraries/support-library/index.html)
  * [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html)
  * [ATSL](https://developer.android.com/topic/libraries/testing-support-library/index.html)
  * [Android KTX](https://github.com/android/android-ktx)
* Others
  * [Retrofit](https://square.github.io/retrofit/)
  * [Glide](https://github.com/bumptech/glide)
  * [EventBus](https://github.com/greenrobot/EventBus)
  * [Logger](https://github.com/orhanobut/logger)

Special thanks [bintray-release](https://github.com/novoda/bintray-release), you save my life.

### License
    Copyright 2018 NY <nyssance@icloud.com>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://nyssance.github.io/genos
[2]: https://search.maven.org/remote_content?g=com.nyssance.genos&a=genos&v=LATEST
[10]: https://developer.android.com/studio/projects/create-project.html
