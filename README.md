Genos
=====
[ ![Download](https://api.bintray.com/packages/nyssance/maven/genos/images/download.svg) ](https://bintray.com/nyssance/maven/genos/_latestVersion)

Genos makes it easier to build better Android apps more quickly and with less code.
For more information please see [the website][1].

Show your the code
------------------
__Step 0 (between  10 minutes to 1 day):__

Install Java8 & [Android Studio 3.0.1](https://developer.android.com/studio/index.html).

__Step 1 (need 2 minutes ):__

Start a new project. [official guide][10]

Screen | Configure
------ | ---------
Target Android Devices | Phone and Tablet : API 17
Add an Activity to Mobile | Empty Activity

Config _Gradle Scripts: build.gradle (Module: app)_.
```gradle
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    // Replace default by Genos
    // implementation 'com.android.support:appcompat-v7:26.1.0'
    // implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    implementation 'com.nyssance.genos:genos:+'
    ...
```

__Step 2 (5 minutes):__

Create 4 classes: _User_, _APIService_, _AppManager_, _UserList_

_User_

```java
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("login")
    public String login;
    @SerializedName("id")
    public long id;
    @SerializedName("avatar_url")
    public String avatarUrl;
}
```

_APIService_
```java
public interface APIService {
    @GET("api/v1/users/")
    Call<List<User>> userList(@Query("page") int page);

    @GET("api/v1/users/{id}/")
    Call<User> userDetail(@Path("id") int id);
}
```

_AppManager_
```java
import genos.BaseAppManager;

public class AppManager extends BaseAppManager {
    public static APIService API;

    @Override
    public void settings() {
        BASE_URL = "https://api.github.com";
        // Create retrofit
        API = onCreateRetrofit().create(APIService.class);
    }
    ...

```

_UserList_
```java
import genos.ui.fragment.TableList;
import genos.ui.viewholder.SubtitleHolder;

public class UserList extends TableList<User, SubtitleHolder> {

    @Override
    protected void onPrepare() {
        mCall = API.userList(mPage);
        mTileId = R.layout.list_item_subtitle;
    }

    @Override
    protected void onDisplayItem(User item, SubtitleHolder holder, int viewType) {
        holder.title.setText(item.login);
        holder.subtitle.setText("id: " + item.id);
        holder.setImage(holder.icon, item.avatarUrl);
    }

    @Override
    protected void onOpenItem(User item) {
        Snackbar.make(mListView, "Replace with your own action", Snackbar.LENGTH_SHORT).show();
    }
}
```

__Step 3(1 minutes)__

Modify _MainActivity_, _AndroidManifest.xml_
```java
import genos.ui.TabBarActivity

publc class MainActivity extends TabBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragments.append(R.id.navigation_home, new UserList());
        mFragments.append(R.id.navigation_discover, PlaceholderFragment.newInstance(2));
        mFragments.append(R.id.navigation_me, PlaceholderFragment.newInstance(3));
        onNavigationItemSelected(R.id.navigation_home);
    }
}
```

_AndroidManifest.xml_
```xml
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        ...
        android:theme="@style/Theme.Genos">
        ...
    </application>

</manifest>
```

__Step 4__

Run it
#### Congratulations!  your are an Android expert~~

Vendor
------
* Android
 * [Support Library](https://developer.android.com/topic/libraries/support-library/index.html)
 * [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html)
 * [ATSL](https://developer.android.com/topic/libraries/testing-support-library/index.html)
* Others
 * [Retrofit](https://square.github.io/retrofit/)
 * [Glide](https://github.com/bumptech/glide)
 * [EventBus](https://github.com/greenrobot/EventBus)
 * [Logger](https://github.com/orhanobut/logger)

Special thanks [bintray-release](https://github.com/novoda/bintray-release), you save my life.

License
=======

    Copyright 2018 NY (nyssance@icloud.com)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


 [1]: https://github.com/nyssance/genos
 [2]: https://search.maven.org/remote_content?g=com.nyssance.genos&a=genos&v=LATEST
 [10]: https://developer.android.com/studio/projects/create-project.html
