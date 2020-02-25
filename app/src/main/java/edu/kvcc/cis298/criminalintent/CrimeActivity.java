package edu.kvcc.cis298.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    // String to act as a key for the Intent we are going to create
    private static final String EXTRA_CRIME_ID =
            "edu.kvcc.cis298.inclass-3.crime_id";

    // Method that will return an Intent that knows how to get this
    // activity started.
    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        // Fetch out the crimeId from the Intent that was used
        // to get this activity started.
        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);
        // Call the newInstance method on CrimeFragment
        // to get a CrimeFragment that contains the proper
        // fragment arguments.
        // We will send over the crimeId that we just fetched out.
        return CrimeFragment.newInstance(crimeId);
    }
}
