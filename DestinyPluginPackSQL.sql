CREATE TABLE `rumbleList` (
  `membershipId` varchar(255) NOT NULL,
  `platform` varchar(255) NOT NULL,
  `playerName` varchar(255) NOT NULL,
  `rank` bigint(255) DEFAULT NULL,
  `elo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`membershipId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `trialsList` (
  `membershipId` varchar(255) NOT NULL,
  `platform` varchar(255) NOT NULL,
  `playerName` varchar(255) NOT NULL,
  `rank` bigint(255) DEFAULT NULL,
  `elo` bigint(20) DEFAULT NULL,
  `flawlessCount` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`membershipId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `registered` (
  `membershipId` varchar(255) NOT NULL,
  `displayName` varchar(45) NOT NULL,
  `currentToken` mediumtext NOT NULL,
  `currentTokenExpire` varchar(45) NOT NULL,
  `renewToken` mediumtext NOT NULL,
  `renewTokenExpire` varchar(45) NOT NULL,
  `platform` varchar(45) NOT NULL,
  PRIMARY KEY (`membershipId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
