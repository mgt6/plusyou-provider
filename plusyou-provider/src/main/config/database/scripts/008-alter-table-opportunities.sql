alter table opportunities change city city varchar(255);
alter table opportunities change street street varchar(255);
alter table opportunities add externalId bigint not null;
alter table opportunities add unique (externalId, vendor_id);
alter table opportunities change organization_id organization_id bigint not null;