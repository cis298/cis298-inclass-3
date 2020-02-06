package edu.kvcc.cis298.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CrimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This method belongs to the AppCompatActivity class that our
        // activity here inherits from. Among other things, it does the work
        // of inflating the layout (view) and turning the XML in to instances
        // of Java objects in memory.
        // If you go look at Fragments, they are done differently.
        // We have to inflate the layout manually.
        setContentView(R.layout.activity_crime);

        // This is the class that will handle all of the work of attaching
        // and removing fragments from a FrameLayout.
        FragmentManager fm = getSupportFragmentManager();

        // This will use the fragment manager to go get the current fragment
        // that exists in the FrameLayout of the layout (view).
        // When the app first launches, there will be no fragment in that
        // container, so the fragment manager will return Null.

        // Later in the app lifecycle if we need the fragment that is currently
        // being hosted in the FrameLayout we can use this to get it.
        // We will not do this, but we could.

        // The R.id.fragment_container matches the ID of the FrameLayout in the layout file.
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // Check and see if the fragment we got back is null.
        // On app start up, it most certainly will.
        if (fragment == null) {
            // Make a new instance of our CrimeFragment
            fragment = new CrimeFragment();

            // User the fragment manager to start a transaction for adding
            // the fragment to the FrameLayout. All fragment work must be
            // done in a transaction that is committed at the end.

            // This style of code uses a combination of the builder pattern
            // and method chaining.

            // Must start by beginning a transaction.
            fm.beginTransaction()
                    // This bit adds our fragment to the FrameLayout
                    .add(R.id.fragment_container, fragment)
                    // This bit commits our work.
                    // If we wanted, we could add multiple fragments to many
                    // different FrameLayouts before we commit the work.
                    .commit();
        }
    }
}
