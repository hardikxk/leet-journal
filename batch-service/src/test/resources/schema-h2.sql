create table if not exists problem(
    id int primary key,
    title varchar(255) not null,
    acceptance varchar(255) not null,
    difficulty varchar(255) not null
);
