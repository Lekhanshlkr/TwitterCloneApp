
<?php

require("DBInfo.inc");

//call service to register
// http://localhost/TwitterAndroidServer/userfollowing.php?op=1&user_id=1&following_user_id=2

// op=1 for follow, op=2 for unfollow

if ($_GET['op']==1){
    $query = "insert into following (user_id,following_user_id) values  
    ( ".$_GET['user_id']."  ,  ".$_GET['following_user_id']." )" ;
}else if ($_GET['op']==2){
    $query = "delete from following where user_id = ".$_GET['user_id']." and following_user_id=".$_GET['following_user_id']." ";
}

$result = mysqli_query($connect,$query);

if (!$result){
    $output = "{'msg':'fail'}";
}else{
    $output = "{'msg':'following is updated'}";
}

echo $output;

mysqli_close($connect);

?>