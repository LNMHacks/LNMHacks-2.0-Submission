<?php
//checking if the script received a post request or not 
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
//Getting post data 
  define('HOST','localhost');
 define('USER','root');
 define('PASS','');
 define('DB','smart mart');
  
 
 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
  
   $id = $_POST['id'];
 //If the values are not blank
 //Connecting to our database by calling dbConnect script 
 //Creating an SQL Query to insert into database 
 $sql = "SELECT * FROM user WHERE id='$id'";
 
 $check = mysqli_fetch_array(mysqli_query($con,$sql));
 
 if(isset($check)){
 $cartno = 'CART1012';
 $sqlassign = "UPDATE user SET cartno='$cartno',status = 1 WHERE id='$id'";
 mysqli_query($con,$sqlassign);
 $money = $check['money'];
echo $money.'#'.$cartno.'#0';
 }

mysqli_close($con);
}
?>
 