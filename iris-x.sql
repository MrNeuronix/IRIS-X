-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Окт 22 2012 г., 12:45
-- Версия сервера: 5.5.24
-- Версия PHP: 5.3.10-1ubuntu3.4

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `iris-x`
--

-- --------------------------------------------------------

--
-- Структура таблицы `config`
--

CREATE TABLE IF NOT EXISTS `config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `param` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Дамп данных таблицы `config`
--

INSERT INTO `config` (`id`, `name`, `param`) VALUES
(1, 'version', '0.0.7-dev'),
(2, 'recordStreams', '6'),
(3, 'recordDuration', '5'),
(5, 'systemName', 'система');

-- --------------------------------------------------------

--
-- Структура таблицы `log`
--

CREATE TABLE IF NOT EXISTS `log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `message` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `modules`
--

CREATE TABLE IF NOT EXISTS `modules` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `command` varchar(255) NOT NULL,
  `param` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Дамп данных таблицы `modules`
--

INSERT INTO `modules` (`id`, `name`, `command`, `param`, `enabled`) VALUES
(1, 'SwitchControl', 'вкл', 'enable', 1),
(2, 'SwitchControl', 'выкл', 'disable', 1);

-- --------------------------------------------------------

--
-- Структура таблицы `scheduler`
--

CREATE TABLE IF NOT EXISTS `scheduler` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `class` varchar(255) NOT NULL COMMENT 'Имя класса для выполнения',
  `command` varchar(255) NOT NULL,
  `type` tinyint(4) NOT NULL COMMENT '1- запуск раз в n минут / 2 - запуск однократно / 3 - запуск до даты period',
  `validto` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'Дата выполнять до которой',
  `interval` varchar(255) NOT NULL COMMENT 'Интервал между запусками',
  `enabled` int(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Дамп данных таблицы `scheduler`
--

INSERT INTO `scheduler` (`id`, `date`, `class`, `command`, `type`, `validto`, `interval`, `enabled`) VALUES
(2, '2012-10-23 09:10:00', 'Say', 'Нужно собираться на работу!', 1, '0000-00-00 00:00:00', '0 10 9 ? * MON,TUE,WED,THU,FRI *', 1),
(4, '2012-10-23 08:00:03', 'Say', 'Доброе утро!', 1, '0000-00-00 00:00:00', '0 0 8 ? * MON,TUE,WED,THU,FRI *', 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
