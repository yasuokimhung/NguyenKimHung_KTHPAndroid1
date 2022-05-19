<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['player']) && isset($_POST['score'])) {
    if ($db->dbConnect()) {
        if ($db->update("highscore", $_POST['player'], $_POST['score'])) {
            echo "Update Success";
        } else echo "Update failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
