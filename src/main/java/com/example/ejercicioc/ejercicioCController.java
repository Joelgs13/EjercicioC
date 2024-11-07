package com.example.ejercicioc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Persona;

/**
 * Controlador para la vista del ejercicio C que gestiona una lista de personas.
 * Permite agregar, modificar y eliminar personas en una tabla, después de validar los datos.
 */
public class ejercicioCController {


    /**
     * El botón que permite modificar los datos de una persona seleccionada en la tabla.
     */
    @FXML
    private Button btn_modificar;


    /**
     * El botón que permite agregar una nueva persona a la lista.
     */
    @FXML
    private Button agregarButton;


    /**
     * El campo de texto que permite al usuario ingresar el nombre de una persona.
     */
    @FXML
    private TextField nombreField;


    /**
     * El campo de texto que permite al usuario ingresar los apellidos de una persona.
     */
    @FXML
    private TextField apellidosField;


    /**
     * El campo de texto que permite al usuario ingresar la edad de una persona.
     */
    @FXML
    private TextField edadField;


    /**
     * La tabla que muestra las personas en la interfaz de usuario.
     */
    @FXML
    private TableView<Persona> personTable;


    /**
     * La columna que muestra el nombre de cada persona en la tabla.
     */
    @FXML
    private TableColumn<Persona, String> nombreColumn;


    /**
     * La columna que muestra los apellidos de cada persona en la tabla.
     */
    @FXML
    private TableColumn<Persona, String> apellidosColumn;


    /**
     * La columna que muestra la edad de cada persona en la tabla.
     */
    @FXML
    private TableColumn<Persona, Integer> edadColumn;


    private ObservableList<Persona> personasList = FXCollections.observableArrayList();

    /**
     * Inicializa la vista y vincula las columnas de la tabla con los datos de las personas.
     * Hace que cada columna de la tabla se corresponda con los valores de los campos de texto.
     */
    @FXML
    public void initialize() {
        nombreColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        apellidosColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));
        edadColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEdad()).asObject());

        personTable.setItems(personasList);
    }

    /**
     * Agrega o modifica una persona en la tabla, dependiendo de si hay una persona seleccionada
     * y cuál botón fue presionado (Agregar o Modificar). Si no hay persona seleccionada y se
     * presiona "Agregar", se agrega una nueva persona. Si hay persona seleccionada y se presiona
     * "Modificar", se modifican sus datos.
     *
     * @param event Evento disparado por los botones Agregar o Modificar.
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
        if (personaSeleccionada != null && event.getSource() == btn_modificar) {
            personaSeleccionada.setNombre(nombre);
            personaSeleccionada.setApellido(apellidos);
            personaSeleccionada.setEdad(edad);
            personTable.refresh(); // Actualizamos la tabla con los cambios
            mostrarInformacion("Persona modificada con éxito.");
        } else if (personaSeleccionada == null && event.getSource() == agregarButton) {
            // Si no hay persona seleccionada y se pulsa agregar
            personasList.add(nuevaPersona);
            mostrarInformacion("Persona agregada con éxito.");
        }

        // Limpiar los campos después de agregar o modificar
        limpiarCampos();
    }

    /**
     * Elimina la persona seleccionada de la tabla. Si no hay persona seleccionada,
     * muestra un mensaje de error. Después de eliminar, limpia los campos del formulario.
     *
     * @param actionEvent Evento disparado por el botón Eliminar.
     */
    @FXML
    public void eliminar(ActionEvent actionEvent) {
        // Obtener la persona seleccionada en la tabla
        Persona personaSeleccionada = personTable.getSelectionModel().getSelectedItem();

        // Verificar si hay una persona seleccionada
        if (personaSeleccionada == null) {
            mostrarError("Debe seleccionar una persona para eliminar.");
            return;
        }

        // Eliminar la persona seleccionada de la lista
        personasList.remove(personaSeleccionada);

        // Mostrar mensaje de confirmación
        mostrarInformacion("Persona eliminada con éxito.");

        // Limpiar los campos y deseleccionar cualquier fila
        limpiarCampos();
    }

    /**
     * Rellena los campos de texto con los datos de la persona seleccionada en la tabla
     * cuando se hace clic en una fila. Si no hay persona seleccionada, no hace nada.
     *
     * @param mouseEvent Evento de clic en la tabla.
     */
    public void rellenar_campos(MouseEvent mouseEvent) {
        Persona personaSeleccionada = personTable.getSelectionModel().getSelectedItem();
        if (personaSeleccionada != null) {
            nombreField.setText(personaSeleccionada.getNombre());
            apellidosField.setText(personaSeleccionada.getApellido());
            edadField.setText(String.valueOf(personaSeleccionada.getEdad()));
        }
    }

    /**
     * Limpia los campos de texto del formulario y deselecciona cualquier fila de la tabla.
     * Se llama después de agregar, modificar o eliminar una persona.
     */
    private void limpiarCampos() {
        nombreField.clear();
        apellidosField.clear();
        edadField.clear();
        personTable.getSelectionModel().clearSelection();
    }

    /**
     * Muestra un mensaje de error en una alerta emergente.
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
}

