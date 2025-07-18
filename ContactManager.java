// Importamos las librerías necesarias para usar listas, mapas, entrada por teclado y manejo de archivos
import java.util.*;
import java.io.*;

public class ContactManager {

    // Nombre del archivo donde se guardarán los contactos
    private static final String FILE_NAME = "contacts.txt";

    // Usamos un HashMap para guardar los contactos, la clave será el nombre en minúsculas para buscar rápido
    private Map<String, Contact> contacts = new HashMap<>();

    // Método principal: aquí empieza el programa
    public static void main(String[] args) {
        // Creamos un objeto de ContactManager para usar sus métodos y datos
        ContactManager manager = new ContactManager();

        // Cargamos los contactos guardados en el archivo (si existe)
        manager.loadContacts();

        // Creamos un objeto Scanner para leer la entrada que el usuario escribe en la consola
        Scanner scanner = new Scanner(System.in);

        // Empezamos un bucle infinito para mostrar el menú y responder a las opciones del usuario
        while (true) {
            // Mostramos el menú con las opciones disponibles
            System.out.println("\nContact Manager");
            System.out.println("1. Add Contact");      // Añadir un contacto
            System.out.println("2. Search Contact");   // Buscar un contacto por nombre
            System.out.println("3. List Contacts");    // Mostrar todos los contactos guardados
            System.out.println("4. Save and Exit");    // Guardar y salir del programa
            System.out.print("Choose option: ");

            // Leemos la opción que el usuario elige (una línea entera)
            String option = scanner.nextLine();

            // Según la opción, llamamos al método correspondiente
            switch(option) {
                case "1":
                    // Añadir contacto
                    manager.addContact(scanner);
                    break;
                case "2":
                    // Buscar contacto
                    manager.searchContact(scanner);
                    break;
                case "3":
                    // Listar todos los contactos
                    manager.listContacts();
                    break;
                case "4":
                    // Guardar contactos en archivo y salir
                    manager.saveContacts();
                    System.out.println("Contacts saved. Goodbye!");
                    scanner.close();  // Cerramos el Scanner para liberar recursos
                    return;           // Salimos del método main y termina el programa
                default:
                    // Si la opción no es válida, mostramos mensaje y volvemos a preguntar
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    // Método para añadir un contacto nuevo
    private void addContact(Scanner scanner) {
        System.out.print("Enter name: ");
        // Leemos el nombre del contacto que escribe el usuario
        String name = scanner.nextLine().trim();

        System.out.print("Enter phone: ");
        // Leemos el teléfono
        String phone = scanner.nextLine().trim();

        System.out.print("Enter email: ");
        // Leemos el email
        String email = scanner.nextLine().trim();

        // Comprobamos que ningún campo esté vacío
        if(name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            System.out.println("All fields are required.");  // Mensaje de error
            return;  // Salimos del método sin añadir contacto
        }

        // Creamos un nuevo objeto Contact con los datos recogidos
        Contact contact = new Contact(name, phone, email);

        // Añadimos el contacto al mapa, usando el nombre en minúsculas como clave para evitar duplicados por mayúsculas/minúsculas
        contacts.put(name.toLowerCase(), contact);

        System.out.println("Contact added.");
    }

    // Método para buscar un contacto por nombre
    private void searchContact(Scanner scanner) {
        System.out.print("Enter name to search: ");
        // Leemos el nombre a buscar y lo convertimos a minúsculas para buscar en el mapa
        String name = scanner.nextLine().trim().toLowerCase();

        // Buscamos el contacto en el mapa
        Contact contact = contacts.get(name);

        if(contact != null) {
            // Si existe, mostramos sus datos (se usa el método toString de Contact)
            System.out.println(contact);
        } else {
            // Si no está, avisamos que no se encontró
            System.out.println("Contact not found.");
        }
    }

    // Método para listar todos los contactos guardados
    private void listContacts() {
        // Si no hay contactos, avisamos
        if(contacts.isEmpty()) {
            System.out.println("No contacts.");
            return;
        }

        System.out.println("Contacts:");
        // Recorremos todos los contactos y los mostramos
        for(Contact contact : contacts.values()) {
            System.out.println(contact);
        }
    }

    // Método para guardar los contactos en un archivo de texto
    private void saveContacts() {
        // Usamos PrintWriter para escribir líneas en el archivo
        try(PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            // Para cada contacto, guardamos una línea con: nombre,teléfono,email separados por coma
            for(Contact contact : contacts.values()) {
                writer.println(contact.getName() + "," + contact.getPhone() + "," + contact.getEmail());
            }
        } catch(IOException e) {
            // Si hay error al guardar, lo mostramos por pantalla
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    // Método para cargar los contactos desde el archivo al iniciar el programa
    private void loadContacts() {
        // Usamos BufferedReader para leer el archivo línea a línea
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            // Leemos cada línea hasta el final del archivo
            while((line = reader.readLine()) != null) {
                // Separamos la línea en partes usando la coma como separador
                String[] parts = line.split(",");
                // Si la línea tiene las 3 partes (nombre, teléfono, email)
                if(parts.length == 3) {
                    // Creamos un contacto con esos datos
                    Contact contact = new Contact(parts[0], parts[1], parts[2]);
                    // Lo añadimos al mapa usando el nombre en minúsculas como clave
                    contacts.put(parts[0].toLowerCase(), contact);
                }
            }
        } catch(IOException e) {
            // Si el archivo no existe (primera vez), no hacemos nada
            // También podríamos avisar, pero no es necesario
        }
    }

    // Clase interna Contact para guardar los datos de cada contacto
    private static class Contact {
        private String name;
        private String phone;
        private String email;

        // Constructor que inicializa los campos
        public Contact(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }

        // Getters para acceder a los datos (podrías usarlos si hicieras más funciones)
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }

        // Método que devuelve una cadena con los datos del contacto para mostrar en consola
        @Override
        public String toString() {
            return "Name: " + name + ", Phone: " + phone + ", Email: " + email;
        }
    }
}