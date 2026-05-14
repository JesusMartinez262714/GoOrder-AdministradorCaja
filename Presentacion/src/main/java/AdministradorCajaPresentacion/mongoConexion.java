package AdministradorCajaPresentacion;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class mongoConexion {

    private static final String URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "AdministradorCaja";

    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    private mongoConexion() {}


    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create(URI);
                database = mongoClient.getDatabase(DATABASE_NAME);
                System.out.println(">>> Conexión a MongoDB establecida exitosamente.");
            } catch (Exception e) {
                System.err.println("Error crítico al conectar a MongoDB: " + e.getMessage());
            }
        }
        return database;
    }


    public static void cerrar() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            System.out.println(">>> Conexión a MongoDB cerrada.");
        }
    }
}