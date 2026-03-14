CREATE DATABASE IF NOT EXISTS social_network;
USE social_network;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_image_url VARCHAR(500) DEFAULT '',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS follows (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       follower_id BIGINT NOT NULL,
                                       followed_id BIGINT NOT NULL,
                                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       UNIQUE (follower_id, followed_id)
    );

CREATE TABLE IF NOT EXISTS posts (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     user_id BIGINT NOT NULL,
                                     content VARCHAR(500) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS post_likes (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          post_id BIGINT NOT NULL,
                                          user_id BIGINT NOT NULL,
                                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          UNIQUE (post_id, user_id)
    );