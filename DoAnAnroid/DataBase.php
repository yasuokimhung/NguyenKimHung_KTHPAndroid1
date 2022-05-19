<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function logIn($table, $player)
    {
        $player = $this->prepareData($player);
        $this->sql = "select * from " . $table . " where player = '" . $player . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbplayer = $row['player'];
            if ($dbplayer == $player) {
                $login = true;
            } else $login = false;
        } else $login = false;

        return $login;
    }

    function signUp($table, $player)
    {
        $player = $this->prepareData($player);
        $this->sql ="INSERT INTO " . $table . " (player) VALUES ('" . $player . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }
    function update($table, $player, $score)
    {
        $player = $this->prepareData($player);
        $score = $this->prepareData($score);
        $this->sql ="INSERT INTO " . $table . " (player, score) 
            VALUES ('" . $player . "', '" . $score . "') 
            ON DUPLICATE KEY UPDATE 
            score = GREATEST(score, VALUES(score))";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

}

?>
