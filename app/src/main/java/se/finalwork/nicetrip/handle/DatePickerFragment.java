package se.finalwork.nicetrip.handle;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private int arriveYear;
    private int arriveMonth;
    private int arriveDay;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        arriveYear = c.get(Calendar.YEAR);
        arriveMonth = c.get(Calendar.MONTH);
        arriveDay = c.get(Calendar.DAY_OF_MONTH);
        Log.d("Calendar", "Date: " + arriveYear + "/" + (arriveMonth + 1) + "/" + arriveDay);

        return new DatePickerDialog(getActivity(), this, arriveYear, arriveMonth, arriveDay);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }

    public int getArriveYear() {
        return arriveYear;
    }

    public void setArriveYear(int arriveYear) {
        this.arriveYear = arriveYear;
    }

    public int getArriveMonth() {
        return arriveMonth;
    }

    public void setArriveMonth(int arriveMonth) {
        this.arriveMonth = arriveMonth;
    }

    public int getArriveDay() {
        return arriveDay;
    }

    public void setArriveDay(int arriveDay) {
        this.arriveDay = arriveDay;
    }
}
