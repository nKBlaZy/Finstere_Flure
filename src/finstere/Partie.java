/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finstere;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nKBlaZy
 */
public class Partie {
    /* Labyrinthe de la partie */
    private Labyrinthe labyrinthe;
    /* Flaques d'hémoglobine */
    private Hemoglobine hemoCarree, hemoLine;
    /* Monstre (placé en (0,0))*/
    private Monstre monstre;
    /* Liste des murs */
    private List<Mur> murs;
    /* Joueurs de la partie */
    private Joueur[] joueurs;
    /* Personnages de tous les Joueur */
    private Personnage[][] personnages;
    /* Nombre de vrais joueurs */
    private int nbJoueurs;
    /* Index du Joueur commencant le tour */
    private int premierJoueur;
    /* Scanner pour la saisi dans la console */
    private Scanner sc;
    
    /* Constructeur */
    public Partie() {
        this.labyrinthe = new Labyrinthe(this);
        this.murs = new ArrayList<>();
        this.joueurs = new Joueur[2];
        this.personnages = new Personnage[2][4];
        this.premierJoueur = 0;
        this.sc = new Scanner(System.in);
    }
    
    /* Initialisation de la Partie (création des Joueur) */
    public void initPartie() {
        System.out.println("=== NOUVELLE PARTIE ===");
        
        System.out.print("Nombre de joueurs humain (1 ou 2) : ");
        this.nbJoueurs = this.scannerInt();
        while (this.nbJoueurs > 2 || this.nbJoueurs < 1) {
            System.out.print("Le nombre de joueurs humain doit être 1 ou 2\n"
                    + "Veuillez ressaisir un nombre : ");
            this.nbJoueurs = this.scannerInt();
        }
        
        int couleurPremier = 0;
        for (int i = 0 ; i < this.nbJoueurs ; i++) {
            System.out.println("Joueur " + (i+1) + " : ");
            System.out.print("Nom : ");
            String nom = this.sc.next();
            System.out.print("Couleurs :\n  1 - Bleu\n  2 - Marron\n"
                    + "  3 - Gris\n  4 - Vert\n  5 - Violet\n  6 - Rouge\n"
                    + "  7 - Jaune\nChoix : ");
            int couleur = this.scannerInt();
            while (couleur < 1 || couleur > 7) {
                System.out.print("Choix incorrecte, ressaisissez un nombre : ");
                couleur = this.scannerInt();
            }
            if (i == 0) {
                couleurPremier = couleur;
            } else {
                while (couleurPremier == couleur) {
                    System.out.print("Cette couleur est déjà prise...\n"
                            + "Choisissez en une autre : ");
                    couleur = this.scannerInt();
                }
            }
            
            this.personnages[i][0] = new Personnage(1, 6, Finstere.COULEURS[couleur - 1], this);
            this.personnages[i][1] = new Personnage(3, 4, Finstere.COULEURS[couleur - 1], this);
            this.personnages[i][2] = new Personnage(4, 3, Finstere.COULEURS[couleur - 1], this);
            this.personnages[i][3] = new Personnage(5, 2, Finstere.COULEURS[couleur - 1], this);
            
            this.joueurs[i] = new Joueur(nom, this, Finstere.COULEURS[couleur - 1], this.personnages[i]);
        }
        this.genLaby();
    }
    
    /* Génère le Labyrinthe et dispose les Pion dessus */
    private void genLaby() {
        System.out.println("\n...Génération du plateau de Jeu...\n");
        // Génération du Labyrinthe
        this.labyrinthe.initLaby();
        // Ajout de la flaque carrée
        this.hemoCarree = new Hemoglobine(8, 2, Finstere.CARRE, this);
        this.labyrinthe.setHemoglobine(8, 2, true);
        this.labyrinthe.setHemoglobine(9, 2, true);
        this.labyrinthe.setHemoglobine(8, 3, true);
        this.labyrinthe.setHemoglobine(9, 3, true);
        // Ajout de la flaque linéaire horizontale
        this.hemoLine = new Hemoglobine(4, 8, Finstere.LINEHORI, this);
        this.labyrinthe.setHemoglobine(4, 8, true);
        this.labyrinthe.setHemoglobine(5, 8, true);
        this.labyrinthe.setHemoglobine(6, 8, true);
        this.labyrinthe.setHemoglobine(7, 8, true);
        // Ajout des murs
        this.murs.add(new Mur(2, 2, this));
        this.labyrinthe.setMur(2, 2, true);
        this.murs.add(new Mur(12, 3, this));
        this.labyrinthe.setMur(12, 3, true);
        this.murs.add(new Mur(7, 4, this));
        this.labyrinthe.setMur(7, 4, true);
        this.murs.add(new Mur(8, 5, this));
        this.labyrinthe.setMur(8, 5, true);
        this.murs.add(new Mur(13, 5, this));
        this.labyrinthe.setMur(13, 5, true);
        this.murs.add(new Mur(6, 6, this));
        this.labyrinthe.setMur(6, 6, true);
        this.murs.add(new Mur(4, 7, this));
        this.labyrinthe.setMur(4, 7, true);
        this.murs.add(new Mur(12, 7, this));
        this.labyrinthe.setMur(12, 7, true);
        this.murs.add(new Mur(14, 8, this));
        this.labyrinthe.setMur(14, 8, true);
        this.murs.add(new Mur(5, 9, this));
        this.labyrinthe.setMur(5, 9, true);
        this.murs.add(new Mur(8, 9, this));
        this.labyrinthe.setMur(8, 9, true);
        //Ajout du monstre
        this.monstre = new Monstre(this);
        this.labyrinthe.setMonstre(0, 0, true);
    }
    
