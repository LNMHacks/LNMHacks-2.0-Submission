<?php
//checking if the script received a post request or not 
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
//Getting post data 
  define('HOST','localhost');
 define('USER','root');
 define('PASS','');
 define('DB','smart mart');
  
 
 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
  
   $id = $_POST['id'] ;
 
 //If the values are not blank
 //Connecting to our database by calling dbConnect script 
 //Creating an SQL Query to insert into database 
 $sql = "SELECT * FROM user WHERE id='$id'";
 
 $check = mysqli_fetch_array(mysqli_query($con,$sql));
 
 if(isset($check)){
 $firstname = $check['firstname'];
 $lastname = $check['lastname'];
 $status = $check['status'];
 $cartno = $check['cartno'];
 $money = $check['money'];
echo $firstname. ' '.$lastname.'#'.$money.'#'.$status.'#'.$cartno; 

}

mysqli_close($con);
}
?>


 