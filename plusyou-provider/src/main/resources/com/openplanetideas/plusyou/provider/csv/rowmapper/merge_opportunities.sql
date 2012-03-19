insert into opportunities(
externalId, title, description, date, beginTime, endTime, vendor_id,
latitude, longitude, street, postcode, city, country_id,
organization_id, created, updated)

values(
:externalId, :title, :description, :date, :beginTime, :endTime, (select id from vendors where upper(name) = upper(:vendor_name)),
:latitude, :longitude, :street, :postcode, :city, (select id from countries where upper(name) = upper(:country_name)),
(select id from organizations where upper(name) = upper(:organization_name)), now(), now())

on duplicate key update
title = values(title),
description = values(description),
date = values(date),
beginTime = values(beginTime),
endTime = values(endTime),
latitude = values(latitude),
longitude = values(longitude),
street = values(street),
postcode = values(postcode),
city = values(city),
country_id = values(country_id),
organization_id = values(organization_id),
updated = values(updated);