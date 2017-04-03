SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

UPDATE `flight_request` set valid_to_date = '2016-12-14 23:00:00' where id = 1;

INSERT INTO `flight_request` (`id`, `valid_from_date`, `valid_to_date`, `user_id`) VALUES
(3,'2016-02-09 23:00:00','2016-12-14 23:00:00',3),
(4,'2016-02-09 23:00:00','2016-12-14 23:00:00',3),
(5,'2016-02-09 23:00:00','2016-12-14 23:00:00',3);

UPDATE `flight_slice` set destination = 'MEL', `date` = '2016-12-19 08:00:00' where id = 1;
UPDATE `flight_slice` set origin = 'MEL', `date` = '2017-01-12 23:00:00' where id = 2;

UPDATE `flight_slice` set destination = 'MEL', `date` = '2016-12-21 08:00:00' where id = 3;
UPDATE `flight_slice` set origin = 'MEL', `date` = '2017-01-14 23:00:00' where id = 4;

INSERT INTO `flight_slice` (`id`, `origin`, `destination`, `preferred_cabin`, `date`, `max_stops`, `max_connection_duration_in_minutes`, `max_price_in_chf`, `refundable`, `flight_request_id`, `user_id`) VALUES
(5,'FRA','MEL','BUSINESS','2016-12-19 08:00:00',3,180,5000,NULL,3,3),
(6,'MEL','FRA','BUSINESS','2017-01-12 23:00:00',3,180,5000,NULL,3,3),
(7,'FRA','SYD','BUSINESS','2016-12-21 08:00:00',3,180,5000,NULL,4,3),
(8,'SYD','FRA','BUSINESS','2017-01-14 23:00:00',3,180,5000,NULL,4,3),
(9,'MXP','SYD','BUSINESS','2016-12-21 08:00:00',3,180,5000,NULL,5,3),
(10,'SYD','MXP','BUSINESS','2017-01-14 23:00:00',3,180,5000,NULL,5,3);

INSERT INTO `passenger` (`id`, `passenger_type`, `flight_slice_id`, `user_id`) VALUES
(9,'ADULT',5,3),
(10,'ADULT',5,3),
(11,'ADULT',6,3),
(12,'ADULT',6,3),
(13,'ADULT',7,3),
(14,'ADULT',7,3),
(15,'ADULT',8,3),
(16,'ADULT',8,3),
(17,'ADULT',9,3),
(18,'ADULT',9,3),
(19,'ADULT',10,3),
(20,'ADULT',10,3);

DELETE FROM permitted_carrier WHERE user_id = 3;

INSERT INTO `permitted_carrier` (`id`, `carrier_code`, `flight_slice_id`, `user_id`) VALUES
(1,'LX',1,3),
(2,'LX',1,3),
(3,'CX',1,3),
(4,'JL',1,3),
(5,'QF',1,3),
(6,'LH',1,3),
(7,'SQ',1,3),
(8,'TG',1,3),
(9,'VS',1,3),
(10,'LX',2,3),
(11,'LX',2,3),
(12,'CX',2,3),
(13,'JL',2,3),
(14,'QF',2,3),
(15,'LH',2,3),
(16,'SQ',2,3),
(17,'TG',2,3),
(18,'VS',2,3),
(19,'LX',3,3),
(20,'CX',3,3),
(21,'JL',3,3),
(22,'QF',3,3),
(23,'LH',3,3),
(24,'SQ',3,3),
(25,'TG',3,3),
(26,'VS',3,3),
(27,'LX',4,3),
(28,'CX',4,3),
(29,'JL',4,3),
(30,'QF',4,3),
(31,'LH',4,3),
(32,'SQ',4,3),
(33,'TG',4,3),
(34,'VS',4,3);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
