package com.example.projectserver.uniserver.reminders;

import com.example.projectserver.uniserver.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "reminders")
public class Reminder {
    private @Id
    @GeneratedValue
    Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String desc;
    @Column(name = "time", nullable = false)
    private LocalTime time;
    @Column(name = "done", nullable = false)
    private Boolean done = false;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Reminder() {
    }
    public Reminder(String title, String desc, LocalTime time, User user) {
        this.desc = desc;
        this.time = time;
        this.title = title;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


