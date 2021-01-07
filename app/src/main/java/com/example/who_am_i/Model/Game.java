package com.example.who_am_i.Model;

public class Game {
    public String getId() {
        return id;
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastAnswer() {
        return lastAnswer;
    }

    public int getTurn() {
        return turn;
    }

    public String getAnswer() {
        return answer;
    }

    public String id;
    public String p1;
    public String p2;
    public String lastMessage;
    public String lastAnswer;
    public int turn;
    public String answer;
    public String answerCode;
    public String winner;

    public String getAnswerCode() {
        return answerCode;
    }
}
