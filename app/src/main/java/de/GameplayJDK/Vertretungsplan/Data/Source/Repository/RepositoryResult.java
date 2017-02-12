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

package de.GameplayJDK.Vertretungsplan.Data.Source.Repository;

import android.content.Context;
import android.util.Log;

import de.GameplayJDK.Vertretungsplan.Data.Model.Result;
import de.GameplayJDK.Vertretungsplan.Data.Source.Cache.DataSourceCacheResult;
import de.GameplayJDK.Vertretungsplan.Data.Source.DataSourceResult;
import de.GameplayJDK.Vertretungsplan.Data.Source.Local.DataSourceLocalResult;
import de.GameplayJDK.Vertretungsplan.Data.Source.Remote.DataSourceRemoteResult;

/**
 * Created by GameplayJDK on 08.12.2016.
 */

public class RepositoryResult implements DataSourceResult {

    private static RepositoryResult sInstance;

    private DataSourceCacheResult mDataSourceCache;
    private DataSourceLocalResult mDataSourceLocal;
    private DataSourceRemoteResult mDataSourceRemote;

    private RepositoryResult(DataSourceLocalResult dataSourceLocal, DataSourceRemoteResult dataSourceRemote, DataSourceCacheResult dataSourceCache) {
        this.mDataSourceCache = dataSourceCache;
        this.mDataSourceLocal = dataSourceLocal;
        this.mDataSourceRemote = dataSourceRemote;

        this.mDataSourceCache.setDirty(true);
    }

    public static RepositoryResult getInstance(DataSourceLocalResult dataSourceLocal, DataSourceRemoteResult dataSourceRemote, DataSourceCacheResult dataSourceCache) {
        if (sInstance == null) {
            sInstance = new RepositoryResult(dataSourceLocal, dataSourceRemote, dataSourceCache);
        }

        return sInstance;
    }

    public static RepositoryResult getInstance() {
        if (sInstance == null) {
            sInstance = new RepositoryResult(DataSourceLocalResult.getInstance(), DataSourceRemoteResult.getInstance(), DataSourceCacheResult.getInstance());
        }

        return sInstance;
    }

    @Override
    public void getResultCurrent(final GetResultCallback callback) {
        if (this.mDataSourceCache.isDirty()) {
            this.mDataSourceCache.setDirty(false);

            if (this.mDataSourceRemote.isAvailable()) {
                this.mDataSourceRemote.getResultCurrent(new GetResultCallback() {
                    @Override
                    public void onSuccess(Result result) {
                        mDataSourceCache.setResultCurrent(result);
                        mDataSourceLocal.setResultCurrent(result);

                        callback.onSuccess(result);
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
            } else {
                if (this.mDataSourceLocal.isAvailableCurrent()) {
                    this.mDataSourceLocal.getResultCurrent(new GetResultCallback() {
                        @Override
                        public void onSuccess(Result result) {
                            mDataSourceCache.setResultCurrent(result);

                            callback.onSuccess(result);
                        }

                        @Override
                        public void onError() {
                            callback.onError();
                        }
                    });
                } else {
                    callback.onError();
                }
            }
        } else {
            this.mDataSourceCache.getResultCurrent(new GetResultCallback() {
                @Override
                public void onSuccess(Result result) {
                    callback.onSuccess(result);
                }

                @Override
                public void onError() {
                    callback.onError();
                }
            });
        }
    }

    @Override
    public void setResultCurrent(Result result) {
        // The implementation is not needed here
    }

    @Override
    public void getResultNext(final GetResultCallback callback) {
        if (this.mDataSourceCache.isDirty()) {
            this.mDataSourceCache.setDirty(false);

            if (this.mDataSourceRemote.isAvailable()) {
                this.mDataSourceRemote.getResultNext(new GetResultCallback() {
                    @Override
                    public void onSuccess(Result result) {
                        mDataSourceCache.setResultNext(result);
                        mDataSourceLocal.setResultNext(result);

                        callback.onSuccess(result);
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
            } else {
                if (this.mDataSourceLocal.isAvailableNext()) {
                    this.mDataSourceLocal.getResultNext(new GetResultCallback() {
                        @Override
                        public void onSuccess(Result result) {
                            mDataSourceCache.setResultNext(result);

                            callback.onSuccess(result);
                        }

                        @Override
                        public void onError() {
                            callback.onError();
                        }
                    });
                } else {
                    callback.onError();
                }
            }
        } else {
            this.mDataSourceCache.getResultNext(new GetResultCallback() {
                @Override
                public void onSuccess(Result result) {
                    callback.onSuccess(result);
                }

                @Override
                public void onError() {
                    callback.onError();
                }
            });
        }
    }

    @Override
    public void setResultNext(Result result) {
        // The implementation is not needed here
    }

    public void setApplicationContext(Context context) {
        this.mDataSourceLocal.setApplicationContext(context);
        this.mDataSourceRemote.setApplicationContext(context);
    }

    public void setDirty(boolean mDirty) {
        this.mDataSourceCache.setDirty(mDirty);
    }

    public void getResultCurrent(GetResultCallback callback, boolean dirty) {
        this.setDirty(dirty);

        this.getResultCurrent(callback);
    }

    public void getResultNext(GetResultCallback callback, boolean dirty) {
        this.setDirty(dirty);

        this.getResultNext(callback);
    }
}
