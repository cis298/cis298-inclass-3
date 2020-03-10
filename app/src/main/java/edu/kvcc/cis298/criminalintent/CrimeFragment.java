package edu.kvcc.cis298.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    // Key that will be used to store the crime id into a Bundle
    // object that will be used as fragment arguments
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    // Request code that we will use to start the DateDialog.
    // We can then check for this in Fragment.onActivityResult
    // and handle returning from that dialog.
    private static final int REQUEST_DATE = 0; // Zero

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;

    // Method that can be called by an activity to get a new
    // instance of this fragment with the proper arguments
    // added to the Bundle that this fragment will get as arguments.
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fetch out the UUID from the Bundle object that is
        // used as fragment arguments. The Bundle can be accessed
        // by using the getArguments() method.

        // In order to get the UUID out of the Intent, we need to
        // use the getSerializableExtra method. That method is used
        // when we want to fetch out complex objects rather than
        // simple values such as an int. The downside is that the
        // object that gets stored in the intent must implement
        // the serializable interface in order to be able to be
        // placed inside the Intent with a putExtra.
        UUID crimeId = (UUID) getArguments()
                .getSerializable(ARG_CRIME_ID);
        // Find the correct crime from the Singleton by using
        // the getCrime getter and sending it the crime id to find.
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // First param is the layout to inflate.
        // Second is the parent, which is usually the activity
        // Third is whether to attach the view to the activity and we say no because we
        // are going to do that manually in the activity.

        // This inflating takes the XML for the layout and and turns it into
        // Java code. This MUST be done if we plan to try and get access to the
        // widgets in the layout.
        // This is us doing the manual work of inflating. In Activities, this is done
        // via the setContent method that Activities have.
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        // Now it is time to setup any events for the widgets that exist on that layout file.
        // start with the EditText for the title.
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do not need to do anything here. But must override.
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do not need to do anything here. But must override.
            }
        });

        // Date button does not do much right now, but in a later chapter it will.
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(
                        CrimeFragment.this,
                        REQUEST_DATE
                );
                dialog.show(manager, DIALOG_DATE);
            }
        });

        // Setup the checkbox for the solved state.
        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        // Now that we are done wiring up the widgets with code, we can return the view.
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Don't do anything if the result was not okay.
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        // If we are returning from the datepicker.
        // We check this by comparing the requestcode that was sent to this
        // method against the one we used to start the datepicker fragment.
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }
}
