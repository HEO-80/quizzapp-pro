package com.example.Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



    public class DbConnection {
        private static final String URL = "jdbc:oracle:thin:@//localhost:1521/xe"; // Cambia "localhost" y "xe" según tu configuración
        private static final String USERNAME = "dbquiz"; // Reemplaza con tu usuario de SQL Developer
        private static final String PASSWORD = "dbquiz"; // Reemplaza con tu contraseña de SQL Developer

        private static Connection connection;

        // Método para obtener la conexión
        public static Connection getConnection() throws SQLException {
            if (connection == null || connection.isClosed()) {
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver"); // Cargar el driver de Oracle
                    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    System.out.println("Conexión exitosa a la base de datos.");
                } catch (ClassNotFoundException e) {
                    System.err.println("Error al cargar el driver de la base de datos.");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.err.println("Error al establecer la conexión con la base de datos.");
                    e.printStackTrace();
                    throw e;
                }
            }
            return connection;
        }

        // Método para cerrar la conexión
        public static void closeConnection() {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("Conexión cerrada correctamente.");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión con la base de datos.");
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {
            try {
                Connection conn = DbConnection.getConnection();
                if (conn != null) {
                    System.out.println("¡Conexión establecida!");
                }
                DbConnection.closeConnection();
            } catch (SQLException e) {
                System.err.println("Error al conectar: " + e.getMessage());
            }
        }
    }
