<!doctype html>
<html lang="en">
<head>
	<title>Results</title>
    <link id="style1" rel="stylesheet" type="text/css" href="postform.css" />
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
                <li><a href="credits.htlm" accesskey="c" tabindex="4"><span>Credit</span></a></li>
                <li><a href="admin.php" accesskey="a" tabindex="5"><span>Admin</span></a></li>
                <li><a href="Wireframe.pdf" accesskey="w" tabindex="6"><span>Wireframe</span></a></li>
            </ul>
        </nav>

    <div class="content">
    <h1>Your details</h1>
<?php
			// this is how the php connects to the server to input all the information into the SQL table.
            $conn = mysqli_connect('localhost', 'unn_w15033021', 'nevermore', 'unn_w15033021');
            if (mysqli_connect_errno()) {
                echo "<p>Connection failed:".mysqli_connect_error()."</p>\n";}

 $firstname = $_POST['firstname']; // this is a post method to get the first name from the form and show it

 echo "<p>First Name: $firstname</p>"; // a string that shows the firstname that was entered into the form.

// a method that checks if the firstname text field is empty, if it is it gives an approprite message and a link to go back to the form
if (empty($firstname)) {
    echo '<p>Sorry firstname needed ';
    echo '<a href="findOutMore.html">Go Back';
    echo '</a> to the form</p></body></html>';
    exit;}

// a post method retrieving the lastname
 $lastname = $_POST['lastname'];

 // a string showing the lsatname
 echo "<p>Last name: $lastname</p> ";

 // a method that checks if the lastname text field is empty, if it is it gives an approprite message and a link to go back to the form
if (empty($lastname)) {
    echo '<p>Sorry surname needed, ';
    echo '<a href="findOutMore.html">Go Back';
    echo '</a> to the form</p></body></html>';
    exit;}

// a post method that gets the email entered, if it isnt specified it sets it to null
 $email = isset ($_POST['email']) ? $_POST ['email'] : null;

  // a string showing the email
 echo "<p>Email: $email</p> ";


// a post method that gets the email entered, if it isnt specified it sets it to null
 $number = isset ($_POST['number']) ? $_POST['number'] : null;

  // a string showing the telephone number
 echo "<p>Telephone number: $number</p>";

// a post method that gets the email entered, if it isnt specified it sets it to null
 $mobile = isset ($_POST['mobile']) ? $_POST['mobile'] : null;

  // a string showing the mobile
 echo "<p>Mobile number: $mobile</p> ";


// post method retrieving the gender of the users input
 $gender= $_POST['gender'];

  // a string showing the gender
 echo "<p>Gender: $gender</p>";

 // a method that checks if the gender text field is empty, if it is it gives an approprite message and a link to go back to the form
if (empty($gender)) {
    echo '<p>Sorry gender needed, ';
    echo '<a href="findOutMore.html">Go Back';
    echo '</a> to the form</p></body></html>';
    exit;}

	// a post method retrieving the address line 1
 $address1 = $_POST['address1'];

  // a string showing address 1
 echo "<p>House Number: $address1 </p>";

// a post method that gets the email entered, if it isnt specified it sets it to null
 $address2 = isset ($_POST['address2']) ? $_POST['address2'] : null;

 // a string showing the address 2
 echo "<p>Address Line 2: $address2 </p>";

// a post method retrieving the address line 1
 $city = $_POST['city'];

  // a string showing the city
 echo "<p>City: $city </p>";

 // a method that checks if the city text field is empty, if it is it gives an approprite message and a link to go back to the form
if (empty($city)) {
    echo '<p>Sorry city needed, ';
    echo '<a href="findOutMore.html">Go Back';
    echo '</a> to the form</p></body></html>';
    exit;}

	// a post method retrieving the country
 $country = $_POST['country'];

 // a string showing the country
 echo "<p>Country: $country </p>";

 // a method that checks if the country text field is empty, if it is it gives an approprite message and a link to go back to the form
if (empty($country)) {
    echo '<p>Sorry country needed, ';
    echo '<a href="findOutMore.html">Go Back';
    echo '</a> to the form</p></body></html>';
    exit;}

// a post method retrieving the address line 1
 $postcode = $_POST['postcode'];

// a string showing the postcode
 echo "<p>Postcode: $postcode </p>";

// a method that checks if the postcode text field is empty, if it is it gives an approprite message and a link to go back to the form
if (empty($postcode)) {
    echo '<p>Sorry postcode needed, ';
    echo '<a href="findOutMore.html">Go Back';
    echo '</a> to the form</p></body></html>';
    exit;}

// a post method retrieving the room
 $room = $_POST['room'];

 // a string showing the room selected
 echo "<p>Type of room:  $room</p>";

// a post method retrieving the contact
 $contact = $_POST['contact'];

 // a string showing the contact
 echo "<p>Prefered contact method:  $contact</p>";


 // this is an if statement checking which contact method was chosen and appropritately displays an image for the chosen method.
 if($contact == 'SMS'){
    echo  "<img src = 'sms.jpg' alt='sms' email' width = 10% height= 10%>";
 }
 else if($contact == 'Email'){
    echo  "<img src = 'email.jpg' alt='email' width = 10% height= 10%>";
 }
 else{
    echo  "<img src = 'post.jpg' alt='post' email' width = 10% height= 10%>";
 }

 // a post method retrieving the comment
$comment = $_POST['comment'];

// an if statement displaying different messages what if the comment section is null.
 if ($comment == null) {
     echo "<p>there are no comments</p> ";
   } else {
     echo "<p>Comments: $comment</p>";
   }

   // a post method retrieving the terms and condition ticked
$terms = $_POST['terms'];



//this is an if statement checking if they provided the right information in comparison to how they want ot be contacted
// for example if they chose SMS and didnt provide a valid phone number.
if($contact === 'SMS' && empty($mobile))
{
	die ("<p>you asked to be contacted by SMS but did not provide a mobile number</P>");
}

if($contact === 'Email' && empty($email))
{
	die ("<p>you asked to be contacted by Email but did not provide an email address</P>");
}

if($contact === 'Post' && empty ($address1))
{
	die ("<p>you asked to be contacted by Post but did not provide a valid addressr</P>");
}


// an if statement checking if the terms and conditions are ticked, and generates an appropriate message.
if(empty($terms)){
    die("<p> You must agree to the terms and conditions!");
    }
    else{
        echo "<p>You have agreed to the terms and conditions!</p>";
}

			// this is how the information is inserted into the SQL table.
            $insertSQL="INSERT INTO CT_expressedInterest (forename, surname, postalAddress, landLineTelNo, mobileTelNo, email , sendMethod) VALUES ('$firstname', '$lastname', '$address1, $address2, $city, $country , $postcode' , '$number', '$mobile', '$email', '$contact')";
                $success=mysqli_query($conn, $insertSQL) or die (mysqli_error($conn));
                if ($success === false) {
                    echo "<p>sorry, a problem occured while trying to process your request. "; // a message if the connection fails
                    echo "<a href='formProcess.php'>Please try again</a></p>\n"; // a link to go back to the page.
                }
                else{
                    echo "<p>Thanks $firstname for your information, we'll be in touch!</p>\n"; // appropriate message for the user.
                }

                mysqli_close($conn);// closes connection to the SQL table.

?>
</div>
</div>
</body>
</html>

