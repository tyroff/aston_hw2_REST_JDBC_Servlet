insert into doctor(lastname, firstname, patronymic, birthday, patient_id_array) values
    ('Айболит', 'Иван', 'Ивановичь', '2023-03-29', '{1,2,3}'),   --id:1
    ('Врачиха', 'Инна', 'Абрамовна', '2023-03-29', '{4,5}');     --id:2

insert into patient(lastname, firstname, patronymic, birthday, job, doctor_id) values
    ('Пациент', 'Иван', 'Ивановичь', '2023-03-29', 'Работаю', 1),    --id:1
    ('Пациентка', 'Алла', 'Куй', '2023-03-29', 'Домохозяйка', 1),    --id:2
    ('Заболела', 'Тут', 'Везде', '2023-03-29', 'Многодетная мать', 1),--id:3
    ('Нечем', 'Заняться', 'Ихожу', '2023-03-29', 'Пенсионер', 2),     --id:4
    ('Чего', 'Пришёл', 'Сюда', '2023-03-29', 'Нет работы', 2);        --id:5
