# conversion_DNA - Donnée ↔ Séquence ADN
Ce projet Maven, nommé conversion_DNA, permet l'encodage de texte et de fichiers en séquences d'ADN et vice-versa, en utilisant Java 18 comme environnement de développement.

![](https://github.com/Komirstone/conversion_DNA/blob/main/src/main/resources/fichiers/GifTransformation.gif)

## Prérequis 😬
- Java 18
- Maven

## Configuration 
Le projet est configuré pour utiliser Java 18, comme spécifié dans le fichier pom.xml.

## Dépendances 
- JUnit 4.13.2 pour les tests
- Log4j 1.2.17 pour les logs 

## Installation 👍
Clonez ce dépôt et naviguez dans le dossier du projet :
```ruby
git clone https://github.com/Komirstone/conversion_DNA
cd conversion_DNA
mvn clean install
```

## Utilisation 😎
Quatre fonctions de la classe Conversion permettent l'encodage et le décodage de texte et de fichiers

| Fonctions             | Explications                                                                |
| ----------------- | ------------------------------------------------------------------ |
| **encoderMessageEnADN**(String message) | encode le texte en séquence d'ADN |
| **decoderADNEnMessage**(String sequenceADN) | décode la séquence d'ADN en texte |
| **encoderFichierEnADN**(String cheminFichier) | encode le fichier en séquence d'ADN |
| **decoderADNEnFichier**(String sequenceADN, String cheminOutput) | décode la séquence d'ADN en fichier et l'écrit dans le chemin spécifié |

La classe **Main** contient un exemple d'utilisation de ces fonctions.


## Contribuer ✨
Les contributions sont les bienvenues. Veuillez forker le dépôt, créer une branche pour vos modifications, et soumettre une pull request pour révision.

### Auteur ✒️
- [@Komirstone](https://www.github.com/Komirstone)

