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


-- More Buses
INSERT INTO Bus (BusID, Model, YearOfBus) VALUES
  (12, 'Volvo 9700', 2019),
  (13, 'New Flyer Xcelsior', 2021),
  (14, 'MCI D4505', 2017);

-- More Drivers
INSERT INTO Driver (DriverName, DriverTelephoneNumber) VALUES
  ('Dan Fox',   '555-0103'),
  ('Eva Long',  '555-0104'),
  ('Sam Wu',    '555-0105'),
  ('Lena Park', '555-0106');

-- More Stops
INSERT INTO TripStop (StopNumber, StopAddress) VALUES
  (104, 'Mall Transit Center'),
  (105, 'Stadium Main Gate'),
  (106, 'City Hall'),
  (107, 'Tech Park');

-- More Trips (some duplicate routes with new TripNumbers)
INSERT INTO Trip (TripNumber, StartLocationName, DestinationName) VALUES
  (4,  'Campus',   'Downtown'),
  (5,  'Campus',   'Downtown'),
  (6,  'Campus',   'Airport'),
  (7,  'Campus',   'Airport'),
  (8,  'Downtown', 'Campus'),
  (9,  'Downtown', 'Campus'),
  (10, 'Campus',   'Mall'),
  (11, 'Mall',     'Campus'),
  (12, 'Campus',   'Stadium'),
  (13, 'Stadium',  'Campus');

-- TripStopInfo for the new trips
-- Trips 4 & 5 follow the same pattern as Trip 1
INSERT INTO TripStopInfo (TripNumber, StopNumber, SequenceNumber, DrivingTime) VALUES
  (4, 100, 1, '00:15:00'),
  (4, 101, 2, '00:20:00'),
  (4, 102, 3, NULL),
  (5, 100, 1, '00:15:00'),
  (5, 101, 2, '00:20:00'),
  (5, 102, 3, NULL);

-- Trips 6 & 7 follow same pattern as Trip 2
INSERT INTO TripStopInfo VALUES
  (6, 100, 1, '00:35:00'),
  (6, 103, 2, NULL),
  (7, 100, 1, '00:35:00'),
  (7, 103, 2, NULL);

-- Trips 8 & 9 follow same pattern as Trip 3
INSERT INTO TripStopInfo VALUES
  (8, 102, 1, '00:10:00'),
  (8, 101, 2, '00:15:00'),
  (8, 100, 3, NULL),
  (9, 102, 1, '00:10:00'),
  (9, 101, 2, '00:15:00'),
  (9, 100, 3, NULL);

-- Trip 10: Campus -> Mall
INSERT INTO TripStopInfo VALUES
  (10, 100, 1, '00:10:00'),
  (10, 104, 2, NULL);

-- Trip 11: Mall -> Campus
INSERT INTO TripStopInfo VALUES
  (11, 104, 1, '00:10:00'),
  (11, 100, 2, NULL);

-- Trip 12: Campus -> Stadium
INSERT INTO TripStopInfo VALUES
  (12, 100, 1, '00:10:00'),
  (12, 106, 2, '00:15:00'),
  (12, 105, 3, NULL);

-- Trip 13: Stadium -> Campus
INSERT INTO TripStopInfo VALUES
  (13, 105, 1, '00:08:00'),
  (13, 107, 2, '00:12:00'),
  (13, 100, 3, NULL);

-- MORE TRIP OFFERINGS (same dates, same routes, different tripnumbers)
INSERT INTO TripOffering
  (TripNumber, DateOfTrip, ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID)
VALUES
  (4,  '2025-11-23', '08:00:00', '08:40:00', 'Alice Smith', 10),
  (5,  '2025-11-23', '09:00:00', '09:40:00', 'Bob Lee',     11),

  (6,  '2025-11-24', '07:30:00', '08:05:00', 'Cara Diaz',   10),
  (7,  '2025-11-24', '12:00:00', '12:35:00', 'Alice Smith', 12),

  (8,  '2025-11-25', '18:00:00', '18:25:00', 'Dan Fox',     12),
  (9,  '2025-11-26', '19:00:00', '19:25:00', 'Eva Long',    13),

  (10, '2025-11-27', '10:00:00', '10:20:00', 'Sam Wu',      12),
  (11, '2025-11-27', '10:30:00', '10:50:00', 'Sam Wu',      12),

  (12, '2025-11-28', '16:00:00', '16:35:00', 'Lena Park',   13),
  (13, '2025-11-28', '17:00:00', '17:35:00', 'Lena Park',   13);

-- EXTRA ActualTripStopInfo (only some offerings)
INSERT INTO ActualTripStopInfo
  (TripNumber, DateOfTrip, ScheduledStartTime, StopNumber,
   ScheduledArrivalTime, ActualStartTime, ActualArrivalTime,
   NumberOfPassengerIn, NumberOfPassengerOut)
VALUES
  (4, '2025-11-23', '08:00:00', 100, '08:00:00', '08:00:00', '08:01:00', 7, 0),
  (4, '2025-11-23', '08:00:00', 101, '08:15:00', '08:14:00', '08:15:00', 3, 1),
  (4, '2025-11-23', '08:00:00', 102, '08:40:00', '08:39:00', '08:41:00', 0, 9),

  (10,'2025-11-27','10:00:00', 100,'10:00:00','10:00:00','10:00:00', 5, 0),
  (10,'2025-11-27','10:00:00', 104,'10:20:00','10:19:00','10:20:00', 0, 5);

