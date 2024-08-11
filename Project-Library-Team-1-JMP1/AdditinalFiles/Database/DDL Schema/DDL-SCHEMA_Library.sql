-- done
create table role (
userRoleID smallint NOT NULL,
userRole varchar(20)
-- PRIMARY KEY (userRoleID)
);

-- done
create table library (
libraryID bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
LibraryName varchar(50)
);

-- done
create table access (
accessID smallint NOT NULL,
bookAccessName varchar(20)
-- PRIMARY KEY (accessID)
);

-- done
create table language (
languageID bigint NOT NULL,
languageName varchar(50)
-- PRIMARY KEY (languageID)
);

-- done
create table genre (
genreID bigint NOT NULL,
genreName varchar(50)
-- PRIMARY KEY (languageID)
);

-- done
create table author (
authorID bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
UserID bigint
);

-- done
create table user (
UserID	bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
FirstName varchar(50),
LastName varchar(50),
email varchar(50),
phoneNumber	varchar(50),
isLocked bool
);

INSERT INTO user (FirstName,LastName,email,phoneNumber,isLocked) values ('Cameran', 'Nieves', 'in.cursus@outlook.com', '(874) 671-5157', TRUE);
INSERT INTO user (FirstName,LastName,email,phoneNumber,isLocked) values ('Armand', 'Mckinney', 'sit@outlook.org', '(681) 686-2353', FALSE);
INSERT INTO user (FirstName,LastName,email,phoneNumber,isLocked) values ('Deacon', 'Harrington', 'mauris.nulla@aol.com', '(672) 931-2728', FALSE);
INSERT INTO user (FirstName,LastName,email,phoneNumber,isLocked) values ('Serina', 'Middleton', 'justo@yahoo.net', '1-124-825-0799', FALSE);
INSERT INTO user (FirstName,LastName,email,phoneNumber,isLocked) values ('Libby', 'Juarez', 'enim@icloud.net', '(499) 383-9009', FALSE);


-- ------------------------------------------------------------
-- done
create table userRole (
ID bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
UserID bigint,
UserRoleID smallint
);

-- done
create table credentials (
userID bigint,
username varchar(50) UNIQUE,
password varchar(50)
);

-- done
CREATE table userLibrary (
ID bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
LibraryID bigint,
UserID bigint
);

-- no imported data
create table bookLibrary (
ID bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
LibraryID bigint,
BookID bigint
);

-- no imported data
create table book (
BookID bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
Title varchar(50),
ISBN varchar(20),
AuthorID bigInt,
GenreID bigInt,
rating bigInt,
AccessID bigInt,
LanguageID bigInt
);