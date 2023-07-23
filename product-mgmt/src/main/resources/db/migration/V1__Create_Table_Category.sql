create table category (
    id uuid not null,
    created_by varchar(100) not null default '99999',
    created_on timestamp(6),
    modified_by varchar(100),
    modified_on timestamp(6),
    name varchar(255) not null not null,
    status varchar(1) not null default 'A',
    primary key (id)
);