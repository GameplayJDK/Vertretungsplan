/*
 *     Vertretungsplan Android App
 *     Copyright (C) 2017  GameplayJDK
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.GameplayJDK.Vertretungsplan.Activity.List.Extra;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GameplayJDK on 24.12.2016.
 */

public abstract class PersistentFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private final FragmentManager mFragmentManager;
    private final Context mContext;

    private final int mSize;

    private List<String> mTagList;

    public PersistentFragmentPagerAdapter(FragmentManager fragmentManager, Context context, int size) {
        super(fragmentManager);

        this.mFragmentManager = fragmentManager;
        this.mContext = context;

        this.mSize = size;

        this.mTagList = new ArrayList<String>(size);

        while (this.mTagList.size() < this.mSize) {
            this.mTagList.add(null);
        }
    }

    @Override
    public int getCount() {
        return this.mSize;
    }

    @Override
    public abstract T getItem(int position);

    public Fragment getPersistentItem(int position) {
        if (position < 0 || position > this.mSize) {
            return null;
        }

        String tag = this.mTagList.get(position);

        if (tag == null) {
            return null;
        }

        Fragment fragment = this.mFragmentManager.findFragmentByTag(tag);

        if (fragment == null) {
            return null;
        }

        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);

        if (object instanceof Fragment) {
            String tag = ((Fragment) object).getTag();

            this.mTagList.set(position, tag);
        }

        return object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof Fragment) {
            this.mTagList.set(position, null);
        }

        super.destroyItem(container, position, object);
    }

    public Context getContext() {
        return this.mContext;
    }
}
