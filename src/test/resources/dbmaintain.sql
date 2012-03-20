create schema ${database.provider.username};
create user ${database.provider.username}@localhost
identified by '${database.provider.password}';
grant all on ${database.provider.username}.* to ${database.provider.username}@localhost;