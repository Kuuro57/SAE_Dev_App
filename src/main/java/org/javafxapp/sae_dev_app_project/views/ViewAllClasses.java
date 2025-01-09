package org.javafxapp.sae_dev_app_project.views;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.javafxapp.sae_dev_app_project.classComponent.Constructor;
import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.importExport.Export;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.javafxapp.sae_dev_app_project.classComponent.Constructor;
import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.importExport.Import;
import org.javafxapp.sae_dev_app_project.menuHandler.ContextMenuHandler;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import java.util.ArrayList;


/**
 * Classe qui représente la vue qui contient toutes les classes représentées graphiquements
 */
public class ViewAllClasses extends Pane implements Observer {

    // Attributs
    private ArrayList<ModelClass> allClassesList; // Liste qui contient toutes les classes sur le diagramme
    private VBox draggedBox;
    private double initialMouseX;
    private double initialMouseY;
    private double offsetX; // Décalage en X entre la souris et le coin haut-gauche de la VBox
    private double offsetY; // Décalage en Y entre la souris et le coin haut-gauche de la VBox
    private Node parentNode; // Le conteneur parent de la boîte, pour calculer correctement les coordonnées


    /**
     * Constructeur de la classe
     */
    public ViewAllClasses() {
        this.allClassesList = new ArrayList<>(1000);
    }

    /**
     * Méthode qui ajoute une classe à la liste des classes représentées graphiquement
     *
     * @param m Objet de type ModelClass que l'on veut ajouter à la liste
     * @return True si la classe à bien été ajoutée, false sinon
     */
    public boolean addClass(ModelClass m) {

        // Si la classe n'est pas déjà présente
        if (!this.allClassesList.contains(m)) {

            // On lui donne un nouvel id et on l'ajoute à la liste
            m.setId(this.allClassesList.size());
            this.allClassesList.add(m.getId(), m);

            // On notifie l'observeur que le modèle est ajouté à la liste
            m.notifyObservers();

            return true;

        }

        // Sinon on retourne false
        return false;
    }

    /**
     * Méthode update, elle permet de mettre à jour la vue
     */
    @Override
    public void update() {

        // Si il y a des éléments sur le diagramme on enlève tout le contenu du Pane
        if (!this.getChildren().isEmpty()) this.getChildren().clear();

        // On recharge toutes les classes
        this.reloadAllClasses();

        // On boucle sur la liste des classes
        for (ModelClass m : this.allClassesList) {
            if (m.isVisible()) {
                VBox display = m.getDisplay(this);
                attachMouseHandlers(display, m);
                this.getChildren().add(display);
            }
        }

        // Une fois que toutes les VBox sont chargées et mises sur dans le GridPane, on affiche les dépendances
        this.displayAllDependancies();

    }



