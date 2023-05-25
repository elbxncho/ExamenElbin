/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package elbinordinaria1;

/**
 *
 * @author madrid
 */
import java.sql.*;
import java.io.*;
import java.util.*;

// Clase base para las autoras
abstract class Autora {
    protected String nombre;
    protected String paisResidencia;
    protected String campoTrabajo;
  
    public Autora(String nombre, String paisResidencia, String campoTrabajo) {
        this.nombre = nombre;
        this.paisResidencia = paisResidencia;
        this.campoTrabajo = campoTrabajo;
    }
  
    // Método abstracto para realizar el trabajo específico de cada autora
    public abstract void realizarTrabajo();
  
    // Getters
    public String getNombre() {
        return nombre;
    }
  
    public String getPaisResidencia() {
        return paisResidencia;
    }
  
    public String getCampoTrabajo() {
        return campoTrabajo;
    }
}

// Clase para escritoras
class Escritora extends Autora {
    public Escritora(String nombre, String paisResidencia, String campoTrabajo) {
        super(nombre, paisResidencia, campoTrabajo);
    }
  
    @Override
    public void realizarTrabajo() {
        System.out.println("La escritora " + nombre + " está escribiendo un libro.");
    }
}

// Clase para directoras
class Directora extends Autora {
    public Directora(String nombre, String paisResidencia, String campoTrabajo) {
        super(nombre, paisResidencia, campoTrabajo);
    }
  
    @Override
    public void realizarTrabajo() {
        System.out.println("La directora " + nombre + " está dirigiendo una película.");
    }
}

// Clase para actrices
class Actriz extends Autora {
    public Actriz(String nombre, String paisResidencia, String campoTrabajo) {
        super(nombre, paisResidencia, campoTrabajo);
    }
  
    @Override
    public void realizarTrabajo() {
        System.out.println("La actriz " + nombre + " está interpretando un papel.");
    }
}

// Clase para científicas
class Cientifica extends Autora {
    public Cientifica(String nombre, String paisResidencia, String campoTrabajo) {
        super(nombre, paisResidencia, campoTrabajo);
    }
  
    @Override
    public void realizarTrabajo() {
        System.out.println("La científica " + nombre + " está haciendo un descubrimiento.");
    }
}

// Clase principal del programa
public class ElbinOrdinaria1 {
    private static Connection connection;
  
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean cargarDesdeBaseDatos = false;
      
        System.out.println("¿Deseas cargar los datos desde una base de datos? (S/N)");
        String opcion = scanner.nextLine();
      
        if (opcion.equalsIgnoreCase("S")) {
            cargarDesdeBaseDatos = true;
            conectarBaseDatos();
        }
      
        List<Autora> autoras = new ArrayList<>();
      
