-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema proyecto2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema proyecto2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `proyecto2` DEFAULT CHARACTER SET utf8 ;
USE `proyecto2` ;

-- -----------------------------------------------------
-- Table `proyecto2`.`Medico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto2`.`Medico` (
  `nombre` VARCHAR(45) NULL,
  `especialidad` VARCHAR(45) NULL,
  `id` VARCHAR(10) NOT NULL,
  `clave` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto2`.`Paciente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto2`.`Paciente` (
  `id` VARCHAR(10) NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `fechaNacimiento` DATE NULL,
  `telefono` VARCHAR(10) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto2`.`Receta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto2`.`Receta` (
 `numero` INT NOT NULL AUTO_INCREMENT,
  `fechaConfeccion` DATE NULL,
  `fechaRetiro` DATE NULL,
  `estado` VARCHAR(20) NULL,
  `medico` VARCHAR(10) NOT NULL,
  `paciente` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`numero`),
  INDEX `fk_Receta_Medico1_idx` (`medico` ASC) VISIBLE,
  INDEX `fk_Receta_Paciente1_idx` (`paciente` ASC) VISIBLE,
  CONSTRAINT `fk_Receta_Medico1`
    FOREIGN KEY (`medico`)
    REFERENCES `proyecto2`.`Medico` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Receta_Paciente1`
    FOREIGN KEY (`paciente`)
    REFERENCES `proyecto2`.`Paciente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto2`.`Medicamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto2`.`Medicamento` (
  `codigo` INT NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `presentacion` VARCHAR(45) NULL,
  PRIMARY KEY (`codigo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto2`.`Linea`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Linea` (
 `numero` INT NOT NULL AUTO_INCREMENT,
 `cantidad` INT NULL,
 `indicaciones` VARCHAR(100) NULL,
    `duracionDias` INT NULL,
    `medicamento` VARCHAR(10) NOT NULL,
    `receta` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`numero`),
    INDEX `fk_Linea_Medicamento1_idx` (`medicamento` ASC) VISIBLE,
    INDEX `fk_Linea_Receta1_idx` (`receta` ASC) VISIBLE,
    CONSTRAINT `fk_Linea_Medicamento1`
    FOREIGN KEY (`medicamento`)
    REFERENCES `proyecto2`.`Medicamento` (`codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_Linea_Receta1`
    FOREIGN KEY (`receta`)
    REFERENCES `proyecto2`.`Receta` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    ) ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `proyecto2`.`Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto2`.`Usuario` (
  `id` VARCHAR(10) NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `clave` VARCHAR(45) NULL,
    rol ENUM('admin', 'medico', 'farmaceuta') NOT NULL
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto2`.`Farmaceuta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto2`.`Farmaceuta` (
  `id` VARCHAR(10) NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `clave` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
