package com.example.cybank.Logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Test for null inputs for helper methods
 */
public class HelperTest {
    ArrayList <JSONObject> obj;
    String str = "{ \"uniqueUserId\": 12, \"name\": \"murat\", \"email\": \"murat1@gmail.com\", \"password\": \"123456789\", \"accountType\": \"C\", \"createdOn\": \"2021-03-08T19:14:32.998+00:00\", \"ssn\": null, \"age\": null, \"income\": null }";
    ArrayList read;
    JSONArray array = new JSONArray();
    JSONObject sample = new JSONObject();
    ArrayList<JSONObject> accountArr;


    @Before
    public void setUp() throws JSONException {

        JSONObject sample2 = new JSONObject(str);

        array.put(sample2);
        sample.put("data", array);
        read = new ArrayList <JSONObject> ();
        read.add(sample);

    }
    @Test
    public void testgetUID(){
        try (MockedStatic<Helper> theMock = Mockito.mockStatic(Helper.class)) {
            theMock.when(() -> Helper.getUID()).thenReturn("19");
            assertEquals("19", Helper.getUID());
        }

    }

    @Test
    public void testJSONObjecttoArraylistObj() throws Exception {
        setUp();
        try (MockedStatic<Helper> theMock = Mockito.mockStatic(Helper.class)) {
            theMock.when(() -> Helper.JSONObjecttoArraylistObj("data", sample)).thenReturn(read);
            ArrayList<JSONObject> result = Helper.JSONObjecttoArraylistObj("data", sample);
            assertEquals(read, result);
        }

    }

    @Test
    public void testJSONArraytoArraylistObj() throws Exception {
        setUp();
        try (MockedStatic<Helper> theMock = Mockito.mockStatic(Helper.class)) {
            theMock.when(() -> Helper.JSONArraytoArraylistObj(array)).thenReturn(read);
            ArrayList<JSONObject> result = Helper.JSONArraytoArraylistObj(array);
            assertEquals(read, result);
        }
    }
    @Test
    public void testgetAccountDataRequest() throws Exception {
        JSONObject obj = new JSONObject(str);
        ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
        accountArr = null;
        arr.add(obj);
        try (MockedStatic<Helper> theMock = Mockito.mockStatic(Helper.class)) {
            theMock.when(() -> Helper.getAccountDataRequest()).thenAnswer(x -> {

                accountArr = new ArrayList<>(arr);
                return null;
            });
            Helper.getAccountDataRequest();
            assertEquals(arr, accountArr);
        }
    }
    @Test
    public void testgetEventDataRequest() throws Exception {
        JSONObject obj = new JSONObject(str);
        ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
        accountArr = null;
        arr.add(obj);
        try (MockedStatic<Helper> theMock = Mockito.mockStatic(Helper.class)) {
            theMock.when(() -> Helper.getAccountDataRequest()).thenAnswer(x -> {

                accountArr = new ArrayList<>(arr);
                return null;
            });
            Helper.getAccountDataRequest();
            assertEquals(arr, accountArr);
        }
    }

}