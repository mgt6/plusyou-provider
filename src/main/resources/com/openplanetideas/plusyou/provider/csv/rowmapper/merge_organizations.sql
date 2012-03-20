insert into organizations(name, street, postcode, city, country_id, telephone, email, website)
values(:name, :street, :postcode, :city, (select id from countries where upper(name) = upper(:country_name)), :telephone, :email, :website)

on duplicate key update
street = values(street),
postcode = values(postcode),
city = values(city),
country_id = values(country_id),
telephone = values(telephone),
email = values(email),
website = values(website);