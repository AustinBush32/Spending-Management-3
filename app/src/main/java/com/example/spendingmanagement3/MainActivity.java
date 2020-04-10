package com.example.spendingmanagement3;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.content.SharedPreferences;
import java.text.DecimalFormat;
import android.database.Cursor;
import android.app.DatePickerDialog;
import java.util.Calendar;
import android.widget.DatePicker;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    DBHelper db;
    public static final String pref = "SpendingManagement";
    public static final String bal = "BalanceKey";
    public static final String lastNote = "LastNote";
    private static DecimalFormat df = new DecimalFormat("0.00");
    Button add, sub, dateButton, firstDate, secondDate, search;
    ToggleButton adding, spending;
    EditText amount, description, amount1, amount2;
    TextView balance, logLabel;
    LinearLayout log;
    DatePickerDialog datePickerDialog;
    int year, month, dayOfMonth;
    Calendar calendar;
    float currBal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = Calendar.getInstance();
        logLabel = findViewById(R.id.LogLabel);
        search = findViewById(R.id.search);
        search.setOnClickListener(onClickForSearch());
        adding = findViewById(R.id.adding);
        spending = findViewById(R.id.spending);
        amount1 = findViewById(R.id.amount1);
        amount2 = findViewById(R.id.amount2);
        firstDate = findViewById(R.id.FirstDate);
        firstDate.setOnClickListener(onClickForFirstDate());
        secondDate = findViewById(R.id.SecondDate);
        secondDate.setOnClickListener(onClickForSecondDate());
        dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(onClickForDate());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dateButton.setText(Integer.toString((month+1)) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year));
        secondDate.setText(Integer.toString((month+1)) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year));
        firstDate.setText("1/1/1970");
        add = findViewById(R.id.add);
        add.setOnClickListener(onClickForAdd());
        sub = findViewById(R.id.sub);
        sub.setOnClickListener(onClickForSub());
        amount = findViewById(R.id.amount);
        description = findViewById(R.id.description);
        balance = findViewById(R.id.CurrentBalance);
        log = findViewById(R.id.log);

        currBal = 0.0f;

        db = new DBHelper(this);

        Cursor savedData = db.getData();
        while(savedData.moveToNext()) {
            log.addView(createNewTextView(savedData.getString(1)));
            currBal = savedData.getFloat(2);
        }

        balance.setText("Current Balance: $" + df.format(currBal));

    }

    private  OnClickListener onClickForSearch() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = firstDate.getText().toString();
                String endDate = secondDate.getText().toString();
                float lowAmount = 0.0f;
                float highAmount = Float.MAX_VALUE;
                try {
                    lowAmount = Float.parseFloat(amount1.getText().toString());
                } catch (Exception e) { }
                try {
                    highAmount = Float.parseFloat(amount2.getText().toString());
                } catch (Exception e) { }

                logLabel.setText("Search Results");
                log.removeAllViews();

                Cursor savedData = db.getData();
                while(savedData.moveToNext()) {
                    String spendingType = savedData.getString(3);
                    String date = savedData.getString(4);
                    float currAmount = savedData.getFloat(5);
                    boolean isAdd = spendingType.equals("add") && adding.isChecked();
                    boolean isSub = spendingType.equals("sub") && spending.isChecked();
                    boolean isValidAmount = currAmount >= lowAmount && currAmount <= highAmount;
                    boolean isValidDate = compare(date, startDate) >= 0 && compare(date, endDate) <= 0;
                    if ((isAdd || isSub) && isValidAmount && isValidDate) {
                        log.addView(createNewTextView(savedData.getString(1)));
                    }
                }
            }
        };
    }

    public int compare(String s1, String s2) {
        String[] s1Components = s1.split("/");
        String[] s2Components = s2.split("/");
        if (Integer.parseInt(s1Components[2]) > Integer.parseInt(s2Components[2])) {
            return 1;
        } else if (Integer.parseInt(s1Components[2]) < Integer.parseInt(s2Components[2])){
            return -1;
        } else {
            if (Integer.parseInt(s1Components[0]) > Integer.parseInt(s2Components[0])) {
                return 1;
            } else if (Integer.parseInt(s1Components[0]) < Integer.parseInt(s2Components[0])) {
                return -1;
            } else {
                if (Integer.parseInt(s1Components[1]) > Integer.parseInt(s2Components[1])) {
                    return 1;
                } else if (Integer.parseInt(s1Components[1]) < Integer.parseInt(s2Components[1])) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    private  OnClickListener onClickForDate() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dateButton.setText((month+1) + "/" + day + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(0);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        };
    }

    private  OnClickListener onClickForFirstDate() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                firstDate.setText((month+1) + "/" + day + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(0);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        };
    }

    private  OnClickListener onClickForSecondDate() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                secondDate.setText((month+1) + "/" + day + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(0);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        };
    }

    private OnClickListener onClickForAdd() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                logLabel.setText("History");
                log.removeAllViews();

                Cursor savedData = db.getData();
                while(savedData.moveToNext()) {
                    log.addView(createNewTextView(savedData.getString(1)));
                }

                String s = "Adding $" + amount.getText().toString() + " on " + dateButton.getText().toString() +
                        " from " + description.getText().toString();
                log.addView(createNewTextView(s));

                currBal += Float.parseFloat(amount.getText().toString());

                db.addData(s, currBal, "add", dateButton.getText().toString(), Float.parseFloat(amount.getText().toString()));
                balance.setText("Current Balance: $" + df.format(currBal));
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                dateButton.setText(Integer.toString((month+1)) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year));
                amount.setText("");
                description.setText("");
                secondDate.setText(Integer.toString((month+1)) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year));
                firstDate.setText("1/1/1970");
                amount1.setText("");
                amount2.setText("");
                adding.setChecked(false);
                spending.setChecked(false);
            }
        };
    }

    private OnClickListener onClickForSub() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                logLabel.setText("History");
                log.removeAllViews();

                Cursor savedData = db.getData();
                while(savedData.moveToNext()) {
                    log.addView(createNewTextView(savedData.getString(1)));
                }

                String s = "Spent $" + amount.getText().toString() + " on " + dateButton.getText().toString() +
                        " for " + description.getText().toString();
                log.addView(createNewTextView(s));

                currBal -= Float.parseFloat(amount.getText().toString());

                db.addData(s, currBal, "sub", dateButton.getText().toString(), Float.parseFloat(amount.getText().toString()));
                balance.setText("Current Balance: $" + df.format(currBal));
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                dateButton.setText(Integer.toString((month+1)) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year));
                amount.setText("");
                description.setText("");
                secondDate.setText(Integer.toString((month+1)) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year));
                firstDate.setText("1/1/1970");
                amount1.setText("");
                amount2.setText("");
                adding.setChecked(false);
                spending.setChecked(false);
            }
        };
    }

    private TextView createNewTextView(String text) {
        final LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final TextView newNote = new TextView(this);
        newNote.setLayoutParams(lparams);
        newNote.setText(text);
        newNote.setTextSize(15);
        newNote.setPadding(20, 50, 20, 50);
        return newNote;
    }

}
