use aibcontact1;

delete from aibcontact1.company;
delete from aibcontact1.worldregion;
delete from aibcontact1.country;
    

insert into aibcontact1.worldregion (worldregion_id,descr,post_price,post_status,post_number) 
select post_id,post_name,post_price,post_status,post_number 
  from aibcontact.postage 
 where not exists (select worldregion_id from worldregion where worldregion_id=post_id);
 
insert into aibcontact1.country (country_id,country,shortname,worldregion_id,status) 
select country_id,country_name,country_code,country_post_id,country_status 
  from aibcontact.countries 
 where not exists (select country_id from country where country_id=countries.country_id);
 
insert into aibcontact1.company (
                     company_id, 
                     full_name, 
                     abbreviation, 
                     is_dummy,
                     address,   
                     country_id,
                     main_phone,
                     main_fax)
             select org_id, 
                    if(length(ifnull(org_name,''))>length(ifnull(org_other_name,'')) and length(ifnull(org_name,''))>length(ifnull(org_3,'')),org_name,
                       if(length(ifnull(org_other_name,''))>length(ifnull(org_3,'')),org_other_name,org_3)
                    ), 
                    if(length(ifnull(org_3,''))<length(ifnull(org_name,'')) and length(ifnull(org_3,''))<length(ifnull(org_other_name,'')) and length(ifnull(org_3,''))>0, 
                       org_3, 
                       if(length(ifnull(org_other_name,''))<length(ifnull(org_name,'')) and length(ifnull(org_other_name,''))>0,
                          org_other_name,
                          org_name
                       )
                    ), 
                    if(ifnull(org_status,0)>0,0,1), 
                    (select concat(ifnull(Address_1,''),'\n ',ifnull(Address_2,''),'\n ',ifnull(Town_code_1,''),'\n',ifnull(Town_code_2,''))
                      from contacts where contact_org=org_id order by contact_id limit 1),
                    (select country_id
                        from countries 
                       where country_name=(select country from contacts 
                                            where contact_org=organisations.org_id 
                                             and exists (select country_name from countries where country_name=contacts.country) order by contact_id limit 1)),
                    (select ifnull(tel,mobile)
                        from contacts 
                       where contact_org=org_id and not ifnull(tel,mobile) is null
                    order by contact_id limit 1),
                     (select fax 
                        from contacts 
                       where contact_org=org_id and not fax is null
                    order by contact_id limit 1)
               from organisations;
               
               