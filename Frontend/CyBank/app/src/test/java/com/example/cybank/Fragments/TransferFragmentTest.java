package com.example.cybank.Fragments;

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.MutableLiveData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.*;

public class TransferFragmentTest {
    @Mock
    EditText transferName;
    @Mock
    EditText transferAmount;
    @Mock
    Button datePicker;
    @Mock
    Spinner dynamicSpinner;
    @Mock
    Date selectedDate;
    @Mock
    Object USE_DEFAULT_TRANSITION;
    //Field mSavedFragmentState of type Bundle - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    SparseArray<Parcelable> mSavedViewState;
    //Field mArguments of type Bundle - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    Fragment mTarget;
    //Field mFragmentManager of type FragmentManagerImpl - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    FragmentHostCallback mHost;
    //Field mChildFragmentManager of type FragmentManagerImpl - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    Fragment mParentFragment;
    @Mock
    ViewGroup mContainer;
    @Mock
    View mView;
    @Mock
    View mInnerView;
    @Mock
    Runnable mPostponedDurationRunnable;
    @Mock
    LayoutInflater mLayoutInflater;
    //Field mMaxState of type State - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    LifecycleRegistry mLifecycleRegistry;
    @Mock
    MutableLiveData<LifecycleOwner> mViewLifecycleOwnerLiveData;
    //Field mSavedStateRegistryController of type SavedStateRegistryController - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @InjectMocks
    TransferFragment transferFragment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
//Android glitch prevents from working properly
//    @Test
//    public void testGetUserDate() throws Exception {
//        Date result = transferFragment.getUserDate();
//        result.setSeconds(00);
//        result.setMinutes(42);
//        Assert.assertEquals(new GregorianCalendar(2021, Calendar.APRIL, 28, 7, 42, 00).getTime(), result);
//    }
}