    /* Boucle de jeu */
    public void jouer() {
        System.out.println("La partie commence !!!\nBonne chance à vous !!!\n");
        int nbTour = 1;
        
        /* Boucle pour la Partie */
        while (nbTour < 17 && this.joueurs[0].getNbRestant() 
                + this.joueurs[1].getNbRestant() > 0) {
            int maxJouer = 4;
            if (nbTour == 1) maxJouer = 2;
            
            /* Boucle pour un tour */
            for (int nbJouer = 0 ; nbJouer < maxJouer ; nbJouer++) {
                
                /* Boucle pour chaque Joueur */
                for (int indexJoueur = 0 ; indexJoueur < this.joueurs.length ; indexJoueur++) {
                    
                    /* Si le Joueur a encore des Personnage jouables */
                    if (nbJouer < this.joueurs[indexJoueur].getNbRestant()) {
                        
                        /* Affichage des informations */
                        if (nbTour < 9) {
                            System.out.println("== MANCHE 1 - TOUR " + nbTour 
                                    + " ==\n" );
                        } else {
                            System.out.println("== MANCHE 2 - TOUR " 
                                    + (nbTour - 8) + " ==\n" );
                        }
                        this.afficherLaby();
                        System.out.println("\n" + this.joueurs[indexJoueur] + "\n");
                        
                        /* Choix du Personnage */
                        System.out.println("Choix du Personnage");
                        Map<Integer,String> persoJouables = this.joueurs[indexJoueur].persoJouables();
                        for (Map.Entry<Integer, String> entry : persoJouables.entrySet()) {
                            System.out.println("   " + entry.getKey() + " => " 
                                    + entry.getValue());
                        }
                        System.out.print("Choix : ");
                        int indexPerso = this.scannerInt();
                        while (!persoJouables.containsKey(indexPerso)) {
                            System.out.print("Personnage non disponible, veuillez"
                                    + " en choisir un autre : ");
                            indexPerso = this.scannerInt();
                        }
                        indexPerso--;
                
                        /* Actions avec le Personnage */
                        boolean continuer = true;
                        while (continuer) {
                            System.out.println("Actions possibles");
                            Map<Integer,Action> actions = this.personnages[indexJoueur][indexPerso].getActions();
                            for (Map.Entry<Integer,Action> entry : actions.entrySet()) {
                                System.out.println("   " + entry.getKey() 
                                        + " => " + entry.getValue().getAction());
                            }
                            System.out.print("Choix : ");
                            int choix = this.scannerInt();
                            while (!actions.containsKey(choix)) {
                                System.out.print("Action non disponible, veuillez"
                                        + " en choisir une autre : ");
                                choix = this.scannerInt();
                            }
                            Action action = actions.get(choix);
                            try {
                                continuer = (boolean) action.getMethode()
                                        .invoke(this.personnages[indexJoueur][indexPerso], action.getParam());
                            } catch (Exception ex) {
                                System.out.println(ex);
                            }
                        }
                    }               
                }
            }
            for (int j = 0 ; j < this.personnages.length ; j++) {
                for (int i = 0 ; i < this.personnages[j].length; i++) {
                    this.personnages[j][i].finTour();
                }
            }
            nbTour++;
        }
    }
    
    /* Retourne le Personnage aux coordonnées _x, _y sous la forme d'une chaine */
    public String personnageAt(int _x, int _y) {
        for (int j = 0 ; j < this.personnages.length ; j++) {
            for (int i = 0 ; i < this.personnages[0].length ; i++) {
                if (this.personnages[j][i].getX() == _x 
                        && this.personnages[j][i].getY() == _y) {
                    return this.personnages[j][i].shortString();
                }
            }
        }
        return "   ";
    }
    
    /* Affiche le Labyrinthe */
    public void afficherLaby() {
        System.out.println(this.labyrinthe);
    }
    
    /* Affiche la liste des Joueur avec leurs Personnage */
    public void afficherJoueurs() {
        for (Joueur joueur : this.joueurs) {
            System.out.println(joueur);
        }
    }
    
    /* Protection du Scanner (saisie obligatoire d'un int) */
    private int scannerInt() {
        int n = -1;
        boolean valide = false;
        
        do {
            if(this.sc.hasNextInt()) {
                n = this.sc.nextInt();
                valide = true;
            } else {
                System.out.print("Veuillez saisir un nombre : ");
                this.sc.next();
            }
        } while(!valide);
        
        return n;
    }
    
    /* Getters */
    public Labyrinthe getLabyrinthe() {
        return this.labyrinthe;
    }

    public Monstre getMonstre() {
        return this.monstre;
    }
    
    
    public void test() {
        for(Map.Entry<Integer,Action> entry : this.personnages[0][1].getActions().entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue().getAction());
        }
    }
}