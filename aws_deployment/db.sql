-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema cr_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cr_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cr_db` DEFAULT CHARACTER SET latin1 ;
USE `cr_db` ;

-- -----------------------------------------------------
-- Table `cr_db`.`Escalate_level`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cr_db`.`Escalate_level` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `level` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `cr_db`.`profile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cr_db`.`profile` (
  `id` VARCHAR(128) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `role` INT(11) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `cr_db`.`post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cr_db`.`post` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `escalate_lvl` INT(11) NOT NULL,
  `chief` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_post_esc_lvl_idx` (`escalate_lvl` ASC) VISIBLE,
  INDEX `fk_post_chief_idx` (`chief` ASC) VISIBLE,
  CONSTRAINT `fk_post_chief`
    FOREIGN KEY (`chief`)
    REFERENCES `cr_db`.`profile` (`id`),
  CONSTRAINT `fk_post_esc_lvl`
    FOREIGN KEY (`escalate_lvl`)
    REFERENCES `cr_db`.`Escalate_level` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `cr_db`.`crime_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cr_db`.`crime_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `cr_db`.`case`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cr_db`.`case` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `profile` VARCHAR(128) NULL DEFAULT NULL,
  `type` INT(11) NULL DEFAULT '0',
  `address` VARCHAR(128) NOT NULL,
  `date` DATETIME NOT NULL,
  `description` VARCHAR(250) NOT NULL,
  `longitude` DOUBLE NULL DEFAULT NULL,
  `latitude` DOUBLE NULL DEFAULT NULL,
  `post` INT(11) NULL DEFAULT NULL,
  `is_closed` INT(11) NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `fk_case_profile_idx` (`profile` ASC) VISIBLE,
  INDEX `fk_case_type_idx` (`type` ASC) VISIBLE,
  INDEX `fk_case_post_idx` (`post` ASC) VISIBLE,
  CONSTRAINT `fk_case_post`
    FOREIGN KEY (`post`)
    REFERENCES `cr_db`.`post` (`id`),
  CONSTRAINT `fk_case_profile`
    FOREIGN KEY (`profile`)
    REFERENCES `cr_db`.`profile` (`id`),
  CONSTRAINT `fk_case_type`
    FOREIGN KEY (`type`)
    REFERENCES `cr_db`.`crime_type` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 313
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `cr_db`.`case_assign`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cr_db`.`case_assign` (
  `case` INT(11) NOT NULL,
  `profile` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`case`, `profile`),
  INDEX `fk_ca_case_idx` (`case` ASC) VISIBLE,
  INDEX `fk_ca_profile_idx` (`profile` ASC) VISIBLE,
  CONSTRAINT `fk_ca_case`
    FOREIGN KEY (`case`)
    REFERENCES `cr_db`.`case` (`id`),
  CONSTRAINT `fk_ca_profile`
    FOREIGN KEY (`profile`)
    REFERENCES `cr_db`.`profile` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `cr_db`.`case_evidence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cr_db`.`case_evidence` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `case` INT(11) NULL DEFAULT NULL,
  `evidence` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_case_evidence_id_idx` (`id` ASC) VISIBLE,
  INDEX `fk_case_evidence_case_idx` (`case` ASC) VISIBLE,
  CONSTRAINT `fk_case_evidence_case`
    FOREIGN KEY (`case`)
    REFERENCES `cr_db`.`case` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `cr_db`.`case_notes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cr_db`.`case_notes` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `case` INT(11) NOT NULL,
  `profile` VARCHAR(45) NOT NULL,
  `timestamp` DATETIME NOT NULL,
  `note` VARCHAR(1024) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_cn_case_idx` (`case` ASC) VISIBLE,
  INDEX `fk_cn_profile_idx` (`profile` ASC) VISIBLE,
  CONSTRAINT `fk_cn_case`
    FOREIGN KEY (`case`)
    REFERENCES `cr_db`.`case` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_cn_profile`
    FOREIGN KEY (`profile`)
    REFERENCES `cr_db`.`profile` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `cr_db`.`case_other_crimes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cr_db`.`case_other_crimes` (
  `id` INT(11) NOT NULL,
  `crime` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_case_other_crimes_id`
    FOREIGN KEY (`id`)
    REFERENCES `cr_db`.`case` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `cr_db`.`lea_post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cr_db`.`lea_post` (
  `profile` VARCHAR(45) NOT NULL,
  `post` INT(11) NOT NULL,
  PRIMARY KEY (`profile`, `post`),
  INDEX `fk_lp_post_idx` (`post` ASC) VISIBLE,
  CONSTRAINT `fk_lp_post`
    FOREIGN KEY (`post`)
    REFERENCES `cr_db`.`post` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_lp_profile`
    FOREIGN KEY (`profile`)
    REFERENCES `cr_db`.`profile` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


INSERT INTO `cr_db`.`Escalate_level`(`id`,`level`)
VALUES (4, "Local"), (5, "State"), (6, "Federal");