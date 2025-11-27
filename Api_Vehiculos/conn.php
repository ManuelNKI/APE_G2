<?php

class Conexion
{
    public function connect()
    {
        $servername = "localhost:3307";
        $username = "root";
        $password = "";
        $dbname = "catalogo";

        try {

            $connect = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
        } catch (Exception $e) {
            die("Fallo : " . $e->getMessage());
        }

        return $connect;
    }
}
