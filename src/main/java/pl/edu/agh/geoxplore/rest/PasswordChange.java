package pl.edu.agh.geoxplore.rest;

import lombok.Data;

@Data
public class PasswordChange {
    String oldPassword;
    String newPassword;
}
