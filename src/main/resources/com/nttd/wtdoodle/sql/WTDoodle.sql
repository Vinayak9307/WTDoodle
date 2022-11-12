-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 11, 2022 at 08:42 PM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `WTDoodle`
--

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE `friends` (
  `serialNum` int(255) NOT NULL,
  `person` varchar(255) NOT NULL,
  `friend` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `friends`
--

INSERT INTO `friends` (`serialNum`, `person`, `friend`) VALUES
(7, 'codor07', 'vinayak9307'),
(8, 'vinayak9307', 'codor07'),
(9, 'socket', 'codor07'),
(10, 'codor07', 'socket'),
(11, 'vinayak9307', 'socket'),
(12, 'socket', 'vinayak9307');

-- --------------------------------------------------------

--
-- Table structure for table `game`
--

CREATE TABLE `game` (
  `gameId` bigint(255) NOT NULL,
  `winner` varchar(20) NOT NULL,
  `numOfPlayers` int(255) NOT NULL,
  `highScore` bigint(255) NOT NULL,
  `playerUsername` longtext NOT NULL,
  `playerScore` varchar(255) NOT NULL,
  `Date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `game`
--

INSERT INTO `game` (`gameId`, `winner`, `numOfPlayers`, `highScore`, `playerUsername`, `playerScore`, `Date`) VALUES
(1, 'prashant', 20, 200, 'prashant;vinayak', '200,400', '2022-11-09'),
(2, 'vinayak', 30, 260, 'prashant;vinayak', '260,280', '2022-11-09'),
(3, 'prajdf', 25, 11, 'prashant;vinayak', '260,280', '2022-11-09');

-- --------------------------------------------------------

--
-- Table structure for table `gameplayed`
--

CREATE TABLE `gameplayed` (
  `serialNum` bigint(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  `gameId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `gameplayed`
--

INSERT INTO `gameplayed` (`serialNum`, `username`, `gameId`) VALUES
(1, 'vinayak9307', 1),
(2, 'vinayak9307', 2),
(9, 'vinayak9307', 3);

-- --------------------------------------------------------

--
-- Table structure for table `globalleader`
--

CREATE TABLE `globalleader` (
  `serialNum` bigint(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `totalScore` bigint(255) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `globalleader`
--

INSERT INTO `globalleader` (`serialNum`, `username`, `totalScore`, `date`) VALUES
(1, 'vinayak9307', 1000, '2022-11-09'),
(2, 'prashant', 2121, '2022-11-09'),
(3, 'socket', 20000, '2022-11-09');

-- --------------------------------------------------------

--
-- Table structure for table `request`
--

CREATE TABLE `request` (
  `serialNum` int(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `request` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userId` int(255) NOT NULL,
  `name` varchar(256) NOT NULL,
  `username` varchar(256) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(256) NOT NULL,
  `totalGamesPlayed` int(255) NOT NULL DEFAULT 0,
  `totalScore` int(255) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userId`, `name`, `username`, `email`, `password`, `totalGamesPlayed`, `totalScore`) VALUES
(1, 'Vinayak kushwaha', 'vinayak9307', 'kushwahavinayak286@gmail.com', 'kush0987', 0, 0),
(2, 'Prashant', 'codor07', 'prashant@gmail.com', 'prashant', 0, 0),
(3, 'Saket ', 'socket', 'saket9max@gmail.com', '1234567', 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `friends`
--
ALTER TABLE `friends`
  ADD PRIMARY KEY (`serialNum`);

--
-- Indexes for table `game`
--
ALTER TABLE `game`
  ADD PRIMARY KEY (`gameId`);

--
-- Indexes for table `gameplayed`
--
ALTER TABLE `gameplayed`
  ADD PRIMARY KEY (`serialNum`);

--
-- Indexes for table `globalleader`
--
ALTER TABLE `globalleader`
  ADD PRIMARY KEY (`serialNum`);

--
-- Indexes for table `request`
--
ALTER TABLE `request`
  ADD PRIMARY KEY (`serialNum`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `friends`
--
ALTER TABLE `friends`
  MODIFY `serialNum` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `game`
--
ALTER TABLE `game`
  MODIFY `gameId` bigint(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `gameplayed`
--
ALTER TABLE `gameplayed`
  MODIFY `serialNum` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `globalleader`
--
ALTER TABLE `globalleader`
  MODIFY `serialNum` bigint(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `request`
--
ALTER TABLE `request`
  MODIFY `serialNum` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `userId` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;