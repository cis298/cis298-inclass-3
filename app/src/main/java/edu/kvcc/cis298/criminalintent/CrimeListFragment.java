package edu.kvcc.cis298.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

public class CrimeListFragment extends Fragment {

    // Var to hold a reference to the RecyclerViewe
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_crime_list,
                container,
                false
        );
        mCrimeRecyclerView = (RecyclerView) view.findViewById(
                R.id.crime_recycler_view
        );

        // Must set the LayoutManager for the RecyclerView.
        // This will tell the RecyclerView how the elements inside it
        // should be rendered out.
        // For this one, and all the ones we will do for the class,
        // we will use LinearLayoutManager.
        // Also have to pass a context to LinearLayoutManager constructor.
        // we do that by calling this fragments getActivity method which
        // returns the hosting activity.
        mCrimeRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity())
        );

        // Call the updateUI method to get the data into the RecyclerView
        updateUI();

        // Now that everything is setup for the view, we return it.
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    // Method to update the recycler view with data
    private void updateUI() {
        // Get the singleton of the Crimelab. Send in the hosting activity
        // as the context.
        CrimeLab crimeLab = CrimeLab.get(getActivity());

        // If the crimeLab is empty, load the crimes to populate the list
        if (crimeLab.isEmpty() && !crimeLab.isDataLoadedOnce()) {
            InputStream csvStream = getResources()
                    .openRawResource(R.raw.crimes);
            crimeLab.loadCrimeList(csvStream);
            // Can optionally add the default crimes if desired.
             crimeLab.addDefaultCrimes();
        }


        // Get the list of crimes
        List<Crime> crimes = crimeLab.getCrimes();

        // Check to see if we already have an adapter.
        // If so, we only need to notify the RecyclerView
        // that its dataset has changed rather than make a new adapter.
        if (mAdapter == null) {
            // Create a new adapter
            mAdapter = new CrimeAdapter(crimes);
            // Set the adapter on the RecyclerView
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            // Already have an adapter.
            // Just need to tell it to update its data.
            mAdapter.notifyDataSetChanged();
        }
    }

    // This will be an inner class that our RecyclerView needs to operate.
    // We make this inner class inherit from RecyclerView's similar inner
    // class called ViewHolder which will give us some base functionality.
    // The class can also be declared private because it will only be used
    // inside this RecyclerView.
    private class CrimeHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        // Var to hold the specific crime we want to disply
        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(
                    R.layout.list_item_crime,
                    parent,
                    false
            ));
            // This will tell the class to look at itself for
            // the onclick handler
            itemView.setOnClickListener(this);

            // itemView is a variable that hold the inflated layout.
            // it is provided to us via the inherited functionality of
            // RecyclerView.ViewHolder
            // NOTE: Intellisense seemed bad at suggesting itemView
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
        }

        // Method to take in a crime object and bind it's properties
        // to the widgets in the layout.
        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }

        @Override
        public void onClick(View view) {
            // Call the static method on CrimeActivity that
            // knows how to create an Intent to get CrimeActivity started.
            // The method will return the intent we need to send to
            // startActivity.
            Intent intent = CrimePagerActivity.newIntent(
                    getActivity(), // This fragments hosting activity (context)
                    mCrime.getId() // Crime id.
            );
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        // Setup a var to hold our list of crimes
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the layout inflater from the activity.
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            // Send it into the constructor for a CrimeHolder
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int position) {
            // Get the crime that we need to display from
            // the crime list sent to this constructor.
            // Then use the Holder's bind method
            // we wrote to wire it up with the layout.
            Crime crime = mCrimes.get(position);
            crimeHolder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
