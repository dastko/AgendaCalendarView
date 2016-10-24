package com.github.tibolte.sample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.tibolte.agendacalendarview.models.DayItem;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity implements View.OnClickListener, TimePicker.OnTimePickerSelectedListener {

    @Bind(R.id.time_event)
    TextView mTime;
    @Bind(R.id.minutes_1)
    TextView mOne;
    @Bind(R.id.minutes_2)
    TextView mTwo;
    @Bind(R.id.minutes_3)
    TextView mThree;
    @Bind(R.id.minutes_4)
    TextView mFour;


    @Bind(R.id.first_layout)
    RelativeLayout mFirst;
    @Bind(R.id.second_layout)
    RelativeLayout mSecond;
    @Bind(R.id.fourth_layout)
    RelativeLayout mThird;
    @Bind(R.id.fifth_layout)
    RelativeLayout mFourth;

    @Bind(R.id.start_date_layout)
    RelativeLayout mStartDateLayout;
    @Bind(R.id.end_date_layout)
    RelativeLayout mEndDateLayout;
    @Bind(R.id.action_picker)
    AppCompatSpinner mActionPicker;

    @Bind(R.id.type)
    AppCompatSpinner mType;


    @Bind(R.id.client_name)
    TextView mClientName;
    private int mAdjust;
    boolean isOk = true;
    DiscreteSeekBar seekBar;
    @Bind(R.id.button1)
    Button mDone;
    @Bind(R.id.button2)
    Button mEmergency;
    @Bind(R.id.start_time)
    TextView mStartTime;
    @Bind(R.id.end_time)
    TextView mEndTime;
    @Bind(R.id.button)
    TextView mCancel;
    private int isStart = 0;
    private CollapsingToolbarLayout collapsingToolbar;
    private String startTime;
    private String endTime;
    private String position;
    private int positionInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        startTime = intent.getStringExtra("START_TIME");
        endTime = intent.getStringExtra("END_TIME");
        int color = intent.getIntExtra("COLOR", 0);
        DayItem dayItem = intent.getParcelableExtra("DATE");
        //mTime.setText(DateFormatter.formattedDateFromString(dayItem.toString(), null, null));
        setText(color);
        mTime.setText(dayItem.toString());
        mFirst.setOnClickListener(this);
        mSecond.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mThird.setOnClickListener(this);
        mFourth.setOnClickListener(this);
        mStartTime.setText(startTime);
        mEndTime.setText(endTime.replace("AM", ""));
        mStartDateLayout.setOnClickListener(this);
        mEndDateLayout.setOnClickListener(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String[] reminders = getResources().getStringArray(R.array.reminder);
        String[] type = getResources().getStringArray(R.array.type);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reminders);
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, type);
        mActionPicker.setAdapter(adapter);
        mType.setAdapter(adapterType);
        mActionPicker.setSelection(0);
        mType.setSelection(0);

        mActionPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(startTime + ":" + endTime);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.text_white));

        loadBackdrop(color);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupDialog();
            }
        });

        mEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Extra uitgevoerde activiteiten");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.emergency_dialog, null);
        final EditText repeatAction = (EditText) view.findViewById(R.id.repeat_action_info);


        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        KeyboardUtil.hideKeyboard(EventActivity.this);
                        Intent intent1 = new Intent(EventActivity.this, MainActivity.class);
                        String time = startTime + ":" + endTime;
                        intent1.putExtra("CONFIRMED", positionInt);
                        startActivity(intent1);
                    }
                })
                .setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });
        builder.create().show();
    }

    private void showPopupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Bevestig deze actie");
        builder.setMessage("Weet je het zeker?");

        builder.setPositiveButton("JA", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Intent intent1 = new Intent(EventActivity.this, MainActivity.class);
                String time = startTime + ":" + endTime;
                intent1.putExtra("CONFIRMED", positionInt);
                startActivity(intent1);
            }
        });

        builder.setNegativeButton("NEE", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void setText(int color) {
        if (color == -1684967) {
            mClientName.setText("CAECILIA LOUWERS");
            mClientName.setTextColor(color);
            position = "ONE";
            positionInt = 0;
            return;
        }
        if (color == -16755841) {
            mClientName.setText("TOM JENSEN");
            mClientName.setTextColor(color);
            position = "TWO";
            positionInt = 1;
            return;
        }

        if (color == -678365) {
            mClientName.setText("SOPHIE DE VRIES");
            mClientName.setTextColor(color);
            position = "THREE";
            positionInt = 2;
            return;
        }

        if (color == -11049104) {
            mClientName.setText("ERIC VAN DE BERG");
            mClientName.setTextColor(color);
            position = "FOUR";
            positionInt = 3;
            return;
        }

    }

    private void loadBackdrop(int color) {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        //imageView.setBackgroundColor(color);
        Glide.with(this).load(EventCategories.getRandomCheeseDrawable()).centerCrop().into(imageView);
    }

    public void showSeekDialog(Integer i) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Pas uw tijd in minuten");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        seekBar = (DiscreteSeekBar) dialogView.findViewById(R.id.seek_bar_custom);
        dialog.setView(dialogView);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isOk = true;
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isOk = false;
                dialog.dismiss();
            }
        });
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                mAdjust = seekBar.getProgress();
                if (isOk) {
                    if (i == 1) {
                        mOne.setText(mAdjust + " minuten");
                        return;
                    }
                    if (i == 2) {
                        mTwo.setText(mAdjust + " minuten");
                        return;
                    }
                    if (i == 3) {
                        mThree.setText(mAdjust + " minuten");
                        return;
                    }
                    if (i == 4) {
                        mFour.setText(mAdjust + " minuten");
                        return;
                    }
                }
            }
        });
        dialog.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_layout:
                showSeekDialog(1);
                String one = mOne.getText().toString();
                seekBar.setProgress(Integer.valueOf(one.replace(" minuten", "")));
                break;
            case R.id.second_layout:
                showSeekDialog(2);
                String two = mTwo.getText().toString();
                seekBar.setProgress(Integer.valueOf(two.replace(" minuten", "")));
                break;
            case R.id.fourth_layout:
                showSeekDialog(3);
                String three = mThree.getText().toString();
                seekBar.setProgress(Integer.valueOf(three.replace(" minuten", "")));
                break;
            case R.id.fifth_layout:
                showSeekDialog(4);
                String four = mFour.getText().toString();
                seekBar.setProgress(Integer.valueOf(four.replace(" minuten", "")));
                break;
            case R.id.start_date_layout:
                isStart = 1;
                showTimePicker();
                break;
            case R.id.end_date_layout:
                isStart = 2;
                showTimePicker();
                break;
            case R.id.button:
                onBackPressed();
        }

    }

    private void showTimePicker() {
        TimePicker picker = new TimePicker();
        picker.show(getFragmentManager(), "TimePicker");
    }

    @Override
    public void onTimeSelected(int hour, int minute, int id) {
        if (isStart == 1) {
            startTime = null;
            startTime = String.valueOf(hour) + ":" + String.valueOf(minute);
            collapsingToolbar.setTitle(startTime + "-" + endTime);
            mStartTime.setText(startTime);
        }
        if (isStart == 2) {
            endTime = null;
            endTime = String.valueOf(hour) + ":" + String.valueOf(minute);
            collapsingToolbar.setTitle(startTime + "-" + endTime);
            mEndTime.setText(endTime);
        }
        Log.d("Hour + Minute", String.valueOf(hour));
        Log.d("Integer id", String.valueOf(id));
        isStart = 0;
    }
}
