CREATE TABLE outbox_event (
  id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
  type varchar(100),
  payload json,
  status varchar(100),
  created_at timestamp,
  updated_at timestamp
);