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

package de.GameplayJDK.Vertretungsplan.Activity.List.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import de.GameplayJDK.Vertretungsplan.Activity.List.Fragment.Extra.ChildSwipeRefreshLayout;
import de.GameplayJDK.Vertretungsplan.Activity.List.Fragment.Extra.ListItemArrayAdapter;
import de.GameplayJDK.Vertretungsplan.Activity.List.ListFragment;
import de.GameplayJDK.Vertretungsplan.Data.Model.Result;
import de.GameplayJDK.Vertretungsplan.R;

/**
 * Created by GameplayJDK on 03.12.2016.
 */

public class ItemListFragment extends Fragment implements ItemListContract.View {

    private static final String BUNDLE_TAB_POSITION = "bundle_tab_position";

    private ItemListContract.Presenter mPresenter;

    private ChildSwipeRefreshLayout mChildSwipeRefreshLayout;

    private ListView mListView;

    private LinearLayout mLinearLayoutEmpty;
    private TextView mTextViewEmpty;

    private ListItemArrayAdapter mListAdapter;

    private int mTabPosition;

    public ItemListFragment() {
    }

    public static ItemListFragment newInstance(int tabPosition) {
        ItemListFragment itemListFragment = new ItemListFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TAB_POSITION, tabPosition);

        itemListFragment.setArguments(bundle);

        return  itemListFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mListAdapter = new ListItemArrayAdapter(super.getContext());

        Bundle bundle = super.getArguments();

        this.mTabPosition = bundle.getInt(BUNDLE_TAB_POSITION);
    }

    @Override
    public void onResume() {
        super.onResume();

        // not needed in this view, as it is already called by its parent
        // this.mPresenter.start();

        ((ListFragment) this.getParentFragment()).onChildViewCreated(this);
    }

    @Override
    public void setPresenter(ItemListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_list, container, false);

        this.mChildSwipeRefreshLayout = (ChildSwipeRefreshLayout) root.findViewById(R.id.child_swipe_refresh_layout);
        this.mChildSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        this.mChildSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadResult(true, false, true);
            }
        });

        this.mListView = (ListView) root.findViewById(R.id.list_view);
        this.mListView.setAdapter(this.mListAdapter);

        this.mLinearLayoutEmpty = (LinearLayout) root.findViewById(R.id.linear_layout_empty);
        this.mTextViewEmpty = (TextView) root.findViewById(R.id.text_view_empty);

        this.mChildSwipeRefreshLayout.setChildView(this.mListView);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public int getTabPosition() {
        return this.mTabPosition;
    }

    @Override
    public boolean isActive() {
        return super.isAdded();
    }

    @Override
    public void showLoadingIndicator(final boolean active) {
        View root = super.getView();

        if (root == null) {
            return;
        }

        this.mChildSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mChildSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showEmptyItemList() {
        View root = super.getView();

        if (root == null) {
            return;
        }

        this.mListView.setVisibility(View.GONE);
        this.mLinearLayoutEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearResultItemList() {
        View root = super.getView();

        if (root == null) {
            return;
        }

        this.mListView.setVisibility(View.VISIBLE);
        this.mLinearLayoutEmpty.setVisibility(View.GONE);

        this.mListAdapter.clear();
    }

    @Override
    public void showResultItemList(List<Result.ParentClass.Day.ListItem> listItemList) {
        View root = super.getView();

        if (root == null) {
            return;
        }

        this.mListView.setVisibility(View.VISIBLE);
        this.mLinearLayoutEmpty.setVisibility(View.GONE);

        this.mListAdapter.update(listItemList);
    }
}
