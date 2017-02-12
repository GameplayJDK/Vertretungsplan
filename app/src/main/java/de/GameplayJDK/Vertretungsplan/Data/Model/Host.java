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

package de.GameplayJDK.Vertretungsplan.Data.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Created by GameplayJDK on 07.12.2016.
 */

public class Host {

    @Expose
    @SerializedName("addressList")
    private Address[] mAddressList;

    public Host() {
    }

    public List<Address> getAddressList() {
        return Arrays.asList(this.mAddressList);
    }

    public void setAddressList(Address[] mAddressList) {
        this.mAddressList = mAddressList;
    }

    public static Host map(Host host) {
        for (Address address : host.getAddressList()) {
            address.setHost(host);
        }

        return host;
    }

    public static class Address {

        @Expose
        @SerializedName("realName")
        private String mRealName;

        @Expose
        @SerializedName("route")
        private String mRoute;

        private Host mHost;

        public Address() {
        }

        public Host getHost() {
            return this.mHost;
        }

        public void setHost(Host host) {
            this.mHost = host;
        }

        public String getRealName() {
            return this.mRealName;
        }

        public void setRealName(String mRealName) {
            this.mRealName = mRealName;
        }

        public String getRoute() {
            return this.mRoute;
        }

        public void setRoute(String mRoute) {
            this.mRoute = mRoute;
        }
    }
}
