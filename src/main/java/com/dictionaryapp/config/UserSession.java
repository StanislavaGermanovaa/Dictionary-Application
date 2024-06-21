package com.dictionaryapp.config;

import com.dictionaryapp.model.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {

    private long id;

    private String username;

    public void login(User user){
        this.id=user.getId();
        this.username=user.getUsername();
    }

    public void logout() {
        id = 0;
        username = null;
    }

    public boolean isLoggedIn(){
    return id>0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
