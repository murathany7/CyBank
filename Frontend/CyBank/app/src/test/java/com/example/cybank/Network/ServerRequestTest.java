package com.example.cybank.Network;

import com.example.cybank.Logic.GeneralLogic;
import com.example.cybank.Logic.IVolleyListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ServerRequestTest {
    @Mock
    IVolleyListener l;
    @InjectMocks
    ServerRequest serverRequest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testAddVolleyListener() throws Exception {
        serverRequest.addVolleyListener(new GeneralLogic(new ServerRequest()));
    }
}

