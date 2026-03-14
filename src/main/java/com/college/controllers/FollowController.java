package com.college.controllers;

import com.college.requests.FollowRequest;
import com.college.responses.BasicResponse;
import com.college.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private DbUtils dbUtils;

    @PostMapping("/follow")
    public BasicResponse follow(@RequestBody FollowRequest request) {
        try {

            if (request.getFollowerId().equals(request.getFollowedId())) {
                return new BasicResponse(false, 1);
            }

            Connection connection = dbUtils.getConnection();

            String checkSql = "SELECT id FROM follows WHERE follower_id = ? AND followed_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setLong(1, request.getFollowerId());
            checkStmt.setLong(2, request.getFollowedId());

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return new BasicResponse(false, 2);
            }

            String insertSql = "INSERT INTO follows (follower_id, followed_id) VALUES (?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setLong(1, request.getFollowerId());
            insertStmt.setLong(2, request.getFollowedId());

            insertStmt.executeUpdate();

            return new BasicResponse(true, 0);

        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, 500);
        }
    }

    @PostMapping("/unfollow")
    public BasicResponse unfollow(@RequestBody FollowRequest request) {
        try {

            Connection connection = dbUtils.getConnection();

            String deleteSql = "DELETE FROM follows WHERE follower_id = ? AND followed_id = ?";
            PreparedStatement stmt = connection.prepareStatement(deleteSql);
            stmt.setLong(1, request.getFollowerId());
            stmt.setLong(2, request.getFollowedId());

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                return new BasicResponse(false, 1);
            }

            return new BasicResponse(true, 0);

        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, 500);
        }
    }
}