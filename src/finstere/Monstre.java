/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finstere;

/**
 *
 * @author TomWyso
 */
public class Monstre extends Pion {
    /* Direction du Monstre (Finstere.HAUT .BAS ...) */
    private int direction;
    
    /* Constructeur */
    public Monstre(Partie _partie) {
        super(0, 0, _partie);
        this.direction = Finstere.DROITE;
    }
}
