-- DOUBLECheck Demo: Database storage for 'demo' information
CREATE DATABASE DCDemoDB;

CREATE TABLE `DCDemoDB`.`User` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `IDFR` VARCHAR(256) NOT NULL,
  `Password` VARCHAR(1024) NULL,
  `Name` VARCHAR(256) NULL,
  `Lastname` VARCHAR(256) NULL,
  `Email` VARCHAR(256) NULL,
  `PhoneNumber` VARCHAR(32) NULL,
  `Photo` MEDIUMBLOB NULL,
  `LCStatus` VARCHAR(32) NOT NULL,
  `Timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `IDFR_UNIQUE` (`IDFR` ASC) );

CREATE TABLE `DCDemoDB`.`Role` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `IDFR` VARCHAR(256) NOT NULL,
  `Role` VARCHAR(32) NOT NULL,
  `UserId` INT NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Role_UNIQUE` (`IDFR` ASC, `Role` ASC),
  INDEX `Role_User_idx` (`UserId` ASC),
  CONSTRAINT `Role_User`
    FOREIGN KEY (`UserId`)
    REFERENCES `DCDemoDB`.`User` (`Id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
