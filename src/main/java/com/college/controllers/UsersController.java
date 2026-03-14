package com.college.controllers;

import com.college.requests.UpdateProfileImageRequest;
import com.college.responses.BasicResponse;
import com.college.responses.SearchUserItem;
import com.college.responses.SearchUsersResponse;
import com.college.responses.UserResponse;
import com.college.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private DbUtils dbUtils;

    @GetMapping("/me")
    public UserResponse getMe(@RequestParam Long userId) {
        try {
            Connection connection = dbUtils.getConnection();

            String userSql = "SELECT id, username, profile_image_url FROM users WHERE id = ?";
            PreparedStatement userStmt = connection.prepareStatement(userSql);
            userStmt.setLong(1, userId);
            ResultSet userRs = userStmt.executeQuery();

            if (!userRs.next()) {
                return new UserResponse(false, 1);
            }

            Long id = userRs.getLong("id");
            String username = userRs.getString("username");
            String profileImageUrl = userRs.getString("profile_image_url");

            Integer followersCount = getCount(connection,
                    "SELECT COUNT(*) AS count FROM follows WHERE followed_id = ?", userId);

            Integer followingCount = getCount(connection,
                    "SELECT COUNT(*) AS count FROM follows WHERE follower_id = ?", userId);

            Integer postCount = getCount(connection,
                    "SELECT COUNT(*) AS count FROM posts WHERE user_id = ?", userId);

            return new UserResponse(true, 0, id, username, profileImageUrl, followersCount, followingCount, postCount);

        } catch (Exception e) {
            e.printStackTrace();
            return new UserResponse(false, 500);
        }
    }

    @PostMapping("/profile-image")
    public BasicResponse updateProfileImage(@RequestBody UpdateProfileImageRequest request) {
        try {
            if (request.getUserId() == null) {
                return new BasicResponse(false, 1);
            }

            String profileImageUrl = request.getProfileImageUrl() == null ? "" : request.getProfileImageUrl().trim();

            Connection connection = dbUtils.getConnection();

            String sql = "UPDATE users SET profile_image_url = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, profileImageUrl);
            stmt.setLong(2, request.getUserId());

            int updatedRows = stmt.executeUpdate();

            if (updatedRows == 0) {
                return new BasicResponse(false, 2);
            }

            return new BasicResponse(true, 0);

        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, 500);
        }
    }

    @GetMapping("/search")
    public SearchUsersResponse searchUsers(@RequestParam Long userId, @RequestParam String query) {
        try {
            String cleanQuery = query == null ? "" : query.trim();

            if (cleanQuery.length() < 3) {
                return new SearchUsersResponse(true, 0, new ArrayList<>());
            }

            Connection connection = dbUtils.getConnection();

            String sql = "SELECT id, username, profile_image_url FROM users WHERE username LIKE ? AND id <> ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + cleanQuery + "%");
            stmt.setLong(2, userId);

            ResultSet rs = stmt.executeQuery();

            List<SearchUserItem> users = new ArrayList<>();

            while (rs.next()) {
                Long foundUserId = rs.getLong("id");

                String followCheckSql = "SELECT id FROM follows WHERE follower_id = ? AND followed_id = ?";
                PreparedStatement followStmt = connection.prepareStatement(followCheckSql);
                followStmt.setLong(1, userId);
                followStmt.setLong(2, foundUserId);
                ResultSet followRs = followStmt.executeQuery();

                boolean followedByMe = followRs.next();

                SearchUserItem item = new SearchUserItem(
                        foundUserId,
                        rs.getString("username"),
                        rs.getString("profile_image_url"),
                        followedByMe
                );

                users.add(item);
            }

            return new SearchUsersResponse(true, 0, users);

        } catch (Exception e) {
            e.printStackTrace();
            return new SearchUsersResponse(false, 500, new ArrayList<>());
        }
    }

    private Integer getCount(Connection connection, String sql, Long userId) throws Exception {
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, userId);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt("count");
    }
}