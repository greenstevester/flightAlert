SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DELETE FROM flight_request where id in (2);

DELETE from flight_slice where id in (3, 4);

UPDATE flight_slice set `date` = '2016-12-14 08:00:00' where id in (1,5,7,9);
UPDATE flight_slice set `date` = '2017-01-13 23:00:00' where id in (2,6,8,10);

DELETE from `passenger` where flight_slice_id in (3, 4);

DELETE from `permitted_carrier`;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
