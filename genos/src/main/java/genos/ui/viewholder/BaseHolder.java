/*
 * Copyright 2018 NY (nyssance@icloud.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package genos.ui.viewholder;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

public class BaseHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews = new SparseArray<>();

    public BaseHolder(View itemView) {
        super(itemView);
    }

    @NonNull
    public final <V extends View> V getView(@IdRes int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            if (view != null) {
                mViews.put(id, view);
            } else {
                Logger.t("viewholder").e("itemView.findViewById return null. check your tile id, IdRes: " + itemView.getContext().getResources().getResourceName(id));
            }
        }
        return (V) view;
    }

    public final void setText(@IdRes int id, CharSequence text) {
        this.<TextView>getView(id).setText(text);
    }

    public final void setImage(@IdRes int id, String string) {
        setImage(getView(id), string);
    }

    public final void setImage(ImageView imageView, String string) {
        Glide.with(itemView.getContext()).load(string).into(imageView);
    }
}
