#ISD Práctica 1 - LetsMalus
#Creacion de tablas
#Contamos con tan solo dos tablas, Oferta y Reserva.

---------drop the (data)base------------
DROP TABLE offer;
DROP TABLE reserve;

-----------------offer------------------
CREATE TABLE offer(
	offer_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	offer_name VARCHAR(100) NOT NULL,
	offer_description VARCHAR(200) NOT NULL,
	offer_price FLOAT(5,2) NOT NULL,
	offer_reduced_price FLOAT(5,2) NOT NULL,
	offer_reserved INT DEFAULT 0,
	offer_claimed INT DEFAULT 0,
	offer_state VARCHAR(10) NOT NULL DEFAULT 'CREATED',
	offer_max_reserves INT DEFAULT NULL,
	offer_ini_date_reserve DATETIME NOT NULL,
	offer_end_date_reserve DATETIME NOT NULL,
	offer_end_date_claim DATETIME NOT NULL
) ENGINE = InnoDB;


---------------reserve------------------
CREATE TABLE reserve(
	reserve_id BIGINT NOT NULL AUTO_INCREMENT,
	reserve_offer_id BIGINT NOT NULL REFERENCES offer(offer_id) ON DELETE CASCADE ON UPDATE CASCADE,
	reserve_user_mail VARCHAR(50) NOT NULL,
	reserve_user_creditcard VARCHAR(25) NOT NULL,
	reserve_state VARCHAR(10) NOT NULL DEFAULT 'PENDANT',
	reserve_date DATETIME NOT NULL,

	CONSTRAINT ReservePK PRIMARY KEY(reserve_id),
	CONSTRAINT ReserveOfferIdFK FOREIGN KEY(reserve_offer_id)
        REFERENCES offer(offer_id) ON DELETE CASCADE 
) ENGINE = InnoDB;

-------------indexes--------------------
#Definimos los indices para Id de Oferta, Fechas de la Oferta y estado de la reserva.
CREATE INDEX offer_dates_index ON offer(offer_ini_date_reserve, offer_end_date_reserve);