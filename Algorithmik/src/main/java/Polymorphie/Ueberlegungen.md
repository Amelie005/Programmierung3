Ausgaben bei Polymorphietest:

Aublauf Oberklasse:
"Polymorphie.Oberklasse - textAusgeben"
"x in Polymorphie.Oberklasse: 7"
"x in Polymorphie.Oberklasse: 7"
"------------------"
"getX() in Polymorphie.Oberklasse aufgerufen: 7"
"thisXAusgeben in Polymorphie.Oberklasse aufgerufen: 7"
"thisXAusgeben2 in Polymorphie.Oberklasse aufgerufen: 7"
"------------------"
"this.x in Polymorphie.Oberklasse: 7"

Ablauf Unterklasse:
"Polymorphie.Unterklasse - textAusgeben"
"x in Polymorphie.Unterklasse: 7"
"x in Polymorphie.Unterklasse: 11"
"------------------"
"super-Methode aufgerufen: "

...

Ausgabe von Polymorphietest:

Polymorphie.Oberklasse - textAusgeben
x in Polymorphie.Oberklasse: 7
x in Polymorphie.Oberklasse: 7
------------------
getX() in Polymorphie.Oberklasse aufgerufen: 7
thisXAusgeben in Polymorphie.Oberklasse aufgerufen: x in Polymorphie.Oberklasse: 7
thisXAusgeben2 in Polymorphie.Oberklasse aufgerufen: x in Polymorphie.Oberklasse: 7
------------------
this.x in Polymorphie.Oberklasse: 7

Polymorphie.Unterklasse - textAusgeben
x in Polymorphie.Oberklasse: 7
x in Polymorphie.Unterklasse: 11
------------------
getX() in Polymorphie.Oberklasse aufgerufen: 11
thisXAusgeben in Polymorphie.Oberklasse aufgerufen: x in Polymorphie.Oberklasse: 7
thisXAusgeben2 in Polymorphie.Oberklasse aufgerufen: x in Polymorphie.Unterklasse: 11
------------------
------------------
super-Methode aufgerufen: this.x in Polymorphie.Oberklasse: 7
this.x in Polymorphie.Unterklasse: 11
------------------

Polymorphie.Unterklasse - textAusgeben
x in Polymorphie.Oberklasse: 7
x in Polymorphie.Unterklasse: 11
------------------
getX() in Polymorphie.Oberklasse aufgerufen: 11
thisXAusgeben in Polymorphie.Oberklasse aufgerufen: x in Polymorphie.Oberklasse: 7
thisXAusgeben2 in Polymorphie.Oberklasse aufgerufen: x in Polymorphie.Unterklasse: 11
------------------
------------------
super-Methode aufgerufen: this.x in Polymorphie.Oberklasse: 7
this.x in Polymorphie.Unterklasse: 11
------------------

Process finished with exit code 0


