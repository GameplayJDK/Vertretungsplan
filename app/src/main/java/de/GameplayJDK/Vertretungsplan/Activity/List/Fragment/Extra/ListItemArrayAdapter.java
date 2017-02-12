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

package de.GameplayJDK.Vertretungsplan.Activity.List.Fragment.Extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.GameplayJDK.Vertretungsplan.Data.Model.Result;
import de.GameplayJDK.Vertretungsplan.R;

/**
 * Created by GameplayJDK on 03.12.2016.
 */

public class ListItemArrayAdapter extends ArrayAdapter<Result.ParentClass.Day.ListItem> {

    private static final int ITEM_NORMAL = 0;
    private static final int ITEM_SPECIAL = 1;
    private static final int ITEM_HEADER = 2;

    private static final int LAYOUT_NORMAL = R.layout.layout_item_normal;
    private static final int LAYOUT_SPECIAL = R.layout.layout_item_special;
    private static final int LAYOUT_HEADER = R.layout.layout_item_header;

    public ListItemArrayAdapter(Context context) {
        super(context, ListItemArrayAdapter.ITEM_NORMAL);
    }

    public void update(List<Result.ParentClass.Day.ListItem> listItemList) {
        this.clear();

        this.addAll(listItemList);
    }

    @Override
    public int getViewTypeCount() {
        int[] layout = {
                ITEM_NORMAL,
                ITEM_SPECIAL,
                ITEM_HEADER
        };

        return layout.length;
    }

    @Override
    public int getItemViewType(int position) {
        Result.ParentClass.Day.ListItem item = this.getItem(position);

        int type = ITEM_NORMAL;
        if (item instanceof Result.ParentClass.Day.Entry) {
            type = ITEM_NORMAL;
        } else if (item instanceof Result.ParentClass.Day.Message) {
            type = ITEM_SPECIAL;
        } else if (item instanceof Result.ParentClass.Day.Header) {
            type = ITEM_HEADER;
        }

        return type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());

        int type = this.getItemViewType(position);
        if (type == ITEM_NORMAL) {
            Result.ParentClass.Day.Entry itemNormal = (Result.ParentClass.Day.Entry) this.getItem(position);
            ViewHolderNormal viewHolderNormal;

            if (convertView == null) {
                convertView = layoutInflater.inflate(LAYOUT_NORMAL, parent, false);

                viewHolderNormal = new ViewHolderNormal(convertView);
                viewHolderNormal.loadContent();

                convertView.setTag(viewHolderNormal);
            } else {
                viewHolderNormal = (ViewHolderNormal) convertView.getTag();
            }

            viewHolderNormal.getTextViewHour().setText(itemNormal.getHour());
            viewHolderNormal.getTextViewTeacher().setText(itemNormal.getTeacher());
            viewHolderNormal.getTextViewSubject().setText(itemNormal.getFor());
            viewHolderNormal.getTextViewRoom().setText(itemNormal.getRoom());
            viewHolderNormal.getTextViewInsteadOf().setText(itemNormal.getInsteadOf());
            viewHolderNormal.getTextViewInfo().setText(itemNormal.getInfo());
            viewHolderNormal.getTextViewType().setText(itemNormal.getType());
        } else if (type == ITEM_SPECIAL) {
            Result.ParentClass.Day.Message itemSpecial = (Result.ParentClass.Day.Message) this.getItem(position);
            ViewHolderSpecial viewHolderSpecial;

            if (convertView == null) {
                convertView = layoutInflater.inflate(LAYOUT_SPECIAL, parent, false);

                viewHolderSpecial = new ViewHolderSpecial(convertView);
                viewHolderSpecial.loadContent();

                convertView.setTag(viewHolderSpecial);
            } else {
                viewHolderSpecial = (ViewHolderSpecial) convertView.getTag();
            }

            viewHolderSpecial.getTextViewFirst().setText(itemSpecial.getFirst());
            viewHolderSpecial.getTextViewLast().setText(itemSpecial.getLast());
        } else if (type == ITEM_HEADER) {
            Result.ParentClass.Day.Header itemHeader = (Result.ParentClass.Day.Header) this.getItem(position);
            ViewHolderHeader viewHolderHeader;

            if (convertView == null) {
                convertView = layoutInflater.inflate(LAYOUT_HEADER, parent, false);

                viewHolderHeader = new ViewHolderHeader(convertView);
                viewHolderHeader.loadContent();

                convertView.setTag(viewHolderHeader);
            } else {
                viewHolderHeader = (ViewHolderHeader) convertView.getTag();
            }

            viewHolderHeader.getTextViewTitle().setText(itemHeader.getTitle());
        }

