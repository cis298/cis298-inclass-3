package edu.kvcc.cis298.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    // String to use as a constant for the key in the intent
    // that will contain the data that is sent back to the
    // calling fragment. Since we will be calling onActivityResult
    // to do this, there is a chance for string collision, so we
    // need to make it long with the package name.
    public static final String EXTRA_DATE =
            "edu.kvcc.cis298.inclass-3.date";

    // String constant for storing this fragments args
    private static final String ARG_DATE = "date";

    // Var for datepicker widget
    private DatePicker mDatePicker;

    // Used to make a new instance of the DatePickerFragment
    // with the correct arguments.
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        // There is no putDate, so we must use serializable
        // Luckily Date is serializable already.
        args.putSerializable(ARG_DATE, date);
        // Make new instance
        DatePickerFragment fragment = new DatePickerFragment();
        // Set args setup above
        fragment.setArguments(args);
        // return the fragment
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Fetch the Crime's date from the fragment arguments that
        // were set on this fragment from the newInstance method.
        Date date = (Date)getArguments().getSerializable(ARG_DATE);

        // Convert the Date to values that can be displayed on the
        // datepicker widget.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Inflate the view to use
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        // Get a handle to the DatePicker widget and then set the date
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        // Make a new AlertDialog by calling the constructor of the
        // inner class Builder to get an instance of AlertDialog.
        // Then we can method chain onto that AlertDialog with
        // methods to set the title, and positive button
        // Lastly, we have to call create to actually create it.

        // Listener is set to a new onClickListener that will get
        // the parts of the date from the widget, and create a new date.
        // Then send the date to the targetFragment via the
        // sendResult method down below.

        // There are two other buttons that could be added if desired.
        // A negative one, and a neutral commonly used for cancel.
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Fetch out parts from widget
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                // Make a new date object using GregorianCalendar
                                Date date = new GregorianCalendar(year, month, day).getTime();
                                // Send the result to the targetFragment
                                sendResult(Activity.RESULT_OK, date);
                            }
                        })
//                .setNegativeButton(android.R.string.no, null)
//                .setNeutralButton(android.R.string.cancel, null)
                .create();
    }

    // This method will send the result back to the calling fragment
    private void sendResult(int resultCode, Date date) {
        // Safety check to make sure a target fragment is set.
        if (getTargetFragment() == null) {
            return;
        }
        // Make a new intent to store the data that we are returning
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        // Get the target fragment and make an explicit call to
        // it's onActivityResult with the request code,
        // intent data, and the result code
        getTargetFragment()
                .onActivityResult(
                        getTargetRequestCode(),
                        resultCode,
                        intent
                );
    }
}
