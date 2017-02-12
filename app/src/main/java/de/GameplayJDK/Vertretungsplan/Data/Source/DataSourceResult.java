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

package de.GameplayJDK.Vertretungsplan.Data.Source;

import de.GameplayJDK.Vertretungsplan.Data.Model.Result;

/**
 * Created by GameplayJDK on 07.12.2016.
 */

public interface DataSourceResult extends DataSource {

    interface GetResultCallback {

        void onSuccess(Result result);

        void onError();
    }

    // GET /vpl/ess
    // GET /vpl/ess/current
    void getResultCurrent(GetResultCallback callback);

    void setResultCurrent(Result result);

    // GET /vpl/ess/next
    void getResultNext(GetResultCallback callback);

    void setResultNext(Result result);
}
