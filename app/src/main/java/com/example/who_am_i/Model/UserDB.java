package com.example.who_am_i.Model;

public class UserDB {
    private String username;
    private String password;
    private String email;

    public UserDB(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserDB() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
