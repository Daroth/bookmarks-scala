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
  `id` int(11) NOT NULL,
  `link` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `link` (`link`)
);

CREATE TABLE `tag` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
);

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mail` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

ALTER TABLE `bookmark`
  ADD CONSTRAINT `fk_bookmark_link_id` FOREIGN KEY (`link_id`) REFERENCES `link` (`id`),
  ADD CONSTRAINT `fk_bookmark_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
  
ALTER TABLE `bookmark_tag`
  ADD CONSTRAINT `fk_bookmark_tag_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`),
  ADD CONSTRAINT `fk_bookmark_tag_bookmark_id` FOREIGN KEY (`bookmark_id`) REFERENCES `bookmark` (`id`);