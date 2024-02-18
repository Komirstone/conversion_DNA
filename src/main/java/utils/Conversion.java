package utils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

    /**
     * Cette classe permet de convertir des données String en données ADN et vice-versa en passant par le format binaire.
        * String --> Binaire --> ADN
        * ADN --> Binaire --> String
        * Cette méthode ne gère que très peu les redondances (espaces, répétitions de caractères)
          mais elle sert tout de même de proof of concept.

     * Elle permet également de convertir des fichiers en données binaires puis de les convertir à leur tour en données ADN.
        * Fichier --> Binaire --> ADN
        * ADN --> Binaire --> Fichier
        * Pour ce traitement, il n'y pas de gestion de redondance de séquences dans le binaire ou la séquence ADN.
            * Ce qui peut rendre un synthèse beaucoup plus complexe, mais il était relativement complexe à implémenter.
    **/

public class Conversion {
    static Logger logger = Logger.getLogger(Conversion.class);
    private Map<String, String> chiffrement;
    private ArrayList<String> remplacement;
    private String DEBUT = "DEBUT";
    private String FIN = "FIN";

    public Conversion() {
        /***
         Initialisation la clé de chiffrement
            "A" <--> "00"
            "G" <--> "01"
            "C" <--> "10"
            "T" <--> "11"
         */
        chiffrement = new HashMap<String, String>();
        chiffrement.put("A", "00");
        chiffrement.put("G", "01");
        chiffrement.put("C", "10");
        chiffrement.put("T", "11");
        chiffrement.put("00", "A");
        chiffrement.put("01", "G");
        chiffrement.put("10", "C");
        chiffrement.put("11", "T");
        remplacement = new ArrayList(Arrays.asList(" ", "#", "&", "_", "+", "^", "$", "="));
    }

