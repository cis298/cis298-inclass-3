package edu.kvcc.cis298.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    // String to act as a key for the Intent we are going to create
    private static final String EXTRA_CRIME_ID =
            "edu.kvcc.cis298.inclass-3.crime_id";

    // Method that will return an Intent that knows how to get this
    // activity started.
    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    // Variable for the layout widget of ViewPager
    private ViewPager mViewPager;
    // Var for list of crimes
    private List<Crime> mCrimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        // Get a handle to the view pager
        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        // Get the crimes from the crimeLab singleton sending 'this'
        // as the context that it requires.
        mCrimes = CrimeLab.get(this).getCrimes();

        // Need to get the Fragment Manager. It will be used slightly
        // different than how it was used in SingleFragmentActivity
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Set the adapter on the ViewPager to a new FragmentStatePagerAdapter
        // which is a built in Pager Adapter that we can use.
        // We are forced to provide a fragmentManager to it which is why
        // we got it in the code above.
        // We also have to override these two methods for it to work.
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                // Get the crime with the index of position from the list.
                Crime crime = mCrimes.get(position);
                // Create a new CrimeFragment that the ViewPager should show.
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        // Fetch out the crimeId from the Intent that was used
        // to get this activity started.
        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);

        // Loop through all of the crimes looking for the one that
        // has the same UUID as the one we got out of the Intent.
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                // Set the ViewPager's current item to the correct crime.
                mViewPager.setCurrentItem(i);
                // Don't loop any longer than we need to.
                break;
            }
        }

    }
}
