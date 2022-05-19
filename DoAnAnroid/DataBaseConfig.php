<?php

class DataBaseConfig
{
    public $servername;
    public $player;
    public $score;
    public $databasename;

    public function __construct()
    {

        $this->servername = 'localhost';
        $this->username = 'root';
        $this->password = '';
        $this->databasename = 'doanandroid1';

    }
}

?>
