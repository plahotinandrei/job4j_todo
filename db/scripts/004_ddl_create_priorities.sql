create table priorities (
   id serial primary key,
   "name" varchar unique not null ,
   position int
);