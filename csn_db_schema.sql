SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `csn` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

CREATE TABLE IF NOT EXISTS `csn`.`csn_app` (
  `id` BIGINT(20) NOT NULL,
  `app_name` VARCHAR(45) NULL DEFAULT NULL,
  `reg_time` DATETIME NULL DEFAULT NULL,
  `dereg_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `csn`.`csn_tag` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`, `name`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `csn`.`csn_topic` (
  `id` BIGINT(20) NOT NULL DEFAULT '0',
  `path` VARCHAR(60) NOT NULL,
  `sn_id` BIGINT(20) NOT NULL,
  `subs_cnt` INT(11) NOT NULL DEFAULT '0',
  `status` VARCHAR(12) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `csn`.`csn_sn` (
  `id` VARCHAR(8) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `reg_time` DATETIME NULL DEFAULT NULL,
  `dereg_time` DATETIME NULL DEFAULT NULL,
  `status` VARCHAR(12) NULL DEFAULT NULL,
  `topic_id` BIGINT(20) NULL DEFAULT NULL,
  `topic_path` VARCHAR(60) NULL DEFAULT NULL,
  `child_net_cnt` INT(11) NOT NULL DEFAULT 0,
  INDEX `topic_id_idx` (`topic_id` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `topic_id`
    FOREIGN KEY (`topic_id`)
    REFERENCES `csn`.`csn_topic` (`id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `csn`.`csn_sn_member` (
  `parent_id` VARCHAR(8) NOT NULL,
  `member_id` VARCHAR(8) NOT NULL,
  PRIMARY KEY (`parent_id`, `member_id`),
  CONSTRAINT `parent`
    FOREIGN KEY (`parent_id`)
    REFERENCES `csn`.`csn_sn` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `csn`.`csn_sn_meta` (
  `sn_id` VARCHAR(8) NOT NULL,
  `meta_key` VARCHAR(45) NOT NULL,
  `meta_value` VARCHAR(60) NULL DEFAULT NULL,
  PRIMARY KEY (`sn_id`, `meta_key`),
  INDEX `network_idx` (`sn_id` ASC),
  CONSTRAINT `network_meta`
    FOREIGN KEY (`sn_id`)
    REFERENCES `csn`.`csn_sn` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `csn`.`csn_topic_subs` (
  `topic_id` BIGINT(20) NOT NULL,
  `app_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`topic_id`, `app_id`),
  INDEX `app_idx` (`app_id` ASC),
  CONSTRAINT `app_subscribing`
    FOREIGN KEY (`app_id`)
    REFERENCES `csn`.`csn_app` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `topic_subscribing`
    FOREIGN KEY (`topic_id`)
    REFERENCES `csn`.`csn_topic` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `csn`.`csn_annotate` (
  `sn_id` VARCHAR(8) NOT NULL,
  `tag_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`tag_id`, `sn_id`),
  INDEX `network_idx` (`sn_id` ASC),
  CONSTRAINT `network_annotate`
    FOREIGN KEY (`sn_id`)
    REFERENCES `csn`.`csn_sn` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `tag`
    FOREIGN KEY (`tag_id`)
    REFERENCES `csn`.`csn_tag` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
