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

import java.util.List;

import de.GameplayJDK.Vertretungsplan.Activity.List.Extra.MultipleViewPresenter;
import de.GameplayJDK.Vertretungsplan.Activity.List.Fragment.ItemListContract;
import de.GameplayJDK.Vertretungsplan.Data.Model.Result;
import de.GameplayJDK.Vertretungsplan.Mvp.BaseView;

/**
 * Created by GameplayJDK on 03.12.2016.
 */

public interface ListContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showLoadingAnimation(boolean active);

        void showEmptyList();

        void showResultList(List<Result.ParentClass> parentClassList, int selectedList);

        void showLastTime(long timestampMs);

        void showError();

        void showError(boolean forceUpdate, boolean showLoadingAnimation, boolean showLoadingIndicator);

        void showAboutDialog();
    }

    interface Presenter extends MultipleViewPresenter<ItemListContract.View> {

        void loadResult(boolean forceUpdate, boolean showLoadingAnimation, boolean showLoadingIndicator);

        void displayLastTime();

        int getSelectedList();

        void setSelectedList(int selectedList);

        long getTimestampMs();

        void setTimestampMs(long timestampMs);

        int getSelectedTab();

        void setSelectedTab(int selectedTab);
    }
}
