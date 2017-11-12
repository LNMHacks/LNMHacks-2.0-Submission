<?php
if(isset($_POST["Submit"])){
$name = $_POST["Cname"];
$email = $_POST["Cemail"];
$min_qual= $_POST["min_quality"];
$password = $_POST["password"];
$contactno= $_POST["Contactno"];
$city = $_POST["city"];
$jobs = $_POST["jobs"];
$age = $_POST["age"];
$Connection=mysql_connect('localhost','root','');

$Selected = mysql_select_db('spg',$Connection);


$Query = "INSERT INTO company(Cname,Cemail,Contactno,city,min_qual,age,jobs,companypassword) VALUES('$name','$email','$contactno','$city','$min_qual','$age','$jobs','$password')";
  
$execute = mysql_query($Query);
}
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

$sql = "SELECT userdetail.Name,userdetail.Email,userdetail.Contactno,userdetail.City from company,userdetail where company.min_qual=userdetail.max_qual and  company.city=userdetail.City and company.age>=userdetail.age";

		
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
		<caption class="title">Eligible Candidates for your Job</caption>
		<thead>
			<tr>
				<th>Name</th>
				<th>Email</th>
				<th>Contactno</th>
				<th>City</th>
			
			</tr>
		</thead>
		<tbody>
		<?php
		
		while ($row = mysqli_fetch_array($query,MYSQLI_ASSOC))
		{
			
			echo "<tr><td>";
			echo $row['Name'];
			echo "<tr><td>";
			echo $row['Email'];
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