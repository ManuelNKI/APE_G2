/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Lenovo LOQ
 */
public class Vehiculo {
    private String ID;
    private String MARCA;
    private String MODELO;
    private String CHASIS;
    private String PLACA;
    private String ANIO;
    private String COLOR;

    public Vehiculo(String MARCA, String MODELO, String PLACA, String CHASIS, String ANIO, String COLOR) {
        this.MARCA = MARCA;
        this.MODELO = MODELO;
        this.PLACA = PLACA;
        this.CHASIS = CHASIS;
        this.ANIO = ANIO;
        this.COLOR = COLOR;
    }

    public Vehiculo(String ID, String MARCA, String MODELO, String CHASIS, String PLACA, String ANIO, String COLOR) {
        this.ID = ID;
        this.MARCA = MARCA;
        this.MODELO = MODELO;
        this.CHASIS = CHASIS;
        this.PLACA = PLACA;
        this.ANIO = ANIO;
        this.COLOR = COLOR;
    }
    

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMARCA() {
        return MARCA;
    }

    public void setMARCA(String MARCA) {
        this.MARCA = MARCA;
    }

    public String getMODELO() {
        return MODELO;
    }

    public void setMODELO(String MODELO) {
        this.MODELO = MODELO;
    }

    public String getPLACA() {
        return PLACA;
    }

    public void setPLACA(String PLACA) {
        this.PLACA = PLACA;
    }

    public String getCHASIS() {
        return CHASIS;
    }

    public void setCHASIS(String CHASIS) {
        this.CHASIS = CHASIS;
    }

    public String getANIO() {
        return ANIO;
    }

    public void setANIO(String ANIO) {
        this.ANIO = ANIO;
    }

    public String getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }
    
    
}
