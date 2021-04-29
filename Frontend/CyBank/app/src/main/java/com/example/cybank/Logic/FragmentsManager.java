package com.example.cybank.Logic;
import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.cybank.Fragments.AccountsFragment;
import com.example.cybank.Fragments.AddAccountFragment;
import com.example.cybank.Fragments.PlannerFragment;
import com.example.cybank.Fragments.TransactionFragment;
import com.example.cybank.Fragments.TransferFragment;
import com.example.cybank.R;
import com.example.cybank.UIActivity;

import java.util.List;

/**
 * This class is a helper class that helps navigate through fragments
 */
public class FragmentsManager extends AppCompatActivity{
    private static final String TAG = "FragmentsManager";
    private static FragmentTransaction transaction;
    public static Bundle bundle = new Bundle();
    public static String data;
    //    Return Fragment Transaction

    /**
     * This returns the fragment traansaction
     * @param activity
     * @return Fragment transaction
     */
    private static FragmentTransaction getTransaction(Activity activity){
//        if (transaction == null) {
//            return transaction;
//        }
        return getFragmentManager(activity).beginTransaction();
    }
    //    Return Fragment Manager

    /**
     * returns the fragment manager
     * @param activity
     * @return Fragment transaction
     */
    private static FragmentManager getFragmentManager(Activity activity){
        return ((AppCompatActivity)activity).getSupportFragmentManager();
    }

    /**
     * Add Fragment to the given ID
     * @param activity
     * @param fragment
     * @param addBackstack
     */
    public static void addFragment(Activity activity, Fragment fragment, boolean addBackstack){
        int id = R.id.flContent;
        transaction = getTransaction(activity);
        transaction.add(id,fragment,fragment.getClass().getName());
        if (addBackstack)
            transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }


    /**
     * replaces the fragment
     * @param activity
     * @param fragment
     * @param addBackstack
     */
    public static void replaceFragment(Activity activity, Fragment fragment, boolean addBackstack) {
        int id = R.id.flContent;
        Fragment check_Fragment = getFragmentManager(activity).findFragmentByTag(fragment.getClass().getName());
        if (check_Fragment == null) {
            transaction = getTransaction(activity)
                    .replace(id,fragment,fragment.getClass().getName());
            if (addBackstack)
                transaction.addToBackStack(fragment.getClass().getName());
            transaction.commit();
        }
        else{
            transaction = getTransaction(activity);
            transaction.replace(id,check_Fragment,check_Fragment.getClass().getName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Goes to the next fragment after replacing the current one.
     * @param toFragment
     * @param replace
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void nextFragment(Class toFragment, Boolean replace) throws InstantiationException, IllegalAccessException {
        Fragment fragment = null;
        fragment = (Fragment) toFragment.newInstance();
        if (replace == true)
            FragmentsManager.replaceFragment((Activity) UIActivity.getUIContext(), fragment, true);
        else
            FragmentsManager.addFragment((Activity) UIActivity.getUIContext(), fragment, true);
        if (toFragment == AccountsFragment.class)
            clearBackStack();
    }

    /**
     * Goes to the next fragment bundle
     * @param toFragment
     * @param replace
     * @param bundledata
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void nextFragmentBundle(Class toFragment, Boolean replace, String bundledata) throws InstantiationException, IllegalAccessException {
        Bundle bundle = new Bundle();
        Fragment fragment = null;
        fragment = (Fragment) toFragment.newInstance();
        fragment.setArguments(bundle);
        if (bundledata != null)
            data = bundledata;
        bundle.putString("data", data);
        if (replace == true)
            FragmentsManager.replaceFragment((Activity) UIActivity.getUIContext(), fragment, true);
        else
            FragmentsManager.addFragment((Activity) UIActivity.getUIContext(), fragment, true);
        if (toFragment == AccountsFragment.class)
            clearBackStack();
    }

    /**
     * gets the current fragment class.
     * @return Current fragment class
     */
    public static Class getVisibleFragmentClass(){
        FragmentManager fragmentManager = getFragmentManager((Activity) UIActivity.getUIContext());
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment.getClass();
            }
        }
        return null;
    }

    /**
     * clears back stack
     */
    public static void clearBackStack(){
        FragmentManager fragmentManager = getFragmentManager((Activity) UIActivity.getUIContext());
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
    }
    public static void loadNextFragmentMove(Class visible) throws IllegalAccessException, InstantiationException {
        if (AddAccountFragment.class.equals(visible) || TransferFragment.class.equals(visible) || TransactionFragment.class.equals(visible)) {
            UIActivity.goBack();
        } else if (PlannerFragment.class.equals(visible)) {
            nextFragment(PlannerFragment.class, true);
        } else {
            clearBackStack();
            nextFragment(AccountsFragment.class, true);
        }
    }
}
