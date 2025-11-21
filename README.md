# Sistema de Gestión de Vehículos 🚗

Aplicación Java con interfaz gráfica Swing para la gestión de vehículos mediante una API REST en PHP.

## Características ✨

- ✅ Registro de vehículos (Marca, Modelo, Placa, Chasis, Año, Color)
- ✅ Actualización de información de vehículos
- ✅ Eliminación de vehículos con confirmación
- ✅ Búsqueda de vehículos por placa
- ✅ Validaciones completas en formularios
- ✅ Interfaz gráfica intuitiva con Swing
- ✅ Consumo de API REST con HttpClient

## Tecnologías 💻

- Java 11+
- Maven
- Swing (GUI)
- Gson (JSON parsing)
- HttpClient (Java 11)
- API REST PHP (backend)
- MySQL (Base de datos)

## Requisitos Previos 📋

- **JDK 11 o superior** - [Descargar aquí](https://www.oracle.com/java/technologies/downloads/)
- **Apache NetBeans** (o cualquier IDE Java)
- **XAMPP** - [Descargar aquí](https://www.apachefriends.org/download.html)
- **Maven** (incluido en NetBeans)

## Instalación y Configuración 🚀

### Paso 1: Configurar la Base de Datos

1. **Instalar XAMPP** y ejecutarlo
2. **Iniciar los servicios**:
   - Apache
   - MySQL
3. **Copiar la carpeta `Api_Vehiculos`** en la carpeta `htdocs` de XAMPP:
   ```
   C:\xampp\htdocs\Api_Vehiculos
   ```
4. **Crear la base de datos**:
   - Abre http://localhost/phpmyadmin
   - Crea una base de datos llamada `vehiculos_db` (o el nombre que uses en tu API)
   - Importa el archivo SQL si lo tienes, o crea la tabla necesaria

### Paso 2: Clonar el Repositorio

```bash
git clone https://github.com/CarlitosssssssR/APE_G2.git
cd APE_G2
```

### Paso 3: Ejecutar la Aplicación

#### Opción 1: Usando NetBeans (Recomendado)

1. Abre **Apache NetBeans**
2. Ve a `File` → `Open Project`
3. Selecciona la carpeta `APE_G2`
4. Haz clic derecho en el proyecto → `Run`

#### Opción 2: Usando Maven (Línea de comandos)

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn exec:java -Dexec.mainClass="vista.FormularioRegistro"
```

#### Opción 3: Compilar y ejecutar con Java directamente

```bash
# Compilar
javac -cp "lib/*" src/main/java/**/*.java -d target/classes

# Ejecutar
java -cp "target/classes;lib/*" vista.FormularioRegistro
```

## Estructura del Proyecto 📁

```
APE_G2/
├── Api_Vehiculos/          # API REST en PHP (copiar a htdocs)
│   ├── api.php
│   ├── conn.php
│   └── Crud.php
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
├── .gitignore
└── README.md
```

## Configuración de la API 🔧

Asegúrate de que la URL de la API en `VehiculoControlador.java` coincida con tu configuración:

```java
private final String apiUrl = "http://localhost/Api_Vehiculos/api.php";
```

## Uso de la Aplicación 📖

1. **Nuevo Vehículo**:
   - Haz clic en el botón "Nuevo"
   - Completa todos los campos obligatorios
   - Haz clic en "Guardar"

2. **Editar Vehículo**:
   - Selecciona un vehículo de la tabla
   - Modifica los campos deseados
   - Haz clic en "Editar"

3. **Eliminar Vehículo**:
   - Selecciona un vehículo de la tabla
   - Haz clic en "Eliminar"
   - Confirma la eliminación

4. **Buscar Vehículo**:
   - Ingresa la placa en el campo de búsqueda
   - Haz clic en "Filtrar"

## Validaciones Implementadas ✔️

- Campos obligatorios: Marca, Modelo, Placa, Chasis, Año
- Validación de año numérico (1900-2100)
- Confirmación antes de eliminar
- Mensajes informativos de éxito/error
- Validación de placa única
- Prevención de campos vacíos

## Solución de Problemas 🔍

### Error: "Connection refused"
- Verifica que XAMPP esté ejecutándose
- Asegúrate de que Apache esté activo
- Verifica que la carpeta `Api_Vehiculos` esté en `htdocs`

### Error: "No se puede conectar a la base de datos"
- Verifica que MySQL esté activo en XAMPP
- Revisa las credenciales en `conn.php`

### Error al compilar
- Verifica que tengas JDK 11 o superior instalado
- Ejecuta `mvn clean install` para descargar las dependencias

## Autor 👨‍💻

**Carlos** - [CarlitosssssssR](https://github.com/CarlitosssssssR)

## Licencia 📄

Este proyecto es de uso educativo.

---

⭐ Si te fue útil este proyecto, no olvides darle una estrella en GitHub!
