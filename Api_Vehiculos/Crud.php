<?php
include_once "conn.php";

class CRUD
{

    public static function select()
    {
        $connect = new Conexion();
        $conectat = $connect->connect();
        //se crea la consulta
        $sqlSelect = "SELECT * FROM VEHICULO";
        //preparar la consulta
        //el prepare es para evitar inyecciones SQL porque
        //si se pone directamente en el execute puede ser peligroso 
        $result = $conectat->prepare($sqlSelect);
        // el execute ejecuta la consulta
        $result->execute();
        //fetchAll obtiene todos los registros
        // PDO::FETCH_ASSOC obtiene solo los nombres de las columnas
        // ejemplo: array("CEDULA"=>"123456","NOMBRE"=>"Juan")
        $data = $result->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($data);
    }

    public static function insert()
    {
        $connect = new Conexion();
        $conectat = $connect->connect();

        // se obtienen los datos enviados por POST esto se puede
        // cambiar por los datos que se quieran enviar
        // en este caso se envían los datos del estudiante
        $marca = $_POST['MARCA'];
        $modelo = $_POST['MODELO'];
        $placa = $_POST['PLACA'];
        $chasis = $_POST['CHASIS'];
        $anio = $_POST['ANIO'];
        $color = $_POST['COLOR'];


        // se crea la consulta
        $sqlInsert = "INSERT INTO VEHICULO (MARCA, MODELO, PLACA, CHASIS, ANIO, COLOR) VALUES ('$marca' ,'$modelo','$placa', '$chasis', '$anio', '$color')";
        $resultado = $conectat->prepare($sqlInsert);
        $resultado->execute();
        $data = "Insertado";
        echo json_encode($data);
    }

    public static function  selectPlaca()
    {
        $connect = new Conexion();
        $conectat = $connect->connect();

        if (!isset($_GET['PLACA'])) {
            http_response_code(400);
            echo json_encode(["error" => "Falta el parámetro 'PLACA'"]);
            return;
        }

        $placa = $_GET['PLACA'];
        $sqlSelect = "SELECT * FROM VEHICULO WHERE PLACA='$placa'";
        $result = $conectat->prepare($sqlSelect);
        $result->execute();
        $data = $result->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($data);
    }

    public static function  selectChasis()
    {
        $connect = new Conexion();
        $conectat = $connect->connect();

        if (!isset($_GET['CHASIS'])) {
            http_response_code(400);
            echo json_encode(["error" => "Falta el parámetro 'CHASIS'"]);
            return;
        }

        $chasis = $_GET['CHASIS'];
        $sqlSelect = "SELECT * FROM VEHICULO WHERE CHASIS='$chasis'";
        $result = $conectat->prepare($sqlSelect);
        $result->execute();
        $data = $result->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($data);
    }

    public static function update()
    {
        $connect = new Conexion();
        $conectat = $connect->connect();

        // se obtienen los datos enviados por PUT
        // OBTENEMOS LA cEDULA POR LA URL
        // EL Get es para obtener los datos de la URL
        // ejemplo: api.php?cedula=123456         
        $id = $_GET['ID'];
        $marca = $_GET['MARCA'];
        $modelo = $_GET['MODELO'];
        $placa = $_GET['PLACA'];
        $chasis = $_GET['CHASIS'];
        $anio = $_GET['ANIO'];
        $color = $_GET['COLOR'];
        // se crea la consulta
        $sqlUpdate = "UPDATE VEHICULO SET MARCA='$marca', MODELO='$modelo', PLACA='$placa', CHASIS='$chasis', ANIO='$anio', COLOR='$color' WHERE ID='$id'";
        $resultado = $conectat->prepare($sqlUpdate);
        $resultado->execute();
        $data = "Actualizado";
        echo json_encode($data);
    }

    public static function delete()
    {
        $connect = new Conexion();
        $conectat = $connect->connect();

        // se obtienen los datos enviados por DELETE
        // OBTENEMOS LA CEDULA POR LA URL
        // EL Get es para obtener los datos de la URL
        // ejemplo: api.php?CEDULA=123456
        if (isset($_GET['ID'])) {
            $id = $_GET['ID'];
        } else {
            http_response_code(400);
            echo json_encode(["error" => "Falta el parámetro 'ID'"]);
            return;
        }
        // se crea la consulta
        $sqlDelete = "DELETE FROM VEHICULO WHERE ID='$id'";
        $resultado = $conectat->prepare($sqlDelete);
        $resultado->execute();
        $data = "Eliminado";
        echo json_encode($data);
    }
}
