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

import de.GameplayJDK.Vertretungsplan.Data.Model.Host;
import de.GameplayJDK.Vertretungsplan.Data.Source.Cache.DataSourceCacheHost;
import de.GameplayJDK.Vertretungsplan.Data.Source.DataSourceHost;
import de.GameplayJDK.Vertretungsplan.Data.Source.Local.DataSourceLocalHost;
import de.GameplayJDK.Vertretungsplan.Data.Source.Remote.DataSourceRemoteHost;

/**
 * Created by GameplayJDK on 08.12.2016.
 */

public class RepositoryHost implements DataSourceHost {

    private static RepositoryHost sInstance;

    private DataSourceCacheHost mDataSourceCache;
    private DataSourceLocalHost mDataSourceLocal;
    private DataSourceRemoteHost mDataSourceRemote;

    public RepositoryHost(DataSourceCacheHost dataSourceCache, DataSourceLocalHost dataSourceLocal, DataSourceRemoteHost dataSourceRemote) {
        this.mDataSourceCache = dataSourceCache;
        this.mDataSourceLocal = dataSourceLocal;
        this.mDataSourceRemote = dataSourceRemote;
    }

    public static RepositoryHost getInstance(DataSourceCacheHost dataSourceCache, DataSourceLocalHost dataSourceLocal, DataSourceRemoteHost dataSourceRemote) {
        if (sInstance == null) {
            sInstance = new RepositoryHost(dataSourceCache, dataSourceLocal, dataSourceRemote);
        }

        return sInstance;
    }

    public static RepositoryHost getInstance() {
        if (sInstance == null) {
            sInstance = new RepositoryHost(DataSourceCacheHost.getInstance(), DataSourceLocalHost.getInstance(), DataSourceRemoteHost.getInstance());
        }

        return sInstance;
    }

    @Override
    public void getHost(final GetHostCallback callback) {
        if (this.mDataSourceCache.isDirty()) {
            this.mDataSourceCache.setDirty(false);

            if (this.mDataSourceRemote.isAvailable()) {
                this.mDataSourceRemote.getHost(new GetHostCallback() {
                    @Override
                    public void onSuccess(Host host) {
                        mDataSourceCache.setHost(host);
                        mDataSourceLocal.setHost(host);

                        callback.onSuccess(host);
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
            } else {
                if (this.mDataSourceLocal.isAvailable()) {
                    this.mDataSourceLocal.getHost(new GetHostCallback() {
                        @Override
                        public void onSuccess(Host host) {
                            mDataSourceCache.setHost(host);

                            callback.onSuccess(host);
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
            this.mDataSourceCache.getHost(new GetHostCallback() {
                @Override
                public void onSuccess(Host host) {
                    callback.onSuccess(host);
                }

                @Override
                public void onError() {
                    callback.onError();
                }
            });
        }
    }

    @Override
    public void setHost(Host host) {
        // The implementation is not needed here
    }

    public void setApplicationContext(Context context) {
        this.mDataSourceLocal.setApplicationContext(context);
        this.mDataSourceRemote.setApplicationContext(context);
    }

    public void setDirty(boolean mDirty) {
        this.mDataSourceCache.setDirty(mDirty);
    }

    public void getHost(GetHostCallback callback, boolean dirty) {
        this.setDirty(dirty);

        this.getHost(callback);
    }
}
