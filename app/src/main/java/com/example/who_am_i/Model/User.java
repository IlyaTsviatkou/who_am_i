package com.example.who_am_i.Model;

public class User{
        private static volatile User instance = null;
        private String name;
        public User(String username) {
            this.name = username;
        }

    public String getName() {
        return name;
    }

    public static User getInstance(String name) {
            if (instance == null) {
                instance = new User(name);
            }
            return instance;
        }

        public static void reset(User user) {
            instance = user;
        }
}
