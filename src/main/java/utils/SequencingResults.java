package utils;

import java.util.ArrayList;

public class SequencingResults {

    public static String sequenceComplementaire(String seqADN){
        //On parcourt la séquence ADN de la fin vers le début vu que la lecture s'est faite dans le sens inverse
        String seqADNComplementaire = "";
        for (int i = seqADN.length() - 1; i >= 0; i--) {
            if (seqADN.charAt(i) == 'A') {
                seqADNComplementaire += 'T';
            } else if (seqADN.charAt(i) == 'T') {
                seqADNComplementaire += 'A';
            } else if (seqADN.charAt(i) == 'C') {
                seqADNComplementaire += 'G';
            } else if (seqADN.charAt(i) == 'G') {
                seqADNComplementaire += 'C';
            } else {
                //On laisse l'élément non reconnu tel que nucléotide
                seqADNComplementaire += seqADN.charAt(i);
            }
        }
        return seqADNComplementaire;
    }

    public static String validerSequence(String seq){
        /**vérifier que notre séquence ADN ne contient que des nucléotides A T C G*/
        ArrayList nucleotidesFaux = new ArrayList();
        boolean estValide = true;
        for (int i = 0; i < seq.length(); i++) {
            if (seq.charAt(i) != 'A' && seq.charAt(i) != 'T' && seq.charAt(i) != 'C' && seq.charAt(i) != 'G') {
                estValide = false;
                nucleotidesFaux.add(i+1);
            }
        }
        if (!estValide) {
            return "La sequence ADN n'est pas valide, elle contient des nucleotides non reconnus aux positions suivantes : " + nucleotidesFaux;
        }
        return "La sequence ADN est valide";
    }



    public static void main(String[] args) {
        String SEQUENCEREFERENCE = "GAGAGAGGGAACGGGGGGGAGGGAGCCGGCTGACAAGAACGCGGGTACGCTCGAGGGGACGTATACTGGATAGCGGGAGGACAAGGATACGTGCGGGTCAGCATGTGGGTATGAGGACGCGCGAACGTGCAGGTGCGCTTGACGGTACACAAGAATGCCAGATTGCCGGGATGACGACGCACACGACAGTGAGGGAGTAAATCCACTTACTTACACACTCGGTTGATAGCGGGTATACATGCGCGCTTGGACGGATGCTAGAAGGTATGCCAGGATACGAGAGGGTGAGCAGGCCGGCGGGATCGGGAACGCGAGAGAGGACGCGTGAGTACGATTGGAAACTAGGTTGATGGAAGGACGGTATACCTGCCGGATAACGAGTAAGCGGGCTCGGATGCAGGACGGGGAGGTTGTAGGGGGGAGGGGTTGCATACGTGCGGGTGAGAAGGCCGGGGAACCTGTGGGATCGAGGACGCGCACGCTTGCTCGATCGCGGACATGACGGCGAGAGGGCGGACTCGAGCGACGGATC";
        String adapter5 = "GGCATTTTGCTGCCGGTCACG";

        String seqForward = "NNNNNNNNNNNNNGNNNNNNCGGCTGANAGAACGCGGGTACGCTCGAGGGGACGTATACTGGATAGCGGGAGGNCAAGGATACGTGCGGGTCAGCATGTGGGTATGAGGACGCGCGAACGTGCAGGTGCGCTTGACGGTACACAAGAATGCCAGATTGCCGGGATGACGACGCACACGACAGTGAGGGAGTAAATCCACTTACTTACACACTCGGTTGATAGCGGGTATACATGCGCGCTTGGACGGATGCTAGAAGGTATGCCAGGATACGAGAGGGTGAGCAGGCCGGCGGGATCGGGAACGCGAGAGAGGACGCGTGAGTACGATTGGAAACTAGGTTGATGGAAGGACGGTATACCTGCCGGATAACGAGTAAGCGGGCTCGGATGCAGGACGGGGAGGTTGTAGGGGGGAGGGGTTGCATACGTGCGGGTGAGAAGGCCGGGGAACCTGTGGGATCGAGGACGCGCACGCTTGCTCGATCGCGGACATGACGGCGAGAGGGCGGACTCGAGCGACGGATCN";
        //Dans seqReverse la qualité de la dernière base est mauvaise, on la supprime
        String seqReverse = "NNNNNNNNNNNNNCNNNNANNGNNNAAGCGTGCGCGTCCTCGATCCCACAGGTTCCCCGGCCTTCTCACCCGCACGTATGCAACCCCTCCCCCCTACAACCTCCCCGTCCTGCATCCGAGCCCGCTTACTCGTTATCCGGCAGGTATACCGTCCTTCCATCAACCTAGTTTCCAATCGTACTCACGCGTCCTCTCTCGCGTTCCCGATCCCGCCGGCCTGCTCACCCTCTCGTATCCTGGCATACCTTCTAGCATCCGTCCAAGCGCGCATGTATACCCGCTATCAACCGAGTGTGTAAGTAAGTGGATTTACTCCCTCACTGTCGTGTGCGTCGTCATCCCGGCAATCTGGCATTCTTGTGTACCGTCAAGCGCACCTGCACGTTCGCGCGTCCTCATACCCACATGCTGACCCGCACGTATCCTTGTCCTCCCGCTATCCAGTATACGTCCCCTCGAGCGTACCCGCGTTCTTGTCAGCCGGCTCCCTCCCCCCCGTTCCCTCTCTCCGTGACCGGCAGCAAAATGCCA";

        /**Trouver la séquence complémentaire à la lecture backward*/
        seqReverse = seqReverse.substring(0, seqReverse.length() - 1);
        String seqComplementaire = sequenceComplementaire(seqReverse);
        //System.out.println(seqComplementaire);

        /** Ne garder que d'environ la [moitié, fin] pour les deux lectures
         * Le début de lecture contient souvent quelques erreurs
         * Étant donné qu'elles sont complémentaires cela permet de réduire les erreurs*/
        String seqPart2 = seqForward.substring(seqForward.length()/2, seqForward.length());
        String debutSeqPart2 = seqPart2.substring(0, seqPart2.length()/10);
        //Ne conserver que la partie de la séquence qui est avant le début de la partie 2
        String seqPart1 = seqComplementaire.substring(0, seqComplementaire.indexOf(debutSeqPart2));

        /**Assembler les deux parties et enlever l'adaptateur et ce qui peut précéder*/
        String seqFinale = seqPart1 + seqPart2;
        seqFinale = seqFinale.substring(seqFinale.indexOf(adapter5) + adapter5.length());

        /**Afficher la séquence finale*/
        System.out.println("\nValidation de sequence  : ");
        System.out.println(validerSequence(seqFinale));
        //On supprime le dernier nucléotide car il est en fin de séquence est il est non-défini (N)
        seqFinale = seqFinale.substring(0, seqFinale.length() - 1);
        //On affiche la séquence finale
        System.out.println("\nLa sequence finale : ");
        System.out.println(seqFinale + "\n");

        /**Vérifier si la séquence ADN contient bien notre message une fois décodée*/
        Conversion conversion = new Conversion();
        String message = conversion.decoderADNEnMessage(seqFinale);

        System.out.println("\nLa sequence finale : ");
        System.out.println(seqFinale);
        System.out.println("\nMessage encode dans la sequence ADN : ");
        System.out.println(message);
    }
}
