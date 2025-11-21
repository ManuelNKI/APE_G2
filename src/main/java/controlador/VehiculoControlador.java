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

        // 1. Crear la solicitud GET
        HttpRequest getRequest;
        try {
            getRequest = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
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

    private String construirParametros(Vehiculo vehiculo) {
        // Se construye la cadena con el formato clave=valor&clave2=valor2
        return "MARCA=" + vehiculo.getMARCA()
                + "&MODELO=" + vehiculo.getMODELO()
                + "&PLACA=" + vehiculo.getPLACA()
                + "&CHASIS=" + vehiculo.getCHASIS()
                + "&ANIO=" + vehiculo.getANIO()
                + "&COLOR=" + vehiculo.getCOLOR();
    }

    public boolean insertarVehiculo(Vehiculo vehiculo) {

        // 1. Convertir el objeto a formato de formulario
        String formData = construirParametros(vehiculo);
        HttpRequest postRequest;
        try {
            postRequest = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    // 2. Se envía en forma de formulario
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    // 3. Se envía la cadena del formulario
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

    private String construirParametrosEditar(Vehiculo vehiculo) {
        // Se construye la cadena con el formato clave=valor&clave2=valor2
        return "ID=" + vehiculo.getID()
                + "&MARCA=" + vehiculo.getMARCA()
                + "&MODELO=" + vehiculo.getMODELO()
                + "&PLACA=" + vehiculo.getPLACA()
                + "&CHASIS=" + vehiculo.getCHASIS()
                + "&ANIO=" + vehiculo.getANIO()
                + "&COLOR=" + vehiculo.getCOLOR();
    }

    private String construirConsulta(String parametros) {
        return apiUrl + "?" + parametros;
    }

    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        try {
            // 1. Crear la cadena de parámetros
            String fullUrl = construirConsulta(construirParametrosEditar(vehiculo));
            // 2. Crear la solicitud PUT (se envía un cuerpo vacío ya que los datos van en la URL)
            HttpRequest putRequest = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .PUT(HttpRequest.BodyPublishers.noBody()) // Usamos noBody() porque los datos van en la URL
                    .build();
            // 3. Enviar la solicitud
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
            // 1. Crear la cadena de parámetro
            String fullUrl = construirConsulta(
                    "ID=" + URLEncoder.encode(id, StandardCharsets.UTF_8)
            );
            // 2. Crear la solicitud DELETE
            HttpRequest deleteRequest = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .DELETE()
                    .build();
            // 3. Enviar la solicitud
            HttpResponse<String> response = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

            return true;

        } catch (Exception e) {
            System.err.println("Error al eliminar el estudiante: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Vehiculo buscarVehiculo(String placa) {
        String urlBuscar = construirConsulta("PLACA=" + URLEncoder.encode(placa.trim(), StandardCharsets.UTF_8));
        try {
            HttpRequest peticionGet = HttpRequest.newBuilder()
                    .uri(new URI(urlBuscar))
                    .GET()
                    .build();

            HttpResponse<String> respuesta = httpClient.send(peticionGet, HttpResponse.BodyHandlers.ofString());

            if (respuesta.statusCode() == 200) {
                String jsonRespuesta = respuesta.body().toString();
                System.out.println(jsonRespuesta);
                Type tipoArray = new TypeToken<ArrayList<Vehiculo>>() {
                }.getType();
                ArrayList<Vehiculo> lista = gson.fromJson(jsonRespuesta, tipoArray);

                if (lista != null && !lista.isEmpty()) {
                    return lista.get(0);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
