insert into syntax.category (id, name) values (1, 'n'), (2, 'n/n');

-- TODO так добавляются только леммы, а надо формы, а не леммы!
insert into syntax.category_to_form (category_id, form_id)
select 1, f.id from dictionary.form f, dictionary.form_to_grammeme fg
where fg.grammeme_id = 'NOUN' and fg.form_id = f.id;

insert into syntax.category_to_form (category_id, form_id)
select 2, f.id from dictionary.form f, dictionary.form_to_grammeme fg
where (fg.grammeme_id = 'ADJF' or fg.grammeme_id = 'ADJS') and fg.form_id = f.id;