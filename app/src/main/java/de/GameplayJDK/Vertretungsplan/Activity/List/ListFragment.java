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

package de.GameplayJDK.Vertretungsplan.Activity.List;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.GameplayJDK.Vertretungsplan.Activity.List.Extra.ItemListFragmentPersistentFragmentPagerAdapter;
import de.GameplayJDK.Vertretungsplan.Activity.List.Extra.PersistentFragmentPagerAdapter;
import de.GameplayJDK.Vertretungsplan.Activity.List.Fragment.AboutDialogFragment;
import de.GameplayJDK.Vertretungsplan.Activity.List.Fragment.ItemListFragment;
import de.GameplayJDK.Vertretungsplan.Activity.List.Listener.OnChildViewCreatedListener;
import de.GameplayJDK.Vertretungsplan.Data.Model.Result;
import de.GameplayJDK.Vertretungsplan.Helper.SharedPreferencesHelper;
import de.GameplayJDK.Vertretungsplan.R;

/**
 * Created by GameplayJDK on 03.12.2016.
 */

public class ListFragment extends Fragment implements ListContract.View, OnChildViewCreatedListener<ItemListFragment> {

    private static final int ORDER_OFFSET = 10;

    private ListContract.Presenter mPresenter;

    private DrawerLayout mActivityDrawerLayout;

    private CoordinatorLayout mActivityCoordinatorLayout;

    private FloatingActionButton mActivityFloatingActionButton;

    private NavigationView mActivityNavigationView;

    private TextView mActivityNavigationViewHeader;

    private ViewPager mViewPager;
    private PersistentFragmentPagerAdapter mPagerAdapter;

    private TabLayout mTabLayout;

    private Animation mRotateAnimation;

    public ListFragment() {
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setHasOptionsMenu(true);

        this.mPagerAdapter = new ItemListFragmentPersistentFragmentPagerAdapter(super.getChildFragmentManager(), super.getContext());

        this.mRotateAnimation = AnimationUtils.loadAnimation(super.getContext(), R.anim.rotate);
    }

    @Override
    public void setPresenter(ListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        this.mViewPager = (ViewPager) root.findViewById(R.id.view_pager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mPresenter.setSelectedTab(position);

                // mPresenter.loadResult(false, false, false);
            }
        });

        this.mTabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        this.mTabLayout.setupWithViewPager(this.mViewPager);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mActivityDrawerLayout = (DrawerLayout) super.getActivity().findViewById(R.id.drawer_layout);
        this.mActivityDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                mPresenter.displayLastTime();
            }
        });

        this.mActivityCoordinatorLayout = (CoordinatorLayout) super.getActivity().findViewById(R.id.coordinator_layout);

        this.mActivityFloatingActionButton = (FloatingActionButton) super.getActivity().findViewById(R.id.floating_action_button);
        this.mActivityFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadResult(true, true, false);
            }
        });

        this.mActivityNavigationView = (NavigationView) super.getActivity().findViewById(R.id.navigation_view);
        this.mActivityNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                mPresenter.setSelectedList(-ORDER_OFFSET + menuItem.getOrder() - 1);
                mPresenter.loadResult(false, false, false);

                if (mActivityDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mActivityDrawerLayout.closeDrawer(GravityCompat.START);
                }

                return true;
            }
        });

        this.mActivityNavigationViewHeader = (TextView) this.mActivityNavigationView.getHeaderView(0);
    }

    @Override
    public void onResume() {
        super.onResume();

        this.mPresenter.setSelectedList(SharedPreferencesHelper.get(super.getContext(), SharedPreferencesHelper.PREF_SELECTED_LIST, 0));
        this.mPresenter.setTimestampMs(SharedPreferencesHelper.get(super.getContext(), SharedPreferencesHelper.PREF_TIMESTAMP_MS, 0L));
        this.mPresenter.setSelectedTab(SharedPreferencesHelper.get(super.getContext(), SharedPreferencesHelper.PREF_SELECTED_TAB, 0));

        this.mPresenter.start();

        this.mViewPager.setCurrentItem(this.mPresenter.getSelectedTab());
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferencesHelper.set(super.getContext(), SharedPreferencesHelper.PREF_SELECTED_LIST, this.mPresenter.getSelectedList());
        SharedPreferencesHelper.set(super.getContext(), SharedPreferencesHelper.PREF_TIMESTAMP_MS, this.mPresenter.getTimestampMs());
        SharedPreferencesHelper.set(super.getContext(), SharedPreferencesHelper.PREF_SELECTED_TAB, this.mPresenter.getSelectedTab());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.menu_item_list_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_select_class:
                // impl: class selection

                break;
            case R.id.menu_about:
                this.showAboutDialog();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isActive() {
        return super.isAdded();
    }

    @Override
    public void showLoadingAnimation(boolean active) {
        View root = super.getView();

        if (root == null) {
            return;
        }

        if (active) {
            this.mActivityFloatingActionButton.startAnimation(this.mRotateAnimation);
        } else {
            this.mActivityFloatingActionButton.clearAnimation();
        }
    }

    @Override
    public void showEmptyList() {
        View root = super.getView();

        if (root == null) {
            return;
        }

        Menu menu = this.mActivityNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_class);

        SubMenu subMenu = menuItem.getSubMenu();
        subMenu.removeGroup(R.id.nav_class_list);
    }

    @Override
    public void showResultList(List<Result.ParentClass> parentClassList, int selectedItem) {
        View root = super.getView();

        if (root == null) {
            return;
        }

        List<String> stringList = new ArrayList<String>();

        for (Result.ParentClass parentClass : parentClassList) {
            stringList.add(parentClass.getName());
        }

        Menu menu = this.mActivityNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_class);

        SubMenu subMenu = menuItem.getSubMenu();
        subMenu.removeGroup(R.id.nav_class_list);

        int count = 0;
        for (String item : stringList) {
            MenuItem subMenuItem = subMenu.add(R.id.nav_class_list, Menu.NONE, (ORDER_OFFSET + count + 1), item);

            if (subMenuItem.getOrder() == (ORDER_OFFSET + selectedItem + 1)) {
                subMenuItem.setChecked(true);
            }

            count++;
        }

        subMenu.setGroupCheckable(R.id.nav_class_list, true, true);
    }

    @Override
    public void showLastTime(long timestampMs) {
        View root = super.getView();

        if (root == null) {
            return;
        }

        if (timestampMs == 0) {
            this.mActivityNavigationViewHeader.setText(R.string.nav_last_time);
        } else {
            this.mActivityNavigationViewHeader.setText(DateUtils.getRelativeTimeSpanString(timestampMs, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        }
    }

    @Override
    public void showError() {
        View root = super.getView();

        if (root == null) {
            return;
        }

        Snackbar snackbar = Snackbar.make(this.mActivityCoordinatorLayout, R.string.snackbar_msg_error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void showError(final boolean forceUpdate, final boolean showLoadingAnimation, final boolean showLoadingIndicator) {
        View root = super.getView();

        if (root == null) {
            return;
        }

        Snackbar snackbar = Snackbar.make(this.mActivityCoordinatorLayout, R.string.snackbar_msg_error, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.snackbar_msg_error_action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadResult(forceUpdate, showLoadingAnimation, showLoadingIndicator);
            }
        });
        snackbar.show();
    }

    @Override
    public void showAboutDialog() {
        AboutDialogFragment fragment = AboutDialogFragment.newInstance();
        String tag = fragment.getClass().getName();

        fragment.show(super.getFragmentManager(), tag);
    }

    @Override
    public void onChildViewCreated(ItemListFragment view) {
        this.mPresenter.addView(view);
    }
}
