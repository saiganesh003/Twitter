package com.example.twitter1;

public class ErrorResp {
    private String Error;

    //getters and setters


    public ErrorResp(String Error) {
        this.Error = Error;
    }

    public void setError(String Error) {
        this.Error = Error;
    }

    public String getError() {
        return Error;
    }
}
