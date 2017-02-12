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

public interface UseCaseScheduler {

    void execute(Runnable runnable);

    <V extends UseCase.ResponseValue, W extends UseCase.ErrorResponseValue> void notifyResponse(final V responseValue, final UseCase.UseCaseCallback<V, W> useCaseCallback);

    <V extends UseCase.ResponseValue, W extends UseCase.ErrorResponseValue> void notifyError(final W errorResponseValue, final UseCase.UseCaseCallback<V, W> useCaseCallback);
}
