
    create sequence app_form_seq start with 1 increment by 1;

    create sequence app_seq start with 1 increment by 1;

    create sequence f_upload_seq start with 1 increment by 1;

    create table application_forms (
        created date,
        position integer not null,
        application_id bigint not null,
        id bigint not null,
        content jsonb,
        name varchar(255),
        primary key (id)
    );

    create table applications (
        created timestamp(6),
        id bigint not null,
        name varchar(255),
        primary key (id)
    );

    create table file_upload (
        created date,
        application_id bigint not null,
        file_size bigint,
        id bigint not null,
        file_name varchar(255),
        file_path varchar(255) not null,
        file_type varchar(255),
        primary key (id)
    );

    alter table if exists application_forms 
       add constraint FKbtafjevwc5vnwmantb6moifk0 
       foreign key (application_id) 
       references applications;

    alter table if exists file_upload 
       add constraint FK41kx1pkwuw5uhmhyetqk24hfb 
       foreign key (application_id) 
       references applications;
