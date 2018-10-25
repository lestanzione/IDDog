package br.com.stanzione.iddog.data;

import com.google.gson.JsonObject;

public class LoginRequest {

    private static final String PROPERTY_EMAIL = "email";

    private String email;

    public LoginRequest(String email){
        this.setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        return jsonObject;
    }
}
