<?php
	require_once 'db_config.php';
	$db = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error " . mysqli_connect_error($link)); //new DB_CONNECT();
	//接資料
	$account = $_POST["Account"];
	$password = $_POST["PassWord"];
	//資料庫查詢資料
	$sql = "SELECT * FROM  `user` WHERE  Account = '"."$account'"; 
	$excute = mysqli_query($db,$sql);
	$row = mysqli_fetch_array($excute);
	if($row["PassWord"]==$password){
		echo "登入者為".$row["Name"];
	}
	else{
		echo "密碼錯誤";
	}
 ?>