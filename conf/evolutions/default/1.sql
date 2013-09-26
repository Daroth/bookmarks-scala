CREATE TABLE `bookmark` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` text NOT NULL,
  `user_id` int(11) NOT NULL,
  `link_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `link_id` (`link_id`)
);

CREATE TABLE `bookmark_tag` (
  `bookmark_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  PRIMARY KEY (`bookmark_id`,`tag_id`)
);

CREATE TABLE `link` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `link` (`link`)
);

CREATE TABLE `oauth1_info` (
  `id_user` int(11) NOT NULL,
  `token` varchar(255) NOT NULL,
  `secret` varchar(255) NOT NULL,
  PRIMARY KEY (`id_user`)
);

CREATE TABLE `oauth2_info` (
  `id_user` int(11) NOT NULL,
  `access_token` varchar(255) NOT NULL,
  `token_type` varchar(255) DEFAULT NULL,
  `expires_in` int(11) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_user`)
);

CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
);

CREATE TABLE `token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `uuid` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `creation_time` datetime NOT NULL,
  `expiration_time` datetime NOT NULL,
  `is_sign_up` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `token` (`token`)
);

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `provider_id` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `auth_method` varchar(255) NOT NULL,
  `hasher` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

ALTER TABLE `bookmark`
  ADD CONSTRAINT `fk_bookmark_link_id` FOREIGN KEY (`link_id`) REFERENCES `link` (`id`),
  ADD CONSTRAINT `fk_bookmark_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
  
ALTER TABLE `bookmark_tag`
  ADD CONSTRAINT `fk_bookmark_tag_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`),
  ADD CONSTRAINT `fk_bookmark_tag_bookmark_id` FOREIGN KEY (`bookmark_id`) REFERENCES `bookmark` (`id`);

ALTER TABLE `oauth1_info`
  ADD CONSTRAINT `oauth1_info_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`);

ALTER TABLE `oauth2_info`
  ADD CONSTRAINT `oauth2_info_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`);
