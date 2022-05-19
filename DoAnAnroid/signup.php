<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['player'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("highscore", $_POST['player'])) {
            echo "Sign Up Success";
        } else echo "Sign up Failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
