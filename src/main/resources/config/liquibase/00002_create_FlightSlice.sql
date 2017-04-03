SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE TABLE IF NOT EXISTS `flight_slice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `origin` varchar(3) NOT NULL,
  `destination` varchar(3) NOT NULL,
  `preferred_cabin` varchar(255) DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `max_stops` int(11) DEFAULT NULL,
  `max_connection_duration_in_minutes` int(11) DEFAULT NULL,
  `max_price_in_chf` decimal(10,2) DEFAULT NULL,
  `refundable` bit(1) DEFAULT NULL,
  `flight_request_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_flightslice_flightrequest_id` (`flight_request_id`),
  CONSTRAINT `fk_flightslice_flightrequest_id` FOREIGN KEY (`flight_request_id`) REFERENCES `flight_request` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

INSERT INTO `flight_slice` (`id`, `origin`, `destination`, `preferred_cabin`, `date`, `max_stops`, `max_connection_duration_in_minutes`, `max_price_in_chf`, `refundable`, `flight_request_id`, `user_id`) VALUES
(1,'MXP','SFO','BUSINESS','2016-06-08 08:00:00',3,180,5000,NULL,1,3),
(2,'SFO','MXP','BUSINESS','2016-06-21 18:00:00',3,180,5000,NULL,1,3),
(3,'MXP','BNE','BUSINESS','2016-12-19 23:00:00',3,180,5000,NULL,2,3),
(4,'BNE','MXP','BUSINESS','2017-01-12 23:00:00',3,180,5000,NULL,2,3);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
