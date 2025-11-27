/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author ramir
 */
public class Validador {
    // --- NUEVO MÉTODO DE VALIDACIÓN ---

    public String validarDatos(String marca, String modelo, String placa, String chasis, String anio, String color) {

        // 1. Validar vacíos
        if (marca.isEmpty()) {
            return "El campo MARCA es obligatorio";
        }
        if (modelo.isEmpty()) {
            return "El campo MODELO es obligatorio";
        }
        if (placa.isEmpty()) {
            return "El campo PLACA es obligatorio";
        }
        if (chasis.isEmpty()) {
            return "El campo CHASIS es obligatorio";
        }
        if (anio.isEmpty()) {
            return "El campo AÑO es obligatorio";
        }
        if (color.isEmpty()) {
            return "El campo AÑO es obligatorio";
        }

        // 2. Validar Año Numérico y Rango
        try {
            int anioNumerico = Integer.parseInt(anio);
            if (anioNumerico < 1900 || anioNumerico > 2100) {
                return "El año debe estar entre 1900 y 2100";
            }
        } catch (NumberFormatException e) {
            return "El año debe ser un número válido";
        }

        // 3. Validar Formatos con Regex
        if (!chasis.matches("^[A-Z0-9]+$")) {
            return "El chasis solo puede contener letras y números.";
        }

        if (!placa.matches("^[A-Z]{3}-\\d{4}$")) {
            return "La placa debe tener el formato AAA-1234 (Ej: PCH-1234)";
        }

        return null; // Retorna null si TODO está correcto
    }
}
