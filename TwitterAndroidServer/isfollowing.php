
<?php

require("DBInfo.inc");

//call service to register
// http://localhost/TwitterAndroidServer/isfollowing.php?user_id=1&following_user_id=2


$query = "select * from following where user_id =' ".$_GET['user_id']." ' and following_user_id =' ".$_GET['following_user_id']." '";

$result = mysqli_query($connect,$query);

if (!$result){
    die('Error cannot run query');
}

$userInfo = array();
while($row  = mysqli_fetch_assoc($result)){
    $userInfo[] = $row;
    break; // to be save
}

if($userInfo){
    print("{'msg':'is following'}");
}else{
    print("'msg':'not following'");
}

mysqli_free_result($result);

mysqli_close($connect);

?>