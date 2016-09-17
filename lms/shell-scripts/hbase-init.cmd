disable 'permissions'
drop 'permissions'
create 'permissions', {NAME => 'p', VERSIONS => 3, BLOOMFILTER => 'ROW'}
put 'permissions', 'home', 'p:id', 'home_view'
put 'permissions', 'dashboard', 'p:id', 'dashboard_view'
put 'permissions', 'assignments', 'p:id', 'assignments_view'

disable 'users'
drop 'users'
create 'users', {NAME => 'a', VERSIONS => 3, BLOOMFILTER => 'ROW'}
put 'users', 'admin', 'a:password', '4L�cZIU�f�Ur���,J��t���7U*=l'
put 'users', 'admin', 'a:salt', '�I�Ѭ�BJUIIr���a'
put 'users', 'admin', 'a:fname', 'Admin'
put 'users', 'admin', 'a:lname', 'User'
put 'users', 'admin', 'a:birthday', '1990-01-01'
put 'users', 'admin', 'a:gender', 'Male'
put 'users', 'admin', 'a:mobile', '987654321'
put 'users', 'admin', 'a:email', 'admin@google.com'

disable 'user_permissions'
drop 'user_permissions'
create 'user_permissions', {NAME => 'p', VERSIONS => 3, BLOOMFILTER => 'ROW'}
put 'user_permissions', 'admin', 'p:pid', 'home_view'
put 'user_permissions', 'admin', 'p:pid', 'dashboard_view'
put 'user_permissions', 'admin', 'p:pid', 'assignments_view'