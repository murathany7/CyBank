package coms.vb1.CyBank.Controllers;


import coms.vb1.CyBank.Tables.*;
import coms.vb1.CyBank.Repos.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/chat/{senderId}")  // this is Websocket url
public class ChatSocket {

  // cannot autowire static directly (instead we do it by the below
  // method

   private static MessageRepo msgRepo; 

   private static UserRepo userRepo; //Repository of all current users

	/*
   * Grabs the MessageRepository singleton from the Spring Application
   * Context.  This works because of the @Controller annotation on this
   * class and because the variable is declared as static.
   * There are other ways to set this. However, this approach is
   * easiest.
	 */
	@Autowired
	public void setMessageRepo(MessageRepo repo) {
		msgRepo = repo;  // we are setting the static variable
	}

	@Autowired
	public void setUserRepo(UserRepo repo) {
		userRepo = repo;  // we are setting the static variable
	}

	private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("senderId") int senderUserId) 
      throws IOException {


		User loggedIn = userRepo.findById(senderUserId).get();

		ActiveUsers.addUser(loggedIn, session);

		logger.info("Entered into Open");

		logger.info("Adding a user of type - " + loggedIn.getAccountType());


		String message = "Welcom to the char room!";

		// Welcome message?
		// try {
			
		// 	session.getBasicRemote().sendText(message);
		// } 
   		// catch (IOException e) {
		// 	logger.info("Exception: " + e.getMessage().toString());
		// 	e.printStackTrace();
		// }
	}


	@OnMessage
	public void onMessage(Session session, String message ) throws IOException {

		// Handle new messages
		logger.info("Entered into Message: Got Message:" + message);

		ArrayList<Integer> recievers = ActiveUsers.getReceivers(session);

		for(int i = 0; i < recievers.size(); i++){
			Session sendToSession = ActiveUsers.getSessionById(recievers.get(i));
			try {
				sendToSession.getBasicRemote().sendText(message);

				User sender = userRepo.findById(ActiveUsers.getIdBySession(session)).get();
				User reciever = userRepo.findById(recievers.get(i)).get();

				// Saving chat history to repository
				msgRepo.save(new Message(sender, reciever, message));
			} 
			   catch (IOException e) {
				logger.info("Exception: " + e.getMessage().toString());
				e.printStackTrace();
			}
		}

	}


	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");
		int disUser = ActiveUsers.getIdBySession(session);
		ActiveUsers.disconectUser(disUser);
	}


	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}

} // end of Class