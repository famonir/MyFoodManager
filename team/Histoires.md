# Histoires
Informations récapitulatives concernant les différentes histoires.


----------------------

## Pondération

| Priorité/3 | N° | Description | Risque/3 | Heures estimées | Heures prestées itération 1 | Heures prestées itération 2 | Heures prestées itération 3 | Heures prestées itération 4 |
| ------ | ------ | ------ | ------ | ------ | ------ | ------ |------ |------ |
| 1 | [1](#gestion-dune-liste-de-courses) | Gestion d’une liste de courses | 2 | 60 | 110 | 50 | 9 | |
|   | [2](#gestion-de-recettes)| Gestion de recettes| 3 | 50 | 100 | 50 | 11 | |
|   | [3](#gestion-et-génération-de-menus-sur-plusieurs-jours)| Gestion et génération de menus sur plusieurs jours | 1 | 65 |  | 35 | 11 |
| 2 | [4](#export-d’une-liste-de-courses)| Export d’une liste de courses | 1 | 50 |  | 50 | 6 |
|   | [5](#gestion-des-magasins) | Gestion des magasins | 1 | 80 | | | 110 |
|   | [6](#magasin-le-plus-intéressant) | Magasin le plus intéressant | 2 | 60 | | | 4 |
|   | [10](#ajout-de-données-des-produits-d’un-magasin) | Ajout de données des produits d’un magasin| 2 | 30 |  | |
| 3 | [7](#tour-a-prix-minimum-pour-une-liste) | Tour à prix minimum pour une liste | 2 | 40 |  | |
|   | [8](#calcul-du-plus-court-chemin) | Calcul du plus court chemin | 3 | 150 |  | |
|   | [9](#créer-un-utilisateur-login-et-mot-de-passe) | Créer un utilisateur, login et mot de passe | 3 | 15 |  | 20 | 22 |
|   | [11](#valeur-nutritionnelle-d’une-recette) | Valeur nutritionnelle d’une recette | 3 | 35 |  |
|   | [12](#section-d’aide) | Section d’aide | 3 | 35 |  | |
|   | [13](#envoi-d’une-liste-de-courses-vers-une-application-android) | Envoi d’une liste de courses vers une application android | 1 | 200 |  | |
|   | [14](#intégration-a-remember-the-milk) |  Intégration à remember the milk | 2 | 20 |  | |


----------------------


## Description

### Gestion d’une liste de courses

**Actuellement :**          
- L'histoire est totalement réalisée
- L'archivage est possible
- Pas de filtres encore disponibles
- Changement du nombre de personnes pas encore disponible

**Mise à jour itération 2 :**       
- Passage à l'utilisation de fichiers FXML
- Passage au modèle DAO
- Tous les tests ont dû être refaits suite à ces modifications
- Le parcours des archives est possible
- La database est peuplée de produits

### Gestion de recettes
    
**Actuellement :**
- Les recettes peuvent être créées, modifiées et supprimées
- L'importation de fichiers JSON est possible

**Mise à jour itération 2 :**
- Passage à l'utilisation de fichiers FXML
- Passage au modèle DAO
- Tous les tests ont du être refait suite à ces modifications
- Amélioration de l'import JSON
- Des recettes sous le format JSON sont fournies

### Gestion et génération de menus sur plusieurs jours

**Actuellement :**
- On sait générer des menus hebdomadaires 
- Génération en fonction des préférences
- Modification de la génération possible
- Génération de liste de courses à partir d'un menu possible

### Export d’une liste de courses

**Actuellement :**
- Exportation sous format pdf et odt possible
- Liste de courses triée  
- Prévisualisation modifiable 
- Envoi par mail et export possibles

### Créer un utilisateur login et mot de passe

**Actuellement :**
- Le profil de l'utilisateur existe
- Login et register possible
- L'utilisateur peut changer son régime alimentaire
- Menu ergonomique pour la gestion du profil
- Récupération du mot de passe 

### Gestion des magasins

**Actuellement :**
- La carte affiche les magasins que l'on a ajouté
- Localisation de l'utilisateur
- Consultation des produits de chaque magasin
- Ajouter des produits à chaque magasin

### Magasin le plus intéressant

**Actuellement :**
- Recherche du magasin le plus proche
- Affichage des prix des magasins
- Affichage du magasin le mois cher

### Ajout des produits d'un magasin
**Actuellement :**
- Encodage à la main d'un magasin
- Encodage à la main des produits d'un magasin
- Changement des données d'un produit d'un magasin
- Suppression des produits d'un magasin
- Importation d'un fichier JSON avec de nouveaux produits d'un magasin

### Section d'aide
** Actuellement :**
- Bouton d'aide disponible à chaque instant avec une section d'aide disponible pour chaque
section majeure du logiciel

**A améliorer / ajouter:**
- rentabilité prix / distance 
- section d'aide plus fortement développée
- importation plus intelligente

