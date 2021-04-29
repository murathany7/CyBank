package coms.vb1.CyBank.Tables;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Message {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="sent_id", nullable=false)
    private User sentUser;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="recieved_id", nullable=false)
    private User recievedUser;
	
    private String content;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();
	
	public Message() {};
	
	public Message(User sentUser, User recievedUser, String content) {
        this.sentUser = sentUser;
        this.recievedUser = recievedUser;
        this.content = content;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getSentUser() {
        return sentUser;
    }

    public void setSentUser(User sentUser) {
        this.sentUser = sentUser;
    }

    public User getRecievedUser() {
        return recievedUser;
    }

    public void setRecievedUser(User recievedUser) {
        this.recievedUser = recievedUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    
}