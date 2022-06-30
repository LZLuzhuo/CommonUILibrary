/* Copyright 2020 Luzhuo. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.luzhuo.lib_common_ui.year_month_day;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import me.luzhuo.lib_common_ui.R;
import me.luzhuo.lib_core.date.DateCalendar;
import me.luzhuo.lib_core.date.enums.CalendarRule;
import me.luzhuo.lib_core.math.bit.BitFilter;
import me.luzhuo.lib_core.ui.dialog.DatePickerDialogManager;
import me.luzhuo.lib_core.ui.dialog.enums.DatePickerType;

/**
 * Description: 年月日选择
 * @Author: Luzhuo
 * @Creation Date: 2020/3/14 18:09
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class YearMonthDayView extends LinearLayoutCompat implements View.OnClickListener {
    public static final int Mode_Year   = 0x01 << 0;
    public static final int Mode_Month  = 0x01 << 1;
    public static final int Mode_Day    = 0x01 << 2;
    public static final int Mode_Hour   = 0x01 << 3;
    public static final int Mode_Minute = 0x01 << 4;
    public static final int Mode_Second = 0x01 << 5;

    private int currentMode;
    private BitFilter bitFilter = new BitFilter();

    private TextView core_ymd_year, core_ymd_month, core_ymd_day, core_ymd_hour, core_ymd_minute, core_ymd_second;
    private TextView core_ymd_year_t, core_ymd_month_t, core_ymd_day_t, core_ymd_hour_t, core_ymd_minute_t, core_ymd_second_t;

    private int currentYear, currentMonth, currentDay;
    private int currentHour, currentMinute, currentSecond;
    private final DatePickerDialogManager datePicker = new DatePickerDialogManager();
    private final DateCalendar calendar = new DateCalendar();

    private OnDateChangeCallback callback;

    public YearMonthDayView(@NonNull Context context) {
        this(context, null);
    }
    public YearMonthDayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public YearMonthDayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.YearMonthDayView, 0, 0);

        int style = 0;
        int mode = 0;
        try {
            style = typedArray.getInt(R.styleable.YearMonthDayView_year_style, 0);
            mode = typedArray.getInt(R.styleable.YearMonthDayView_year_mode, 0);
        }
        finally { typedArray.recycle(); }

        if (style == 0) LayoutInflater.from(getContext()).inflate(R.layout.ui_year_month_day_layout, this, true);
        else if (style == 1)LayoutInflater.from(getContext()).inflate(R.layout.ui_year_month_day_layout_t2, this, true);
        if (mode == 0) currentMode = Mode_Year | Mode_Month | Mode_Day;
        else currentMode = mode;

        findView();
        updateView();

        firstInitDate();
        updateDateView();
    }

    private void findView() {
        core_ymd_year = findViewById(R.id.ui_ymd_year);
        core_ymd_month = findViewById(R.id.ui_ymd_month);
        core_ymd_day = findViewById(R.id.ui_ymd_day);
        core_ymd_hour = findViewById(R.id.ui_ymd_hour);
        core_ymd_minute = findViewById(R.id.ui_ymd_minute);
        core_ymd_second = findViewById(R.id.ui_ymd_second);
        core_ymd_year_t = findViewById(R.id.ui_ymd_year_t);
        core_ymd_month_t = findViewById(R.id.ui_ymd_month_t);
        core_ymd_day_t = findViewById(R.id.ui_ymd_day_t);
        core_ymd_hour_t = findViewById(R.id.ui_ymd_hour_t);
        core_ymd_minute_t = findViewById(R.id.ui_ymd_minute_t);
        core_ymd_second_t = findViewById(R.id.ui_ymd_second_t);

        core_ymd_year.setOnClickListener(this);
        core_ymd_month.setOnClickListener(this);
        core_ymd_day.setOnClickListener(this);
        core_ymd_hour.setOnClickListener(this);
        core_ymd_minute.setOnClickListener(this);
        core_ymd_second.setOnClickListener(this);
    }

    /**
     * 设置显示模式:
     * 支持单个 YearMode, 支持多个 YearMode or MonthMode
     */
    public void setMode(int mode) {
        this.currentMode = mode;
        updateView();
    }

    private void updateView() {
        checkView(Mode_Year, core_ymd_year, core_ymd_year_t);
        checkView(Mode_Month, core_ymd_month, core_ymd_month_t);
        checkView(Mode_Day, core_ymd_day, core_ymd_day_t);
        checkView(Mode_Hour, core_ymd_hour, core_ymd_hour_t);
        checkView(Mode_Minute, core_ymd_minute, core_ymd_minute_t);
        checkView(Mode_Second, core_ymd_second, core_ymd_second_t);
    }


    private void firstInitDate() {
        currentYear = calendar.get(CalendarRule.Year);
        currentMonth = calendar.get(CalendarRule.Month);
        currentDay = calendar.get(CalendarRule.Day);
        currentHour = calendar.get(CalendarRule.Hour);
        currentMinute = calendar.get(CalendarRule.Minute);
    }

    private void updateDateView() {
        core_ymd_year.setText(String.valueOf(currentYear));
        core_ymd_month.setText(String.valueOf(currentMonth));
        core_ymd_day.setText(String.valueOf(currentDay));
        core_ymd_hour.setText(String.valueOf(currentHour));
        core_ymd_minute.setText(String.valueOf(currentMinute));
        core_ymd_second.setText(String.valueOf(currentSecond));
    }

    private void checkView(int mode, View view, View view_t) {
        if (bitFilter.isAccord(currentMode, mode)) {
            view.setVisibility(View.VISIBLE);
            view_t.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
            view_t.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == core_ymd_year || v == core_ymd_month || v == core_ymd_day) {
            datePicker.show(v.getContext(), DatePickerType.DEVICE_DEFAULT, currentYear, currentMonth, currentDay, dateCallback);
        } else if (v == core_ymd_hour || v == core_ymd_minute || v == core_ymd_second) {
            datePicker.show(v.getContext(), DatePickerType.DEVICE_DEFAULT, currentHour, currentMinute, timeCallback);
        }
    }

    private DatePickerDialogManager.OnDatePickerCallback dateCallback = new DatePickerDialogManager.OnDatePickerCallback() {
        @Override
        public void onDatePicker(int year, int month, int day) {
            currentYear = year;
            currentMonth = month;
            currentDay = day;
            updateDateView();

            if (callback != null) {
                callback.onDatePicker(currentYear, currentMonth, currentDay);
                callback.onTimePicker(currentHour, currentMinute);
            }
        }
    };
    private DatePickerDialogManager.OnTimePickerCallback timeCallback = new DatePickerDialogManager.OnTimePickerCallback(){
        @Override
        public void onTimePicker(int hour, int minute) {
            currentHour = hour;
            currentMinute = minute;
            updateDateView();

            if (callback != null) {
                callback.onDatePicker(currentYear, currentMonth, currentDay);
                callback.onTimePicker(currentHour, currentMinute);
            }
        }
    };

    // ====================== 获取数据 ===========
    public int year() {
        return this.currentYear;
    }
    public int month() {
        return this.currentMonth;
    }
    public int day() {
        return this.currentDay;
    }
    public int hour() {
        return this.currentHour;
    }
    public int minute() {
        return this.currentMinute;
    }
    public int second() {
        return this.currentSecond;
    }
    public long time() {
        return calendar.getTime(currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond);
    }

    public interface OnDateChangeCallback extends DatePickerDialogManager.OnDatePickerCallback, DatePickerDialogManager.OnTimePickerCallback { }
    public void setDateChangeCallback(@Nullable OnDateChangeCallback callback) {
        this.callback = callback;
    }
}
