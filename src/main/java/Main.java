import utils.Conversion;

import static utils.Conversion.binaireToString;

public class Main {
    public static void main(String[] args) {
        Conversion conversion = new Conversion();

        String seqADN = conversion.encoderMessageEnADN("Ceci est ma phrase a encoder.");
        System.out.println("\n\n");
        String phrase = conversion.decoderADNEnMessage(seqADN);
        System.out.println("\n\n");

        //Copie du fichier text.txt dans le dossier output
        String fichierEnSeqADN = conversion.encoderFichierEnADN("src/main/resources/fichiers/test.txt");
        System.out.println("\n\n");
        String fichierDecode = conversion.decoderADNEnFichier(fichierEnSeqADN, "src/main/resources/output/");
    }
}
