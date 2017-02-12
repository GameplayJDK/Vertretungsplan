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

package de.GameplayJDK.Vertretungsplan.Data.Source.Cache;

import de.GameplayJDK.Vertretungsplan.Data.Model.Result;
import de.GameplayJDK.Vertretungsplan.Data.Source.DataSourceResult;

/**
 * Created by GameplayJDK on 08.12.2016.
 */

public class DataSourceCacheResult implements DataSourceCache, DataSourceResult {

    private static DataSourceCacheResult sInstance;

    private boolean mDirty;

    private Result mResultCurrent;
    private Result mResultNext;

    private DataSourceCacheResult() {
        this.mDirty = false;

        this.mResultCurrent = null;
        this.mResultNext = null;
    }

    public static DataSourceCacheResult getInstance() {
        if (sInstance == null) {
            sInstance = new DataSourceCacheResult();
        }

        return sInstance;
    }

    @Override
    public void getResultCurrent(GetResultCallback callback) {
        if (this.mResultCurrent == null) {
            callback.onError();
        } else {
            callback.onSuccess(this.mResultCurrent);
        }
    }

    @Override
    public void setResultCurrent(Result result) {
        this.mResultCurrent = result;
    }

    @Override
    public void getResultNext(GetResultCallback callback) {
        if (this.mResultNext == null) {
            callback.onError();
        } else {
            callback.onSuccess(this.mResultNext);
        }
    }

    @Override
    public void setResultNext(Result result) {
        this.mResultNext = result;
    }

    public void clear() {
        this.mResultCurrent = null;
        this.mResultNext = null;
    }

    public boolean isDirty() {
        return this.mDirty;
    }

    public void setDirty(boolean mDirty) {
        if (!this.mDirty) {
            this.mDirty = mDirty;
        }
    }

    @Override
    public boolean isAvailable() {
        return this.isAvailableCurrent() && this.isAvailableNext();
    }

    public boolean isAvailableCurrent() {
        return (this.mResultCurrent != null);
    }

    public boolean isAvailableNext() {
        return (this.mResultNext != null);
    }
}
