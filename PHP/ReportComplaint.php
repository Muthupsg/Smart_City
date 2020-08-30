<?php
if($_SERVER['REQUEST_METHOD']=='POST'){

include 'DatabaseConfig.php';

 $con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);

 $S_Username = $_POST['fromuser'];
 $S_Ptype = $_POST['problem_type'];
 $S_Pcategory = $_POST['problem_category'];
 $S_Descrip = $_POST['descrip'];
 $S_Lati = $_POST['latitude'];
 $S_Longi = $_POST['longitude'];
 $S_Address = $_POST['address'];


 $Sql_Query = "INSERT INTO report (fromuser,problem_type,problem_category,descrip,latitude,longitude,address) values ('$S_Username','$S_Ptype','$S_Pcategory','$S_Descrip','$S_Lati','$S_Longi','$S_Address')";

 if(mysqli_query($con,$Sql_Query))
{
 echo 'Complaint Registered';
}
else
{
 echo 'Oops, Something went wrong';
 }
 }
 mysqli_close($con);
?>