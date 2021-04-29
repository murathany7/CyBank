package com.example.cybank.Logic;

public interface IVolleyListener {
    public void onSuccess(String s) throws IllegalAccessException, InstantiationException;
    public void onError(String s);
}
