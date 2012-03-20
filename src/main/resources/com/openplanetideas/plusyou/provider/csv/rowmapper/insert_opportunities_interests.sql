insert into x_opportunities_interests(opportunity_id, interest_id)
values(
  (
    select id
    from opportunities
    where externalId = :externalId
    and vendor_id = (
      select id
      from vendors
      where name = :vendor_name
    )
  ),

  (
    select id
    from interests
    where interestCategory = :interest_type
  )
);