package com.github.tibolte.agendacalendarview.models;

import android.widget.ImageView;

import java.util.Calendar;

public interface CalendarEvent {

    long getId();

    void setId(long mId);

    int getColor();

    Calendar getStartTime();

    void setStartTime(Calendar mStartTime);

    Calendar getEndTime();

    void setEndTime(Calendar mEndTime);

    String getTitle();

    ImageView getSharedImage();

    void setTitle(String mTitle);

    Calendar getInstanceDay();

    void setInstanceDay(Calendar mInstanceDay);

    DayItem getDayReference();

    void setDayReference(DayItem mDayReference);

    WeekItem getWeekReference();

    void setWeekReference(WeekItem mWeekReference);

    Integer getImageView();

    void setImageView(Integer id);

    CalendarEvent copy();
}
