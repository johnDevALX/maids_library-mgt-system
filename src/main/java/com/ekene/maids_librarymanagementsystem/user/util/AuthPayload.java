package com.ekene.maids_librarymanagementsystem.user.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthPayload {
    private String email;
    private String password;
}
