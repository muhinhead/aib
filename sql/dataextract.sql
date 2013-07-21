SET group_concat_max_len = 4096;

drop table if exists extract_tmp;

create table extract_tmp as
select distinct c.contact_id,
       t.title_name,
       c.contact_first_name,
       c.contact_last_name,
       c.contact_suffix,
       c.contact_greeting,
       c.contact_position,
       o.org_name,
       op.org_name as parent_org_name,
       #-- sf.sub_feature_name,
       (select group_concat(distinct sf.sub_feature_name separator ',') 
          from subfeatures sf,contactfeatures cf 
         where sf.sub_feature_id=cf.pf_sub_feature_id 
           and c.contact_id=cf.pf_contact_id 
           and cf.pf_feature_id=4) as sub_feature_name,
       cf5_20.pf_value pfvalue5_20,
       cf5_21.pf_value pfvalue5_21,
       cf5_22.pf_value pfvalue5_22,
       cf5_23.pf_value pfvalue5_23,
       cf9_36.pf_value pfvalue9_36,
       cf9_37.pf_value pfvalue9_37,
       cf9_38.pf_value pfvalue9_38,
       cf9_39.pf_value pfvalue9_39,
       ifnull(sf1.sub_feature_name,concat('(',Country,')')) as sub_feature_name1,
       cf8_32.pf_value pfvalue8_32,
       cf8_33.pf_value pfvalue8_33,
       cf8_43.pf_value pfvalue8_43,
       cf8_34.pf_value pfvalue8_34,
       cf8_35.pf_value pfvalue8_35,
       cf11_44.pf_value pfvalue11_44,
       cf11_49.pf_value pfvalue11_49,
       cf8_48.pf_value pfvalue8_48,
       cf12_45.pf_value pfvalue12_45,
       if(exists(select pf_id from contactfeatures where pf_contact_id=c.contact_id and pf_sub_feature_id=52),'Y','N') subfeature52,     
       if(exists(select pf_id from contactfeatures where pf_contact_id=c.contact_id and pf_sub_feature_id=55),'Y','N') subfeature55,
       if(exists(select pf_id from contactfeatures where pf_contact_id=c.contact_id and pf_sub_feature_id=367),'Y','N') subfeature367,
       if(exists(select pf_id from contactfeatures where pf_contact_id=c.contact_id and pf_sub_feature_id=423),'Y','N') subfeature423,
       if(exists(select pf_id from contactfeatures where pf_contact_id=c.contact_id and pf_sub_feature_id=424),'Y','N') subfeature424,
       if(exists(select pf_id from contactfeatures where pf_contact_id=c.contact_id and pf_sub_feature_id=334),'Y','N') subfeature334,
       if(exists(select pf_id from contactfeatures where pf_contact_id=c.contact_id and pf_sub_feature_id=337),'Y','N') subfeature337,
       if(exists(select pf_id from contactfeatures where pf_contact_id=c.contact_id and pf_sub_feature_id=335),'Y','N') subfeature335,
       sf2.sub_feature_name as sub_feature_name2,
       c.`timestamp`,
       rtrim(ltrim(concat(ifnull(notes,''),' ',ifnull(info,'')))) notes_info
  from contacts c left outer join aib_titles t on c.contact_title=t.title_id
                  left outer join organisations o on c.contact_org=o.org_id
                  left outer join organisations op on o.org_parent_id=op.org_id
                  #-- left outer join contactfeatures cf on (c.contact_id=cf.pf_contact_id and cf.pf_feature_id=4)
                  #-- left outer join subfeatures sf on cf.pf_sub_feature_id=sf.sub_feature_id
                  left outer join contactfeatures cf5_20 on (c.contact_id=cf5_20.pf_contact_id and cf5_20.pf_feature_id=5 and cf5_20.pf_sub_feature_id=20)
                  left outer join contactfeatures cf5_21 on (c.contact_id=cf5_21.pf_contact_id and cf5_21.pf_feature_id=5 and cf5_21.pf_sub_feature_id=21)
                  left outer join contactfeatures cf5_22 on (c.contact_id=cf5_22.pf_contact_id and cf5_22.pf_feature_id=5 and cf5_22.pf_sub_feature_id=22)
                  left outer join contactfeatures cf5_23 on (c.contact_id=cf5_23.pf_contact_id and cf5_23.pf_feature_id=5 and cf5_23.pf_sub_feature_id=23)
                  left outer join contactfeatures cf9_36 on (c.contact_id=cf9_36.pf_contact_id and cf9_36.pf_feature_id=9 and cf9_36.pf_sub_feature_id=36)
                  left outer join contactfeatures cf9_37 on (c.contact_id=cf9_37.pf_contact_id and cf9_37.pf_feature_id=9 and cf9_37.pf_sub_feature_id=37)
                  left outer join contactfeatures cf9_38 on (c.contact_id=cf9_38.pf_contact_id and cf9_38.pf_feature_id=9 and cf9_38.pf_sub_feature_id=38)
                  left outer join contactfeatures cf9_39 on (c.contact_id=cf9_39.pf_contact_id and cf9_39.pf_feature_id=9 and cf9_39.pf_sub_feature_id=39)
                  left outer join contactfeatures cf1 on (c.contact_id=cf1.pf_contact_id 
                                  and (cf1.pf_sub_feature_id between 60 and 326 or cf1.pf_sub_feature_id in (366,378,379,398))
                                  )
                  left outer join subfeatures sf1 on cf1.pf_sub_feature_id=sf1.sub_feature_id
                  left outer join contactfeatures cf8_32 on (c.contact_id=cf8_32.pf_contact_id and cf8_32.pf_feature_id=8 and cf8_32.pf_sub_feature_id=32)
                  left outer join contactfeatures cf8_33 on (c.contact_id=cf8_33.pf_contact_id and cf8_33.pf_feature_id=8 and cf8_33.pf_sub_feature_id=33)
                  left outer join contactfeatures cf8_43 on (c.contact_id=cf8_43.pf_contact_id and cf8_43.pf_feature_id=8 and cf8_43.pf_sub_feature_id=43)
                  left outer join contactfeatures cf8_34 on (c.contact_id=cf8_34.pf_contact_id and cf8_34.pf_feature_id=8 and cf8_34.pf_sub_feature_id=34)
                  left outer join contactfeatures cf8_35 on (c.contact_id=cf8_35.pf_contact_id and cf8_35.pf_feature_id=8 and cf8_35.pf_sub_feature_id=35)
                  left outer join contactfeatures cf11_44 on (c.contact_id=cf11_44.pf_contact_id and cf11_44.pf_feature_id=11 and cf11_44.pf_sub_feature_id=44)
                  left outer join contactfeatures cf11_49 on (c.contact_id=cf11_49.pf_contact_id and cf11_49.pf_feature_id=11 and cf11_49.pf_sub_feature_id=49)
                  left outer join contactfeatures cf8_48 on (c.contact_id=cf8_48.pf_contact_id and cf8_48.pf_feature_id=8 and cf8_48.pf_sub_feature_id=48)
                  left outer join contactfeatures cf12_45 on (c.contact_id=cf12_45.pf_contact_id and cf12_45.pf_feature_id=12 and cf12_45.pf_sub_feature_id=45)
                  left outer join contactfeatures cf2 on (c.contact_id=cf2.pf_contact_id and cf2.pf_feature_id=30)
                  left outer join subfeatures sf2 on cf2.pf_sub_feature_id=sf2.sub_feature_id;

alter table extract_tmp add note_text text;

update extract_tmp set note_text=(select group_concat(note_text order by note_id) from notes where note_contact_id=contact_id);
