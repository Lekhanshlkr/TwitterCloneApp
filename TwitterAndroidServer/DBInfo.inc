<?php

$host="127.0.0.1";
$user = "root";
$password = "lekhansh69";
$database = "TWITTER";

$connect = mysqli_connect($host,$user,$password,$database);

if(mysqli_connect_errno()){

    die("cannot connect to database".mysqli_connect_error());
}

?>