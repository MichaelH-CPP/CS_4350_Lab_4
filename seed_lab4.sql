USE lab4;

-- 1. Clear existing data in safe order
DELETE FROM ActualTripStopInfo;
DELETE FROM TripOffering;
DELETE FROM TripStopInfo;
DELETE FROM TripStop;
DELETE FROM Driver;
DELETE FROM Bus;
DELETE FROM Trip;

-- 2. Insert Trips
INSERT INTO Trip (TripNumber, StartLocationName, DestinationName) VALUES
  (1, 'Campus', 'Downtown'),
  (2, 'Campus', 'Airport'),
  (3, 'Downtown', 'Campus');

-- 3. Insert Buses
INSERT INTO Bus (BusID, Model, YearOfBus) VALUES
  (10, 'Volvo 9700', 2020),
  (11, 'MCI D4505', 2018);

-- 4. Insert Drivers
INSERT INTO Driver (DriverName, DriverTelephoneNumber) VALUES
  ('Alice Smith', '555-0100'),
  ('Bob Lee', '555-0101'),
  ('Cara Diaz', '555-0102');

-- 5. Insert Stops
INSERT INTO TripStop (StopNumber, StopAddress) VALUES
  (100, 'Campus Center'),
  (101, 'Main St & 1st'),
  (102, 'Downtown Station'),
  (103, 'Airport Terminal');

-- 6. Insert TripStopInfo (Trip -> Stops -> order)
-- Trip 1: Campus -> Main St -> Downtown
INSERT INTO TripStopInfo (TripNumber, StopNumber, SequenceNumber, DrivingTime) VALUES
  (1, 100, 1, '00:15:00'),
  (1, 101, 2, '00:20:00'),
  (1, 102, 3, NULL);

-- Trip 2: Campus -> Airport
INSERT INTO TripStopInfo (TripNumber, StopNumber, SequenceNumber, DrivingTime) VALUES
  (2, 100, 1, '00:35:00'),
  (2, 103, 2, NULL);

-- Trip 3: Downtown -> Main St -> Campus
INSERT INTO TripStopInfo (TripNumber, StopNumber, SequenceNumber, DrivingTime) VALUES
  (3, 102, 1, '00:10:00'),
  (3, 101, 2, '00:15:00'),
  (3, 100, 3, NULL);

-- 7. Insert TripOfferings (actual scheduled runs)
INSERT INTO TripOffering
  (TripNumber, DateOfTrip, ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID)
VALUES
  (1, '2025-11-21', '08:00:00', '08:40:00', 'Alice Smith', 10),
  (1, '2025-11-21', '18:00:00', '18:40:00', 'Bob Lee',     11),
  (2, '2025-11-21', '09:00:00', '09:35:00', 'Cara Diaz',   10),
  (3, '2025-11-22', '07:30:00', '07:55:00', 'Alice Smith', 11);

-- 8. Insert ActualTripStopInfo (real arrival data)
-- Trip 1, morning
INSERT INTO ActualTripStopInfo
  (TripNumber, DateOfTrip, ScheduledStartTime, StopNumber,
   ScheduledArrivalTime, ActualStartTime, ActualArrivalTime,
   NumberOfPassengerIn, NumberOfPassengerOut)
VALUES
  (1, '2025-11-21', '08:00:00', 100, '08:00:00', '08:00:00', '08:00:00', 5, 0),
  (1, '2025-11-21', '08:00:00', 101, '08:15:00', '08:13:00', '08:14:00', 3, 1),
  (1, '2025-11-21', '08:00:00', 102, '08:40:00', '08:38:00', '08:42:00', 0, 7),

  -- Trip 2, morning
  (2, '2025-11-21', '09:00:00', 100, '09:00:00', '09:01:00', '09:01:00', 10, 0),
  (2, '2025-11-21', '09:00:00', 103, '09:35:00', '09:33:00', '09:34:00', 0, 10),

  -- Trip 3, early
  (3, '2025-11-22', '07:30:00', 102, '07:30:00', '07:29:00', '07:30:00', 4, 0),
  (3, '2025-11-22', '07:30:00', 101, '07:40:00', '07:39:00', '07:40:00', 2, 1),
  (3, '2025-11-22', '07:30:00', 100, '07:55:00', '07:56:00', '07:57:00', 0, 5);
