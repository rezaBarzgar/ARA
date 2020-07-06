package com.a.ara;

public class user {
    private int id; // primary key
    private String firstname;
    private String lastname;
    private String username; // uniqe
    private String password;
    private String email;
    private String region;
    private String question;
    private String answer;

    public static String key_user_id = "userid";
    public static String key_user_first_name = "firstname";
    public static String key_user_last_name = "lastname";
    public static String key_user_name = "username";
    public static String key_user_password= "password";
    public static String key_user_email= "email";
    public static String key_user_region= "region";
    public static String key_user_question= "question";
    public static String key_user_answer= "answer";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
