# Sistema de Gestión de Vehículos

Aplicación Java con interfaz gráfica Swing para la gestión de vehículos mediante una API REST.

## Características

- ✅ Registro de vehículos (Marca, Modelo, Placa, Chasis, Año, Color)
- ✅ Actualización de información de vehículos
- ✅ Eliminación de vehículos con confirmación
- ✅ Búsqueda de vehículos por placa
- ✅ Validaciones completas en formularios
- ✅ Interfaz gráfica intuitiva con Swing
- ✅ Consumo de API REST con HttpClient

## Tecnologías

- Java 11+
- Maven
- Swing (GUI)
- Gson (JSON parsing)
- HttpClient (Java 11)
- API REST PHP (backend)

## Requisitos

- JDK 11 o superior
- Maven 3.6+
- API REST de vehículos en ejecución (http://localhost/Api_Vehiculos/api.php)

## Instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/TU_USUARIO/APE_G2.git
cd APE_G2
```

2. Compilar el proyecto:
```bash
mvn clean compile
```

3. Ejecutar la aplicación:
```bash
mvn exec:java
```

## Estructura del Proyecto

```
APE_G2/
├── src/
│   └── main/
│       └── java/
│           ├── controlador/
│           │   └── VehiculoControlador.java
│           ├── modelo/
│           │   └── Vehiculo.java
│           └── vista/
│               ├── FormularioRegistro.java
│               └── FormularioRegistro.form
├── pom.xml
└── README.md
```

## Uso

1. Inicia la aplicación
2. Usa el botón "Nuevo" para registrar un vehículo
3. Selecciona un vehículo de la tabla para editarlo o eliminarlo
4. Usa el filtro para buscar vehículos por placa

## Validaciones Implementadas

- Campos obligatorios: Marca, Modelo, Placa, Chasis, Año
- Validación de año numérico (1900-2100)
- Confirmación antes de eliminar
- Mensajes informativos de éxito/error
- Validación de placa única

## Autor

Lenovo LOQ

## Licencia

Este proyecto es de uso educativo.