        return convertView;
    }

    public interface ViewHolder {

        void loadContent();

        View getView();
    }

    public static class ViewHolderNormal implements ViewHolder {

        private TextView mTextViewHour;
        private TextView mTextViewTeacher;
        private TextView mTextViewSubject;
        private TextView mTextViewRoom;
        private TextView mTextViewInsteadOf;
        private TextView mTextViewInfo;
        private TextView mTextViewType;

        private View mView;

        public ViewHolderNormal(View view) {
            this.mView = view;
        }

        @Override
        public void loadContent() {
            this.mTextViewHour = (TextView) this.mView.findViewById(R.id.item_hour);
            this.mTextViewTeacher = (TextView) this.mView.findViewById(R.id.item_teacher);
            this.mTextViewSubject = (TextView) this.mView.findViewById(R.id.item_subject);
            this.mTextViewRoom = (TextView) this.mView.findViewById(R.id.item_room);
            this.mTextViewInsteadOf = (TextView) this.mView.findViewById(R.id.item_insteadof);
            this.mTextViewInfo = (TextView) this.mView.findViewById(R.id.item_info);
            this.mTextViewType = (TextView) this.mView.findViewById(R.id.item_type);
        }

        @Override
        public View getView() {
            return this.mView;
        }

        public TextView getTextViewHour() {
            return this.mTextViewHour;
        }

        public void setTextViewHour(TextView mTextViewHour) {
            this.mTextViewHour = mTextViewHour;
        }

        public TextView getTextViewTeacher() {
            return this.mTextViewTeacher;
        }

        public void setTextViewTeacher(TextView mTextViewTeacher) {
            this.mTextViewTeacher = mTextViewTeacher;
        }

        public TextView getTextViewSubject() {
            return this.mTextViewSubject;
        }

        public void setTextViewSubject(TextView mTextViewSubject) {
            this.mTextViewSubject = mTextViewSubject;
        }

        public TextView getTextViewRoom() {
            return this.mTextViewRoom;
        }

        public void setTextViewRoom(TextView mTextViewRoom) {
            this.mTextViewRoom = mTextViewRoom;
        }

        public TextView getTextViewInsteadOf() {
            return this.mTextViewInsteadOf;
        }

        public void setTextViewInsteadOf(TextView mTextViewInsteadOf) {
            this.mTextViewInsteadOf = mTextViewInsteadOf;
        }

        public TextView getTextViewInfo() {
            return this.mTextViewInfo;
        }

        public void setTextViewInfo(TextView mTextViewFor) {
            this.mTextViewInfo = mTextViewFor;
        }

        public TextView getTextViewType() {
            return this.mTextViewType;
        }

        public void setTextViewType(TextView mTextViewType) {
            this.mTextViewType = mTextViewType;
        }
    }

    public static class ViewHolderSpecial implements ViewHolder {

        private TextView mTextViewFirst;
        private TextView mTextViewSecond;

        private View mView;

        public ViewHolderSpecial(View view) {
            this.mView = view;
        }

        @Override
        public void loadContent() {
            this.mTextViewFirst = (TextView) this.mView.findViewById(R.id.item_first);
            this.mTextViewSecond = (TextView) this.mView.findViewById(R.id.item_second);
        }

        @Override
        public View getView() {
            return this.mView;
        }

        public TextView getTextViewFirst() {
            return this.mTextViewFirst;
        }

        public void setTextViewFirst(TextView mTextViewFirst) {
            this.mTextViewFirst = mTextViewFirst;
        }

        public TextView getTextViewLast() {
            return this.mTextViewSecond;
        }

        public void setTextViewSecond(TextView mTextViewSecond) {
            this.mTextViewSecond = mTextViewSecond;
        }
    }

    public static class ViewHolderHeader implements ViewHolder {

        private TextView mTextViewTitle;

        private View mView;

        public ViewHolderHeader(View view) {
            this.mView = view;
        }

        @Override
        public void loadContent() {
            this.mTextViewTitle = (TextView) this.mView.findViewById(R.id.item_title);
        }

        @Override
        public View getView() {
            return this.mView;
        }

        public TextView getTextViewTitle() {
            return this.mTextViewTitle;
        }

        public void setTextViewTitle(TextView mTextViewTitle) {
            this.mTextViewTitle = mTextViewTitle;
        }
    }
}
