/*
 * Copyright 2018 NY <nyssance@icloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package genos.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class Item extends BaseItem {
    public Drawable icon;
    public String subtitle;
    public String uri;

    public Item(@NonNull String name, Drawable icon, String uri, @NonNull Context context) {
        super(name, context);
        this.icon = icon;
        this.uri = uri;
        // this(name, null, icon, null, uri);
    }

    public Item(@NonNull String name, @NonNull String title, Drawable icon, String uri) {
        this(name, title, icon, null, uri);
    }

    public Item(@NonNull String name, @NonNull String title, Drawable icon, String subtitle, String uri) {
        super(name, title);
        this.icon = icon;
        this.subtitle = subtitle;
        this.uri = uri;
    }
}
