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
- GitHub repozitorij prikazuje **177 contributors**.
- Zadnji release: **2.2.1 (Nov 14, 2025)**.}

---

## Časovna in prostorska zahtevnost (ocena)

SQLDelight sedi nad SQLite, zato je učinkovitost odvisna predvsem od:
- strukture tabel, indeksov, query plana.

Tipično:
- SELECT po indeksiranem stolpcu: približno `O(log n)` (B-tree indeks),
- brez indeksa: `O(n)` scan,
- INSERT: `O(1)` amortizirano + strošek indeksov,
- prostor: `O(n)` glede na število vrstic + indeksi.

---

## Demo

```kotlin
plugins {
    id("app.cash.sqldelight")
}

dependencies {
    implementation("app.cash.sqldelight:android-driver:2.0.0")
}


```md
```kotlin
sqldelight {
    databases {
        create("DemoDatabase") {
            packageName.set("com.example.sqldelightdemo.db")
        }
    }
}


---

## 🔹 Struktura projekta

```md
```text
app/
 ├─ src/main/java/com/example/sqldelightdemo
 │   ├─ MainActivity.kt
 │   └─ db/
 │       └─ DatabaseFactory.kt
 │
 ├─ src/main/sqldelight/com/example/sqldelightdemo/db
 │   ├─ Song.sq
 │   └─ migrations/
 │       └─ 1.sqm


---

## 🔹 SQL shema in poizvedbe (`Song.sq`)

```md
```sql
CREATE TABLE song (
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  title TEXT NOT NULL,
  artist TEXT NOT NULL,
  mood TEXT NOT NULL
);

insertSong:
INSERT INTO song(title, artist, mood)
VALUES (?, ?, ?);

selectAll:
SELECT * FROM song
ORDER BY id DESC;

deleteAll:
DELETE FROM song;

insertWithId:
INSERT INTO song(id, title, artist, mood)
VALUES (?, ?, ?, ?);

---

## 🔹 Inicializacija baze (`DatabaseFactory.kt`)

```md
```kotlin
object DatabaseFactory {

    fun create(context: Context): DemoDatabase {
        val driver = AndroidSqliteDriver(
            DemoDatabase.Schema,
            context,
            "demo.db"
        )
        return DemoDatabase(driver)
    }
}

---

## 🔹 Uporaba baze v aplikaciji (`MainActivity.kt`)

```md
```kotlin
val db = DatabaseFactory.create(this)
val queries = db.songQueries

queries.insertSong("Mask Off", "Future", "Chill")
val songs = queries.selectAll().executeAsList()

---

## 🔹 Uporaba baze v aplikaciji (`MainActivity.kt`)

```md
```kotlin
val db = DatabaseFactory.create(this)
val queries = db.songQueries

queries.insertSong("Mask Off", "Future", "Chill")
val songs = queries.selectAll().executeAsList()

---

## 🔹 Migracija baze (`1.sqm`)

```md
```sql
ALTER TABLE song
ADD COLUMN visited INTEGER NOT NULL DEFAULT 0;
