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

import java.util.List;

import de.GameplayJDK.Vertretungsplan.Data.Model.Result;
import de.GameplayJDK.Vertretungsplan.Mvp.BasePresenter;
import de.GameplayJDK.Vertretungsplan.Mvp.BaseView;

/**
 * Created by GameplayJDK on 03.12.2016.
 */

public interface ItemListContract {

    interface View extends BaseView<Presenter> {

        int getTabPosition();

        boolean isActive();

        void showLoadingIndicator(boolean active);

        void showEmptyItemList();

        void showResultItemList(List<Result.ParentClass.Day.ListItem> listItemList);
    }

    interface Presenter extends BasePresenter {

        void loadResult(boolean forceUpdate, boolean showLoadingAnimation, boolean showLoadingIndicator);
    }
}
