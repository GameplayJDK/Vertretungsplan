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

package de.GameplayJDK.Vertretungsplan.Mvp.Clean;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by GameplayJDK on 02.12.2016.
 */

public class UseCaseSchedulerThreadPool implements UseCaseScheduler {

    private final Handler mHandler;

    public static final int POOL_SIZE = 2;
    public static final int MAX_POOL_SIZE = 4;
    public static final int TIMEOUT = 30;

    private ThreadPoolExecutor mThreadPoolExecutor;

    public UseCaseSchedulerThreadPool() {
        this.mHandler = new Handler();

        this.mThreadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(POOL_SIZE), new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    @Override
    public void execute(Runnable runnable) {
        this.mThreadPoolExecutor.execute(runnable);
    }

    @Override
    public <V extends UseCase.ResponseValue, W extends UseCase.ErrorResponseValue> void notifyResponse(final V responseValue, final UseCase.UseCaseCallback<V, W> useCaseCallback) {
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                useCaseCallback.onSuccess(responseValue);
            }
        });
    }

    @Override
    public <V extends UseCase.ResponseValue, W extends UseCase.ErrorResponseValue> void notifyError(final W errorResponseValue, final UseCase.UseCaseCallback<V, W> useCaseCallback) {
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                useCaseCallback.onError(errorResponseValue);
            }
        });
    }
}
