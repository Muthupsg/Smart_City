<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

 include 'DatabaseConfig.php';
 
 $con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);
 

 $username = $_POST['username'];
 
 $Sql_Query = "select * from report where fromuser = '$username' LIMIT 1";
 
 $check = mysqli_fetch_array(mysqli_query($con,$Sql_Query));
 
 
 if(isset($check)){
 echo $check[3]," ",$check[8];
 }
 else{
 echo "Try Again";
 }
 
 }else{
 echo "Check Again";
 }
mysqli_close($con);

?>