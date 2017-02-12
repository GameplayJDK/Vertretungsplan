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

import de.GameplayJDK.Vertretungsplan.Data.Model.Result;
import de.GameplayJDK.Vertretungsplan.Data.Source.DataSourceResult;

/**
 * Created by GameplayJDK on 07.12.2016.
 */

public class DataSourceLocalResult implements DataSourceLocal, DataSourceResult {

    private static final String LOCAL_FILE_CURRENT = "cache-current.json";
    private static final String LOCAL_FILE_NEXT = "cache-next.json";

    private static DataSourceLocalResult sInstance;

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

    private DataSourceLocalResult() {
    }

    public static DataSourceLocalResult getInstance() {
        if (sInstance == null) {
            sInstance = new DataSourceLocalResult();
        }

        return sInstance;
    }

    public void setApplicationContext(Context context) {
        sFilesDirectory = context.getFilesDir();
    }

    @Override
    public void getResultCurrent(GetResultCallback callback) {
        File file = new File(sFilesDirectory, LOCAL_FILE_CURRENT);

        this.readFile(file, callback);
    }

    @Override
    public void setResultCurrent(Result result) {
        File file = new File(sFilesDirectory, LOCAL_FILE_CURRENT);

        this.writeFile(file, result);
    }

    @Override
    public void getResultNext(GetResultCallback callback) {
        File file = new File(sFilesDirectory, LOCAL_FILE_NEXT);

        this.readFile(file, callback);
    }

    @Override
    public void setResultNext(Result result) {
        File file = new File(sFilesDirectory, LOCAL_FILE_NEXT);

        this.writeFile(file, result);
    }

    private void readFile(File file, GetResultCallback callback) {
        String content = "";

        if (file.exists()) {
            int length = (int) file.length();

            try (FileReader fileReader = new FileReader(file)) {
                char[] contentBuffer = new char[length];

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

        Result result = sGson.fromJson(content, Result.class);

        callback.onSuccess(result);
    }

    private void writeFile(File file, Result result) {
        String content = sGson.toJson(result);

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
        return this.isAvailableCurrent() && this.isAvailableNext();
    }

    public boolean isAvailableCurrent() {
        File file = new File(sFilesDirectory, LOCAL_FILE_CURRENT);

        return file.exists();
    }

    public boolean isAvailableNext() {
        File file = new File(sFilesDirectory, LOCAL_FILE_NEXT);

        return file.exists();
    }
}
