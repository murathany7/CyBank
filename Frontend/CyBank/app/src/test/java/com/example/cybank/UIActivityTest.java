package com.example.cybank;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.SimpleArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.core.app.ComponentActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentController;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;

import com.google.android.material.navigation.NavigationView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class UIActivityTest {
    @Mock
    DrawerLayout mDrawer;
    @Mock
    Toolbar toolbar;
    @Mock
    NavigationView nvDrawer;
    @Mock
    Activity c;
    @Mock
    ProgressBar simpleProgressBar;
    @Mock
    ActionBarDrawerToggle drawerToggle;
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
    SimpleArrayMap<Class<? extends ComponentActivity.ExtraData>, ComponentActivity.ExtraData> mExtraDataMap;
    @InjectMocks
    UIActivity uIActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetUIContext() throws Exception {
        Context result = UIActivity.getUIContext();
        Assert.assertEquals(null, result);
    }
}

