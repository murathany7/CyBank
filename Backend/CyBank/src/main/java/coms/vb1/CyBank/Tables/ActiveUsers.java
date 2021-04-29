package coms.vb1.CyBank.Tables;

import coms.vb1.CyBank.Repos.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveUsers {

    private final static Logger logger = LoggerFactory.getLogger(ActiveUsers.class);

    private ActiveUsers(){}


    private static HashMap< Integer , Session> uidSessionLookup = new HashMap<>();
    private static HashMap<Session, Integer> sessionUIDLookup = new HashMap<>();

    // Stores uid of a sender and uid of a list of recievers
    private static HashMap<Integer, ArrayList<Integer> > senderReceiverLookup = new HashMap<Integer, ArrayList<Integer>>();

    private static HashMap<Integer, Character> userTypeLookup = new HashMap<>();


    public static void addToList(int uid, Session session, Character userType){
        uidSessionLookup.put(uid, session);
        sessionUIDLookup.put(session, uid);
        userTypeLookup.put(uid, userType);
    }

    public static void removeFromList(int uid){
        sessionUIDLookup.remove(uidSessionLookup.get(uid));
        uidSessionLookup.remove(Integer.valueOf(uid));
        userTypeLookup.remove(uid);
    }

    public static void disconectUser(int uid){
        for (Entry<Integer, ArrayList<Integer>> entry : senderReceiverLookup.entrySet()) {

            ArrayList<Integer> v = entry.getValue();
            v.remove(Integer.valueOf(uid));

        }

        removeFromList(uid);

    }

    public static void addUser(User user, Session session){

        logger.info("Searching active users, found user - ");

        userTypeLookup.forEach( (k, v) -> {

            logger.info( "" + k);

        });


        if(user.getAccountType().equals('A') || user.getAccountType().equals('S')){
            
            userTypeLookup.forEach( (k, v) -> {

                if(!senderReceiverLookup.containsKey(k) && v.equals('U')){

                    logger.info("Connecting Admin and User");

                    connectUser(k, user.getUniqueUserId());

                    return;

                }

            });

        } else {

            userTypeLookup.forEach( (k, v) -> {

                if(!senderReceiverLookup.containsKey(k) && (v.equals('A') || v.equals('S'))){

                    logger.info("Connecting Admin and User");

                    connectUser(k, user.getUniqueUserId());

                    return;

                }

            });


        }

        addToList(user.getUniqueUserId(), session, user.getAccountType());
    }

    public static void connectUser(int u1, int u2){
        addUserToRecieveList( u1, u2);
        addUserToRecieveList( u2, u1);
    }

    public static Session getSessionById( int uid){
        return uidSessionLookup.get(uid);
    }

    public static int getIdBySession( Session session){
        return sessionUIDLookup.get(session);
    }

    public static ArrayList<Integer> getReceivers(Session session){
        return senderReceiverLookup.get(sessionUIDLookup.get(session));
    }

    public static ArrayList<Integer> getReceivers(int uid){
        return senderReceiverLookup.get(uid);
    }

    public static void addUserToRecieveList( Session sessionSender, int uidReceiver ){
        addUserToRecieveList(sessionUIDLookup.get(sessionSender), uidReceiver);
    }

    public static void addUserToRecieveList( int uidSender, Session sessionReceiver ){
        addUserToRecieveList(uidSender, sessionUIDLookup.get(sessionReceiver));
    }

    public static void addUserToRecieveList( Session sessionSender, Session sessionReceiver ){
        addUserToRecieveList( sessionUIDLookup.get(sessionSender), sessionUIDLookup.get(sessionReceiver));
    }

    public static void addUserToRecieveList( int uidSender, int uidReceiver ){
        if (!senderReceiverLookup.containsKey(uidSender))
            senderReceiverLookup.put(uidSender, new ArrayList<Integer>());

        senderReceiverLookup.get(uidSender).add(uidReceiver);
    }

    public static void removeUserFromRecieveList( Session sessionSender, int uidReceiver ){
        removeUserFromRecieveList(sessionUIDLookup.get(sessionSender), uidReceiver);
    }

    public static void removeUserFromRecieveList( int uidSender, Session sessionReceiver ){
        removeUserFromRecieveList(uidSender, sessionUIDLookup.get(sessionReceiver));
    }

    public static void removeUserFromRecieveList( Session sessionSender, Session sessionReceiver ){
        removeUserFromRecieveList( sessionUIDLookup.get(sessionSender), sessionUIDLookup.get(sessionReceiver));
    }

    public static void removeUserFromRecieveList( int uidSender, int uidReceiver ){
        if (!senderReceiverLookup.containsKey(uidSender)){
            senderReceiverLookup.put(uidSender, new ArrayList<Integer>());
        }

        senderReceiverLookup.get(uidSender).remove(Integer.valueOf(uidReceiver));
    }

}