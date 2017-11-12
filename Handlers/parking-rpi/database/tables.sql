create table parking_status (
	id INT UNSIGNED auto_increment primary key NOT NULL,
	name varchar(100) NOT NULL UNIQUE,
	lat DECIMAL(10,8) NOT NULL,
	lng DECIMAL(11,8) NOT NULL,
	total int UNSIGNED NOT NULL DEFAULT 0,
	four_avail int UNSIGNED NOT NULL DEFAULT 0,
	three_avail int UNSIGNED NOT NULL DEFAULT 0,
	two_avail int UNSIGNED NOT NULL DEFAULT 0,

	UNIQUE(lat,lng)
);

insert into parking_status (name,lat,lng,total) values ("kailash colony", 26.9356401, 75.9239636, 100);

insert into parking_status (name,lat,lng,total) values ("parking 1", 26.9556401, 75.9239636, 100);
insert into parking_status (name,lat,lng,total) values ("parking 2", 26.9156401, 75.9239636, 100);


create table vehicles (
	id INT UNSIGNED auto_increment primary key NOT NULL,
	veh_num varchar(12) NOT NULL UNIQUE,
	entry_time DATETIME NULL,
	name varchar(100) NOT NULL
);