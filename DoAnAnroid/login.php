<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['player'])) {
    if ($db->dbConnect()) {
        if ($db->logIn("highscore", $_POST['player'])) {
            echo "Login Success";
        } else echo "No found player";
    } else echo "Error: Database connection";
} else echo "Error";
?>
