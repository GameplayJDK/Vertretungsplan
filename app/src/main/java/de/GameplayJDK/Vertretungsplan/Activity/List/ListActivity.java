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

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.FrameLayout;

import de.GameplayJDK.Vertretungsplan.Data.Source.Repository.RepositoryHost;
import de.GameplayJDK.Vertretungsplan.Data.Source.Repository.RepositoryResult;
import de.GameplayJDK.Vertretungsplan.R;

/**
 * Created by GameplayJDK on 02.12.2016.
 */

public class ListActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;

    private CoordinatorLayout mCoordinatorLayout;
    private FrameLayout mFrameLayout;
    private FloatingActionButton mFloatingActionButton;

    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_list);

        RepositoryHost.getInstance().setApplicationContext(super.getApplicationContext());
        RepositoryResult.getInstance().setApplicationContext(super.getApplicationContext());

        this.mDrawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);

        this.mToolbar = (Toolbar) super.findViewById(R.id.toolbar);
        super.setSupportActionBar(this.mToolbar);

        this.mDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, this.mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.mDrawerToggle.syncState();

        this.mDrawerLayout.addDrawerListener(this.mDrawerToggle);

        this.mCoordinatorLayout = (CoordinatorLayout) super.findViewById(R.id.coordinator_layout);
        this.mFrameLayout = (FrameLayout) super.findViewById(R.id.frame_layout);

        this.mFloatingActionButton = (FloatingActionButton) super.findViewById(R.id.floating_action_button);

        ActionBar actionBar = super.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.mNavigationView = (NavigationView) super.findViewById(R.id.navigation_view);

        FragmentManager fragmentManager = super.getSupportFragmentManager();

        ListFragment listFragment = (ListFragment) fragmentManager.findFragmentById(R.id.frame_layout);

        if (listFragment == null) {
            listFragment = ListFragment.newInstance();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.frame_layout, listFragment);
            transaction.commit();
        }

        ListPresenter listPresenter = new ListPresenter(listFragment);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        this.mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        this.mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.menu_item_list, menu);

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
