truncate file_transfers;
truncate x_opportunities_interests;
truncate opportunities;
truncate organizations;

insert into file_transfers(host, port, user, password, directory, contactPerson, enabled)
values('localhost', 22, 'TestUser', 'uTWguGFKOKRkYvo9mMVHH2E/wE8ns1yH', '/var/plus_you/download', 'test01.plusyou@gmail.com', 0);

insert into organizations(city, postcode, street, email, name, telephone, website, country_id) values('Willesden', 'NW10 2PT', '3rd Floor, 144-150 High Road', 'volunteering@brava.org.uk', 'Brent Volunteer Centre - BrAVA', '0208 438 1520', null, 1);
insert into organizations(city, postcode, street, email, name, telephone, website, country_id) values('Sutton', 'SM1 1SJ', '31 West Street', 'vcsutton@vcsutton.org.uk', 'Volunteer Centre Sutton', '0208 661 5900', null, 1);

--Sutton
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Sutton', 'SM1 3', '127-135 Oakhill Rd', '09:00:00', now(), now(), 'We have a team of 300 people running in the Royal Parks Half marathon. Some of them will speed around and finish near the front; some will find it a challenge and take 3 hours to finish the race. All of them will be raising money for Right To Play', '11:00:00', 50.928791, 4.497528, 'Cheering Squad', now(), 1, 1, 1, -1);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Sutton', 'SM1 4', 'Duke St', '14:30:00', now(), '2011-10-07', 'Learn how to make TV with something to say Free summer film training for 16-25 year olds', '16:30:00', 50.913532, 4.33342, 'Free Summer Film Training', now(), 1, 2, 1, -2);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Sutton', 'SM1 2SW', 'Gibson Rd', '11:00:00', now(), now(), 'To act as a sighted guide, on a one to one basis, with the client during eye appointments.', '13:00:00', 50.937229, 4.369125, 'Volunteer Support for eye related appointments (driver)', now(), 1, 2, 1, -3);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Sutton', 'SM5 3', '4 Ashcombe Rd', '12:15:00', now(), '2011-10-18', 'To meet participants and other volunteers and demonstrate their particular skill.', '15:00:00', 50.885167, 4.465771, 'Volunteer shorter roles and one-off projects', now(), 1, 2, 1, -4);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Sutton', 'SM1 4', '1-67 Warwick Rd', '15:00:00', now(), now(), 'To act as a sighted guide during group outings to give support to our visually impaired clients enabling them to get out and about more gaining confidence and independence through the sighted guide volunteer.', '18:00:00', 50.882351, 4.3717, 'Sighted Guide for Group Outings', now(), 1, 2, 1, -5);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Sutton', 'SM1 4', '41-65 Lower Rd', '09:30:00', now(), '2011-10-29', 'To act as a sighted guide on a one to one basis giving support to individual visually impaired clients enabling them to do personal shopping for birthdays, clothes and Christmas shopping etc.', '11:30:00', 50.911368, 4.469547, 'Sighted Guide for one to one outings', now(), 1, 2, 1, -6);

