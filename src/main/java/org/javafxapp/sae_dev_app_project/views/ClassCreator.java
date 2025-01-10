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
import org.javafxapp.sae_dev_app_project.classComponent.Parameter;
import org.javafxapp.sae_dev_app_project.importExport.Export;
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
    private ModelClass previs = new ModelClass(name, attributes, methods, constructors, type);
    private VBox apercu;
    private ViewAllClasses view;
    private Button valider = new Button("Valider");

    // Singleton pour les éléments que l'on souhaite afficher une seule et unique fois à chaque chargement de la fenêtre
    private ClassCreator(ViewAllClasses view) {

        this.view = view;
        apercu = previs.getDisplay(view);
        square.getChildren().addAll(apercu, valider);
        square.setAlignment(Pos.CENTER);
        square.setMinWidth(150);
        square.setSpacing(30);
        previs.setVisibility(true);

    }

    public static ClassCreator getInstance(ViewAllClasses view) {
        if (instance == null) {
            instance = new ClassCreator(view);
        }
        return instance;
    }

    // Méthode de création du formulaire avec tous les champs nécessaires
    public void classCreation() {

        Stage form = new Stage(); // Nouvelle fenêtre
        form.setTitle("Créer une classe");

        HBox hbox = new HBox();
        hbox.setSpacing(100);

        //VBox de contenu
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setSpacing(15);

        // Bouton de création de la classe en l'état
        valider.setPadding(new Insets(5));
        valider.setStyle("-fx-font-size: 14px;");
        valider.setOnAction(actionEvent -> {

            ModelClass classe = new ModelClass(name, attributes, methods, constructors, type);
            classe.setId(ModelClass.getNewId());
            classe.addObserver(view);
            classe.setVisibility(true);
            view.addClass(classe);
            form.close();

        });

        //Titres des encarts
        Label title1 = new Label("Propriétés");
        title1.setPadding(new Insets(2));
        title1.setStyle("-fx-font-size: 16px;");

        Label title2 = new Label("Attributs");
        title2.setPadding(new Insets(2));
        title2.setStyle("-fx-font-size: 16px;");

        Label title3 = new Label("Constructeurs");
        title3.setPadding(new Insets(2));
        title3.setStyle("-fx-font-size: 16px;");

        Label title4 = new Label("Méthodes");
        title4.setPadding(new Insets(2));
        title4.setStyle("-fx-font-size: 16px;");



        // ------------------------------------------ //
        // -------------- PROPRIETES ---------------- //
        // ------------------------------------------ //

        // Encart Propriétés de la classe
        VBox proprietes = new VBox();
        proprietes.setPadding(new Insets(2));

        HBox hNameClass = new HBox();
        hNameClass.setPadding(new Insets(2));
        hNameClass.setSpacing(2);

        HBox hTypeClass = new HBox();
        hTypeClass.setPadding(new Insets(2));
        hTypeClass.setSpacing(2);

        Label nomClasse = new Label("Nom de la classe :");
        nomClasse.setPadding(new Insets(2));

        TextField className = new TextField();
        className.setPromptText("Nom");

        Label typeClass = new Label("Type de la classe :");
        typeClass.setPadding(new Insets(2));

        ComboBox<String> classType = new ComboBox<>();
        classType.getItems().addAll("Class", "Interface", "Abstract");
        classType.setPromptText("Choisir");
        classType.setOnAction(actionEvent -> {
            name = className.getText();
            System.out.println(className.getText());
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
        attributs.setPadding(new Insets(2));

        HBox hAttName = new HBox();
        hAttName.setPadding(new Insets(2));
        hAttName.setSpacing(2);

        HBox hAttType = new HBox();
        hAttType.setPadding(new Insets(2));
        hAttType.setSpacing(2);

        HBox hAttModifier = new HBox();
        hAttModifier.setPadding(new Insets(2));
        hAttModifier.setSpacing(2);

        Label nomAtt = new Label("Nom de la attribut :");
        nomAtt.setPadding(new Insets(2));
        TextField attName = new TextField();
        attName.setPromptText("Nom");

        Label typeAtt = new Label("Type de la attribut :");
        typeAtt.setPadding(new Insets(2));
        TextField attType = new TextField();
        attType.setPromptText("Type");

        Label modifier1 = new Label("Modifiers :");
        modifier1.setPadding(new Insets(2));

        ComboBox<String> accesMod = new ComboBox<>();
        accesMod.getItems().addAll("public", "private", "protected");
        accesMod.setPromptText("Choisir");

        ComboBox<String> staticMod = new ComboBox<>();
        staticMod.getItems().addAll("static", " ");
        staticMod.setPromptText("Choisir");

        ComboBox<String> finalMod = new ComboBox<>();
        finalMod.getItems().addAll("final", " ");
        finalMod.setPromptText("Choisir");

        Button ajtAtt = new Button("Ajouter l'attribut");
        ajtAtt.setPadding(new Insets(4));
        ajtAtt.setOnAction(actionEvent -> {

            if (!attName.getText().isEmpty() && !attType.getText().isEmpty()) {
                Attribute a = new Attribute(
                        accesMod.getValue() + " " + staticMod.getValue() + " " + finalMod.getValue(),
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
        constructeurs.setPadding(new Insets(2));

        HBox hConstMod = new HBox();
        hConstMod.setPadding(new Insets(2));
        hConstMod.setSpacing(2);

        HBox hConstParams = new HBox();
        hConstParams.setPadding(new Insets(2));
        hConstParams.setSpacing(2);

        Label modifierConst = new Label("Modifiers :");
        modifierConst.setPadding(new Insets(2));

        ComboBox<String> accesConst = new ComboBox<>();
        accesConst.getItems().addAll("public", "private", "protected");
        accesConst.setPromptText("Choisir");

        Label ajtParam = new Label("Ajouter un paramètre :");
        ajtParam.setPadding(new Insets(2));

        TextField nomConstParam = new TextField();
        nomConstParam.setPromptText("Nom");
        nomConstParam.setPadding(new Insets(2));

        TextField typeConstParam = new TextField();
        typeConstParam.setPromptText("Type");
        typeConstParam.setPadding(new Insets(2));

        Button ajtConstParam = new Button("Ajouter un paramètre");
        ajtConstParam.setPadding(new Insets(4));
        ajtConstParam.setOnAction(actionEvent -> {

            if (!typeConstParam.getText().isEmpty() && !nomConstParam.getText().isEmpty()){

                Parameter p = new Parameter(typeConstParam.getText(), nomConstParam.getText());

                if (!constructors.isEmpty()) {

                    Constructor c = constructors.getLast();
                    c.addParameter(p);

                } else {

                    Constructor c = new Constructor(accesConst.getValue(), className.getText());
                    c.addParameter(p);
                    constructors.add(c);

                }

            }

            updatePrevis();
            typeConstParam.clear();
            nomConstParam.clear();

        });

        Button ajtConst = new Button("Ajouter un constructeur");
        ajtConst.setPadding(new Insets(4));
        ajtConst.setOnAction(actionEvent -> {

            Constructor c = new Constructor(accesConst.getValue(), className.getText());
            constructors.add(c);
            updatePrevis();

        });

        hConstMod.getChildren().addAll(modifierConst, accesConst, ajtConst);
        hConstParams.getChildren().addAll(ajtParam, nomConstParam, typeConstParam, ajtConstParam);
        constructeurs.getChildren().addAll(hConstMod, hConstParams);




        // ------------------------------------------ //
        // ---------------- METHODES ---------------- //
        // ------------------------------------------ //

        // Encart Méthodes de la classe
        VBox methodes = new VBox();
        methodes.setPadding(new Insets(2));

        HBox hMethName = new HBox();
        hMethName.setPadding(new Insets(2));
        hMethName.setSpacing(2);

        HBox hMethType = new HBox();
        hMethType.setPadding(new Insets(2));
        hMethType.setSpacing(2);

        HBox hMethMod = new HBox();
        hMethMod.setPadding(new Insets(2));
        hMethMod.setSpacing(2);

        HBox hMethParams = new HBox();
        hMethParams.setPadding(new Insets(2));
        hMethParams.setSpacing(2);

        Label nomMeth = new Label("Nom :");
        nomMeth.setPadding(new Insets(2));
        TextField methName = new TextField();
        methName.setPromptText("Nom");

        Label typeMeth = new Label("Type de retour :");
        typeMeth.setPadding(new Insets(2));
        TextField methType = new TextField();
        methType.setPromptText("Type / Void");

        Label modMeth = new Label("Modifiers :");
        modMeth.setPadding(new Insets(2));

        ComboBox<String> accesMeth = new ComboBox<>();
        accesMeth.getItems().addAll("public", "private", "protected");
        accesMeth.setPromptText("Choisir");

        ComboBox<String> staticMeth = new ComboBox<>();
        staticMeth.getItems().addAll("static", " ");
        staticMeth.setPromptText("Choisir");

        Label ajtMethParam = new Label("Ajouter un paramètre :");
        ajtMethParam.setPadding(new Insets(2));

        TextField nomMethParam = new TextField();
        nomMethParam.setPromptText("Nom");
        nomMethParam.setPadding(new Insets(2));

        TextField typeMethParam = new TextField();
        typeMethParam.setPromptText("Type");
        typeMethParam.setPadding(new Insets(2));

        Button btnMethParam = new Button("Ajouter un paramètre");
        btnMethParam.setPadding(new Insets(4));
        btnMethParam.setOnAction(actionEvent -> {

            if (!typeMethParam.getText().isEmpty() && !nomMethParam.getText().isEmpty()){

                Parameter p = new Parameter(typeMethParam.getText(), nomMethParam.getText());

                if (!methods.isEmpty()) {

                    Method m = methods.getLast();
                    m.addParameter(p);

                } else {
                    Method m = new Method(accesMeth.getValue() + " " + staticMeth.getValue(), methName.getText(), new ArrayList<Parameter>(), methType.getText());
                    m.addParameter(p);
                    methods.add(m);

                }

            }

            updatePrevis();
            typeMethParam.clear();
            nomMethParam.clear();
            methName.clear();
            methType.clear();

        });

        Button btnMeth = new Button("Ajouter une méthode");
        btnMeth.setPadding(new Insets(4));
        btnMeth.setOnAction(actionEvent -> {

            Method m = new Method(accesMeth.getValue() + " " + staticMeth.getValue(), methName.getText(), methType.getText());
            methods.add(m);
            updatePrevis();

        });


        hMethName.getChildren().addAll(nomMeth, methName);
        hMethType.getChildren().addAll(typeMeth, methType, btnMeth);
        hMethMod.getChildren().addAll(modMeth, accesMeth, staticMeth);
        hMethParams.getChildren().addAll(ajtMethParam, nomMethParam, typeMethParam, btnMethParam);
        methodes.getChildren().addAll(hMethName, hMethType, hMethMod, hMethParams);

        // --------------------------------------------- //

        vbox.getChildren().addAll(title1, proprietes, title2, attributs, title3, constructeurs, title4, methodes);
        vbox.autosize();

        hbox.getChildren().addAll(vbox, square);


        // Création de la scène et attachement au Stage
        Scene scene = new Scene(hbox);// Définir une taille pour la fenêtre
        form.setMinWidth(850);
        form.setScene(scene);

        // Configurer la fenêtre comme modale
        form.initModality(Modality.APPLICATION_MODAL); // Empêche l'interaction sur la fenêtre précédente

        // Afficher la fenêtre
        form.showAndWait(); // Pause jusqu'à la fermeture de cette fenêtre
    }

    // Méthode d'actualisation du rendu de prévisualisation
    public void updatePrevis(){

        square.getChildren().remove(0);
        previs.setName(name);
        VBox apercu = previs.getDisplay(view);
        square.getChildren().add(0, apercu);
        square.setAlignment(Pos.CENTER);
        square.setMinWidth(150);
        square.setSpacing(30);

    }

}
