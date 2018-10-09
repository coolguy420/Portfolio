<!DOCTYPE html>
<html>
<head>
	<title>Admin</title>
	<link id="style1" rel="stylesheet" type="text/css" href="admin.css" />
	<meta charset="UTF-8" />
</head>
<body>
<div id="wrapper">
    <div class ="top-header">Chollerton Tearooms</div>

    <nav>
      <ul>
        <li><a href="homepage.html" accesskey="h" tabindex="1"><span>Home</span></a></li>
        <li><a href="findOutMore.html" accesskey="f" tabindex="2"><span>Find out more</span></a></li>
        <li><a href="OfferPage.html" accesskey="o" tabindex="3"><span>Offers</span></a></li>
        <li><a href="credits.html" accesskey="c" tabindex="4"><span>Credit</span></a></li>
        <li><a href="admin.php" accesskey="a" tabindex="5"><span>Admin</span></a></li>
        <li><a href="Wireframe.pdf" accesskey="w" tabindex="6"><span>Wireframe</span></a></li>
      </ul>
    </nav>
    <section class="content">
    <h3>Customers seeking more information</h3>
    <br />
<?php

	$conn = mysqli_connect('localhost', 'unn_w15033021', 'nevermore', 'unn_w15033021');
  if (mysqli_connect_errno()) {
	echo "<p>Connection failed:".mysqli_connect_error()."</p>\n";}

$sql = "SELECT forename, surname, postalAddress, landLineTelNo, mobileTelNo, email, sendMethod FROM CT_expressedInterest";

	$queryresult = mysqli_query($conn, $sql) or die(mysqli_error($conn));

      while ($row = mysqli_fetch_assoc($queryresult)) {
      	$forename = $row['forename'];
            $surname = $row['surname'];
            $address = $row['postalAddress'];
	      $telephone = $row['landLineTelNo'];
	      $mobile = $row['mobileTelNo'];
	      $email = $row['email'];
	      $contact = $row['sendMethod']; 
	
            echo "<div><ul>
            <li>Forename: $forename</li>
            <li>Surname: $surname</li>
            <li>Address: $address</li>
            <li>Telephone: $telephone</li>
            <li>Mobile: $mobile</li>
            <li>Email: $email</li>
            <li>Contact Method: $contact</li></div> <hr />";
        } 
 
	mysqli_free_result($queryresult); 
      mysqli_close($conn);
?>

</section>
</section>


  <footer>
    <div>Copy Right 2015 Chollerton Tearoom and B&amp;B
    All rights reserved.
    </div>

    <div>Latest news
    Our new lunchtime menu is launching next week. Come back then to view it and for exclusive offers.
    </div>

    <div>
    <p>Contact us
    Chollerton Tea Room and B&amp;B, Chollerton, Northumberland, NE46</p>
    </div>

  </footer>

</body>
</html>