insert into x_opportunities_interests(opportunity_id, interest_id) values(1,1);
insert into x_opportunities_interests(opportunity_id, interest_id) values(1,8);
insert into x_opportunities_interests(opportunity_id, interest_id) values(1,9);
insert into x_opportunities_interests(opportunity_id, interest_id) values(2,2);
insert into x_opportunities_interests(opportunity_id, interest_id) values(2,3);
insert into x_opportunities_interests(opportunity_id, interest_id) values(3,4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(3,5);
insert into x_opportunities_interests(opportunity_id, interest_id) values(4,2);
insert into x_opportunities_interests(opportunity_id, interest_id) values(4,4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(4,6);
insert into x_opportunities_interests(opportunity_id, interest_id) values(4,7);
insert into x_opportunities_interests(opportunity_id, interest_id) values(5,4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(5,5);
insert into x_opportunities_interests(opportunity_id, interest_id) values(6,4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(6,5);

--Zaventem
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Zaventem', '1930', 'Diegemstraat', '09:30:00', now(), now(), 'Onderhouden van het park', '11:30:00', 50.890067, 4.461656, 'Onderhouden van het park', now(), 1, 2, 1, -7);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Zaventem', '1930', 'Fabriekstraat', '09:30:00', now(), now(), 'Wouter Vandenhoute laat op zich wachte, VT4 maakt nog steeds geen deftige programmas', '11:30:00', 50.887817, 4.461501, 'Help VT4', now(), 1, 2, 1, -8);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Zaventem', '1930', 'Leonardo da Vincilaan', '09:30:00', now(), '2011-10-29', 'Bezet het rond punt', '11:30:00', 50.892571, 4.461586, 'Bezet het rond punt', now(), 1, 2, 1, -9);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Zaventem', '1930', '41-65 Lower Rd', '09:30:00', now(), '2012-10-29', 'Hoek af', '11:30:00', 50.889452, 4.46267, 'Hoek af', now(), 1, 2, 1, -10);

insert into x_opportunities_interests(opportunity_id, interest_id) values(7,5);
insert into x_opportunities_interests(opportunity_id, interest_id) values(7,4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(8,1);
insert into x_opportunities_interests(opportunity_id, interest_id) values(8,5);
insert into x_opportunities_interests(opportunity_id, interest_id) values(8,4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(9,6);
insert into x_opportunities_interests(opportunity_id, interest_id) values(10,1);

--Hasselt
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Hasselt', '3500', 'Waterleliestraat', '09:30:00', now(), '2012-03-10', 'Verzorgen van waterlelies', '11:30:00', 50.96473, 5.349019, 'Verzorgen van waterlelies', now(), 1, 2, 1, -11);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Hasselt', '3500', 'Waterleliestraat', '09:30:00', now(), '2012-03-11', 'Leer jongeren basketten', '11:30:00', 50.964546, 5.349854, 'Basketbal training', now(), 1, 2, 1, -12);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Hasselt', '3500', 'Vijversstraat', '09:30:00', now(), '2011-10-29', 'Onderhouden van een zwembad', '11:30:00', 50.96361, 5.34793, 'Onderhouden van een zwembad', now(), 1, 2, 1, -13);

insert into x_opportunities_interests(opportunity_id, interest_id) values(11,2);
insert into x_opportunities_interests(opportunity_id, interest_id) values(11,3);
insert into x_opportunities_interests(opportunity_id, interest_id) values(11,4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(12,5);
insert into x_opportunities_interests(opportunity_id, interest_id) values(13,7);

--Leuven
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Leuven', '3000', 'Redersstraat', '09:30:00', now(), now(), 'Inbev heeft een tekort aan bekwame bierbrouwers, help hen een deftig bier te brouwen', '11:30:00', 50.888213, 4.708425, 'Bier brouwen', now(), 1, 2, 1, -14);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Leuven', '3010', 'Domeinstraat 88', '09:30:00', now(), now(), 'Jongeren sporten niet genoeg, breng hen het nut van sporten op een ludieke manier bij', '11:30:00', 50.897281, 4.722678, 'Sportkampen', now(), 1, 2, 1, -15);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Leuven', '3001', 'Hertogstraat 189', '09:30:00', now(), '2011-10-29', 'Speelplaatswerking op de speelplaats van een school waar er gespeeld wordt', '11:30:00', 50.858207, 4.700196, 'Spelen op de speelplaats van een school', now(), 1, 2, 1, -16);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Leuven', '3001', 'Leeuwerikenstraat 17-59', '09:30:00', now(), now(), 'Houdt de stoep proper', '11:30:00', 50.861861, 4.699021, 'Houdt de stoep proper', now(), 1, 2, 1, -17);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Leuven', '3000', 'Dalemhof 87-99', '09:30:00', now(), now(), 'Den duivel is langs geweest, drijf hem uit', '11:30:00', 50.861681, 4.739935, 'Duiveluitdrijving', now(), 1, 2, 1, -18);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Leuven', '3010', 'Marie-Elisabeth Belpairestraat', '09:30:00', now(), now(), 'Jongeren sporten niet genoeg, breng hen het nut van athletiek op een ludieke manier bij', '11:30:00', 50.883272, 4.733332, 'Athletiekkampen', now(), 1, 2, 1, -19);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Leuven', '3001', 'Brabantlaan 1', '09:30:00', now(), '2011-10-29', 'Zoeken van jobs voor werklozen', '11:30:00', 50.853927, 4.72823, 'Zoeken van jobs voor werklozen', now(), 1, 2, 1, -20);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Leuven', '3001', 'Eburonenlaan 14', '09:30:00', now(), now(), 'Houdt de stoep proper', '11:30:00', 50.859433, 4.689896, 'Houdt de stoep proper', now(), 1, 2, 1, -21);

