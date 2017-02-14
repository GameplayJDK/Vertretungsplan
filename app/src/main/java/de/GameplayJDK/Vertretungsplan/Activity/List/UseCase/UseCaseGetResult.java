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

package de.GameplayJDK.Vertretungsplan.Activity.List.UseCase;

import de.GameplayJDK.Vertretungsplan.Data.Model.Result;
import de.GameplayJDK.Vertretungsplan.Data.Source.DataSourceResult;
import de.GameplayJDK.Vertretungsplan.Data.Source.Remote.DataSourceRemoteResult;
import de.GameplayJDK.Vertretungsplan.Data.Source.Repository.RepositoryResult;
import de.GameplayJDK.Vertretungsplan.Mvp.Clean.UseCase;

/**
 * Created by GameplayJDK on 09.12.2016.
 */

public class UseCaseGetResult extends UseCase<UseCaseGetResult.RequestValue, UseCaseGetResult.ResponseValue, UseCaseGetResult.ErrorResponseValue> {

    private final RepositoryResult mRepositoryResult;

    public UseCaseGetResult(RepositoryResult repositoryResult) {
        this.mRepositoryResult = repositoryResult;
    }

    public static UseCaseGetResult newInstance() {
        return new UseCaseGetResult(RepositoryResult.getInstance());
    }

    @Override
    protected void executeUseCase(final RequestValue requestValue) {

        boolean forceUpdate = requestValue.isForceUpdate();
        boolean getNext = requestValue.isGetNext();

        // only mark as updated if an update is forced and network is available
        final boolean updated = forceUpdate && DataSourceRemoteResult.getInstance().isAvailable();

        DataSourceResult.GetResultCallback callback = new DataSourceResult.GetResultCallback() {
            @Override
            public void onSuccess(Result result) {
                ResponseValue responseValue = new ResponseValue(result, requestValue, updated);

                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onError() {
                ErrorResponseValue errorResponseValue = new ErrorResponseValue(requestValue);

                getUseCaseCallback().onError(errorResponseValue);
            }
        };

        if (getNext) {
            this.mRepositoryResult.getResultNext(callback, forceUpdate);
        } else {
            this.mRepositoryResult.getResultCurrent(callback, forceUpdate);
        }
    }

    public static class RequestValue implements UseCase.RequestValue {

        private final boolean mGetNext;
        private final boolean mForceUpdate;
        private final boolean mShowLoadingAnimation;
        private final boolean mShowLoadingIndicator;

        private final int mSelectedList;

        public RequestValue(boolean getNext, boolean forceUpdate, boolean showLoadingAnimation, boolean showLoadingIndicator, int selectedList) {
            this.mGetNext = getNext;
            this.mForceUpdate = forceUpdate;
            this.mShowLoadingAnimation = showLoadingAnimation;
            this.mShowLoadingIndicator = showLoadingIndicator;

            this.mSelectedList = selectedList;
        }

        public boolean isGetNext() {
            return this.mGetNext;
        }

        public boolean isForceUpdate() {
            return this.mForceUpdate;
        }

        public boolean isShowLoadingAnimation() {
            return this.mShowLoadingAnimation;
        }

        public boolean isShowLoadingIndicator() {
            return this.mShowLoadingIndicator;
        }

        public int getSelectedList() {
            return this.mSelectedList;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

        private final Result mResult;
        private final RequestValue mRequestValue;

        private final boolean mUpdated;

        public ResponseValue(Result result, RequestValue requestValue, boolean updated) {
            this.mResult = result;
            this.mRequestValue = requestValue;

            this.mUpdated = updated;
        }

        public Result getResult() {
            return this.mResult;
        }

        public RequestValue getRequestValue() {
            return this.mRequestValue;
        }

        public boolean isUpdated() {
            return this.mUpdated;
        }
    }

    public static class ErrorResponseValue implements UseCase.ErrorResponseValue {

        private final RequestValue mRequestValue;

        public ErrorResponseValue(RequestValue requestValue) {
            this.mRequestValue = requestValue;
        }

        public RequestValue getRequestValue() {
            return this.mRequestValue;
        }
    }
}
