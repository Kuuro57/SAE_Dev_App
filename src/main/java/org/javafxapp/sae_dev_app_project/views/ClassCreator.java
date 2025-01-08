package org.javafxapp.sae_dev_app_project.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.javafxapp.sae_dev_app_project.classComponent.Constructor;
import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;

import java.util.ArrayList;

public class ClassCreator {

    private static ClassCreator instance;

    private String name = "";
    private ArrayList<Attribute> attributes = new ArrayList<>();
    private ArrayList<Constructor> constructors = new ArrayList<>();
    private ArrayList<Method> methods = new ArrayList<>();
    private String type = "";
    private VBox square = new VBox();

    private ClassCreator() {

    }

    public static ClassCreator getInstance() {
        if (instance == null) {
            instance = new ClassCreator();
        }
        return instance;
    }

    public void classCreation() {

        Stage form = new Stage(); // Nouvelle fenêtre
        form.setTitle("Créer une classe");

        HBox hbox = new HBox();
        hbox.setSpacing(100);

        //VBox de contenu
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setSpacing(15);

        // VBOX de prévisualisation
        ModelClass previs = new ModelClass(name, attributes, methods, constructors, type);
        VBox apercu = previs.getDisplay();
        square.getChildren().addAll(apercu, valid());
        square.setAlignment(Pos.CENTER);
        square.setMinWidth(150);
        square.setSpacing(30);

        //Titres des encarts
        Label title1 = new Label("Propriétés");
        title1.setPadding(new Insets(5));
        title1.setStyle("-fx-font-size: 16px;");

        Label title2 = new Label("Attributs");
        title2.setPadding(new Insets(5));
        title2.setStyle("-fx-font-size: 16px;");

        Label title3 = new Label("Constructeurs");
        title3.setPadding(new Insets(5));
        title3.setStyle("-fx-font-size: 16px;");

        Label title4 = new Label("Méthodes");
        title4.setPadding(new Insets(5));
        title4.setStyle("-fx-font-size: 16px;");



        // ------------------------------------------ //
        // -------------- PROPRIETES ---------------- //
        // ------------------------------------------ //

        // Encart Propriétés de la classe
        VBox proprietes = new VBox();
        proprietes.setPadding(new Insets(5));

        HBox hNameClass = new HBox();
        hNameClass.setPadding(new Insets(5));
        hNameClass.setSpacing(5);

        HBox hTypeClass = new HBox();
        hTypeClass.setPadding(new Insets(5));
        hTypeClass.setSpacing(5);

        Label nomClasse = new Label("Nom de la classe :");
        nomClasse.setPadding(new Insets(5));

        TextField className = new TextField();
        className.setPromptText("Nom");
        className.setOnAction(actionEvent -> {

            name = className.getText();
            updatePrevis();

        });

        Label typeClass = new Label("Type de la classe :");
        typeClass.setPadding(new Insets(5));

        ComboBox<String> classType = new ComboBox<>();
        classType.getItems().addAll("Class", "Interface", "Abstract");
        classType.setPromptText("Choisir");
        classType.setOnAction(actionEvent -> {
            type = classType.getValue().toLowerCase();
            updatePrevis();
        });

        hNameClass.getChildren().addAll(nomClasse, className);
        hTypeClass.getChildren().addAll(typeClass, classType);
        proprietes.getChildren().addAll(hNameClass, hTypeClass);




        // ------------------------------------------ //
        // -------------- ATTRIBUTS ----------------- //
        // ------------------------------------------ //

        // Encart Attributs de la classe
        VBox attributs = new VBox();
        attributs.setPadding(new Insets(5));

        HBox hAttName = new HBox();
        hAttName.setPadding(new Insets(5));
        hAttName.setSpacing(5);

        HBox hAttType = new HBox();
        hAttType.setPadding(new Insets(5));
        hAttType.setSpacing(5);

        HBox hAttModifier = new HBox();
        hAttModifier.setPadding(new Insets(5));
        hAttModifier.setSpacing(5);

        Label nomAtt = new Label("Nom de la attribut :");
        nomAtt.setPadding(new Insets(5));
        TextField attName = new TextField();
        attName.setPromptText("Nom");

        Label typeAtt = new Label("Type de la attribut :");
        typeAtt.setPadding(new Insets(5));
        TextField attType = new TextField();
        attType.setPromptText("Type");

        Label modifier1 = new Label("Modifiers :");
        modifier1.setPadding(new Insets(5));

        ComboBox<String> accesMod = new ComboBox<>();
        accesMod.getItems().addAll("public", "private", "protected");
        accesMod.setPromptText("Choisir");

        ComboBox<String> staticMod = new ComboBox<>();
        staticMod.getItems().addAll("static", "-");
        staticMod.setPromptText("Choisir");

        ComboBox<String> finalMod = new ComboBox<>();
        finalMod.getItems().addAll("final", "-");
        finalMod.setPromptText("Choisir");

        Button ajtAtt = new Button("Ajouter l'attribut");
        ajtAtt.setPadding(new Insets(5));
        ajtAtt.setOnAction(actionEvent -> {

            if (!attName.getText().isEmpty() && !attType.getText().isEmpty()) {
                Attribute a = new Attribute(
                        accesMod.getValue() + staticMod.getValue() + finalMod.getValue(),
                        attType.getText(),
                        attName.getText()
                );

                attributes.add(a);
                updatePrevis();
                attName.clear();
                attType.clear();
            }

        });

        hAttName.getChildren().addAll(nomAtt, attName);
        hAttType.getChildren().addAll(typeAtt, attType);
        hAttModifier.getChildren().addAll(modifier1, accesMod, staticMod, finalMod);
        attributs.getChildren().addAll(hAttName, hAttType, hAttModifier, ajtAtt);




        // ------------------------------------------ //
        // ------------- CONSTRUCTEURS -------------- //
        // ------------------------------------------ //

        // Encart Constructeurs de la classe
        VBox constructeurs = new VBox();
        constructeurs.setPadding(new Insets(5));

        HBox hConstMod = new HBox();
        hConstMod.setPadding(new Insets(5));
        hConstMod.setSpacing(5);

        HBox hConstParams = new HBox();
        hConstParams.setPadding(new Insets(5));
        hConstParams.setSpacing(5);

        Label modifierConst = new Label("Modifiers :");
        modifierConst.setPadding(new Insets(5));

        ComboBox<String> accesConst = new ComboBox<>();
        accesConst.getItems().addAll("public", "private", "protected");
        accesConst.setPromptText("Choisir");

        Label ajtParam = new Label("Ajouter un paramètre :");
        ajtParam.setPadding(new Insets(5));

        TextField nomConstParam = new TextField();
        nomConstParam.setPromptText("Nom");
        nomConstParam.setPadding(new Insets(5));

        TextField typeConstParam = new TextField();
        typeConstParam.setPromptText("Type");
        typeConstParam.setPadding(new Insets(5));

        Button ajtConstParam = new Button("Ajouter un paramètre");
        ajtConstParam.setPadding(new Insets(5));

        Button ajtConst = new Button("Ajouter un constructeur");
        ajtConst.setPadding(new Insets(5));

        hConstMod.getChildren().addAll(modifierConst, accesConst);
        hConstParams.getChildren().addAll(ajtParam, nomConstParam, typeConstParam, ajtConstParam);
        constructeurs.getChildren().addAll(hConstMod, hConstParams, ajtConst);




        // ------------------------------------------ //
        // ---------------- METHODES ---------------- //
        // ------------------------------------------ //

        // Encart Méthodes de la classe
        VBox methodes = new VBox();
        methodes.setPadding(new Insets(5));

        HBox hMethName = new HBox();
        hMethName.setPadding(new Insets(5));
        hMethName.setSpacing(5);

        HBox hMethType = new HBox();
        hMethType.setPadding(new Insets(5));
        hMethType.setSpacing(5);

        HBox hMethMod = new HBox();
        hMethMod.setPadding(new Insets(5));
        hMethMod.setSpacing(5);

        HBox hMethParams = new HBox();
        hMethParams.setPadding(new Insets(5));
        hMethParams.setSpacing(5);

        Label nomMeth = new Label("Nom :");
        nomMeth.setPadding(new Insets(5));
        TextField methName = new TextField();
        methName.setPromptText("Nom");

        Label typeMeth = new Label("Type de retour :");
        typeMeth.setPadding(new Insets(5));
        TextField methType = new TextField();
        methType.setPromptText("Type / Void");

        Label modMeth = new Label("Modifiers :");
        modMeth.setPadding(new Insets(5));

        ComboBox<String> accesMeth = new ComboBox<>();
        accesMeth.getItems().addAll("public", "private", "protected");
        accesMeth.setPromptText("Choisir");

        ComboBox<String> staticMeth = new ComboBox<>();
        staticMeth.getItems().addAll("static", "-");
        staticMeth.setPromptText("Choisir");

        Label ajtMethParam = new Label("Ajouter un paramètre :");
        ajtMethParam.setPadding(new Insets(5));

        TextField nomMethParam = new TextField();
        nomMethParam.setPromptText("Nom");
        nomMethParam.setPadding(new Insets(5));

        TextField typeMethParam = new TextField();
        typeMethParam.setPromptText("Type");
        typeMethParam.setPadding(new Insets(5));

        Button btnMethParam = new Button("Ajouter un paramètre");
        btnMethParam.setPadding(new Insets(5));

        Button btnMeth = new Button("Ajouter une méthode");
        btnMeth.setPadding(new Insets(5));


        hMethName.getChildren().addAll(nomMeth, methName);
        hMethType.getChildren().addAll(typeMeth, methType);
        hMethMod.getChildren().addAll(modMeth, accesMeth, staticMeth);
        hMethParams.getChildren().addAll(ajtMethParam, nomMethParam, typeMethParam, btnMethParam);
        methodes.getChildren().addAll(hMethName, hMethType, hMethMod, hMethParams, btnMeth);

        // --------------------------------------------- //

        vbox.getChildren().addAll(title1, proprietes, title2, attributs, title3, constructeurs, title4, methodes);
        vbox.autosize();

        hbox.getChildren().addAll(vbox, square);


        // Création de la scène et attachement au Stage
        Scene scene = new Scene(hbox); // Définir une taille pour la fenêtre
        form.setScene(scene);

        // Configurer la fenêtre comme modale
        form.initModality(Modality.APPLICATION_MODAL); // Empêche l'interaction sur la fenêtre précédente

        // Afficher la fenêtre
        form.showAndWait(); // Pause jusqu'à la fermeture de cette fenêtre
    }


    public void updatePrevis(){

        square.getChildren().clear();
        ModelClass previs = new ModelClass(name, attributes, methods, constructors, type);
        VBox apercu = previs.getDisplay();
        square.getChildren().addAll(apercu, valid());
        square.setAlignment(Pos.CENTER);
        square.setMinWidth(150);
        square.setSpacing(30);

    }

    public Button valid(){
        Button valider = new Button("Valider");
        valider.setPadding(new Insets(5));
        valider.setStyle("-fx-font-size: 14px;");

        return valider;
    }

}
