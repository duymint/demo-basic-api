START TRANSACTION;

SET SQL_SAFE_UPDATES = 0;

alter table `student` add column `is_deleted` boolean default true;

SET SQL_SAFE_UPDATES = 1;

COMMIT;