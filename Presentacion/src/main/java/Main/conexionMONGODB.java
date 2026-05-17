package Main;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class conexionMONGODB {

    private static final String CADENA_CONEXION = "mongodb://localhost:27017";
    private static final String NOMBRE_BASE_DATOS = "AdministradorCaja";

    private static MongoClient clienteMongo = null;
    private static MongoDatabase baseDatos = null;

    private conexionMONGODB() {
    }

    public static MongoDatabase getBaseDatos() {
        if (baseDatos == null) {
            try {
                clienteMongo = MongoClients.create(CADENA_CONEXION);
                baseDatos = clienteMongo.getDatabase(NOMBRE_BASE_DATOS);
                System.out.println("Conexión a MongoDB (" + NOMBRE_BASE_DATOS + ") establecida exitosamente.");
            } catch (Exception e) {
                System.err.println("Error al conectar con MongoDB: " + e.getMessage());
            }
        }
        return baseDatos;
    }

    public static void cerrarConexion() {
        if (clienteMongo != null) {
            clienteMongo.close();
            System.out.println("Conexión a MongoDB cerrada.");
        }
    }
}