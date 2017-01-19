select file,cloneID
from
(
select clones.cloneID, clones.methodID ,methods.file
from clones
left join methods
on clones.methodID = methods.id
where methods.file is not NULL
order by methods.file
) as t
order by file;
