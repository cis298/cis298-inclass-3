package edu.kvcc.cis298.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
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
        for (int i = 0; i < 100; i++)
        {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0); // Every other one
            mCrimes.add(crime);
        }
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
}
