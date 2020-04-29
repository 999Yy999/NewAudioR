-- MySQL Script generated by MySQL Workbench
-- 06/22/15 09:41:08
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema musiclibary
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema musiclibary
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `musicdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `musicdb` ;

-- -----------------------------------------------------
-- Table `musiclibary`.`MusicInfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `musicdb`.`musicinfo` (
  `idmusicinfo` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NULL,
  `artist` VARCHAR(200) NULL,
  `album` VARCHAR(200) NULL,
  `filedir` VARCHAR(400) NULL,
  `infodir` VARCHAR(400) NULL,
  PRIMARY KEY (`idmusicinfo`, `title`, `album`, `artist`),
  UNIQUE INDEX `idMusicInfo_UNIQUE` (`idmusicinfo` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `musiclibary`.`HashTable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `musicdb`.`hashtable` (
  `idhashtable` INT NOT NULL AUTO_INCREMENT,
  `hash` INT NOT NULL,
  `id` INT NOT NULL,
  `time` INT NOT NULL,
  PRIMARY KEY (`idhashtable`),
  INDEX `hash` (`hash` ASC)  KEY_BLOCK_SIZE=1)
ENGINE = InnoDB
ROW_FORMAT = FIXED;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
