package com.rdx.rdxserver.controllers;

import com.rdx.rdxserver.entities.AppUserEntity;
import com.rdx.rdxserver.services.AppUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping(produces = {"application/json"})
    private ResponseEntity<AppUserEntity> getUser(@RequestParam int id) {
        AppUserEntity appUserEntity = appUserService.getUserById(id);
        return appUserEntity == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(appUserService.getUserById(id));
    }

    @PostMapping(value = "/register", produces = {"application/json"})
    private ResponseEntity<String> saveUser(@RequestBody AppUserEntity tempAppuser) {

        AppUserEntity appUserEntity = appUserService.registerUser(tempAppuser);
        return appUserEntity == null ? ResponseEntity.status(HttpStatus.CONFLICT).body("Email taken") : ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @GetMapping(value = "/all", produces = {"application/json"})
    public ResponseEntity<List<AppUserEntity>> getAllUsers() {
        return ResponseEntity.ok(appUserService.findAll());
    }


}
