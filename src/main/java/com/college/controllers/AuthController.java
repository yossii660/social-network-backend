package com.college.controllers;

import com.college.requests.LoginRequest;
import com.college.requests.RegisterRequest;
import com.college.responses.BasicResponse;
import com.college.responses.LoginResponse;
import com.college.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private DbUtils dbUtils;

    @PostMapping("/register")
    public BasicResponse register(@RequestBody RegisterRequest request) {
        try {
            String username = request.getUsername() == null ? "" : request.getUsername().trim();
            String password = request.getPassword() == null ? "" : request.getPassword().trim();
            String confirmPassword = request.getConfirmPassword() == null ? "" : request.getConfirmPassword().trim();

            if (username.isEmpty()) {
                return new BasicResponse(false, 1);
            }

            if (password.isEmpty()) {
                return new BasicResponse(false, 2);
            }

            if (!password.equals(confirmPassword)) {
                return new BasicResponse(false, 3);
            }

            Connection connection = dbUtils.getConnection();

            String checkSql = "SELECT id FROM users WHERE username = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return new BasicResponse(false, 4);
            }

            String insertSql = "INSERT INTO users (username, password, profile_image_url) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, "");
            insertStmt.executeUpdate();

            return new BasicResponse(true, 0);

        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, 500);
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            String username = request.getUsername() == null ? "" : request.getUsername().trim();
            String password = request.getPassword() == null ? "" : request.getPassword().trim();

            if (username.isEmpty() || password.isEmpty()) {
                return new LoginResponse(false, 1, null, null);
            }

            Connection connection = dbUtils.getConnection();

            String sql = "SELECT id, username FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return new LoginResponse(false, 2, null, null);
            }

            Long userId = rs.getLong("id");
            String dbUsername = rs.getString("username");

            return new LoginResponse(true, 0, userId, dbUsername);

        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse(false, 500, null, null);
        }
    }
}