package utils;

import org.junit.Test;

import static org.junit.Assert.*;
import static utils.Conversion.lireFichierEnBinaire;

public class ConversionTest {

    @Test
    public void format_deformatString() {
        Conversion conversion = new Conversion();
        String phrase = "Salut";
        String phraseEncapsulee = conversion.formaterString(phrase);
        String phraseDeformate = conversion.reformaterString(phraseEncapsulee);
        assertEquals(phrase, phraseDeformate);
    }


    @Test
    public void stringToBinary() {
        Conversion conversion = new Conversion();
        String lettre = "A";
        //System.out.println(conversion.stringToBinary(lettre));
        assertEquals("01000001", conversion.stringToBinaire(lettre));
    }

    @Test
    public void binaryToDNA() {
        Conversion conversion = new Conversion();
        String a = "00011011"; //ACGT
        assertEquals("AGCT", conversion.binaireToDNA(a));
    }

    @Test
    public void dnaToBinary() {
        Conversion conversion = new Conversion();
        String adn = "AGCT";
        assertEquals("00011011", conversion.dnaToBinaire(adn));
    }

    @Test
    public void binaryToString() {
        Conversion conversion = new Conversion();
        String binaire = "01000001";
        assertEquals("A", conversion.binaireToString(binaire));
    }

    @Test
    public void getExtensionFichier() {
        Conversion conversion = new Conversion();
        String cheminFichier = "src/fichiers/test.txt";
        assertEquals(".txt", conversion.getExtensionFichier(cheminFichier));
    }

    @Test
    public void encoder_decoderMessage() {
        Conversion conversion = new Conversion();
        String phrase = "Salut comment ca va ?";
        String seqADN = conversion.encoderMessageEnADN(phrase);
        String phraseDecodee = conversion.decoderADNEnMessage(seqADN);
        String phrase2 = "Je vais extremement bien et toi ? Je suis content de te voir !";
        String seqADN2 = conversion.encoderMessageEnADN(phrase2);
        String phraseDecodee2 = conversion.decoderADNEnMessage(seqADN2);

        assertEquals(phrase, phraseDecodee);
        assertEquals(phrase2, phraseDecodee2);
    }

    @Test
    public void encoder_decoderFichier(){
        Conversion conversion = new Conversion();
        String cheminFichier = "src/test/resources/fichiers/test.txt";
        String cheminOutput = "src/test/resources/output/";
        String seqADNFichier = conversion.encoderFichierEnADN(cheminFichier);
        String cheminFichierDecode = conversion.decoderADNEnFichier(seqADNFichier, cheminOutput);
        //Comparer le binaire des deux fichiers, bien que le nom ne soit pas le même !
        assertEquals(lireFichierEnBinaire(cheminFichier), lireFichierEnBinaire(cheminFichierDecode));
    }

}