        if (cargarDesdeBaseDatos) {
            autoras = leerAutorasDesdeBaseDatos();
        } else {
            System.out.println("Ingresa la ruta del archivo CSV:");
            String rutaArchivo = scanner.nextLine();
            autoras = leerAutorasDesdeArchivoCSV(rutaArchivo);
        }
      
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcionMenu = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            
            switch (opcionMenu) {
                case 1:
                    mostrarDatosAutora(autoras, scanner);
                    break;
                case 2:
                    mostrarAutorasConMasPremios(autoras);
                    break;
                case 3:
                    mostrarAutorasPorPaisResidencia(autoras);
                    break;
                case 4:
                    mostrarAutorasPorCampoTrabajo(autoras);
                    break;
                case 5:
                    agregarAutora(autoras, scanner);
                    break;
                case 6:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida. Inténtalo nuevamente.");
                    break;
            }
        }
      
        guardarAutorasEnCSV(autoras);
        System.out.println("Programa finalizado. Los datos se han guardado en un archivo CSV.");
    }
  
    // Conexión a la base de datos
    private static void conectarBaseDatos() {
        String url = "jdbc:mysql://10.230.109.71:3306/Autoras";
        String usuario = "root";
        String contrasena = "";
      
        try {
            connection = DriverManager.getConnection(url, usuario, contrasena);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  
    // Leer las autoras desde la base de datos
    private static List<Autora> leerAutorasDesdeBaseDatos() {
        List<Autora> autoras = new ArrayList<>();
      
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM autoras";
            ResultSet resultSet = statement.executeQuery(query);
          
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String paisResidencia = resultSet.getString("pais_residencia");
                String campoTrabajo = resultSet.getString("campo_trabajo");
                String tipoAutora = resultSet.getString("tipo_autora");
              
                Autora autora = crearAutora(nombre, paisResidencia, campoTrabajo, tipoAutora);
                autoras.add(autora);
            }
          
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      
        return autoras;
    }
  
    // Leer las autoras desde un archivo CSV
    private static List<Autora> leerAutorasDesdeArchivoCSV(String rutaArchivo) {
        List<Autora> autoras = new ArrayList<>();
      
        try {
            BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
          
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",");
                String nombre = campos[0];
                String paisResidencia = campos[1];
                String campoTrabajo = campos[2];
                String tipoAutora = campos[3];
              
                Autora autora = crearAutora(nombre, paisResidencia, campoTrabajo, tipoAutora);
                autoras.add(autora);
            }
          
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
      
        return autoras;
    }
  
    // Crear una instancia de Autora según el tipo
    private static Autora crearAutora(String nombre, String paisResidencia, String campoTrabajo, String tipoAutora) {
        switch (tipoAutora) {
            case "escritora":
                return new Escritora(nombre, paisResidencia, campoTrabajo);
            case "directora":
                return new Directora(nombre, paisResidencia, campoTrabajo);
            case "actriz":
                return new Actriz(nombre, paisResidencia, campoTrabajo);
            case "cientifica":
                return new Cientifica(nombre, paisResidencia, campoTrabajo);
            default:
                throw new IllegalArgumentException("Tipo de autora inválido: " + tipoAutora);
        }
    }
  
    // Mostrar el menú al usuario
    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== MENÚ =====");
        System.out.println("1. Mostrar datos de una autora y realizar su trabajo");
        System.out.println("2. Mostrar autoras con más premios");
        System.out.println("3. Mostrar número de autoras por país de residencia");
        System.out.println("4. Mostrar número de autoras por campo de trabajo");
        System.out.println("5. Agregar una autora");
        System.out.println("6. Salir");
        System.out.print("Ingresa una opción: ");
    }
  
    // Mostrar los datos y realizar el trabajo de una autora seleccionada por el usuario
    private static void mostrarDatosAutora(List<Autora> autoras, Scanner scanner) {
        System.out.println("Ingresa el nombre de la autora:");
        String nombre = scanner.nextLine();
      
        boolean encontrada = false;
        for (Autora autora : autoras) {
            if (autora.getNombre().equalsIgnoreCase(nombre)) {
                encontrada = true;
                System.out.println("Nombre: " + autora.getNombre());
                System.out.println("País de residencia: " + autora.getPaisResidencia());
                System.out.println("Campo de trabajo: " + autora.getCampoTrabajo());
                autora.realizarTrabajo();
                break;
            }
        }
      
        if (!encontrada) {
            System.out.println("No se encontró una autora con ese nombre.");
        }
    }
  
    // Mostrar las autoras con más premios
    private static void mostrarAutorasConMasPremios(List<Autora> autoras) {
        // Implementa aquí la lógica para mostrar las autoras con más premios
    }
  
    // Mostrar el número de autoras por país de residencia
    private static void mostrarAutorasPorPaisResidencia(List<Autora> autoras) {
        // Implementa aquí la lógica para mostrar el número de autoras por país de residencia
    }
  
    // Mostrar el número de autoras por campo de trabajo
    private static void mostrarAutorasPorCampoTrabajo(List<Autora> autoras) {
        // Implementa aquí la lógica para mostrar el número de autoras por campo de trabajo
    }
  
    // Agregar una nueva autora
    private static void agregarAutora(List<Autora> autoras, Scanner scanner) {
        System.out.println("Ingresa el nombre de la autora:");
        String nombre = scanner.nextLine();
        System.out.println("Ingresa el país de residencia:");
        String paisResidencia = scanner.nextLine();
        System.out.println("Ingresa el campo de trabajo:");
        String campoTrabajo = scanner.nextLine();
        System.out.println("Ingresa el tipo de autora (escritora, directora, actriz o cientifica):");
        String tipoAutora = scanner.nextLine();
      
        Autora autora = crearAutora(nombre, paisResidencia, campoTrabajo, tipoAutora);
        autoras.add(autora);
      
        System.out.println("Autora agregada correctamente.");
    }
  
    // Guardar las autoras en un archivo CSV
    private static void guardarAutorasEnCSV(List<Autora> autoras) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("autoras.csv"));
          
            for (Autora autora : autoras) {
                bw.write(autora.getNombre() + "," + autora.getPaisResidencia() + "," + autora.getCampoTrabajo() + "," + autora.getClass().getSimpleName().toLowerCase());
                bw.newLine();
            }
          
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

