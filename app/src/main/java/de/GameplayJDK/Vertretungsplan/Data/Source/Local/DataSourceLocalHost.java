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

package de.GameplayJDK.Vertretungsplan.Data.Source.Local;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import de.GameplayJDK.Vertretungsplan.Data.Model.Host;
import de.GameplayJDK.Vertretungsplan.Data.Source.DataSourceHost;

/**
 * Created by GameplayJDK on 07.12.2016.
 */

public class DataSourceLocalHost implements DataSourceLocal, DataSourceHost {

    private static final String LOCAL_FILE_HOST = "cache-host.json";

    private static DataSourceLocalHost sInstance;

    private static Gson sGson;

    private static File sFilesDirectory;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.serializeNulls();
        gsonBuilder.serializeSpecialFloatingPointValues();

        sGson = gsonBuilder.create();

        sFilesDirectory = null;
    }

    private DataSourceLocalHost() {
    }

    public static DataSourceLocalHost getInstance() {
        if (sInstance == null) {
            sInstance = new DataSourceLocalHost();
        }

        return sInstance;
    }

    public void setApplicationContext(Context context) {
        sFilesDirectory = context.getFilesDir();
    }

    @Override
    public void getHost(GetHostCallback callback) {
        File file = new File(sFilesDirectory, LOCAL_FILE_HOST);

        this.readFile(file, callback);
    }

    @Override
    public void setHost(Host host) {
        File file = new File(sFilesDirectory, LOCAL_FILE_HOST);

        this.writeFile(file, host);
    }

    private void readFile(File file, GetHostCallback callback) {
        String content = "";

        if (file.exists()) {
            long length = file.length();

            try (FileReader fileReader = new FileReader(file)) {
                char[] contentBuffer = new char[(int) length];

                int contentLength = fileReader.read(contentBuffer);

                if (contentLength != length) {
                    content = "";
                } else {
                    content = new String(contentBuffer, 0, contentLength);
                }
            } catch (IOException ex) {
                content = "";

                ex.printStackTrace();
            }
        }

        if (content.isEmpty()) {
            callback.onError();

            return;
        }

        Host host = sGson.fromJson(content, Host.class);

        callback.onSuccess(host);
    }

    private void writeFile(File file, Host host) {
        String content = sGson.toJson(host);

        try (FileWriter fileWriter = new FileWriter(file)) {
            int contentLength = content.length();

            char[] contentBuffer = content.toCharArray();

            fileWriter.write(contentBuffer, 0, contentLength);
            fileWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isAvailable() {
        File file = new File(sFilesDirectory, LOCAL_FILE_HOST);

        return file.exists();
    }
}
