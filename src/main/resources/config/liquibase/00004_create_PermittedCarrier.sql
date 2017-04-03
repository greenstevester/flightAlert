SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE TABLE IF NOT EXISTS `permitted_carrier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `carrier_code` varchar(255) NOT NULL,
  `flight_slice_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_permittedcarrier_flightslice_id` (`flight_slice_id`),
  CONSTRAINT `fk_permittedcarrier_flightslice_id` FOREIGN KEY (`flight_slice_id`) REFERENCES `flight_slice` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

INSERT INTO `permitted_carrier` (`id`, `carrier_code`, `flight_slice_id`, `user_id`) VALUES
(1,'LX',1,3),
(2,'LX',2,3),
(3,'LX',3,3),
(4,'CX',3,3),
(5,'JL',3,3),
(6,'QF',3,3),
(7,'LH',3,3),
(8,'SQ',3,3),
(9,'TG',3,3),
(10,'VS',3,3),
(11,'LX',4,3),
(12,'CX',4,3),
(13,'JL',4,3),
(14,'QF',4,3),
(15,'LH',4,3),
(16,'SQ',4,3),
(17,'TG',4,3),
(18,'VS',4,3);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
