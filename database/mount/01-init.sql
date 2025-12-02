CREATE USER "hu-s4-lingo-dbadmin" WITH CREATEDB PASSWORD 'hu-s4-lingo-pwd';
CREATE DATABASE "hu-s4-lingo" OWNER "hu-s4-lingo-dbadmin";

CREATE USER "hu-s4-lingo-dbadmin-test" WITH CREATEDB PASSWORD 'hu-s4-lingo-pwd';
CREATE DATABASE "hu-s4-lingo-test" OWNER "hu-s4-lingo-dbadmin-test";

