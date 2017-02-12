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

package de.GameplayJDK.Vertretungsplan.Data.Source.Remote;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import de.GameplayJDK.Vertretungsplan.Data.Model.Host;
import de.GameplayJDK.Vertretungsplan.Data.Source.DataSourceHost;

/**
 * Created by GameplayJDK on 07.12.2016.
 */

public class DataSourceRemoteHost implements DataSourceRemote, DataSourceHost {

    private static final String SERVICE_HOST = "vpl.gameplayjdk.de";
    private static final int SERVICE_PORT = 80;
    private static final int SERVICE_TIMEOUT = 1000 * 2;

    private static final String REQUEST_URL = "http://vpl.GameplayJDK.de/vpl";
    private static final String REQUEST_TAG = "vpl-host";

    private static final int REQUEST_TIMEOUT_MS = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;

    private static DataSourceRemoteHost sInstance;

    private static Gson sGson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.serializeNulls();
        gsonBuilder.serializeSpecialFloatingPointValues();

        sGson = gsonBuilder.create();
    }

    private static RequestQueue sRequestQueue;

    private DataSourceRemoteHost() {
    }

    public static DataSourceRemoteHost getInstance() {
        if (sInstance == null) {
            sInstance = new DataSourceRemoteHost();
        }

        return sInstance;
    }

    public void setApplicationContext(Context context) {
        sRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void getHost(GetHostCallback callback) {
        this.sendStringRequest(this.getRequest(callback));
    }

    @Override
    public void setHost(Host host) {
        // No implementation needed for remote data source
    }

    private StringRequest getRequest(final GetHostCallback callback) {
        String route = REQUEST_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, route, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Host host = sGson.fromJson(response, Host.class);

                callback.onSuccess(host);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        stringRequest.setShouldCache(false);
        stringRequest.setTag(REQUEST_TAG);

        return stringRequest;
    }

    private void sendStringRequest(StringRequest request) {
        this.cancelStringRequest();

        if (sRequestQueue != null) {
            sRequestQueue.add(request);
        } else {
            throw new RuntimeException("Call setApplicationContext on " + DataSourceRemoteHost.class.getName() + " to initialize sRequestQueue!");
        }
    }

    private void cancelStringRequest() {
        if (sRequestQueue != null) {
            sRequestQueue.cancelAll(REQUEST_TAG);
        } else {
            throw new RuntimeException("Call setApplicationContext on " + DataSourceRemoteHost.class.getName() + " to initialize sRequestQueue!");
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            InetAddress address = InetAddress.getByName(SERVICE_HOST);

            try (Socket socket = new Socket()) {
                SocketAddress socketAddress = new InetSocketAddress(address, SERVICE_PORT);

                socket.connect(socketAddress, SERVICE_TIMEOUT);
                if (socket.isConnected()) {
                    socket.close();

                    return true;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return address.isReachable(SERVICE_TIMEOUT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
