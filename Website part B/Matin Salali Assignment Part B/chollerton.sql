
# *************************************************************************
# *** Create the table structure for the table 'CT_expressedInterest' *****
# *************************************************************************

DROP TABLE IF EXISTS `CT_expressedInterest`;
CREATE TABLE IF NOT EXISTS `CT_expressedInterest` (
  `expressInterestID` int(11) NOT NULL AUTO_INCREMENT,
  `forename` varchar(255) NOT NULL,
  `surname` varchar(255) NOT NULL,
  `postalAddress` varchar(255) DEFAULT NULL,
  `landLineTelNo` varchar(19) DEFAULT NULL,
  `mobileTelNo` varchar(15) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `sendMethod` varchar(5) NOT NULL,
  PRIMARY KEY (`expressInterestID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;


# ********************************************************************
# ***Insert record data for the table `CT_expressedInterest`**********
# ********************************************************************

INSERT INTO `CT_expressedInterest` (`forename`, `surname`, `postalAddress`, `landLineTelNo`, `mobileTelNo`, `email`, `sendMethod`) VALUES ('Emma', 'Anderson', 'Pandon Building, Room 241, Camden Street, Newcastle Upon Tyne, NE2 1XE', '0191 243 7294', NULL, 'emma.anderson@northumbria.ac.uk', 'post');

INSERT INTO `CT_expressedInterest` (`forename`, `surname`, `postalAddress`, `landLineTelNo`, `mobileTelNo`, `email`, `sendMethod`) VALUES ('Jill', 'Bradnum', 'Pandon Building, Room 227, Camden Street, Newcastle Upon Tyne, NE2 1XE', '0191 243 7668', '07771111111', 'jill.bradnum@northumbria.ac.uk', 'SMS');

INSERT INTO `CT_expressedInterest` (`forename`, `surname`, `postalAddress`, `landLineTelNo`, `mobileTelNo`, `email`, `sendMethod`) VALUES ('Rob', 'Davis', 'Pandon Building, Room 244, Camden Street, Newcastle Upon Tyne, NE2 1XE', '0191 227 4375', '0701111111', 'davis.rob@northumbria.ac.uk', 'email');

INSERT INTO `CT_expressedInterest` (`forename`, `surname`, `postalAddress`, `landLineTelNo`, `mobileTelNo`, `email`, `sendMethod`) VALUES ('Garry', 'Elvin', 'Pandon Building, Room 212, Camden Street, Newcastle Upon Tyne, NE2 1XE', NULL, '07411111122', 'garry.elvin@northumbria.ac.uk', 'email');

INSERT INTO `CT_expressedInterest` (`forename`, `surname`, `postalAddress`, `landLineTelNo`, `mobileTelNo`, `email`, `sendMethod`) VALUES ('Nanlin', 'Jin', NULL, '0191 243 7613', '07411111123', 'nanlin.jin@northumbria.ac.uk', 'SMS');

INSERT INTO `CT_expressedInterest` (`forename`, `surname`, `postalAddress`, `landLineTelNo`, `mobileTelNo`, `email`, `sendMethod`) VALUES ('Sue', 'Jones', '20 Jones Street, Newcastle Upon Tyne, NE5ABC', '0191 223 5678', '07002345671', NULL, 'post');

INSERT INTO `CT_expressedInterest` (`forename`, `surname`, `postalAddress`, `landLineTelNo`, `mobileTelNo`, `email`, `sendMethod`) VALUES ('Patrick', 'Bloggs', '15, Blogs Street, Durham, DH2 ABC', '0191 123 4567', '07001212111', 'patrick&sheila@madeupemail.com', 'SMS');