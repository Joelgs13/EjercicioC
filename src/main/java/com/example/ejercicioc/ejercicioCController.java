package com.example.ejercicioc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Persona;

/**
 * Controlador para la vista del ejercicio B que gestiona una lista de personas.
 * Permite agregar personas a una tabla después de validar los datos.
 */
public class ejercicioCController {

    @FXML
    private Button btn_modificar;

    @FXML
    private Button agregarButton;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField apellidosField;

    @FXML
    private TextField edadField;

    @FXML
    private TableView<Persona> personTable;

    @FXML
    private TableColumn<Persona, String> nombreColumn;

    @FXML
    private TableColumn<Persona, String> apellidosColumn;

    @FXML
    private TableColumn<Persona, Integer> edadColumn;

    private ObservableList<Persona> personasList = FXCollections.observableArrayList();

    /**
     * Inicializa la vista, lo que hace es vincular las columnas de la tabla con los datos de las personas, haciendo que cada variable sea correspondiente mas tarde a valores que
     * introduciremos en los textfields
     */
    @FXML
    public void initialize() {

        nombreColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        apellidosColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));
        edadColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEdad()).asObject());

        personTable.setItems(personasList);
    }

    /**
     * Agrega una nueva persona a la lista y la muestra en la tabla solo si los datos son válidos
     * y la persona no existe ya en ella.
     * Si los datos son incorrectos o la persona ya existe, muestra un mensaje de error.
     * Si la persona se agrega correctamente, muestra un mensaje de éxito.
     */
    @FXML
    private void agregar(ActionEvent event) {
        // Obtener la persona seleccionada (si existe)
        Persona personaSeleccionada = personTable.getSelectionModel().getSelectedItem();

        String nombre = nombreField.getText().trim();
        String apellidos = apellidosField.getText().trim();
        String edadText = edadField.getText().trim();
        StringBuilder errores = new StringBuilder();

        if (nombre.isEmpty()) {
            errores.append("El campo 'Nombre' no puede estar vacío.\n");
        }
        if (apellidos.isEmpty()) {
            errores.append("El campo 'Apellidos' no puede estar vacío.\n");
        }

        // edad es un número entero
        int edad = -1;
        try {
            edad = Integer.parseInt(edadText);
            if (edad < 0) {
                errores.append("La edad debe ser un número positivo.\n");
            }
        } catch (NumberFormatException e) {
            errores.append("El campo 'Edad' debe ser un número entero válido.\n");
        }

        // Si hay errores, mostrar error
        if (errores.length() > 0) {
            mostrarError(errores.toString());
            return;
        }

        // Crear una nueva persona con los datos ingresados
        Persona nuevaPersona = new Persona(nombre, apellidos, edad);

        // Verificar que la persona no sea duplicada (excepto la persona seleccionada en el caso de modificar)
        for (Persona persona : personasList) {
            if (!persona.equals(personaSeleccionada) && persona.equals(nuevaPersona)) {
                mostrarError("Persona duplicada: Ya existe una persona con los mismos datos.");
                return;
            }
        }

        // Si hay una persona seleccionada y se pulsa modificar
        if (personaSeleccionada != null && event.getSource()==btn_modificar) {
            personaSeleccionada.setNombre(nombre);
            personaSeleccionada.setApellido(apellidos);
            personaSeleccionada.setEdad(edad);
            personTable.refresh(); // Actualizamos la tabla con los cambios
            mostrarInformacion("Persona modificada con éxito.");
        } else if (personaSeleccionada == null && event.getSource()==agregarButton){
            // Si no hay persona seleccionada y se pulsa agregar
            personasList.add(nuevaPersona);
            mostrarInformacion("Persona agregada con éxito.");
        }

        // Limpiar los campos después de agregar o modificar
        limpiarCampos();
    }

    // Método para limpiar los campos después de agregar o modificar
    private void limpiarCampos() {
        nombreField.clear();
        apellidosField.clear();
        edadField.clear();
        personTable.getSelectionModel().clearSelection();
    }


    /**
     * Muestra un mensaje de error en una alerta emergente con los datos recogidos por el anterior metodo.
     *
     * @param mensaje Mensaje de error a mostrar.
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en los datos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje informativo en una alerta emergente.
     *
     * @param mensaje Mensaje informativo a mostrar.
     */
    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    public void eliminar(ActionEvent actionEvent) {

    }

    public void rellenar_campos(MouseEvent mouseEvent) {
        Persona personaSeleccionada = personTable.getSelectionModel().getSelectedItem();
        if (personaSeleccionada!=null) {
            nombreField.setText(personaSeleccionada.getNombre());
            apellidosField.setText(personaSeleccionada.getApellido());
            edadField.setText(String.valueOf(personaSeleccionada.getEdad()));
        }
    }
}
