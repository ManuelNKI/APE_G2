<?php
// importa la clase CRUD que contiene los métodos
// para cada operación de la API 
include_once "Crud.php";
// --- ESTA ES LA LÍNEA QUE BUSCAS ---
header('Content-Type: application/json; charset=utf-8');
// -----------------------------------

// También es bueno agregar estas si vas a probar desde distintos lugares:
header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Headers: X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Request-Method");

// obtiene el método de la petición HTTP
// usando la variable superglobal $_SERVER
// y el índice 'REQUEST_METHOD'
// ejemplo de los métodos: GET, POST, PUT, DELETE
$opc = $_SERVER['REQUEST_METHOD'];

// estructura de control switch para llamar al método
// correspondiente según el método de la petición

switch ($opc) {
    case 'GET':
        if(isset($_GET['PLACA'])) {
        CRUD::selectPlaca(); 
        } else {
        CRUD::select();
        }
        break;
    
    case 'POST':
        CRUD::insert();
        break;
    case 'PUT':
        CRUD::update();
        break;
    case 'DELETE':
        CRUD::delete();
        break;
    default:
        # code...
        break;
}
