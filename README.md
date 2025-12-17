# Monstre_de_poche


# 🐾 Monstre de Poche

**Monstre de Poche** est un jeu de combat de monstres au tour par tour développé en Java. Inspiré des RPG classiques, il propose une interface graphique interactive incluant une arène visuelle, un système de types élémentaires et une gestion d'équipe.

## 🚀 Fonctionnalités

* **Arène Graphique 2.5D** : Visualisation des monstres en combat avec un système de profondeur (Joueur en bas à gauche, Adversaire en haut à droite).
* **Système de Types Dynamique** : Les attaques et les monstres possèdent des types (Feu, Eau, Électrique, Plante, Sol) influençant les dégâts et la couleur de l'interface.
* **HUD Intégré** : Barres de vie dynamiques (Vert/Orange/Rouge) et noms des monstres affichés directement dans l'arène.
* **Gestion de Données** : Chargement des monstres et des attaques via des fichiers textes configurables (`monsters.txt`, `attacks.txt`).
* **Images Distantes** : Support des sprites via des URLs (PokéAPI) avec un système de cache pour optimiser les performances.
* **Journal de Combat** : Un historique détaillé de chaque action (dégâts, objets, switch) s'affiche en temps réel.

## 🛠️ Structure du Projet

```text
Monstre_de_poche/
├── src/                # Code source Java
│   └── com/esiea/pootp/
│       ├── gui/        # BattleFrame, ArenaPanel, LauncherMenu
│       ├── monsters/   # Classes des différents types de monstres
│       ├── attacks/    # Système d'attaques
|       ├── objects/    # Objets consommables, Potion, Medicine
|       ├── types/      # Types élémentaires
│       └── data/       # MonsterLoader, MonsterFactory, MonsterMap
|       ├── action      # Action effectués par le joueur (Attaque, Objets, Changements)
|       ├── controller 
|       ├── main        # Classe principale, lancement du jeu
|       ├── player      # Représentation d'un joueur, team de monstres, etc
|        
└── README.md

```

## 📦 Installation et Lancement

### Prérequis

* **Java JDK 8** ou supérieur installé.
* Les fichiers `monsters.txt` et `attacks.txt` doivent être présents à la racine du projet.

### Compilation

Depuis le dossier `src/` :

```bash
javac com/esiea/pootp/Main.java

```

### Exécution

**Important :** Placez-vous à la racine du projet (là où se trouvent vos fichiers `.txt`) :

```bash
java -cp src com.esiea.pootp.Main

```

## 📝 Configuration des données

Vous pouvez ajouter vos propres monstres dans `monsters.txt` en suivant ce format :

```text
Monster
Name Dracaufeu
Type Fire
HP 180 220
Attack 90 130
Defense 60 95
Speed 100 120
Image https://URL_DE_VOTRE_IMAGE.png
EndMonster

```

## 🎨 Aperçu de l'Interface

* **Centre** : Arène de combat avec décors et sprites.
* **Droite** : Journal de bord textuel.
* **Bas** : Menu d'actions (Attaques colorées par type, Sac d'objets, Changement de monstre).
