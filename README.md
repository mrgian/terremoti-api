<p align="center"><img src="https://github.com/mrgian/terremoti-api/raw/master/images/icons/icon.png" height=100></p>
<h1 align="center">Terremoti REST API</h1>
<h3 align="center">
Open Source REST API per informazioni sui terremoti
</h3>

## Descrizione

Questa REST API permette di ottenere le informazioni sui terremoti registrati dalla **Rete Sismica Nazionale** negli ultimi sette giorni.

I dati sono ricavati dai tweet postati dall'account Twitter dell'**Istituto Nazionale di Geofisica e Vulcanologia** ([@INGVterremoti](https://twitter.com/INGVterremoti)).

## Sommario

* [AppQuake](#appquake)
* [Utilizzo](#utilizzo)
* [Rotte](#rotte)
* [Formato dei dati](#formato-dei-dati)
* [Esempi](#esempi)
* [Filtri](#filtri)
* [Compilazione](#compilazione)
* [Configurazione](#configurazione)
* [Diagramma dei casi d'uso](#diagramma-dei-casi-duso)
* [Diagramma delle classi](#diagramma-delle-classi)
* [Credits](#credits)

## AppQuake

<p align="center"><img src="https://github.com/mrgian/appquake/raw/master/screenshot/screenshot_2.png" width="25%"> <img src="https://github.com/mrgian/appquake/raw/master/screenshot/screenshot_3.png" width="25%"> <img src="https://github.com/mrgian/appquake/raw/master/screenshot/screenshot_4.png" width="25%"></p>

Al fine di dare un'utilità a questa API ho realizzato un'app per Android e iOS chiamata AppQuake che ne fa uso, permette di visualizzare con una semplice interfaccia grafica le informazioni sugli ultimi terremoti.

L'app è stata scritta in Dart con il framework [Flutter](https://github.com/flutter/flutter), il codice sorgente è disponibile in [questo repository](https://github.com/mrgian/appquake) ed è disponibile al download sul [Google Play Store](https://play.google.com/store/apps/details?id=it.mrgian.appquake)

## Utilizzo

Per usufruire dell'API è necessario fare delle richieste `GET` o `POST` all'url `https://api.terremoti.gian.im` (oppure all'url `http://localhost:8000` se si esegue l'applicazione in locale) specificando la rotta e il body della richiesta a seconda dei dati che si vuole ricevere.

## Rotte

|Tipo|Rotta                                                   |Descrizione                                                    |
|----|--------------------------------------------------------|---------------------------------------------------------------|
|GET |`https://api.terremoti.gian.im`                         |Restituisce le informazioni sui terremoti                      |
|GET |`https://api.terremoti.gian.im/stats`                   |Restituisce la media di terremoti al giorno                    |
|GET |`https://api.terremoti.gian.im/stats?field=<campo>`     |Restituisce le statistiche del campo specificato (i campi validi sono `valoreMagnitudo` e `profondita`)|
|GET |`https://api.terremoti.gian.im/metadata`                |Restituisce i metadati                                         |
|POST|`https://api.terremoti.gian.im`                         |Restituisce le informazioni sui terremoti filtrati con le regole specificate nel body della richiesta (vedi la sezione [Filtri](#filtri))|
|POST|`https://api.terremoti.gian.im/stats`                   |Restituisce la media di terremoti al giorno, calcolata solo con i terremoti filtrati con le regole specificate nel body della richiesta (vedi la sezione [Filtri](#filtri))|
|POST|`https://api.terremoti.gian.im/stats?field=<campo>`     |Restituisce le statistiche del campo specificato, calcolate solo con i terremoti filtrati con le regole specificate nel body della richiesta (i campi validi sono `valoreMagnitudo` e `profondita`)|



## Formato dei dati

I dati vengono sempre restituiti in formato JSON con i seguenti campi per le informazioni sui terremoti:

- `valoreMagnitudo` valore della magnitudo
- `tipoMagnitudo` tipo di magnitudo, locale (ML) o momento (Mw)
- `ora` ora a cui è avvenuto il terremoto in formato *hh:mm*
- `data` giorno in cui è avvenuto il terremoto in formato *dd-MM-yyyy*
- `localita` luogo in cui è avvenuto il terremoto
- `profondita` profondità alla quale è avvenuto il terremoto espressa in *km*
- `link` link da seguire per avere maggiori informazioni sul terremoto

e con i seguenti campi per le statistiche:

- `avg` valore medio del campo specificato
- `max` valore massimo del campo specificato
- `min` valore minimo del campo specificato
- `mediaGiorno` media dei terremoti avvenuti in un giorno


## Esempi

`GET https://api.terremoti.gian.im` restituisce un JSON di questo tipo:
```
[
	{
		"valoreMagnitudo": 3.7,
		"tipoMagnitudo": "ML",
		"ora": "17:28",
		"data": "02-07-2020",
		"localita": "Costa Siracusana (Siracusa)",
		"profondita": 14.0,
		"link": "https://t.co/jN8Tgw5zir"
	},
	{
		"valoreMagnitudo": 2.8,
		"tipoMagnitudo": "ML",
		"ora": "21:24",
		"data": "30-06-2020",
		"localita": "2 km NW Lama Mocogno (MO)",
		"profondita": 18.0,
		"link": "https://t.co/Ni1DGcG62E"
	}
]
```

`GET https://api.terremoti.gian.im/stats` restituisce un JSON di questo tipo:
```
{
	"mediaGiorno": 1.6666666,
}
```

`GET https://api.terremoti.gian.im/stats?field=valoreMagnitudo` restituisce un JSON di questo tipo:
```
{
    "min": 2.5,
    "avg": 3.2916667,
    "max": 6.3
}
```

## Filtri

Quando si esegue una richiesta `POST` all'API è possibile specificare nel body le regole in formato JSON in base alle quali verranno filtrati i risultati.

Il filtro ha il seguente formato:

```
{
    "<operatore>": [
        {
            "<operatore>": [
                {
                    "var": <campo>
                },
                <valore>
            ]
        },
        ...
    ]
}
```

il seguente esempio filtra i terremoti che hanno un valore di magnitudo minore di 3 e sono avvenuti a una profondità maggiore di 10 km:

```
{
    "and": [
        {
            "<": [
                {
                    "var": "valoreMagnitudo"
                },
                3
            ]
        },
        {
            ">": [
                {
                    "var": "profondita"
                },
                10
            ]
        }
    ]
}
```

il seguente esempio filtra i terremoti che sono avvenuti dal 2 luglio 2020:

```
{
    ">=": [
        {
            "var": "data"
        },
        20200702
    ]
}
```

### Lista degli operatori

Operatori condizionali:
- `>` maggiore di (solo per i campi `valoreMagnitudo`, `profondita`, `data` in formato *yyyyMMdd*)
- `<` minore di (solo per i campi `valoreMagnitudo`, `profondita`, `data` in formato *yyyyMMdd*)
- `>=` maggiore o uguale di (solo per i campi `valoreMagnitudo`, `profondita`, `data` in formato *yyyyMMdd*) 
- `<=` minore o uguale di (solo per i campi `valoreMagnitudo`, `profondita`, `data` in formato *yyyyMMdd*)
- `==` uguale a

Operatori logici:
- `and`
- `or`

## Compilazione

Per compilare l'applicazione è necessario avere il Java Development Kit 11 e Maven installati ed
eseguire i seguenti comandi da terminale:

`git clone https://github.com/mrgian/terremoti-api.git`

`cd progetto-oop`

`mvn clean package`

se la build va a buon fine dovresti ricevere un avviso di questo tipo:

`[INFO] BUILD SUCCESS`

e troverai il l'eseguibile nella directory `target` con il nome `terremoti-api-1.0.jar`

Per eseguire l'applicazione basta eseguire il comando `java -jar terremoti-api-1.0.jar`

## Configurazione

É possibile modificare la configurazione di default modificando i dati presenti nel file JSON `src/main/resources/defaultConfig.json`, dove:

- `baseUrl` è url dell'API da interrogare per ricavare i dati (senza parametri)
- `token` è il bearer token per l'autenticazione
- `user` è l'username dell'account Twitter da cui ricavare i dati

## Diagramma dei casi d'uso

<img src="https://github.com/mrgian/terremoti-api/raw/master/images/uml/use_case_diagram.png" width="50%">

## Diagramma delle classi

<img src="https://github.com/mrgian/terremoti-api/raw/master/images/uml/class_diagram.png">

## Credits

Progetto interamente sviluppato da [Gianmatteo Palmieri](https://github.com/mrgian)
