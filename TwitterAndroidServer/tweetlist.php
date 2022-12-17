
<?php

require("DBInfo.inc");

//call service to register
//case 1: search op=3, query=? , startFrom=0

if($_GET['op']==1){ // my following

    // http://localhost/TwitterAndroidServer/tweetlist.php?op=1&user_id=1&startfrom=0

    $query="select * from user_tweets where user_id in (select following_user_id from following where user_id=".$_GET['user_id'].")
        or user_id=".$_GET['user_id']." order by tweet_date DESC limit 20 offset ".$_GET['startfrom']." ";
}else if($_GET['op']==2){ // specific person post

    // http://localhost/TwitterAndroidServer/tweetlist.php?op=2&user_id=1&startfrom=0

    $query = "select * from user_tweets where user_id=".$_GET['user_id']." order by tweet_date DESC limit 20 offset ".$_GET['startfrom']." ";
}else if($_GET['op']==3){ // search

    // http://localhost/TwitterAndroidServer/tweetlist.php?op=3&q=hel&startfrom=0

    $query = "select * from tweets where tweet_text like '%".$_GET['q']."%' limit 20 offset ".$_GET['startfrom']." ";
}


$result = mysqli_query($connect,$query);

if (!$result){
    die('Error cannot run query');
}

$userTweets = array();
while($row  = mysqli_fetch_assoc($result)){
    $userTweets[] = $row;
}

if($userTweets){
    print("{'msg':'has tweet', 'info':'".json_encode($userTweets)."'}");
}else{
    print("'msg':'no tweets'");
}

mysqli_free_result($result);

mysqli_close($connect);

?>