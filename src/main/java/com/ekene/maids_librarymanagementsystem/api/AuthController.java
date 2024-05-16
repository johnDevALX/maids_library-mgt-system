package com.ekene.maids_librarymanagementsystem.api;

import com.ekene.maids_librarymanagementsystem.user.util.AuthPayload;
import com.ekene.maids_librarymanagementsystem.user.AuthService;
import com.ekene.maids_librarymanagementsystem.user.util.UserDto;
import com.ekene.maids_librarymanagementsystem.utils.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/")
public class AuthController extends BaseController {
    private final AuthService authService;

    @PostMapping("create")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){
        return getAppResponse(HttpStatus.CREATED, "Successful", authService.createLmsUser(userDto));
    }

    @PostMapping("authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthPayload authPayload){
        return getAppResponse(HttpStatus.OK, "Authenticated", authService.authenticateUser(authPayload));
    }
}
