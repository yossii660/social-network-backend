package com.college.controllers;

import com.college.responses.PostItem;
import com.college.responses.PostsResponse;
import com.college.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    private DbUtils dbUtils;

    @GetMapping
    public PostsResponse getFeed(@RequestParam Long userId) {
        try {
            Connection connection = dbUtils.getConnection();

            String sql =
                    "SELECT p.id, p.user_id, p.content, p.created_at, u.username, u.profile_image_url " +
                            "FROM posts p " +
                            "JOIN users u ON p.user_id = u.id " +
                            "WHERE p.user_id IN (" +
                            "   SELECT followed_id FROM follows WHERE follower_id = ?" +
                            ") " +
                            "ORDER BY p.created_at DESC " +
                            "LIMIT 20";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<PostItem> posts = new ArrayList<>();

            while (rs.next()) {
                Long postId = rs.getLong("id");
                Long postUserId = rs.getLong("user_id");

                Integer likesCount = getLikesCount(connection, postId);
                boolean likedByMe = isLikedByMe(connection, postId, userId);
                boolean ownedByMe = postUserId.equals(userId);

                PostItem item = new PostItem(
                        postId,
                        postUserId,
                        rs.getString("username"),
                        rs.getString("profile_image_url"),
                        rs.getString("content"),
                        rs.getString("created_at"),
                        likesCount,
                        likedByMe,
                        ownedByMe
                );

                posts.add(item);
            }

            Collections.shuffle(posts);

            return new PostsResponse(true, 0, posts);

        } catch (Exception e) {
            e.printStackTrace();
            return new PostsResponse(false, 500, new ArrayList<>());
        }
    }

    private Integer getLikesCount(Connection connection, Long postId) throws Exception {
        String sql = "SELECT COUNT(*) AS count FROM post_likes WHERE post_id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, postId);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt("count");
    }

    private boolean isLikedByMe(Connection connection, Long postId, Long userId) throws Exception {
        String sql = "SELECT id FROM post_likes WHERE post_id = ? AND user_id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, postId);
        stmt.setLong(2, userId);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
}