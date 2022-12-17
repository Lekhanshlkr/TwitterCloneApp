
<?php

require("DBInfo.inc");

//call service to register
// http://localhost/TwitterAndroidServer/register.php?fulll_name=Jagriti&email=jagi@gmail.com&password=12ee4&picture_path=home/user2.png


$query = "insert into login (fulll_name,email,password,picture_path) values 
    (' ".$_GET['fulll_name']." ' , ' ".$_GET['email']." ' ,
      ' ".$_GET['password']." ' , ' ".$_GET['picture_path']." ')" ;

$result = mysqli_query($connect,$query);

if (!$result){
    $output = "{'msg':'fail'}";
}else{
    $output = "{'msg':'user is added'}";
}

echo $output;

mysqli_close($connect);

?>