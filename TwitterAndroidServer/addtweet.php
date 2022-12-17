
<?php

require("DBInfo.inc");

//call service to register
// http://localhost/TwitterAndroidServer/addtweet.php?user_id=2&tweet_text=hello+this+is+second+tweet&tweet_attachable=home/tester2.png


$query = "insert into tweets (user_id,tweet_text,tweet_attachable) values 
    ( ".$_GET['user_id']."  , ' ".$_GET['tweet_text']." ' , ' ".$_GET['tweet_attachable']." ')" ;

$result = mysqli_query($connect,$query);

if (!$result){
    $output = "{'msg':'fail'}";
}else{
    $output = "{'msg':'tweet is added'}";
}

echo $output;

mysqli_close($connect);

?>