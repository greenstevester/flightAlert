SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

INSERT INTO `flight_request` (`id`, `valid_from_date`, `valid_to_date`, `user_id`) VALUES
(6,'2016-02-09 23:00:00','2016-12-14 23:00:00',3),
(7,'2016-02-09 23:00:00','2016-12-14 23:00:00',3),
(8,'2016-02-09 23:00:00','2016-12-14 23:00:00',3);

INSERT INTO `flight_slice` (`id`, `origin`, `destination`, `preferred_cabin`, `date`, `max_stops`, `max_connection_duration_in_minutes`, `max_price_in_chf`, `refundable`, `flight_request_id`, `user_id`) VALUES
(11,'ZRH','ADL','BUSINESS','2016-12-14 08:00:00',3,240,6000,NULL,6,3),
(12,'ADL','ZRH','BUSINESS','2017-01-13 23:00:00',3,240,6000,NULL,6,3),
(13,'ZRH','SYD','BUSINESS','2016-12-14 08:00:00',3,240,6000,NULL,7,3),
(14,'SYD','ZRH','BUSINESS','2017-01-13 23:00:00',3,240,6000,NULL,7,3),
(15,'ZRH','MEL','BUSINESS','2016-12-14 08:00:00',3,240,6000,NULL,8,3),
(16,'MEL','ZRH','BUSINESS','2017-01-13 23:00:00',3,240,6000,NULL,8,3);

INSERT INTO `passenger` (`id`, `passenger_type`, `flight_slice_id`, `user_id`) VALUES
(21,'ADULT',11,3),
(22,'ADULT',11,3),
(23,'ADULT',12,3),
(24,'ADULT',12,3),
(25,'ADULT',13,3),
(26,'ADULT',13,3),
(27,'ADULT',14,3),
(28,'ADULT',14,3),
(29,'ADULT',15,3),
(30,'ADULT',15,3),
(31,'ADULT',16,3),
(32,'ADULT',16,3);

UPDATE flight_slice set `max_connection_duration_in_minutes` = 240, `max_price_in_chf` = 6000 where `id` in (1,2,5,6,7,8,9,10,11,12,13,14,15);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
