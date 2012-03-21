insert into countries(name) values('United Kingdom');

insert into organizations(city, postcode, street, email, name, telephone, website, country_id) values('Willesden', 'NW10 2PT', '3rd Floor, 144-150 High Road', 'volunteering@brava.org.uk', 'Brent Volunteer Centre - BrAVA', '0208 438 1520', null, 1);
insert into organizations(city, postcode, street, email, name, telephone, website, country_id) values('Sutton', 'SM1 1SJ', '31 West Street', 'vcsutton@vcsutton.org.uk', 'Volunteer Centre Sutton', '0208 661 5900', null, 1);

insert into interests(name, interestCategory) values('Sport and Outdoor Activities', 'SPORT');
insert into interests(name, interestCategory) values('Art and Culture', 'ART');
insert into interests(name, interestCategory) values('Politics', 'POLITICS');
insert into interests(name, interestCategory) values('Disability', 'DISABILITY');
insert into interests(name, interestCategory) values('Elderly', 'ELDERLY');
insert into interests(name, interestCategory) values('Music', 'MUSIC');
insert into interests(name, interestCategory) values('Education and Literacy', 'EDUCATION');
insert into interests(name, interestCategory) values('Environment', 'ENVIRONMENT');
insert into interests(name, interestCategory) values('Community Services', 'COMMUNITY_SERVICES');

insert into vendors(name) values('Do-it');

insert into file_transfers(host, port, user, password, directory, contactPerson, enabled)
values('localhost_1', 22, 'test', 'encrypted_password', '/folder_with_files_to_download', 'test@domain.com', 1);
insert into file_transfers(host, port, user, password, directory, contactPerson, enabled)
values('localhost_2', 22, 'test', 'encrypted_password', '/folder_with_files_to_download', 'test@domain.com', 0);

insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId)
        values('Sutton', 'SM1 3', '127-135 Oakhill Rd', '09:00:00', current_timestamp, '2011-10-05', 'We have a team of 300 people running in the Royal Parks Half marathon. Some of them will speed around and finish near the front; some will find it a challenge and take 3 hours to finish the race. All of them will be raising money for Right To Play', '11:00:00', 51.369664, -0.186338, 'Cheering Squad', current_timestamp, 1, 1, 1, -1);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId)
        values('Sutton', 'SM1 4', 'Duke St', '14:30:00', current_timestamp, '2011-10-07', 'Learn how to make TV with something to say Free summer film training for 16-25 year olds', '16:30:00', 51.368324, -0.183892, 'Free Summer Film Training', current_timestamp, 1, 2, 1, -2);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId)
        values('Sutton', 'SM1 2SW', 'Gibson Rd', '11:00:00', current_timestamp, '2011-10-12', 'To act as a sighted guide, on a one to one basis, with the client during eye appointments.', '13:00:00', 51.362463, -0.19572, 'Volunteer Support for eye related appointments (driver)', current_timestamp, 1, 2, 1, -3);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId)
        values('Sutton', 'SM5 3', '4 Ashcombe Rd', '12:15:00', current_timestamp, '2011-10-18', 'To meet participants and other volunteers and demonstrate their particular skill.', '15:00:00', 51.360929, -0.160761, 'Volunteer shorter roles and one-off projects', current_timestamp, 1, 2, 1, -4);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId)
        values('Sutton', 'SM1 4', '1-67 Warwick Rd', '15:00:00', current_timestamp, '2011-10-21', 'To act as a sighted guide during group outings to give support to our visually impaired clients enabling them to get out and about more gaining confidence and independence through the sighted guide volunteer.', '18:00:00', 51.364654, -0.1899, 'Sighted Guide for Group Outings', current_timestamp, 1, 2, 1, -5);
insert into opportunities(city, postcode, street, beginTime, created, date, description, endTime, latitude, longitude, title, updated, country_id, organization_id, vendor_id, externalId)
        values('Sutton', 'SM1 4', '41-65 Lower Rd', '09:30:00', current_timestamp, '2011-10-29', 'To act as a sighted guide on a one to one basis giving support to individual visually impaired clients enabling them to do personal shopping for birthdays, clothes and Christmas shopping etc.', '11:30:00', 51.365565, -0.184107, 'Sighted Guide for one to one outings', current_timestamp, 1, 2, 1, -6);

insert into x_opportunities_interests(opportunity_id, interest_id) values(1,1);
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