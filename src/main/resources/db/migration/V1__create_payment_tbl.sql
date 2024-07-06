CREATE TABLE payment (
  id bigserial PRIMARY KEY,
  first_name varchar(255),
  last_name varchar(255),zip_code varchar(255),
  card_number varchar(50),
  created_at timestamp
);