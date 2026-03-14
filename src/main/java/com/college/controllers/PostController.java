package com.college.controllers;

import com.college.requests.CreatePostRequest;
import com.college.requests.LikeRequest;
import com.college.responses.BasicResponse;
import com.college.responses.PostItem;
import com.college.responses.PostsResponse;
import com.college.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private DbUtils dbUtils;

    @PostMapping("/create")
    public BasicResponse createPost(@RequestBody CreatePostRequest request) {
        try {
            if (request.getUserId() == null) {
                return new BasicResponse(false, 1);
            }

            String content = request.getContent() == null ? "" : request.getContent().trim();

            if (content.isEmpty()) {
                return new BasicResponse(false, 2);
            }

            if (content.length() > 500) {
                return new BasicResponse(false, 3);
            }

            Connection connection = dbUtils.getConnection();

            String sql = "INSERT INTO posts (user_id, content) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, request.getUserId());
            stmt.setString(2, content);
            stmt.executeUpdate();

            return new BasicResponse(true, 0);

        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, 500);
        }
    }


    @GetMapping("/my")
    public PostsResponse getMyPosts(@RequestParam Long userId) {
        try {
            Connection connection = dbUtils.getConnection();

            String sql = "SELECT p.id, p.user_id, p.content, p.created_at, u.username, u.profile_image_url " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "WHERE p.user_id = ? " +
                    "ORDER BY p.created_at DESC";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<PostItem> posts = new ArrayList<>();

            while (rs.next()) {
                Long postId = rs.getLong("id");

                Integer likesCount = getLikesCount(connection, postId);
                boolean likedByMe = isLikedByMe(connection, postId, userId);
                boolean ownedByMe = rs.getLong("user_id") == userId;

                PostItem item = new PostItem(
                        postId,
                        rs.getLong("user_id"),
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

            return new PostsResponse(true, 0, posts);

        } catch (Exception e) {
            e.printStackTrace();
            return new PostsResponse(false, 500, new ArrayList<>());
        }
    }

    @PostMapping("/like")
    public BasicResponse likePost(@RequestBody LikeRequest request) {
        try {
            if (request.getUserId() == null || request.getPostId() == null) {
                return new BasicResponse(false, 1);
            }

            Connection connection = dbUtils.getConnection();

            String checkPostSql = "SELECT id FROM posts WHERE id = ?";
            PreparedStatement checkPostStmt = connection.prepareStatement(checkPostSql);
            checkPostStmt.setLong(1, request.getPostId());
            ResultSet postRs = checkPostStmt.executeQuery();

            if (!postRs.next()) {
                return new BasicResponse(false, 2);
            }

            String checkLikeSql = "SELECT id FROM post_likes WHERE post_id = ? AND user_id = ?";
            PreparedStatement checkLikeStmt = connection.prepareStatement(checkLikeSql);
            checkLikeStmt.setLong(1, request.getPostId());
            checkLikeStmt.setLong(2, request.getUserId());
            ResultSet likeRs = checkLikeStmt.executeQuery();

            if (likeRs.next()) {
                return new BasicResponse(false, 3);
            }

            String insertSql = "INSERT INTO post_likes (post_id, user_id) VALUES (?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setLong(1, request.getPostId());
            insertStmt.setLong(2, request.getUserId());
            insertStmt.executeUpdate();

            return new BasicResponse(true, 0);

        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, 500);
        }
    }

    @PostMapping("/unlike")
    public BasicResponse unlikePost(@RequestBody LikeRequest request) {
        try {
            if (request.getUserId() == null || request.getPostId() == null) {
                return new BasicResponse(false, 1);
            }

            Connection connection = dbUtils.getConnection();

            String deleteSql = "DELETE FROM post_likes WHERE post_id = ? AND user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(deleteSql);
            stmt.setLong(1, request.getPostId());
            stmt.setLong(2, request.getUserId());

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                return new BasicResponse(false, 2);
            }

            return new BasicResponse(true, 0);

        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, 500);
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

    @DeleteMapping("/{postId}")
    public BasicResponse deletePost(@PathVariable Long postId, @RequestParam Long userId) {
        try {
            Connection connection = dbUtils.getConnection();

            String checkSql = "SELECT user_id FROM posts WHERE id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setLong(1, postId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                return new BasicResponse(false, 1);
            }

            long postOwnerId = rs.getLong("user_id");

            if (postOwnerId != userId) {
                return new BasicResponse(false, 2);
            }

            String deleteLikesSql = "DELETE FROM post_likes WHERE post_id = ?";
            PreparedStatement deleteLikesStmt = connection.prepareStatement(deleteLikesSql);
            deleteLikesStmt.setLong(1, postId);
            deleteLikesStmt.executeUpdate();

            String deletePostSql = "DELETE FROM posts WHERE id = ?";
            PreparedStatement deletePostStmt = connection.prepareStatement(deletePostSql);
            deletePostStmt.setLong(1, postId);

            int deletedRows = deletePostStmt.executeUpdate();

            if (deletedRows == 0) {
                return new BasicResponse(false, 3);
            }

            return new BasicResponse(true, 0);

        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, 500);
        }
    }
}