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

import de.GameplayJDK.Vertretungsplan.Activity.List.Fragment.ItemListFragment;
import de.GameplayJDK.Vertretungsplan.R;

/**
 * Created by GameplayJDK on 03.12.2016.
 */

public class ItemListFragmentPersistentFragmentPagerAdapter extends PersistentFragmentPagerAdapter<ItemListFragment> {

    private static final int ITEM_COUNT = 2;

    public ItemListFragmentPersistentFragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager, context, ITEM_COUNT);
    }

    @Override
    public ItemListFragment getItem(int position) {
        Fragment persistentFragment = super.getPersistentItem(position);
        ItemListFragment fragment = null;

        if (persistentFragment instanceof ItemListFragment) {
            fragment = (ItemListFragment) persistentFragment;
        }

        if (fragment == null) {
            fragment = ItemListFragment.newInstance(position);
        }

        return fragment;
    }

    @Override
    public String getPageTitle(int position) {
        return super.getContext().getString((this.isTabNext(position)) ? (R.string.tab_next) : (R.string.tab_current));
    }

    private boolean isTabNext(int position) {
        return (position != 0) && (position == 1);
    }
}
