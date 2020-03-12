package edu.kvcc.cis298.criminalintent;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

public class CrimeLab {
    // Make a static (There can be only one) variable to hold
    // an instance of itself. It has the s prefix because it is
    // static.
    // Also, the variable is private so that it can not be
    // directly accessed by outside classes.
    private static CrimeLab sCrimeLab;

    // The underlying List of Crimes
    private List<Crime> mCrimes;

    // Boolean for whether or not we have already loaded the data
    private boolean mDataLoadedOnce;

    // If someone wants an instance of CrimeLab, they
    // MUST use this get method that will either
    // return the existing instance, or instantiate a
    // new one and store it in the private static variable.
    // NOTE: Right now we do not need this context that
    // is being sent in. However, later on we will, so it is here now.
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    // This is the constructor for the class
    // Notice that it is Private!
    // That way other classes can not make instances
    // of this class. They must instead use the get method.
    // NOTE: Right now we do not need this context that
    // is being sent in. However, later on we will, so it is here now.
    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
    }

    // Moved the default crimes into a separate method that can be
    // used to add some default crimes if desired.
    public void addDefaultCrimes() {
        for (int i = 0; i < 100; i++)
        {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0); // Every other one
            mCrimes.add(crime);
        }
    }

    // Takes in a new crime and then adds it to the list
    public void addCrime(Crime c) {
        mCrimes.add(c);
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        // This is a foreach loop in java.
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    // Getter to see if the list is empty
    public boolean isEmpty() {
        return mCrimes.isEmpty();
    }

    // Getter to see if the data has already been loaded once.
    public boolean isDataLoadedOnce() {
        return mDataLoadedOnce;
    }

    // Method to load the beverage list from a CSV file
    public void loadCrimeList(InputStream inputStream) {
        // Define a scanner
        try (Scanner scanner = new Scanner(inputStream)) {
            // While the scanner has another line to read
            while (scanner.hasNextLine()) {
                // Get the next line and split it into parts
                String line = scanner.nextLine();
                String parts[] = line.split(",");

                // Assign each part to a local var
                String id = parts[0];
                String title = parts[1];
                String dateString = parts[2];
                String solvedString = parts[3];

                // Convert some strings to different types
                UUID uuid = UUID.fromString(id);
                Date date = new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                ).parse(dateString);
                boolean isSolved = (solvedString == "1");

                // Now that we have all of the Crime properties we can
                // make a new one and add it to the List
                mCrimes.add(
                        new Crime(
                                uuid,
                                title,
                                date,
                                isSolved
                        )
                );

            }

            // Data read in, so set the dataLoadedOnce flag to tru
            mDataLoadedOnce = true;
        } catch (Exception e) {
            Log.e("Read CSV", e.toString());
        }
    }













}
