package se.jensen.william.springboot.entities;

import jakarta.persistence.*;


/**
 * Entitetsklass som representerar en användarprofil.
 *
 * klassen modellerar profilinformation för användaren.
 * largrar biografiska information och sökväg till profilbilden som användaren använder.
 *
 * @author William
 */


@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String bio;

    String profilePicturePath;
}