    public String formaterString(String message){
        /***
         Il est important de formatter le message pour qu'il soit :
             - encapsulé par DEBUT et FIN
             - éviter les répétitions de caractères :
                (en mettant en majuscule le caractère répété
                ou en remplaçant les espaces répétitifs par un caractère spécial)
        */
        logger.info("FormaterString - Debut du formatage du message.");
        StringBuilder sb = new StringBuilder();
        Map<Character, Integer> caracteresCompte = new HashMap<Character, Integer>();

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (!caracteresCompte.containsKey(c)) {
                caracteresCompte.put(c, 1);
                sb.append(c);
            } else if (c == ' ') { /* Dès le deuxième ' ' */
                int random = (int) (Math.random() * (remplacement.size() - 1));
                String nouveau_c = remplacement.get(random);
                caracteresCompte.put(c, caracteresCompte.get(c) + 1);
                sb.append(nouveau_c);
            } else if (caracteresCompte.get(c) % 2 != 0) { /* éviter la répétition de caractères */
                sb.append(Character.toUpperCase(c));
                caracteresCompte.put(c, caracteresCompte.get(c) + 1);
            } else {
                sb.append(c);
                caracteresCompte.put(c, caracteresCompte.get(c) + 1);
            }
        }
        /* ENCAPSULATION afin de définir le debut et la fin du message*/
        String phrase_encapsulee = DEBUT + sb.toString() + FIN;
        logger.info("FormaterString - Message formate : " + phrase_encapsulee);
        return phrase_encapsulee;
    }

    public static String stringToBinaire(String messageFormate){
        /***
         Renvoie le message formaté en binaire
         */
        logger.info("StringToBinaire - Conversion... formate en binaire.");
        StringBuilder binaire = new StringBuilder();
        for (char ch : messageFormate.toCharArray()) {
            String binStr = Integer.toBinaryString(ch);
            //S'assurer que le message fait bien 8 bits de long, en rajoutant des 0 au début
            while (binStr.length() < 8) {
                binStr = "0" + binStr;
            }
            binaire.append(binStr);
        }
        logger.info("StringToBinaire - Conversion reussie, sortie binaire : " + binaire.toString());
        return binaire.toString();
    }

    public String binaireToDNA(String messageBinaire){
        /***
         Renvoie le message binaire en ADN, en reprenant la clé de chiffrement définie dans le constructeur
         "00" --> "A"
         "01" --> "G"
         "10" --> "C"
         "11" --> "T"
         */
        logger.info("BinaireToDNA - Conversion du binaire en sequence ADN.");
        StringBuilder adn = new StringBuilder();
        //IMPORTANT : nous devons lire tous les 2 bits !
        for (int i = 0; i < messageBinaire.length(); i += 2) {
            String binStr = messageBinaire.substring(i, i + 2);
            adn.append(chiffrement.get(binStr));
        }
        logger.info("BinaireToDNA - Conversion reussie, sequence ADN : " + adn.toString());
        return adn.toString();
    }

    public String dnaToBinaire(String messageADN){
        /***
         Renvoie le message ADN en binaire, en reprenant la clé de chiffrement définie dans le constructeur
         "A" --> "00"
         "G" --> "01"
         "C" --> "10"
         "T" --> "11"
         */
        logger.info("DnaToBinaire - Conversion de la sequence ADN en binaire.");
        StringBuilder binaire = new StringBuilder();
        for (int i = 0; i < messageADN.toUpperCase().length(); i++) {
            String binStr = chiffrement.get(messageADN.substring(i, i + 1));
            binaire.append(binStr);
        }
        logger.info("DnaToBinaire - Conversion reussie, sortie binaire : " + binaire.toString());
        return binaire.toString();
    }

    public static String binaireToString(String messageBinaire){
        /***
         Renvoie le message binaire en String
         */
        logger.info("BinaireToString - Conversion du binaire en String.");
        StringBuilder sb = new StringBuilder();
        // IMPORTANT : nous devons lire tous les 8 bits !!!
        for (int i = 0; i < messageBinaire.length(); i += 8) {
            String binStr = messageBinaire.substring(i, i + 8);
            char c = (char) Integer.parseInt(binStr, 2);
            sb.append(c);
        }
        logger.info("BinaireToString - Conversion reussie : " + sb.toString());
        return sb.toString();
    }

    public String reformaterString(String messageFormate){
        /***
         Cette méthode permet de reformater le message en enlevant les caractères spéciaux et les majuscules ajoutées
         */
        logger.info("ReformaterString - Debut de reformatage du message.");
        StringBuilder sb = new StringBuilder();
        for (int i = DEBUT.length(); i < messageFormate.length() - FIN.length(); i++) {
            char c = messageFormate.charAt(i);
            if (remplacement.contains(Character.toString(c))) {
                sb.append(" ");
            } else {
                sb.append(c);
            }
        }
        logger.info("ReformaterString - Message reformate : " + sb.toString().toLowerCase(Locale.ROOT));
        return ajouterMajuscules(sb.toString().toLowerCase(Locale.ROOT));
    }

    private static String ajouterMajuscules(String phrase) {
        logger.info("AjouterMajuscules - Debut de la fonction avec la phrase : " + phrase);

        if (phrase == null || phrase.isEmpty()) {
            logger.warn("AjouterMajuscules - La phrase est nulle ou vide.");
            return phrase;
        }

        StringBuilder phraseModifiee = new StringBuilder(phrase.length());
        boolean majusculeNecessaire = true; // Indique s'il faut mettre une majuscule

        for (int i = 0; i < phrase.length(); i++) {
            char c = phrase.charAt(i);
            if (majusculeNecessaire && Character.isLetter(c)) {
                phraseModifiee.append(Character.toUpperCase(c));
                majusculeNecessaire = false; // Désactive la majuscule pour les caractères suivants
                logger.debug("AjouterMajuscules - Majuscule ajoutée au caractere : " + c);
            } else {
                phraseModifiee.append(c);
            }

            // Réactive la majuscule après un point suivi d'un espace
            if ((c == '.' || c == '?' || c == '!') && i < phrase.length() - 1 && phrase.charAt(i + 1) == ' ') {
                majusculeNecessaire = true;
                logger.debug("AjouterMajuscules - \". \", \"? \" \"! \" majuscule necessaire pour le prochain caractere.");
            }
        }
        logger.info("AjouterMajuscules - Fin de la fonction, phrase modifiee : " + phraseModifiee);
        return phraseModifiee.toString();
    }

    public String encoderMessageEnADN(String message){
        /***
         Cette méthode permet d'encoder un message
         */
        logger.info("EncoderMessageEnADN - Demarrage du processus d'encodage du message en ADN.");
        String messageFormate = formaterString(message);
        String messageBinaire = stringToBinaire(messageFormate);
        String sequenceADN = binaireToDNA(messageBinaire);
        logger.info("EncoderMessageEnADN - Fin du processus d'encodage, sequence ADN obtenue : " + sequenceADN);
        return sequenceADN;
    }

    public String decoderADNEnMessage(String sequenceADN){
        /***
         Cette méthode permet de décoder un message
         */
        logger.info("DecoderADNEnMessage - Demarrage du processus de decodage de la sequence ADN en message.");
        String messageBinaire = dnaToBinaire(sequenceADN);
        String messageFormate = binaireToString(messageBinaire);
        String message = reformaterString(messageFormate);
        logger.info("DecoderADNEnMessage - Fin du processus de decodage, message obtenu : " + message);
        return message;
    }


    /***
     * CETTE CLASSE PERMET EGALEMENT DE TRANSFORMET DES FICHIERS EN BINAIRE ET VICE-VERSA
     **/
    //Ce marqueur sert à délimiter le binaire relatif au fichier et celui relatif à l'extension du fichier (.txt, .jpg, etc.)
    public static final String MARQUEUR_UNIQUE = "00000000000000101011111111111111";

    public static String getExtensionFichier(String cheminFichier) {
        /**Cette méthode permet de récupérer l'extension d'un fichier*/
        logger.info("GetExtensionFichier - Recuperation de l'extension du fichier a partir du chemin : " + cheminFichier);
        int indexPoint = cheminFichier.lastIndexOf('.'); //Le dernier point du filepath correspond à l'extension du fichier
        if (indexPoint >= 0 && indexPoint < cheminFichier.length() - 1) {
            logger.info("GetExtensionFichier - Extension du fichier recuperee : " + cheminFichier.substring(indexPoint));
            return cheminFichier.substring(indexPoint); //Retourne ".txt", ".jpg", etc.
        }
        return "";
    }

    public static String lireFichierEnBinaire(String cheminFichier) {
        /***
         * Cette méthode permet de lire un fichier en binaire
         * Elle retourne un String contenant le binaire du fichier
         * Le binaire est composé de 3 parties :
         * 1) Le binaire de l'extension du fichier
         * 2) Un marqueur unique
         * 3) Le binaire du fichier
         */
        logger.info("LireFichierEnBinaire - Lecture du fichier en binaire a partir du chemin : " + cheminFichier);
        String extensionFichier = getExtensionFichier(cheminFichier);
        String binaireExtension = stringToBinaire(extensionFichier);

        StringBuilder binaire = new StringBuilder();
        /**Ouverture du fichier en mode read*/
        try (FileInputStream fis = new FileInputStream(cheminFichier)) {
            int byteData;
            //Lecture du fichier byte par byte
            logger.info("lireFichierEnBinaire - lecture du binaire byte par byte...");
            while ((byteData = fis.read()) != -1) {
                String binaireString = Integer.toBinaryString(byteData);
                //Ajouter des 0 devant la chaine binaire pour être certain qu'elle fasse toujours 8 bits
                while (binaireString.length() < 8) {
                    binaireString = "0" + binaireString;
                }
                //Ajouter le byte lu au "binaire"
                binaire.append(binaireString);
            }
        } catch (IOException e) {
            logger.error("erreur lors de la lecture du fichier : " + cheminFichier);
            e.printStackTrace();
        }
        /**Recomposition de la séquence binaire : binaire + MARQUEUR + extension */
        logger.info("LireFichierEnBinaire - Lecture en binaire terminee, donnees binaires : " + binaire.toString());
        return binaire.toString() + MARQUEUR_UNIQUE + binaireExtension;
    }

    public static String ecrireBinaireDansFichier(String binaireData, String cheminOutput) {
        /**
         * Transforme le binaire en fichier en respectant l'extension du fichier.
         * Identifie le marqueur pour séparer les données réelles de l'extension.
         * Les données binaires sont ensuite écrites dans un fichier de sortie, byte par byte.
         */
        //Déterminer la position de l'extension dans le binaire
        logger.info("EcrireBinaireDansFichier - Ecriture des donnees binaires dans le fichier au chemin : " + cheminOutput);
        int marqueurIndex = binaireData.indexOf(MARQUEUR_UNIQUE);
        if (marqueurIndex == -1) {
            logger.error("EcrireBinaireDansFichier - Le MARQUEUR n'a pas ete trouve dans le binaire. Impossible de continuer.");
            return null;
        }
        //Capturer l'extension (en binaire)
        String binaireExtension = binaireData.substring(marqueurIndex + MARQUEUR_UNIQUE.length());
        //Convertir l'extension en String
        String extensionFichier = binaireToString(binaireExtension);

        //Capturer le binaire du fichier (sans marqueur, sans extension)
        String binaireFichier = binaireData.substring(0, marqueurIndex);
        String cheminOutputFichier = cheminOutput + "output" + extensionFichier;

        //Création d'un nouvel objet FileOutputStream pour écrire le fichier de sortie cheminOutputFichier
        try (FileOutputStream fos = new FileOutputStream(cheminOutputFichier)) {
            /**Il faut lire tous les 8 bits !*/
            logger.info("EcrireBinaireDansFichier - Recomposition du fichier a partir du binaire, en vue de l'ecriture dans : " + cheminOutputFichier);
            for (int i = 0; i < binaireFichier.length(); i += 8) {
                String byteString = binaireFichier.substring(i, i + 8);
                //Convertir le groupe de 8 bits en int
                int valeur = Integer.parseInt(byteString, 2);
                //Ecrire la valeur entière dans le fichier
                fos.write(valeur);
            }
        } catch (IOException e) {
            logger.error("EcrireBinaireDansFichier - Erreur lors de l'ecriture du fichier dans : " + cheminOutputFichier);
            e.printStackTrace();
        }
        logger.info("EcrireBinaireDansFichier - Ecriture terminee, chemin du fichier ecrit : " + cheminOutputFichier);
        return cheminOutputFichier;
    }

    public String encoderFichierEnADN(String cheminFichier){
        /***
         Cette méthode permet d'encrypter un fichier
         */
        logger.info("EncoderFichierEnADN - Encodage du fichier en sequence ADN a partir du chemin : " + cheminFichier);
        //System.out.println("Encodage du fichier \"" + cheminFichier + "\" en sequence ADN...");
        String binaireFichier = lireFichierEnBinaire(cheminFichier);
        String sequenceADN = binaireToDNA(binaireFichier);
        //System.out.println("Fichier \"" + cheminFichier + "\" encode en sequence ADN avec succes (BINAIRE -> DNA): \n\t" + sequenceADN);
        logger.info("EncoderFichierEnADN - Encodage termine, sequence ADN : " + sequenceADN);
        return sequenceADN;
    }

    public String decoderADNEnFichier(String sequenceADN, String cheminOutput) {
        /***
         Cette méthode permet de décrypter un fichier
         */
        logger.info("DecoderADNEnFichier - Decodage de la sequence ADN en fichier, ecriture au chemin : " + cheminOutput);
        //System.out.println("Decodage de la sequence ADN en fichier et ecriture dans le chemin \"" + cheminOutput + "\"");
        String binaireFichier = dnaToBinaire(sequenceADN);
        //System.out.println("Fichier obtenu en binaire avec succès. (DNA -> BINAIRE)");
        String cheminNouveauFichier = ecrireBinaireDansFichier(binaireFichier, cheminOutput);
        //System.out.println("decodage termine avec succes. Fichier dispo \"" + cheminNouveauFichier + "\"");
        logger.info("DecoderADNEnFichier - Decodage termine, chemin du fichier decode : " + cheminNouveauFichier);
        return cheminNouveauFichier;
    }

}

