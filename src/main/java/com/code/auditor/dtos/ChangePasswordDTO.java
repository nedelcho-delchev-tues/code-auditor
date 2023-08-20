package com.code.auditor.dtos;

public class ChangePasswordDTO {
    private String password;
    private String newPassword;
    private String newPasswordRepeat;

    public ChangePasswordDTO(String password, String newPassword, String newPasswordRepeat) {
        this.password = password;
        this.newPassword = newPassword;
        this.newPasswordRepeat = newPasswordRepeat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordRepeat() {
        return newPasswordRepeat;
    }

    public void setNewPasswordRepeat(String newPasswordRepeat) {
        this.newPasswordRepeat = newPasswordRepeat;
    }
}
