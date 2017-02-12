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

package de.GameplayJDK.Vertretungsplan.Data.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by GameplayJDK on 03.12.2016.
 */

public class Result {

    @Expose
    @SerializedName("parentClassList")
    private ParentClass[] mParentClassList;

    public Result() {
    }

    public List<ParentClass> getParentClassList() {
        return Arrays.asList(this.mParentClassList);
    }

    public void setParentClassList(ParentClass[] mParentClassList) {
        this.mParentClassList = mParentClassList;
    }

    public static Result map(Result result) {
        for (ParentClass parentClass : result.getParentClassList()) {
            parentClass.setResult(result);

            for (ParentClass.Day day : parentClass.getDayList()) {
                day.setParentClass(parentClass);

                for (ParentClass.Day.Entry entry : day.getEntryList()) {
                    entry.setDay(day);
                }

                for (ParentClass.Day.Message message : day.getMessageList()) {
                    message.setDay(day);
                }
            }
        }

        return result;
    }

    public static class ParentClass {

        @Expose
        @SerializedName("name")
        private String mName;

        @Expose
        @SerializedName("count")
        private int mCount;

        @Expose
        @SerializedName("dayList")
        private Day[] mDayList;

        @Expose
        @SerializedName("date")
        private String mDate;

        @Expose
        @SerializedName("week")
        private String mWeek;

        private Result mResult;

        public ParentClass() {
        }

        public Result getResult() {
            return this.mResult;
        }

        public void setResult(Result result) {
            this.mResult = result;
        }

        public String getName() {
            return this.mName;
        }

        public void setName(String mName) {
            this.mName = mName;
        }

        public int getCount() {
            return this.mCount;
        }

        public void setCount(int mCount) {
            this.mCount = mCount;
        }

        public int getIndex() {
            return this.getCount() - 1;
        }

        public List<Day> getDayList() {
            return Arrays.asList(this.mDayList);
        }

        public void setDayList(Day[] mDayList) {
            this.mDayList = mDayList;
        }

        public String getDate() {
            return this.mDate;
        }

        public void setDate(String mDate) {
            this.mDate = mDate;
        }

        public String getWeek() {
            return this.mWeek;
        }

        public void setWeek(String mWeek) {
            this.mWeek = mWeek;
        }

        public boolean isWeekA() {
            return this.getWeek().equals("A");
        }

        public boolean isWeekB() {
            return this.getWeek().equals("B");
        }

        public static List<Day.ListItem> flatten(ParentClass parentClass) {
            List<Day.ListItem> itemList = new ArrayList<Day.ListItem>();

            for (ParentClass.Day day : parentClass.getDayList()) {
                Day.Header header = new Day.Header(day);
                itemList.add(header);

                for (ParentClass.Day.Message message : day.getMessageList()) {
                    itemList.add(message);
                }

                for (ParentClass.Day.Entry entry : day.getEntryList()) {
                    itemList.add(entry);
                }
            }

            return itemList;
        }

        public static class Day {

            @Expose
            @SerializedName("name")
            private String mName;

            @Expose
            @SerializedName("entryList")
            private Entry[] mEntryList;

            @Expose
            @SerializedName("messageList")
            private Message[] mMessageList;

            private ParentClass mParentClass;

            public Day() {
            }

            public ParentClass getParentClass() {
                return this.mParentClass;
            }

            public void setParentClass(ParentClass parentClass) {
                this.mParentClass = parentClass;
            }

            public String getName() {
                return this.mName;
            }

            public void setName(String mName) {
                this.mName = mName;
            }

            public List<Entry> getEntryList() {
                return Arrays.asList(this.mEntryList);
            }

            public void setEntryList(Entry[] mEntryList) {
                this.mEntryList = mEntryList;
            }

            public List<Message> getMessageList() {
                return Arrays.asList(this.mMessageList);
            }

            public void setMessageList(Message[] mMessageList) {
                this.mMessageList = mMessageList;
            }

            public interface ListItem {

                Day getDay();

                void setDay(Day day);

                boolean hasDay();
            }

            public static class Entry implements ListItem {

                @Expose
                @SerializedName("for")
                private String mFor;

                @Expose
                @SerializedName("hour")
                private String mHour;

                @Expose
                @SerializedName("teacher")
                private String mTeacher;

                @Expose
                @SerializedName("subject")
                private String mSubject;

                @Expose
                @SerializedName("room")
                private String mRoom;

                @Expose
                @SerializedName("insteadOf")
                private String mInsteadOf;

                @Expose
                @SerializedName("info")
                private String mInfo;

                @Expose
                @SerializedName("type")
                private String mType;

                private Day mDay;

                public Entry() {
                }

                @Override
                public Day getDay() {
                    return this.mDay;
                }

                @Override
                public void setDay(Day day) {
                    this.mDay = day;
                }

                @Override
                public boolean hasDay() {
                    return this.mDay != null;
                }

                public String getFor() {
                    return this.mFor;
                }

                public void setFor(String mFor) {
                    this.mFor = mFor;
                }

                public String getHour() {
                    return this.mHour;
                }

                public void setHour(String mHour) {
                    this.mHour = mHour;
                }

                public String getTeacher() {
                    return this.mTeacher;
                }

                public void setTeacher(String mTeacher) {
                    this.mTeacher = mTeacher;
                }

                public String getSubject() {
                    return this.mSubject;
                }

                public void setSubject(String mSubject) {
                    this.mSubject = mSubject;
                }

                public String getRoom() {
                    return this.mRoom;
                }

                public void setRoom(String mRoom) {
                    this.mRoom = mRoom;
                }

                public String getInsteadOf() {
                    return this.mInsteadOf;
                }

                public void setInsteadOf(String mInsteadOf) {
                    this.mInsteadOf = mInsteadOf;
                }

                public String getInfo() {
                    return this.mInfo;
                }

                public void setInfo(String mInfo) {
                    this.mInfo = mInfo;
                }

                public String getType() {
                    return this.mType;
                }

                public void setType(String mType) {
                    this.mType = mType;
                }
            }

            public static class Message implements ListItem {

                @Expose
                @SerializedName("first")
                private String mFirst;

                @Expose
                @SerializedName("last")
                private String mLast;

                private Day mDay;

                public Message() {
                }

                @Override
                public Day getDay() {
                    return this.mDay;
                }

                @Override
                public void setDay(Day day) {
                    this.mDay = day;
                }

                @Override
                public boolean hasDay() {
                    return this.mDay != null;
                }

                public String getFirst() {
                    return this.mFirst;
                }

                public void setFirst(String mFirst) {
                    this.mFirst = mFirst;
                }

                public String getLast() {
                    return this.mLast;
                }

                public void setLast(String mLast) {
                    this.mLast = mLast;
                }
            }

            public static class Header implements ListItem {

                private String mTitle;

                private Day mDay;

                public Header() {
                }

                public Header(Day day) {
                    this.mDay = day;

                    this.mTitle = this.mDay.getName();
                }

                @Override
                public Day getDay() {
                    return this.mDay;
                }

                @Override
                public void setDay(Day day) {
                    this.mDay = day;
                }

                @Override
                public boolean hasDay() {
                    return this.mDay != null;
                }

                public String getTitle() {
                    if (this.mDay == null)
                    {
                        return this.mTitle;
                    }

                    return this.mDay.getName();
                }

                public void setTitle(String mTitle) {
                    this.mTitle = mTitle;
                }
            }
        }
    }
}
