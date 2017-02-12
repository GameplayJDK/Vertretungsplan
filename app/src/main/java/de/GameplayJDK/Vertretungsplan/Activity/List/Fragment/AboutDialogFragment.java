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

package de.GameplayJDK.Vertretungsplan.Activity.List.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import de.GameplayJDK.Vertretungsplan.BuildConfig;
import de.GameplayJDK.Vertretungsplan.R;

/**
 * Created by GameplayJDK on 10.12.2016.
 */

public class AboutDialogFragment extends DialogFragment implements AboutDialogContract.View {

    private AboutDialogContract.Presenter mPresenter;

    private ImageView mAppIcon;
    private TextView mAppInfo;
    private TextView mAppAbout;

    public AboutDialogFragment() {
    }

    public static AboutDialogFragment newInstance() {
        return new AboutDialogFragment();
    }

    @Override
    public void setPresenter(AboutDialogContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_dialog, container, false);

        this.mAppIcon = (ImageView) root.findViewById(R.id.app_icon);

        this.mAppInfo = (TextView) root.findViewById(R.id.app_info);
        this.mAppInfo.append(" v" + BuildConfig.VERSION_NAME);

        this.mAppAbout = (TextView) root.findViewById(R.id.app_about);

        return root;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }
}