    /**
     * Methode qui attache les gestionnaires de souris à une VBox
     * @param display
     * @param m
     */
    private void attachMouseHandlers(VBox display, ModelClass m) {
        // Gestionnaire de clic pour le menu contextuel
        ContextMenu contextMenu = ContextMenuHandler.createClassContextMenu(this, m);
        display.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) { // Clic droit
                contextMenu.show(display, event.getScreenX(), event.getScreenY());
            }
        });

        // Lorsqu'on clique et commence à glisser une boîte
        display.setOnMousePressed(event -> {
            this.draggedBox = display; // Enregistrer la boîte active

            // Capturer le parent pour calculer correctement les coordonnées locales
            this.parentNode = display.getParent();

            // Calculer le décalage entre la souris et le coin haut-gauche de la boîte
            Point2D mouseInParent = parentNode.sceneToLocal(event.getSceneX(), event.getSceneY());
            offsetX = mouseInParent.getX() - display.getLayoutX();
            offsetY = mouseInParent.getY() - display.getLayoutY();

            // Optionnel : Modifier l'apparence de la boîte pendant le clic (exemple : couleur grise)
            display.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        });

        // Glissement direct (dragging)
        display.setOnMouseDragged(event -> {
            // Convertir la position de la souris dans le contexte local du parent
            Point2D mouseInParent = parentNode.sceneToLocal(event.getSceneX(), event.getSceneY());

            // Nouveaux calculs pour la position de la boîte, tenant compte des décalages
            double newX = Math.max(0, Math.min(this.getWidth() - display.getWidth(), mouseInParent.getX() - offsetX));
            double newY = Math.max(0, Math.min(this.getHeight() - display.getHeight(), mouseInParent.getY() - offsetY));

            // Vérifier les collisions avant de déplacer la boîte
            if (!hasCollision(display, newX, newY)) {
                display.setLayoutX(newX);
                display.setLayoutY(newY);

                // Mise à jour logique dans le modèle lié à la boîte
                m.setX((int) newX);
                m.setY((int) newY);

                // Mise à jour des dépendances (lignes, flèches)
                this.displayAllDependancies();
            }
        });

        // Relâcher la boîte après le glissement
        display.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Restaurer l'apparence normale de la boîte
                display.setBackground(new Background(new BackgroundFill(Color.WHITE, null, new Insets(0, 0, 0, 0))));

                // Réinitialiser les décalages et le parent
                offsetX = 0;
                offsetY = 0;
                parentNode = null;

                // Finaliser les coordonnées dans le modèle
                m.setX((int) display.getLayoutX());
                m.setY((int) display.getLayoutY());

                // Rafraîchir la vue si nécessaire
                this.update();
            }
        });
    }



    /**
     * Méthode qui recharge tous les modèles de la vue
     */
    private void reloadAllClasses() {

        // On boucle sur les classes présentes
        for (ModelClass m : this.allClassesList) {
            // On récupère un nouveau modèle
            ModelClass newM = Import.getModelClass(this, m.getName());

            if (newM != null) {

                // On récupère son ancien ID et ses anciennes coordonnées
                newM.setId(m.getId());
                newM.setX(m.getX());
                newM.setY(m.getY());

            // On récupère son ancienne visibilité (caché ou non)
            newM.setVisibility(m.isVisible());

            // On récupère les booléens hidden de l'ancien modèle
            // pour chaque attribut de la nouvelle classe, on regarde si l'attribut est caché ou non
            for (Attribute a : newM.getAttributes()) {
                for (Attribute a2 : m.getAttributes()) {
                    if (a.getName().equals(a2.getName())) {
                        a.setHidden(a2.isHidden());
                    }
                }
            }

            for (Method m1 : newM.getMethods()) {
                for (Method m2 : m.getMethods()) {
                    if (m1.getName().equals(m2.getName())) {
                        m1.setHidden(m2.isHidden());
                    }
                }
            }

            for (Constructor c : newM.getConstructors()) {
                for (Constructor c2 : m.getConstructors()) {
                    if (c.getName().equals(c2.getName())) {
                        c.setHidden(c2.isHidden());
                    }
                }
            }

            // On remplace l'ancien modèle par le nouveau
            this.allClassesList.set(m.getId(), newM);

            }
        }

    }



    /**
     * Méthode qui affiche sur le diagramme les dépendances de toutes les classes
     */
    private void displayAllDependancies() {
        // On boucle sur les classes
        for (ModelClass m : this.allClassesList) {
            this.displayDependancies(m);
        }
    }



    /**
     * Méthode qui affiche graphiquement les dépendances d'une classe
     * @param m Classe dont on veut afficher les dépendances
     */
    private void displayDependancies(ModelClass m) {

        // On supprime toutes les dépendances du model
        this.getChildren().removeIf(n -> (n instanceof Line || n instanceof Polygon || n instanceof Text) && n.getId().equals(String.valueOf(m.getId())));


        // On boucle sur les attributs de la classe
        // si le type de l'attribut est une classe et que cette classe est dans la liste des classes alors on trace une flèche
        for (Attribute a : m.getAttributes()) {
            if (a.getType() != null) {
                for (ModelClass model : this.allClassesList) {
                    if (model != null && model.getName() != null && a.getType().equals(model.getName())) {
                        if (m.isVisible() && model.isVisible()) {
                            String modifier = Export.convertModifier(a.getModifier());
                        this.drawArrow(m, model, "full", "simple",  modifier + " " + a.getName() + " : " + a.getType());
                        //
                        }

                    } // si l'attribut est de type collection et d'une classe (regex)
                    else if (a.getType().matches(".*<.*>")) {
                        String[] typeArray = a.getType().split("<");
                        String type = typeArray[1].substring(0, typeArray[1].length() - 1);
                            if (model != null && model.getName() != null && type.equals(model.getName())) {
                                if (m.isVisible() && model.isVisible()) {
                                    String modifier = Export.convertModifier(a.getModifier());
                                    this.drawArrow(m, model, "full", "simple", modifier + " " + a.getName() + " : " + a.getType());
                            }
                        } }
                }
            }
        }


        // On boucle sur les classes implémentées par cette classe
        for (ModelClass m_interface : m.getInheritedClasses()) {
            if (m_interface != null && m_interface.getId() >= 0 && m_interface.getId() < this.allClassesList.size()) {
                m_interface = this.allClassesList.get(m_interface.getId());
                if (m.isVisible() && m_interface.isVisible()) this.drawArrow(m, m_interface, "dotted", "empty", "");
            } else {
                System.out.println("Classe non trouvée" + m_interface);
                return;
            }
        }

        // Si la classe hérite d'une classe
        ModelClass m_herit = m.getExtendedClass();
        if (m_herit != null && m_herit.getId() >= 0 && m_herit.getId() < this.allClassesList.size()) {
            m_herit = this.allClassesList.get(m_herit.getId());
            if (m.isVisible() && m_herit.isVisible()) this.drawArrow(m, m_herit, "full", "empty", "");
        }

    }



    /**
     * Méthode qui déssine une flèche en fonction du type de flèche choisi
     * @param m Model de la classe où va partir la flèche
     * @param m2 Model de la classe où va arriver la flèche
     * @param typeOfLine Type de ligne
     * @param typeOfHead Type de la tête de la flèche
     * @param text Texte à afficher sur la flèche (null si pas de texte)
     *
     */
    private void drawArrow(ModelClass m, ModelClass m2, String typeOfLine, String typeOfHead, String text) {

        // On récupère les bonnes coordonnées
        ArrayList<Double> listCoord = this.getNearestCoord(m, m2);
        double x1 = listCoord.getFirst();
        double y1 = listCoord.get(1);
        double x2 = listCoord.get(2);
        double y2 = listCoord.getLast();

        // Calcul de l'angle de la ligne
        double angle = Math.atan2(y2 - y1, x2 - x1);

        // Longueur et largeur de la pointe
        double arrowLength = 15;
        double arrowWidth = 10;

        // Calcul des coordonnées des coins de la pointe
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double x3 = x2 - arrowLength * cos + arrowWidth * sin;
        double y3 = y2 - arrowLength * sin - arrowWidth * cos;
        double x4 = x2 - arrowLength * cos - arrowWidth * sin;
        double y4 = y2 - arrowLength * sin + arrowWidth * cos;


        // On switch sur le type de la ligne
        Line line;
        switch (typeOfLine) {

            case "full":
                // Ligne de la flèche pleine
                line = new Line(x1, y1, x2, y2);
                line.setId(String.valueOf(m.getId()));
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(1.5);
                break;

            case "dotted":
                // Ligne de la flèche en pointillé
                line = new Line(x1, y1, x2, y2);
                line.setId(String.valueOf(m.getId()));
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(1.5);
                line.getStrokeDashArray().addAll(10.0, 5.0);
                break;

            default:
                line = null;
                break;
        }


        // On switch sur le type de la pointe de la flèche
        Polygon arrowHead;
        switch (typeOfHead) {

            case "full":
                // Pointe de la flèche pleine
                arrowHead = new Polygon();
                arrowHead.setId(String.valueOf(m.getId()));
                arrowHead.getPoints().addAll(
                        x2, y2, // Pointe de la flèche
                        x3, y3, // Coin 1
                        x4, y4  // Coin 2
                );
                arrowHead.setStroke(Color.BLACK);
                arrowHead.setStrokeWidth(0.5);
                arrowHead.setFill(Color.BLACK);
                this.getChildren().addAll(line, arrowHead);
                break;

            case "empty":
                // Pointe de la flèche vide
                arrowHead = new Polygon();
                arrowHead.setId(String.valueOf(m.getId()));
                arrowHead.getPoints().addAll(
                        x2, y2, // Pointe de la flèche
                        x3, y3, // Coin 1
                        x4, y4  // Coin 2
                );
                arrowHead.setStroke(Color.BLACK);
                arrowHead.setStrokeWidth(0.5);
                arrowHead.setFill(this.getBackground().getFills().getFirst().getFill());
                this.getChildren().addAll(line, arrowHead);
                break;

            case "simple":
                System.out.println("hey");
                // ligne gauche : part de la pointe de la flèche et va au coin 1 de la pointe
                Line lineLeft = new Line(x2, y2, x3, y3);
                lineLeft.setId(String.valueOf(m.getId()));
                lineLeft.setStroke(Color.BLACK);
                lineLeft.setStrokeWidth(1);
                // ligne droite : part de la pointe de la flèche et va au coin 2 de la pointe
                Line lineRight = new Line(x2, y2, x4, y4);
                lineRight.setId(String.valueOf(m.getId()));
                lineRight.setStroke(Color.BLACK);
                lineRight.setStrokeWidth(1);

                // Si il y a un texte à afficher
                if (!text.isEmpty()) {
                    double xText = (x1 + x2) / (double)2.0F;
                    double yText = (y1 + y2) / (double)2.0F + (double)10.0F;
                    if (text.contains(":")) {
                        String[] textArray = text.split(":");
                        String modifier = textArray[0];
                        if (text.contains(".")) {
                            String[] textArray2 = text.split("\\.");
                            String type = textArray2[textArray2.length - 1];
                            text = modifier + "  : " + type;
                            System.out.printf("text : %s\n", text);
                        }

                        System.out.printf("text : %s\n", text);
                    }

                    Text textArrow = new Text(xText, yText, text);
                    textArrow.setId(String.valueOf(m.getId()));
                    this.getChildren().add(textArrow);
                    if (text.matches(".*<.*>")) {
                        double xText2 = x2 - (double)20.0F;
                        double yText2 = y2 + (double)10.0F;
                        Text textArrow2 = new Text(xText2, yText2 + (double)10.0F, "1..*");
                        textArrow2.setId(String.valueOf(m.getId()));
                        this.getChildren().add(textArrow2);
                    }
                    else {
                        double xText2 = x2 - (double)20.0F;
                        double yText2 = y2 + (double)10.0F;
                        Text textArrow2 = new Text(xText2, yText2 + (double)10.0F, "1");
                        textArrow2.setId(String.valueOf(m.getId()));
                        this.getChildren().add(textArrow2);
                    }
                }

                this.getChildren().addAll(line, lineLeft, lineRight);
                break;
        }

    }


    /**
     * Méthode qui cherche les point les plus proches entre les deux classes
     * @param m1 ModelClass de la première classe
     * @param m2 ModelClass de la deuxième classe
     * @return Une liste comprenant les coordonnées des deux points (0: x1, 1: y1, 2: x2; 3: y2)
     */
    private ArrayList<Double> getNearestCoord(ModelClass m1, ModelClass m2) {

        // On initialise les coordonnées les plus proches et de la distance minimal
        ArrayList<Double> listCoord = new ArrayList<>();
        listCoord.add(Double.MAX_VALUE);
        listCoord.add(Double.MAX_VALUE);
        listCoord.add(Double.MAX_VALUE);
        listCoord.add(Double.MAX_VALUE);
        double minDistance = Integer.MAX_VALUE;

        // Initialisation du nombre de divisions par face
        int nbDivision = 4;

        // On récupère l'affichage des VBox
        VBox vbox1 = m1.getDisplay(this);
        VBox vbox2 = m2.getDisplay(this);

        // On boucle sur les lignes de la première VBox (haut, milieu, bas)
        for (int i1 = 0; i1 < nbDivision + 1; i1++) {
            // On boucle sur les colonnes de la première VBox (gauche, milieu, droite)
            for (int j1 = 0; j1 < nbDivision + 1; j1++) {

                // Si on est pas sur un coin
                if (!(i1 == 0 && j1 == 0) && !(i1 == nbDivision && j1 == nbDivision) && !(i1 == 0 && j1 == nbDivision) && !(i1 == nbDivision && j1 == 0)) {
                    // On récupère les coordonnées du point de la première VBox
                    double coo_x1 = m1.getX() + (vbox1.getWidth() / nbDivision) * i1;
                    double coo_y1 = m1.getY() + (vbox1.getHeight() / nbDivision) * j1;

                    // On boucle sur les colonnes de la deuxième VBox (gauche, milieu, droite)
                    for (int i2 = 0; i2 < nbDivision + 1; i2++) {
                        // On boucle sur les colonnes de la deuxième VBox (gauche, milieu, droite)
                        for (int j2 = 0; j2 < nbDivision + 1; j2++) {

                            // Si on est pas sur un coin
                            if (!(i2 == 0 && j2 == 0) && !(i2 == nbDivision && j2 == nbDivision) && !(i2 == 0 && j2 == nbDivision) && !(i2 == nbDivision && j2 == 0)) {
                                // On récupère les coordonnées du point de la première VBox
                                double coo_x2 = m2.getX() + (vbox2.getWidth() / nbDivision) * i2;
                                double coo_y2 = m2.getY() + (vbox2.getHeight() / nbDivision) * j2;

                                // Si la distance entre ces deux points est plus petite que la distance minimal enregistrée
                                double distance = this.calculateDistance(coo_x1, coo_y1, coo_x2, coo_y2);
                                if (distance < minDistance) {
                                    // On change la distance la plus petite et on récupère les points
                                    minDistance = distance;
                                    listCoord.set(0, coo_x1);
                                    listCoord.set(1, coo_y1);
                                    listCoord.set(2, coo_x2);
                                    listCoord.set(3, coo_y2);
                                }
                            }
                        }
                    }
                }
            }
        }

        // On retourne les points les plus proches
        return listCoord;

    }

    /**
     * Méthode qui vérifie s'il y a une collision entre deux boîtes
     * @param currentBox
     * @param newX
     * @param newY
     * @return
     */
    private boolean hasCollision(VBox currentBox, double newX, double newY) {
        if (currentBox == null) {
            return false; // Si la boîte en cours est null, aucune collision
        }

        // Récupérer les dimensions de `currentBox`
        double currentWidth = currentBox.getWidth();
        double currentHeight = currentBox.getHeight();

        // Vérifier les collisions avec toutes les autres boîtes
        for (ModelClass otherClass : this.allClassesList) {
            VBox otherBox = this.getVBoxById(otherClass.getId());

            // Vérification supplémentaire pour éviter la collision avec soi-même
            if (otherBox == null || currentBox.getId().equals(otherBox.getId())) {
                continue; // Passer si la boîte est elle-même ou introuvable
            }

            // Positions et dimensions de l'autre boîte
            double otherX = otherBox.getLayoutX();
            double otherY = otherBox.getLayoutY();
            double otherWidth = otherBox.getWidth();
            double otherHeight = otherBox.getHeight();

            // Vérification des chevauchements (collision rectangulaire)
            if (newX < otherX + otherWidth &&
                    newX + currentWidth > otherX &&
                    newY < otherY + otherHeight &&
                    newY + currentHeight > otherY) {
                return true; // Collision détectée
            }
        }

        return false; // Pas de collision détectée
    }


    public void hideAttributes(){

        for(ModelClass m : allClassesList){

            m.hideAllAttributes();
            update();

        }

    }

    public void showAttributes(){

        for(ModelClass m : allClassesList){

            m.showAllAttributes();
            update();

        }

    }

    public void hideMethods(){

        for(ModelClass m : allClassesList){

            m.hideAllMethods();
            update();

        }

    }

    public void showMethods(){

        for(ModelClass m : allClassesList){

            m.showAllMethods();
            update();

        }

    }

    public void hideConstructors(){

        for(ModelClass m : allClassesList){

            m.hideConstructors();
            update();

        }

    }

    public void showConstructors(){

        for(ModelClass m : allClassesList){

            m.showConstructors();
            update();

        }

    }

    /**
     * Méthode qui calcul la distance entre deux points
     * @param x1 Coordonnée x du premier point
     * @param y1 Coordonnée y du premier point
     * @param x2 Coordonnée x du deuxième point
     * @param y2 Coordonnée y du deuxième point
     * @return La distance
     */
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Méthode qui cherche un ModelClass en fonction du nom
     * @param name Nom de la classe que l'on cherche
     * @return Objet ModelClass
     */
    public ModelClass findClassByName(String name) {
        for (ModelClass m : this.allClassesList) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }



    /**
     * Méthode updateAttribute met à jour les attributs de la vue qui dépendent d'un modèel classe particulier
     * @param model : ModelClass
     */
    public void updateDependentAttributes(ModelClass model) {
        // parcours des autres models et leur attribut hidden

        // si le type de l'attribut est le même que le nom de la classe
        // on met hidden à false pour permettre le ré-affichage

        for (ModelClass m : this.getAllClasses()) {
            // parcours des attributs
            for (Attribute a : m.getAttributes()) {
                if (a.getType().equals(model.getName())){
                    a.setHidden(false); // on ré-affiche
                }
            }
        }

        this.update();
    }



    /**
     * Méthode qui réaffiche toutes les classes cachées du diagramme
     */
    public void showAllHiddenClasses() {
        for (ModelClass m : this.allClassesList) {
            m.setVisibility(true);
        }
        this.update();
    }




    /*
     * ### GETTERS ###
     */
    public ArrayList<ModelClass> getAllClasses () {
        return this.allClassesList;
    }

    /**
     * Méthode qui retourne une VBox en fonction de son ID
     * @param id
     * @return
     */
    private VBox getVBoxById(int id) {
        for (Node node : this.getChildren()) {
            if (node instanceof VBox) {
                VBox box = (VBox) node;

                // Vérifier si cette boîte correspond à l'ID
                if (box.getId() != null && Integer.parseInt(box.getId()) == id) {
                    return box;
                }
            }
        }

        return null; // Aucun VBox trouvé avec cet ID
    }
}