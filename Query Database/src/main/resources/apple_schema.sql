use AppleDbAi;
DROP TABLE IF EXISTS `apple`;
create table apple ( apple_id int not null auto_increment,
apple_name varchar(255) default null,
apple_taste varchar(255) default null,
primary key (apple_id));