# SQLDelight Android Demo

Repozitorij prikazuje osnovno uporabo SQLDelight v Android aplikaciji,
vključno z ustvarjanjem baze, izvajanjem poizvedb, obravnavo izjem
in uporabo migracij baze podatkov.

SQLDelight je odprtokodna knjižnica, ki jo razvija podjetje **Cash App**.
Namenjena je delu z SQLite bazami in podpira več platform, med drugim tudi Android.

**Uradni GitHub SQLDelight:** https://github.com/sqldelight/sqldelight
---

## Primernost in utemeljitev izbire tehnologije

SQLDelight je primerna izbira za Android aplikacije, saj temelji na SQLite,
ki je standardna lokalna baza podatkov na tej platformi.

Knjižnica je bila izbrana, ker izboljša klasično uporabo SQLite:
- omogoča tipno varno delo z bazo podatkov,
- preveri pravilnost SQL poizvedb že ob prevajanju,
- zmanjša možnost napak med izvajanjem aplikacije,
- ne uporablja refleksije in je zato primerna za mobilne naprave.

Zaradi uporabe Android-specifičnega SQLite driverja in integracije z
Android build sistemom predstavlja primer platformno odvisnega razvoja.

---

## Prednosti
- Type-safe API iz SQL (manj napak)
- Preverjanje sheme/poizvedb ob build-u
- Brez refleksije (primernejše za mobilne naprave)
- Dobra podpora za migracije

## Slabosti
- Potrebuješ znanje SQL
- Ni ORM: več “ročnega” modeliranja (a več kontrole)
- Migracije moraš načrtovati sam (ampak so podprte)

## Licenca
SQLDelight je pod **Apache 2.0**.

## “Število uporabnikov” (indikatorji uporabe)

Število uporabnikov knjižnice lahko aproksimiramo s številom zvezdic na GitHubu,
kjer ima SQLDelight več tisoč zvezdic (7600), kar nakazuje široko uporabo in
zaupanje razvijalske skupnosti.

## Vzdrževanje (aktivnost projekta)
- GitHub repozitorij prikazuje **177 contributors**. :contentReference[oaicite:5]{index=5}
- Zadnji release: **2.2.1 (Nov 14, 2025)**. :contentReference[oaicite:6]{index=6}

---

## 3) Časovna in prostorska zahtevnost (ocena)

SQLDelight sedi nad SQLite, zato je učinkovitost odvisna predvsem od:
- strukture tabel, indeksov, query plana.

Tipično:
- SELECT po indeksiranem stolpcu: približno `O(log n)` (B-tree indeks),
- brez indeksa: `O(n)` scan,
- INSERT: `O(1)` amortizirano + strošek indeksov,
- prostor: `O(n)` glede na število vrstic + indeksi.

---
