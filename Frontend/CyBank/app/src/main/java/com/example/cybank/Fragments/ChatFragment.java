package com.example.cybank.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.example.cybank.Logic.FragmentsManager;
import com.example.cybank.Logic.Helper;
import com.example.cybank.Logic.Message;
import com.example.cybank.Logic.User;
import com.example.cybank.R;
import com.example.cybank.UIActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

public class ChatFragment extends Fragment implements MessageInput.InputListener {
    public static MessagesList messagesList;
    private static WebSocketClient cc;
    static int counter;
    static User user = new User(Helper.getUID(), "user", null, true);
    static User admin = new User("" + 0, "admin", null, true);
    static MessagesListAdapter<Message> adapter = new MessagesListAdapter<>(Helper.getUID(), null);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, parent, false);
        messagesList = (MessagesList)rootView.findViewById(R.id.messagesList);
        MessageInput input = (MessageInput)rootView.findViewById(R.id.input);
        input.setInputListener(this);
        messagesList.setAdapter(adapter);
        counter = 0;
        web();
        return rootView;
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        Message message = new Message("" + counter++, user, input.toString(), new Date());
        adapter.addToStart(message, true);

        cc.send(input.toString());
        return true;
    }

    public void web(){
        Draft[] drafts = {
                new Draft_6455()
        };
        String w = "ws://coms-309-034.cs.iastate.edu:8080/chat/" + Helper.getUID();

        try {
            Log.d("Socket:", "Trying socket");
            cc = new WebSocketClient(new URI(w), (Draft) drafts[0]) {
                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);
                    Message send = new Message("" + counter++, admin, message, new Date());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addToStart(send, true);
                        }
                    });
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                    Message send = new Message("" + counter++, admin, "Connected!", new Date());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addToStart(send, true);
                        }
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                    cc.connect();
                }

                @Override
                public void onError(Exception e) {
                    Log.d("Exception:", e.toString());
                    e.printStackTrace();
                    Message send = new Message("" + counter++, admin, "Error!" + e.toString(), new Date());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addToStart(send, true);
                            try {
                                FragmentsManager.nextFragmentBundle(ErrorFragment.class, false, e.toString());
                            } catch (java.lang.InstantiationException instantiationException) {
                                instantiationException.printStackTrace();
                            } catch (IllegalAccessException illegalAccessException) {
                                illegalAccessException.printStackTrace();
                            }
                        }
                    });

                }
            };
        } catch (URISyntaxException e) {
            Log.d("Exception:", e.getMessage().toString());
            e.printStackTrace();
        }
        cc.connect();

    }

}

