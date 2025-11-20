CREATE DATABASE IF NOT EXISTS lab4;
USE lab4;

CREATE TABLE IF NOT EXISTS Trip (
	TripNumber INT PRIMARY KEY,
	StartLocationName varchar(255) NOT NULL,
	DestinationName varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Bus (
	BusID INT PRIMARY KEY,
	Model varchar(255) NOT NULL,
	YearOfBus YEAR NOT NULL
);

CREATE TABLE IF NOT EXISTS Driver (
	DriverName varchar(255) PRIMARY KEY NOT NULL,
	DriverTelephoneNumber varchar(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS TripStop (
	StopNumber INT PRIMARY KEY NOT NULL,
	StopAddress varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS TripStopInfo (
	TripNumber INT NOT NULL,
	StopNumber INT NOT NULL,
	SequenceNumber INT NOT NULL,
	DrivingTime TIME,
	PRIMARY KEY (TripNumber, StopNumber),
	CONSTRAINT fk_tsi_trip
		FOREIGN KEY (TripNumber) REFERENCES Trip(TripNumber)
		ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_tsi_stop
		FOREIGN KEY (StopNumber) REFERENCES TripStop(StopNumber)
		ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS TripOffering (
	TripNumber INT NOT NULL,
	DateOfTrip DATE NOT NULL,
	ScheduledStartTime TIME NOT NULL,
	ScheduledArrivalTime TIME NOT NULL,
	DriverName varchar(255) NOT NULL,
	BusID INT NOT NULL,
	PRIMARY KEY (TripNumber, DateOfTrip, ScheduledStartTime),
	CONSTRAINT fk_to_trip
		FOREIGN KEY (TripNumber) REFERENCES Trip(TripNumber)
		ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_to_driver
		FOREIGN KEY (DriverName) REFERENCES Driver(DriverName)
		ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_to_bus
		FOREIGN KEY (BusID) REFERENCES Bus(BusID)
		ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ActualTripStopInfo (
	TripNumber INT NOT NULL,
	DateOfTrip DATE NOT NULL,
	ScheduledStartTime TIME NOT NULL,
	StopNumber INT NOT NULL,
	ScheduledArrivalTime TIME NOT NULL,
	ActualStartTime TIME NOT NULL,
	ActualArrivalTime TIME NOT NULL,
	NumberOfPassengerIn INT NOT NULL,
	NumberOfPassengerOut INT NOT NULL,
	PRIMARY KEY (TripNumber, DateOfTrip, ScheduledStartTime, StopNumber),
	CONSTRAINT fk_atsi_offering
		FOREIGN KEY (TripNumber, DateOfTrip, ScheduledStartTime)
		REFERENCES TripOffering (TripNumber, DateOfTrip, ScheduledStartTime)
		ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_atsi_stop
		FOREIGN KEY (StopNumber)
		REFERENCES TripStop (StopNumber)
		ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_atsi_tripstop
		FOREIGN KEY (TripNumber, StopNumber)
		REFERENCES TripStopInfo (TripNumber, StopNumber)
		ON DELETE RESTRICT ON UPDATE CASCADE
);
