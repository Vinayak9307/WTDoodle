-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 06, 2022 at 11:34 AM
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
-- Database: `wt_doodle`
--

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE `friends` (
  `serialNum` int(255) NOT NULL,
  `person` int(255) NOT NULL,
  `friend` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `game`
--

CREATE TABLE `game` (
  `gameId` bigint(255) NOT NULL,
  `isStarted` tinyint(1) NOT NULL,
  `winner` varchar(20) NOT NULL,
  `numOfPlayers` int(255) NOT NULL,
  `highScore` bigint(255) NOT NULL,
  `playerUsername` longtext NOT NULL,
  `playerScore` bigint(255) NOT NULL,
  `ipAddress` longtext NOT NULL,
  `port` int(255) NOT NULL,
  `Date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `gameplayed`
--

CREATE TABLE `gameplayed` (
  `serialNum` bigint(20) NOT NULL,
  `userId` int(11) NOT NULL,
  `gameId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `globalleader`
--

CREATE TABLE `globalleader` (
  `serialNum` bigint(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `score` bigint(255) NOT NULL,
  `gameId` bigint(255) NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `request`
--

CREATE TABLE `request` (
  `serialNum` int(255) NOT NULL,
  `name` int(255) NOT NULL,
  `request` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userId` int(255) NOT NULL,
  `Name` varchar(256) NOT NULL,
  `username` varchar(256) NOT NULL,
  `email` bigint(20) UNSIGNED NOT NULL,
  `Password` varchar(256) NOT NULL,
  `isOnline` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
  MODIFY `serialNum` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `game`
--
ALTER TABLE `game`
  MODIFY `gameId` bigint(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `gameplayed`
--
ALTER TABLE `gameplayed`
  MODIFY `serialNum` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `globalleader`
--
ALTER TABLE `globalleader`
  MODIFY `serialNum` bigint(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `request`
--
ALTER TABLE `request`
  MODIFY `serialNum` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `userId` int(255) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
