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

import de.GameplayJDK.Vertretungsplan.Data.Model.Host;
import de.GameplayJDK.Vertretungsplan.Data.Source.DataSourceHost;

/**
 * Created by GameplayJDK on 08.12.2016.
 */

public class DataSourceCacheHost implements DataSourceCache, DataSourceHost {

    private static DataSourceCacheHost sInstance;

    private boolean mDirty;

    private Host mHost;

    private DataSourceCacheHost() {
        this.mDirty = false;

        this.mHost = null;
    }

    public static DataSourceCacheHost getInstance() {
        if (sInstance == null) {
            sInstance = new DataSourceCacheHost();
        }

        return sInstance;
    }

    @Override
    public void getHost(GetHostCallback callback) {
        if (this.mHost == null) {
            callback.onError();
        } else {
            callback.onSuccess(this.mHost);
        }
    }

    @Override
    public void setHost(Host host) {
        this.mHost = host;
    }

    public void clear() {
        this.mHost = null;
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
        return (this.mHost == null);
    }
}
