package edu.kvcc.cis298.criminalintent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    // Method to update the recycler view with data
    private void updateUI() {
        // Get the singleton of the Crimelab. Send in the hosting activity
        // as the context.
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        // Get the list of crimes
        List<Crime> crimes = crimeLab.getCrimes();
        // Create a new adapter
        mAdapter = new CrimeAdapter(crimes);
        // Set the adapter on the RecyclerView
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    // This will be an inner class that our RecyclerView needs to operate.
    // We make this inner class inherit from RecyclerView's similar inner
    // class called ViewHolder which will give us some base functionality.
    // The class can also be declared private because it will only be used
    // inside this RecyclerView.
    private class CrimeHolder extends RecyclerView.ViewHolder {
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(
                    R.layout.list_item_crime,
                    parent,
                    false
            ));
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
            // Not doing anything on purpose.
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
