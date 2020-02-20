package edu.kvcc.cis298.criminalintent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fetch out the UUID from the Intent that was used to
        // start this fragment's hosting activity and then this
        // fragment.

        // In order to get the UUID out of the Intent, we need to
        // use the getSerializableExtra method. That method is used
        // when we want to fetch out complex objects rather than
        // simple values such as an int. The downside is that the
        // object that gets stored in the intent must implement
        // the serializable interface in order to be able to be
        // placed inside the Intent with a putExtra.
        UUID crimeId = (UUID) getActivity().getIntent()
                .getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
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
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

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
}
