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

import de.GameplayJDK.Vertretungsplan.Data.Model.Result;
import de.GameplayJDK.Vertretungsplan.Data.Source.DataSourceResult;

/**
 * Created by GameplayJDK on 07.12.2016.
 */

public class DataSourceRemoteResult implements DataSourceRemote, DataSourceResult {

    private static final String SERVICE_HOST = "vpl.gameplayjdk.de";
    private static final int SERVICE_PORT = 80;
    private static final int SERVICE_TIMEOUT = 1000 * 2;

    private static final String REQUEST_URL = "http://vpl.GameplayJDK.de/vpl";
    private static final String REQUEST_TAG_CURRENT = "vpl-result-current";
    private static final String REQUEST_TAG_NEXT = "vpl-result-next";

    private static final int REQUEST_TIMEOUT_MS = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;

    private static DataSourceRemoteResult sInstance;

    private static Gson sGson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.serializeNulls();
        gsonBuilder.serializeSpecialFloatingPointValues();

        sGson = gsonBuilder.create();
    }

    private static RequestQueue sRequestQueue;

    private final String mRoute;

    private DataSourceRemoteResult() {
        // impl: dynamic route based on selected host
        this.mRoute = "/ess";
    }

    public static DataSourceRemoteResult getInstance() {
        if (sInstance == null) {
            sInstance = new DataSourceRemoteResult();
        }

        return sInstance;
    }

    @Override
    public void getResultCurrent(GetResultCallback callback) {
        String route = "/current";

        this.sendStringRequest(this.getRequest(route, REQUEST_TAG_CURRENT, callback));
    }

    @Override
    public void setResultCurrent(Result result) {
        // No implementation needed for remote data source
    }

    @Override
    public void getResultNext(GetResultCallback callback) {
        String route = "/next";

        this.sendStringRequest(this.getRequest(route, REQUEST_TAG_NEXT, callback));
    }

    @Override
    public void setResultNext(Result result) {
        // No implementation needed for remote data source
    }

    public void setApplicationContext(Context context) {
        sRequestQueue = Volley.newRequestQueue(context);
    }

    private StringRequest getRequest(String route, String tag, final GetResultCallback callback) {
        route = REQUEST_URL + this.mRoute + route;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, route, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Result result = sGson.fromJson(response, Result.class);

                callback.onSuccess(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        stringRequest.setShouldCache(false);
        stringRequest.setTag(tag);

        return stringRequest;
    }

    private void sendStringRequest(StringRequest request) {
        this.cancelStringRequest((String) request.getTag());

        if (sRequestQueue != null) {
            sRequestQueue.add(request);
        } else {
            throw new RuntimeException("Call setApplicationContext on " + DataSourceRemoteResult.class.getName() + " to initialize mRequestQueue!");
        }
    }

    private void cancelStringRequest(String tag) {
        if (sRequestQueue != null) {
            sRequestQueue.cancelAll(tag);
        } else {
            throw new RuntimeException("Call setApplicationContext on " + DataSourceRemoteResult.class.getName() + " to initialize mRequestQueue!");
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
