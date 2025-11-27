/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import modelo.Vehiculo;

/**
 *
 * @author Lenovo LOQ
 */
public class VehiculoControlador {

    private final String apiUrl = "http://localhost/Api_Vehiculos/api.php";
    private final Gson gson = new Gson();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ArrayList<Vehiculo> obtenerTodosLosVehiculos() {
        return obtener(apiUrl);
    }

    public ArrayList<Vehiculo> buscarVehiculo(String placa) {
        String urlBuscar = construirConsulta("PLACA=" + URLEncoder.encode(placa.trim(), StandardCharsets.UTF_8));
        return obtener(urlBuscar);
    }

    public ArrayList<Vehiculo> obtener(String url) {
        HttpRequest getRequest;
        try {
            getRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
        } catch (Exception e) {
            System.err.println("Error al crear la URI: " + e.getMessage());
            return null;
        }
        try {
            HttpResponse<String> response = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                Type tipoListaEstudiantes = new TypeToken<ArrayList<Vehiculo>>() {
                }.getType();
                ArrayList<Vehiculo> estudiantes = gson.fromJson(jsonResponse, tipoListaEstudiantes);
                return estudiantes;
            } else {
                System.err.println("Error HTTP al obtener estudiantes: " + response.statusCode());
                System.err.println("Respuesta del servidor: " + response.body());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error al enviar o procesar la solicitud GET: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean insertarVehiculo(Vehiculo vehiculo) {
        String formData = construirParametros(vehiculo);
        HttpRequest postRequest;
        try {
            postRequest = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(formData))
                    .build();
        } catch (Exception e) {
            System.err.println("Error al crear la solicitud: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        try {
            HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        try {
            String fullUrl = construirConsulta(construirParametrosEditar(vehiculo));
            HttpRequest putRequest = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .PUT(HttpRequest.BodyPublishers.noBody()) // Usamos noBody() porque los datos van en la URL
                    .build();
            HttpResponse<String> response = httpClient.send(putRequest, HttpResponse.BodyHandlers.ofString());
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar el vehiculo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarVehiculo(String id) {
        try {
            String fullUrl = construirConsulta(
                    "ID=" + URLEncoder.encode(id, StandardCharsets.UTF_8)
            );
            HttpRequest deleteRequest = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .DELETE()
                    .build();
            HttpResponse<String> response = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
            return true;

        } catch (Exception e) {
            System.err.println("Error al eliminar el estudiante: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private String construirConsulta(String parametros) {
        return apiUrl + "?" + parametros;
    }

    private String construirParametros(Vehiculo vehiculo) {
        return "MARCA=" + vehiculo.getMARCA()
                + "&MODELO=" + vehiculo.getMODELO()
                + "&PLACA=" + vehiculo.getPLACA()
                + "&CHASIS=" + vehiculo.getCHASIS()
                + "&ANIO=" + vehiculo.getANIO()
                + "&COLOR=" + vehiculo.getCOLOR();
    }

    private String construirParametrosEditar(Vehiculo vehiculo) {
        return "ID=" + vehiculo.getID()
                + "&MARCA=" + vehiculo.getMARCA()
                + "&MODELO=" + vehiculo.getMODELO()
                + "&PLACA=" + vehiculo.getPLACA()
                + "&CHASIS=" + vehiculo.getCHASIS()
                + "&ANIO=" + vehiculo.getANIO()
                + "&COLOR=" + vehiculo.getCOLOR();
    }
    

}
