-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 27 Lut 2023, 08:59
-- Wersja serwera: 10.4.27-MariaDB
-- Wersja PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `library`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `book`
--

CREATE TABLE `book` (
  `book_id` int(11) NOT NULL,
  `title` varchar(30) NOT NULL,
  `author` varchar(30) NOT NULL,
  `isbn` bigint(20) NOT NULL,
  `category` varchar(40) NOT NULL,
  `rent` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `book`
--

INSERT INTO `book` (`book_id`, `title`, `author`, `isbn`, `category`, `rent`) VALUES
(1, 'Iluzionista', 'Remigiusz Mroz', 9788380759183, 'CRIME', 1),
(3, 'Mamba mentality: How I play', 'Kobe Bryant', 9788381295185, 'BIOGRAPHY', 0),
(4, 'Miecz przeznaczenia. Wiedzmin', 'Andrzej Sapkowski', 9788375780642, 'FANTASY', 0),
(5, 'Darth Bane: Dynasty of Evil', 'Karpyshyn Drew', 9788324136551, 'SCIENCE_FICTION', 1),
(6, 'To', 'Stephen King', 9788381257022, 'THRILLER', 0),
(8, 'Czas Pogardy. Wiedzmin', 'Andrzej Sapkowski', 9788375780666, 'FANTASY', 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `reservation`
--

CREATE TABLE `reservation` (
  `reservation_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `title` varchar(30) NOT NULL,
  `name` varchar(20) NOT NULL,
  `surname` varchar(20) NOT NULL,
  `out_date` varchar(40) NOT NULL,
  `due_date` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `reservation`
--

INSERT INTO `reservation` (`reservation_id`, `book_id`, `title`, `name`, `surname`, `out_date`, `due_date`) VALUES
(1, 1, 'Iluzionista', 'Janusz', 'Nowak', '2023-02-18 13:13:40', '2023-03-04 13:13:40'),
(6, 5, 'Darth Bane: Dynasty of Evil', 'Janusz', 'Nowak', '2023-02-18 15:03:32', '2023-01-04 15:03:32'),
(20, 8, 'Czas Pogardy. Wiedzmin', 'Ewa', 'Rusin', '2023-02-27 08:54:08', '2023-03-13 08:54:08');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `login` varchar(20) NOT NULL,
  `password` varchar(40) NOT NULL,
  `role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `user`
--

INSERT INTO `user` (`user_id`, `login`, `password`, `role`) VALUES
(1, 'janusz', '08e1908dbe5984177cb00d96632fb163', 'USER'),
(2, 'admin', '6fa20fd3308d40fe225ef2daa492d0b0', 'ADMIN');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`book_id`);

--
-- Indeksy dla tabeli `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`reservation_id`),
  ADD KEY `book_id` (`book_id`);

--
-- Indeksy dla tabeli `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `book`
--
ALTER TABLE `book`
  MODIFY `book_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT dla tabeli `reservation`
--
ALTER TABLE `reservation`
  MODIFY `reservation_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT dla tabeli `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `bookId` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`) ON DELETE NO ACTION ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
