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

/**
 * Created by GameplayJDK on 02.12.2016.
 */
public class UseCaseHandler {

    private static UseCaseHandler sInstance;

    private final UseCaseScheduler mUseCaseScheduler;

    public UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        this.mUseCaseScheduler = useCaseScheduler;
    }

    public static UseCaseHandler getInstance() {
        if (sInstance == null) {
            sInstance = new UseCaseHandler(new UseCaseSchedulerThreadPool());
        }

        return sInstance;
    }

    public <P extends UseCase.RequestValue, Q extends UseCase.ResponseValue, R extends UseCase.ErrorResponseValue> void execute(final UseCase<P, Q, R> useCase, P requestValue, UseCase.UseCaseCallback<Q, R> useCaseCallback) {
        useCase.setRequestValue(requestValue);

        UiCallbackWrapper uiCallbackWrapper = new UiCallbackWrapper<Q, R>(useCaseCallback, this);
        useCase.setUseCaseCallback(uiCallbackWrapper);

        this.mUseCaseScheduler.execute(new Runnable() {
            @Override
            public void run() {
                useCase.run();
            }
        });
    }

    private <V extends UseCase.ResponseValue, W extends UseCase.ErrorResponseValue> void notifyResponse(final V responseValue, final UseCase.UseCaseCallback<V, W> useCaseCallback) {
        this.mUseCaseScheduler.notifyResponse(responseValue, useCaseCallback);
    }

    private <V extends UseCase.ResponseValue, W extends UseCase.ErrorResponseValue> void notifyError(final W errorResponseValue, final UseCase.UseCaseCallback<V, W> useCaseCallback) {
        this.mUseCaseScheduler.notifyError(errorResponseValue, useCaseCallback);
    }

    private static class UiCallbackWrapper<V extends UseCase.ResponseValue, W extends UseCase.ErrorResponseValue> implements UseCase.UseCaseCallback<V, W> {

        private final UseCase.UseCaseCallback<V, W> mUseCaseCallback;

        private final UseCaseHandler mUseCaseHandler;

        public UiCallbackWrapper(UseCase.UseCaseCallback<V, W> useCaseCallback, UseCaseHandler useCaseHandler) {
            this.mUseCaseCallback = useCaseCallback;

            this.mUseCaseHandler = useCaseHandler;
        }

        @Override
        public void onSuccess(V response) {
            this.mUseCaseHandler.notifyResponse(response, this.mUseCaseCallback);
        }

        @Override
        public void onError(W errorResponse) {
            this.mUseCaseHandler.notifyError(errorResponse, this.mUseCaseCallback);
        }
    }
}
