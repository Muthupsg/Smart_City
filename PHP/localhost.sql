-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 18, 2019 at 12:24 PM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id10182359_smartcity`
--
CREATE DATABASE IF NOT EXISTS `id10182359_smartcity` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `id10182359_smartcity`;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `department` enum('water','sanitory','electricity','road','all') COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`, `department`) VALUES
(1, 'admin123', 'pass', 'water'),
(2, 'admin', 'admin', 'all');

-- --------------------------------------------------------

--
-- Table structure for table `report`
--

CREATE TABLE `report` (
  `id` int(11) NOT NULL,
  `fromuser` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `problem_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `problem_category` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `descrip` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  `latitude` decimal(11,7) NOT NULL,
  `longitude` decimal(11,7) NOT NULL,
  `address` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `status` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Complaint Received',
  `image` mediumblob DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `report`
--

INSERT INTO `report` (`id`, `fromuser`, `problem_type`, `problem_category`, `descrip`, `latitude`, `longitude`, `address`, `status`, `image`, `date`) VALUES
INSERT INTO `report` (`id`, `fromuser`, `problem_type`, `problem_category`, `descrip`, `latitude`, `longitude`, `address`, `status`, `image`, `date`) VALUES
(2, 'kirubha2020@gmail.com', 'Water', 'Leakage of Water', 'It\'s been 10 days since last water supply to our area.', 11.0019562, 77.0228016, 'Venkata Lakshmi Nagar, Singanallur, Coimbatore 641005', 'Complaint Received', NULL, '2019-07-24 05:48:11'),
(6, 'mlogesh2996@gmail.com', 'Water', 'Contamination', 'yngg', 12.9989133, 77.6258523, 'ITC Infotech India Ltd., No.18, Gate, 1 & 2, Dodda Banaswadi Main Rd, Harischandra layout, Jeevanhalli, Maruthi Sevanagar, Bengaluru, Karnataka 560005', 'Complaint Received', NULL, '2019-07-26 08:25:07'),
(7, 'mlogesh2996@gmail.com', 'Water', 'Contamination', 'gshdjfj', 11.0244528, 77.0043352, 'K Block, Avinashi Rd, Peelamedu, Coimbatore, Tamil Nadu 641004, India\nCoimbatore - 641004\nTamil Nadu\nIndia', 'Complaint Received', NULL, '2019-08-14 08:39:10');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `report`
--
ALTER TABLE `report`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;