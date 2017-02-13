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

import android.util.Log;

import java.util.List;

import de.GameplayJDK.Vertretungsplan.Activity.List.Fragment.ItemListContract;
import de.GameplayJDK.Vertretungsplan.Activity.List.UseCase.UseCaseGetResult;
import de.GameplayJDK.Vertretungsplan.Data.Model.Result;
import de.GameplayJDK.Vertretungsplan.Mvp.Clean.UseCase;
import de.GameplayJDK.Vertretungsplan.Mvp.Clean.UseCaseHandler;

/**
 * Created by GameplayJDK on 03.12.2016.
 */

public class ListPresenter implements ListContract.Presenter, ItemListContract.Presenter {

    private static final int MAX_VIEW_COUNT = 2;

    private final ListContract.View mView;
    private final UseCaseHandler mUseCaseHandler;

    private final UseCaseGetResult mUseCaseGetResultCurrent;
    private final UseCaseGetResult mUseCaseGetResultNext;

    private ItemListContract.View mChildViewCurrent;
    private ItemListContract.View mChildViewNext;

    private boolean mFirstLoad;

    private int mSelectedList;
    private long mTimestampMs;
    private int mSelectedTab;

    public ListPresenter(ListContract.View view) {
        this(view, UseCaseHandler.getInstance(), UseCaseGetResult.newInstance(), UseCaseGetResult.newInstance());
    }

    public ListPresenter(ListContract.View view, UseCaseHandler useCaseHandler, UseCaseGetResult useCaseGetResultCurrent, UseCaseGetResult useCaseGetResultNext) {
        this.mView = view;
        this.mView.setPresenter(this);

        this.mUseCaseHandler = useCaseHandler;

        this.mUseCaseGetResultCurrent = useCaseGetResultCurrent;
        this.mUseCaseGetResultNext = useCaseGetResultNext;

        this.mFirstLoad = true;

        this.mSelectedList = -1;
        this.mTimestampMs = -1L;
        this.mSelectedTab = -1;
    }

    @Override
    public void start() {
        // don't call this too soon
        // this.loadResult(false);
    }

    @Override
    public void addView(ItemListContract.View view) {
        if (this.isTabNext(view.getTabPosition())) {
            this.mChildViewNext = view;
        } else {
            this.mChildViewCurrent = view;
        }

        view.setPresenter(this);

        if (this.mChildViewCurrent != null && this.mChildViewNext != null) {
            this.loadResult(false);
        }
    }

    private void loadResult(boolean forceUpdate) {
        this.loadResult(forceUpdate || this.mFirstLoad, false, true);

        this.mFirstLoad = false;
    }

    @Override
    public void loadResult(boolean forceUpdate, boolean showLoadingAnimation, boolean showLoadingIndicator) {
        if (showLoadingAnimation && showLoadingIndicator) {
            showLoadingIndicator = false;
        }

        this.loadResult(this.isSelectedTabNext(), forceUpdate, showLoadingAnimation, showLoadingIndicator, this.mSelectedList);
        this.loadResult(!this.isSelectedTabNext(), forceUpdate, showLoadingAnimation, showLoadingIndicator, this.mSelectedList);
    }

