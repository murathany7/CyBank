package com.example.cybank;

import android.content.res.Resources;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.SimpleArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.FragmentController;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;

import com.android.volley.RequestQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LoginActivityTest {
    @Mock
    EditText email;
    @Mock
    EditText password;
    @Mock
    Button login;
    @Mock
    Button signup;
    @Mock
    TextView error;
    @Mock
    LoginActivity mInstance;
    @Mock
    RequestQueue mRequestQueue;
    @Mock
    AppCompatDelegate mDelegate;
    @Mock
    Resources mResources;
    @Mock
    FragmentController mFragments;
    @Mock
    LifecycleRegistry mFragmentLifecycleRegistry;
    @Mock
    SparseArrayCompat<String> mPendingFragmentActivityResults;
    @Mock
    LifecycleRegistry mLifecycleRegistry;
    @Mock
    ViewModelStore mViewModelStore;
    @Mock
    SimpleArrayMap<Class<? extends ComponentActivity.ExtraData>, ComponentActivity.ExtraData> mExtraDataMap;

    @InjectMocks
    LoginActivity loginActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCancelPendingRequests() throws Exception {
        loginActivity.cancelPendingRequests("tag");
    }
}