insert into x_opportunities_interests(opportunity_id, interest_id) values(14,7);
insert into x_opportunities_interests(opportunity_id, interest_id) values(15,5);
insert into x_opportunities_interests(opportunity_id, interest_id) values(15,4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(16,1);
insert into x_opportunities_interests(opportunity_id, interest_id) values(16,5);
insert into x_opportunities_interests(opportunity_id, interest_id) values(17,4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(17,6);
insert into x_opportunities_interests(opportunity_id, interest_id) values(18,1);
insert into x_opportunities_interests(opportunity_id, interest_id) values(19,2);
insert into x_opportunities_interests(opportunity_id, interest_id) values(20,3);
insert into x_opportunities_interests(opportunity_id, interest_id) values(20,4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(21,5);
insert into x_opportunities_interests(opportunity_id, interest_id) values(21,7);
insert into x_opportunities_interests(opportunity_id, interest_id) values(21,1);

--Aldershot and Sutton
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Aldershot', 'Hampshire GU11 1JR', '153-163 Victoria Rd', '09:00:00', now(), '2012-01-17', 'We have a team of 300 people running in the Royal Parks Half marathon. Some of them will speed around and finish near the front; some will find it a challenge and take 3 hours to finish the race. All of them will be raising money for Right To Play', '11:00:00', 51.248616, -0.765422, 'Cheering Squad', now(), 1, 1, 1, -22);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Aldershot', 'Hampshire GU11 1EY', '3 Cross St', '14:30:00', now(), '2012-01-18', 'Learn how to make TV with something to say Free summer film training for 16-25 year olds', '16:30:00', 51.248781, -0.766039, 'Free Summer Film Training', now(), 1, 1, 1, -23);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Aldershot', 'Hampshire GU11 1DB', '54-55 Wellington Centre', '11:00:00', now(), '2012-01-19', 'To act as a sighted guide, on a one to one basis, with the client during eye appointments.', '13:00:00', 51.248949, -0.764483, 'Volunteer Support for eye related appointments (driver)', now(), 1, 1, 1, -24);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Aldershot', 'Hampshire GU11 1NS', '2-4 Heathland St', '12:15:00', now(), '2012-01-19', 'To meet participants and other volunteers and demonstrate their particular skill.', '15:00:00', 51.248089, -0.764451, 'Volunteer shorter roles and one-off projects', now(), 1, 1, 1, -25);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Aldershot', 'Hampshire County GU11 1', '173 Victoria Rd', '15:00:00', now(), '2012-01-20', 'To act as a sighted guide during group outings to give support to our visually impaired clients enabling them to get out and about more gaining confidence and independence through the sighted guide volunteer.', '18:00:00', 51.248512, -0.766152, 'Sighted Guide for Group Outings', now(), 1, 1, 1, -26);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Aldershot', 'Hampshire GU11 1JW', '129 Victoria Rd', '09:30:00', now(), '2012-01-18', 'To act as a sighted guide on a one to one basis giving support to individual visually impaired clients enabling them to do personal shopping for birthdays, clothes and Christmas shopping etc.', '11:30:00', 51.248412, -0.764269, 'Sighted Guide for one to one outings', now(), 1, 1, 1, -27);

insert into x_opportunities_interests(opportunity_id, interest_id) values(22, 1);
insert into x_opportunities_interests(opportunity_id, interest_id) values(23, 2);
insert into x_opportunities_interests(opportunity_id, interest_id) values(23, 3);
insert into x_opportunities_interests(opportunity_id, interest_id) values(24, 4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(24, 5);
insert into x_opportunities_interests(opportunity_id, interest_id) values(25, 2);
insert into x_opportunities_interests(opportunity_id, interest_id) values(25, 4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(25, 6);
insert into x_opportunities_interests(opportunity_id, interest_id) values(25, 7);
insert into x_opportunities_interests(opportunity_id, interest_id) values(26, 4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(26, 5);
insert into x_opportunities_interests(opportunity_id, interest_id) values(27, 4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(27, 5);

--Robotium
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Sutton', 'SM5 3', '4 Ashcombe Rd', '12:15:00', now(), '9999-01-01', 'To meet participants and other volunteers and demonstrate their particular skill.', '15:00:00', 50.885167, 4.465771, 'Robotium Future Event', now(), 1, 2, 1, -9999999999);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId) values('Sutton', 'SM5 3', '4 Ashcombe Rd', '12:15:00', now(), '1111-01-01', 'To meet participants and other volunteers and demonstrate their particular skill.', '15:00:00', 50.885167, 4.465771, 'Robotium Past Event', now(), 1, 2, 1, -1111111111);

insert into x_opportunities_interests(opportunity_id, interest_id) values(28, 2);
insert into x_opportunities_interests(opportunity_id, interest_id) values(28, 4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(28, 6);
insert into x_opportunities_interests(opportunity_id, interest_id) values(28, 7);
insert into x_opportunities_interests(opportunity_id, interest_id) values(29, 2);
insert into x_opportunities_interests(opportunity_id, interest_id) values(29, 4);
insert into x_opportunities_interests(opportunity_id, interest_id) values(29, 6);
insert into x_opportunities_interests(opportunity_id, interest_id) values(29, 7);