package com.example.cybank.Logic;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cybank.Fragments.AccountsFragment;
import com.example.cybank.Network.ServerRequest;
import com.example.cybank.Fragments.PlannerFragment;
import com.example.cybank.UIActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Helper class for global events occurring throughout application
 */
public class Helper {
    public static String UID;
    public static String name;
    public static boolean cang;
    public static int accountPosition;
    public static int loanPosition;
    public static boolean isAdmin;
    public static ArrayList<JSONObject> accountArr;
    public static ArrayList<JSONObject> loanArr;
    public static ArrayList<JSONObject> eventArr;
    public static ArrayList<String> accountNameArr = new ArrayList<String>();
    public static ArrayList<String> accountNumberArr = new ArrayList<String>();
    public static ArrayList<String> accountIdArr = new ArrayList<String>();
    public static ArrayList<String> loanamountArr = new ArrayList<String>();
    public static ArrayList<String> loanIdArr = new ArrayList<String>();
    public static ArrayList<String> minPaymentArr = new ArrayList<String>();
    public static ArrayList<String> loanBalArr = new ArrayList<String>();

    public  static void setCang(boolean toset) {
        cang = toset;
    }

    public  static boolean getCang() {
        return cang;
    }
    /**
     * Uses String pull to set JSONArray to pass to method JSONArraytoArrayListObj
     * @param pull
     * @param obj
     * @return
     * @throws JSONException
     */
    public static ArrayList<JSONObject> JSONObjecttoArraylistObj(String pull, JSONObject obj) throws JSONException {
        JSONArray arr = obj.getJSONArray(pull);
        return JSONArraytoArraylistObj(arr);
    }

    /**
     * Converts JSONArray to Arraylist of type Object
     * @param arr
     * @return
     * @throws JSONException
     */
    public static ArrayList<JSONObject> JSONArraytoArraylistObj(JSONArray arr) throws JSONException {
        ArrayList<JSONObject> listdata = new ArrayList<JSONObject>();
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                listdata.add(arr.getJSONObject(i));
            }
        }
        return listdata;
    }

    /**
     * returns UID of user
     * @return
     */
    public static String getUID() {
        return UID;
    }

    /**
     * sets UID of user
     * @param localName
     */
    public static void setName(String localName) {
        name = localName;
    }
    public static String getName() {
        return name;
    }

    /**
     * sets UID of user
     * @param localUID
     */
    public static void setUID(String localUID) {
        UID = localUID;
    }

    /**
     * logouts user and resets application
     */
    public static void logout() {
        UID = null;
        System.exit(0);
    }

    /**
     * sets array of account values
     * @param localList
     */
    public static void setAccountStrings(ArrayList<JSONObject> localList) {
        accountNumberArr.clear();
        accountIdArr.clear();
        accountNameArr.clear();
        for (int i = 0; i < localList.size(); i++) {
            JSONObject obj = localList.get(i);
            try {
                accountNameArr.add(obj.getString("accountName"));
                accountNumberArr.add(obj.getString("accountNumber"));
                accountIdArr.add(obj.getString("uniqueAccountId"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getAccountDataRequest() throws JSONException {
        UIActivity.setProgressBar(50);
        ServerRequest serverRequest = new ServerRequest();
        final GeneralLogic logic = new GeneralLogic(serverRequest);
        logic.getRequest("api/accounts/" + getUID(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            UIActivity.setProgressBar(90);
                            accountArr = JSONObjecttoArraylistObj("data", response);
                            setAccountStrings(accountArr);
                            UIActivity.setProgressBar(100);
                            if (FragmentsManager.getVisibleFragmentClass() != AccountsFragment.class){
                                FragmentsManager.nextFragment(AccountsFragment.class, true);
                            }

                        } catch (JSONException | InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    public static void setLoanStrings(ArrayList<JSONObject> localList) {
        loanamountArr.clear();
        loanBalArr.clear();
        minPaymentArr.clear();
        for (int i = 0; i < localList.size(); i++) {
            JSONObject obj = localList.get(i);
            try {

                loanamountArr.add(obj.getString("principleBalance"));
                loanIdArr.add(obj.getString("uniqueLoanId"));
                loanBalArr.add(obj.getString("remainingBalance"));
                minPaymentArr.add(obj.getString("minimumPayment"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getLoanDataRequest() throws JSONException {
        ServerRequest serverRequest = new ServerRequest();
        final GeneralLogic logic = new GeneralLogic(serverRequest);
        logic.getRequest("api/loans/" + getUID(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            loanArr = JSONObjecttoArraylistObj("data", response);
//                            Log.d("sananna", loanArr.get(0).toString());
                            setLoanStrings(loanArr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Log.d("sananna", loanArr.get(0).toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }
    public static ArrayList<JSONObject> getLoanData(){
//        Log.d("sananna", loanArr.get(0).toString());
        return loanArr;
    }

    public static ArrayList<JSONObject> getAccountData(){
//        Log.d("sananna", accountArr.get(0).toString());
        return accountArr;
    }
    public static void setLoanPosition(int position){
        loanPosition = position;
    }
    public static int getLoanPosition(){
        return loanPosition;
    }

    public static void setAccountPosition(int position){
        accountPosition = position;
    }
    public static int getAccountPosition(){
        return accountPosition;
    }

    public static void getEventDataRequest() throws JSONException {
            ServerRequest serverRequest = new ServerRequest();
            final GeneralLogic logic = new GeneralLogic(serverRequest);
            logic.getRequest("api/transactions/" + getUID(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                eventArr = JSONObjecttoArraylistObj("data", response);
                                PlannerFragment.setEvents();
                                if (FragmentsManager.getVisibleFragmentClass() != PlannerFragment.class) {
                                    FragmentsManager.nextFragment(PlannerFragment.class, true);
                                }
                            } catch (JSONException | InstantiationException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
        }

    public static ArrayList<JSONObject> getEventData(){
        return eventArr;
    }

    public static void sendDataToFrag(JSONObject json, String url, Class frag, Boolean replace) throws JSONException {
        ServerRequest serverRequest = new ServerRequest();
        final GeneralLogic logic = new GeneralLogic(serverRequest);
        logic.postRequestToFrag(json, url, frag, replace);
    }

    public static void setIsAdmin(Boolean admin){
        isAdmin = admin;
    }
    public static boolean getIsAdmin(){
        return isAdmin;
    }


}
