SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE TABLE IF NOT EXISTS `passenger` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `passenger_type` varchar(255) NOT NULL,
  `flight_slice_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_passenger_flightslice_id` (`flight_slice_id`),
  CONSTRAINT `fk_passenger_flightslice_id` FOREIGN KEY (`flight_slice_id`) REFERENCES `flight_slice` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

INSERT INTO `passenger` (`id`, `passenger_type`, `flight_slice_id`, `user_id`) VALUES
(1,'ADULT',1,3),
(2,'ADULT',1,3),
(3,'ADULT',2,3),
(4,'ADULT',2,3),
(5,'ADULT',3,3),
(6,'ADULT',3,3),
(7,'ADULT',4,3),
(8,'ADULT',4,3);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
