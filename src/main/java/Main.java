import utils.Conversion;

import static utils.Conversion.binaireToString;

public class Main {
    public static void main(String[] args) {
        Conversion conversion = new Conversion();
        String seqADN = conversion.encoderMessageEnADN("Ceci est ma phrase a encoder.");
        String phrase = conversion.decoderADNEnMessage(seqADN);

        //Copie du fichier text.txt dans le dossier output
        String fichierEnSeqADN = conversion.encoderFichierEnADN("src/main/resources/fichiers/test.txt");
        String fichierDecode = conversion.decoderADNEnFichier(fichierEnSeqADN, "src/main/resources/output/");
    }
}
