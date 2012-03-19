delete from x_opportunities_interests
where opportunity_id = (
  select id
  from opportunities
  where externalId = :externalId
  and vendor_id = (
    select id
    from vendors
    where name = :vendor_name
  )
);