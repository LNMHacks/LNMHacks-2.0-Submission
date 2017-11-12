<?php 
if(isset($_POST["Submit"])){
	echo "hi";
$name = $_POST["name"];
$email = $_POST["email"];
$max_qual= $_POST["max_qual"];
$password = $_POST["password"];
$contactno= $_POST["contactno"];
$city = $_POST["city"];
$gender=$_POST["gender"];
$age = $_POST["age"];
$Connection=mysql_connect('localhost','root','');
$Selected=mysql_select_db('spg',$Connection);

$Query = "INSERT INTO userdetail(Name,Email,Contactno,City,max_qual,age,gender,userpassword) VALUES('$name','$email','$contactno','$city','$max_qual','$age','$gender','$password')";
  
$execute = mysql_query($Query);}



?>

<?php
$db_host = 'localhost'; // Server Name
$db_user = 'root'; // Username
$db_pass = ''; // Password
$db_name = 'spg'; // Database Name

$conn = mysqli_connect($db_host, $db_user, $db_pass, $db_name);
if (!$conn) {
	die ('Failed to connect to MySQL: ' . mysqli_connect_error());	
}


$sql = "select Cname,Cemail,company.Contactno,company.city from company,userdetail where company.city=userdetail.City and company.min_qual=userdetail.max_qual and company.age>=userdetail.age";

		
$query = mysqli_query($conn, $sql);

if (!$query) {
	die ('SQL Error: ' . mysqli_error($conn));
}
?>
<html>
<head>
	<title>Displaying MySQL Data in HTML Table</title>
	<style type="text/css">
		body {
			font-size: 15px;
			color: #343d44;
			font-family: "segoe-ui", "open-sans", tahoma, arial;
			padding: 0;
			margin: 0;
		}
		table {
			margin: auto;
			font-family: "Lucida Sans Unicode", "Lucida Grande", "Segoe Ui";
			font-size: 12px;
		}

		h1 {
			margin: 25px auto 0;
			text-align: center;
			text-transform: uppercase;
			font-size: 17px;
		}

		table td {
			transition: all .5s;
		}
		
		/* Table */
		.data-table {
			border-collapse: collapse;
			font-size: 14px;
			min-width: 537px;
		}

		.data-table th, 
		.data-table td {
			border: 1px solid #e1edff;
			padding: 7px 17px;
		}
		.data-table caption {
			margin: 7px;
		}

		/* Table Header */
		.data-table thead th {
			background-color: #508abb;
			color: #FFFFFF;
			border-color: #6ea1cc !important;
			text-transform: uppercase;
		}

		/* Table Body */
		.data-table tbody td {
			color: #353535;
		}
		.data-table tbody td:first-child,
		.data-table tbody td:nth-child(4),
		.data-table tbody td:last-child {
			text-align: right;
		}

		.data-table tbody tr:nth-child(odd) td {
			background-color: #f4fbff;
		}
		.data-table tbody tr:hover td {
			background-color: #ffffa2;
			border-color: #ffff0f;
		}

		/* Table Footer */
		.data-table tfoot th {
			background-color: #e5f5ff;
			text-align: right;
		}
		.data-table tfoot th:first-child {
			text-align: left;
		}
		.data-table tbody td:empty
		{
			background-color: #ffcccc;
		}
	</style>
</head>
<body>
	
	<table class="data-table">
		<caption class="title">Companies where You can get Jobs</caption>
		<thead>
			<tr>
				<th>Company Name</th>
				<th>Company Email</th>
				<th>Company Contactno</th>
				<th>City</th>
			
			</tr>
		</thead>
		<tbody>
		<?php
		
		while ($row = mysqli_fetch_array($query,MYSQLI_ASSOC))
		{
			
			echo "<tr><td>";
			echo $row['CName'];
			echo "<tr><td>";
			echo $row['CEmail'];
			echo "<tr><td>";
			echo $row['City'];
			echo "<tr><td>";
			echo $row['Contactno'];
			echo "</td></tr>";
		}?>
		</tbody>
		
	</table>
</body>
</html>