    private void loadResult(final boolean getNext, final boolean forceUpdate, final boolean showLoadingAnimation, final boolean showLoadingIndicator, int selectedList) {
        ItemListContract.View childView = (getNext ? (this.mChildViewNext) : (this.mChildViewCurrent));

        if (showLoadingAnimation) {
            this.mView.showLoadingAnimation(true);
        }

        if (showLoadingIndicator) {
            childView.showLoadingIndicator(true);
        }

        UseCaseGetResult.RequestValue requestValue = new UseCaseGetResult.RequestValue(getNext, forceUpdate, showLoadingAnimation, showLoadingIndicator, selectedList);

        UseCaseGetResult useCaseGetResult = (getNext ? (this.mUseCaseGetResultNext) : (this.mUseCaseGetResultCurrent));

        this.mUseCaseHandler.execute(useCaseGetResult, requestValue, new UseCase.UseCaseCallback<UseCaseGetResult.ResponseValue, UseCaseGetResult.ErrorResponseValue>() {
            @Override
            public void onSuccess(UseCaseGetResult.ResponseValue response) {
                Result result = response.getResult();
                UseCaseGetResult.RequestValue requestValue = response.getRequestValue();

                boolean getNext = requestValue.isGetNext();
                boolean forceUpdate = requestValue.isForceUpdate();
                boolean showLoadingAnimation = requestValue.isShowLoadingAnimation();
                boolean showLoadingIndicator = requestValue.isShowLoadingIndicator();

                int selectedList = requestValue.getSelectedList();

                ItemListContract.View childView = (getNext ? (mChildViewNext) : (mChildViewCurrent));

                if (!mView.isActive() || !childView.isActive()) {
                    return;
                }

                if (showLoadingAnimation) {
                    mView.showLoadingAnimation(false);
                }

                if (showLoadingIndicator) {
                    childView.showLoadingIndicator(false);
                }

                result = Result.map(result);

                List<Result.ParentClass> parentClassList = result.getParentClassList();

                if (parentClassList.size() == 0) {
                    this.onError();

                    return;
                }

                if (selectedList >= parentClassList.size()) {
                    selectedList = parentClassList.size() - 1;
                }

                if (selectedList < 0) {
                    selectedList = 0;
                }

                Result.ParentClass parentClass = parentClassList.get(selectedList);

                List<Result.ParentClass.Day.ListItem> listItemList = Result.ParentClass.flatten(parentClass);

                mView.showResultList(parentClassList, selectedList);
                if (listItemList.isEmpty()) {
                    childView.showEmptyItemList();
                } else {
                    Result.ParentClass.Day.Header infoHeader = new Result.ParentClass.Day.Header();
                    infoHeader.setTitle(parentClass.getName());
                    listItemList.add(0, infoHeader);

                    Result.ParentClass.Day.Message infoMessage = new Result.ParentClass.Day.Message();
                    infoMessage.setFirst(parentClass.getDate());
                    infoMessage.setLast(parentClass.hasWeek() ? "(" + parentClass.getWeek() + ")" : "");
                    listItemList.add(1, infoMessage);

                    childView.showResultItemList(listItemList);
                }

                if (!forceUpdate) {
                    return;
                }

                setTimestampMsNow();

                displayLastTime();
            }

            @Override
            public void onError(UseCaseGetResult.ErrorResponseValue errorResponse) {
                UseCaseGetResult.RequestValue requestValue = errorResponse.getRequestValue();

                boolean getNext = requestValue.isGetNext();
                boolean forceUpdate = requestValue.isForceUpdate();
                boolean showLoadingAnimation = requestValue.isShowLoadingAnimation();
                boolean showLoadingIndicator = requestValue.isShowLoadingIndicator();

                int selectedList = requestValue.getSelectedList();

                ItemListContract.View childView = (getNext ? (mChildViewNext) : (mChildViewCurrent));

                if (!mView.isActive() || !childView.isActive()) {
                    return;
                }

                if (showLoadingAnimation) {
                    mView.showLoadingAnimation(false);
                }

                if (showLoadingIndicator) {
                    childView.showLoadingIndicator(false);
                }

                mView.showError(forceUpdate, showLoadingAnimation, showLoadingIndicator);
            }

            private void onError() {
                ItemListContract.View childView = (getNext ? (mChildViewNext) : (mChildViewCurrent));

                if (!mView.isActive() || !childView.isActive()) {
                    return;
                }

                if (showLoadingAnimation) {
                    mView.showLoadingAnimation(false);
                }

                if (showLoadingIndicator) {
                    childView.showLoadingIndicator(false);
                }

                mView.showError();
            }
        });
    }

    @Override
    public void displayLastTime() {
        this.mView.showLastTime(this.mTimestampMs);
    }

    @Override
    public int getSelectedList() {
        return this.mSelectedList;
    }

    @Override
    public void setSelectedList(int selectedList) {
        this.mSelectedList = selectedList;
    }

    @Override
    public long getTimestampMs() {
        return this.mTimestampMs;
    }

    private void setTimestampMsNow() {
        this.setTimestampMs(System.currentTimeMillis());
    }

    @Override
    public void setTimestampMs(long timestampMs) {
        this.mTimestampMs = timestampMs;
    }

    private boolean isSelectedTabNext() {
        return this.isTabNext(this.mSelectedTab);
    }

    @Override
    public int getSelectedTab() {
        return this.mSelectedTab;
    }

    @Override
    public void setSelectedTab(int selectedTab) {
        this.mSelectedTab = selectedTab;
    }

    private boolean isTabNext(int position) {
        return (position != 0) && (position == 1);
    }
}
