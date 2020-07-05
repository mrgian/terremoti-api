<p align="center"><img src="https://github.com/mrgian/progetto-oop/raw/master/images/icons/icon.png"></p>
<h1 align="center">Terremoti REST API</h1>
<h3 align="center">
Open Source REST API per informazioni sui terremoti
</h3>

Questa REST API permette di ottenere informazioni sui terremoti degli ultimi 7 giorni registrati dalla **Rete Sismica Nazionale**.
I dati sono ricavati dai tweet postati dall'account Twitter dell'**Istituto Nazionale di Geofisica e Vulcanologia** ([@INGVterremoti](https://twitter.com/INGVterremoti)).


## Uso

Per usufruire dell'API è necessario fare delle richieste `GET` o `POST` all'url `https://mrgian.it/terremoti` (oppure all'url `http://localhost:8080` se si fa partire l'applicazione su una macchina locale) specificando la rotta e il body della richiesta a seconda dei dati che si vuole ricevere.

## Rotte

|Tipo|Rotta                                |Descrizione                                                    |
|----|-------------------------------------|---------------------------------------------------------------|
|GET |`/terremoti`                         |Restituisce le informazioni sui terremoti  |
|GET |`/terremoti/stats`                   |Restituisce la media di terremoti al giorno|
|GET |`/terremoti/stats?field=<campo>`     |Restituisce le statistiche del campo specificato (i campi validi sono `valoreMagnitudo` e `profondita`|
|GET |`/terremoti/metadata`                |Restituisce i metadati                                         |
|POST|`/terremoti`                         |Restituisce le informazioni sui terremoti filtrati con le regole specificate nel body della richiesta (vedi la sezione **Filtri**)|
|POST|`/terremoti/stats`                   |Restituisce la media di terremoti al giorno, calcolata solo con i terremoti filtrati con le regole specificate nel body della richiesta (vedi la sezione **Filtri**)|
|POST|`/terremoti/stats?field=<campo>`     |Restituisce le statistiche del campo specificato, calcolate solo con i terremoti filtrati con le regole specificate nel body della richiesta (i campi validi sono `valoreMagnitudo` e `profondita`)|



## Formato dei dati

I dati vengono sempre restituiti in formato JSON con i seguenti campi per le informazioni sui terremoti:

- `valoreMagnitudo` valore della magnitudo
- `tipoMagnitudo` tipo di magnitudo, locale (ML) o momento (Mw)
- `ora` ora a cui è avvenuto il terremoto in formato **hh:mm**
- `data` giorno in cui è avvenuto il terremoto in formato **dd-MM-yyyy**
- `localita` luogo in cui è avvenuto il terremoto
- `profondita` profondità alla quale è avvenuto il terremoto espressa in km
- `link` link da seguire per avere maggiori informazioni sul terremoto

e con i seguenti campi per le statistiche:

- `avg` valore medio del campo specificato
- `max` valore massimo del campo specificato
- `min` valore minimo del campo specificato
- `mediaGiorno` media dei terremoti avvenuti in un giorno


## Esempi

`GET https://mrgian.it/terremoti` restituisce un JSON di questo tipo:
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

`GET https://mrgian.it/terremoti/stats` restituisce un JSON di questo tipo:
```
{
	"mediaGiorno": 1.6666666,
}
```

`GET https://mrgian.it/terremoti/stats?field=valoreMagnitudo` restituisce un JSON di questo tipo:
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
    "<=": [
        {
            "var": "data"
        },
        20200702
    ]
}
```

### Lista degli operatori

Operatori condizionali:
- `>` maggiore di (solo per i campi `valoreMagnitudo`, `profondita`, `data` in formato **yyyyMMdd**)
- `<` minore di (solo per i campi `valoreMagnitudo`, `profondita`, `data` in formato **yyyyMMdd**)
- `>=` maggiore o uguale di (solo per i campi `valoreMagnitudo`, `profondita`, `data` in formato **yyyyMMdd**) 
- `<=` minore o uguale di (solo per i campi `valoreMagnitudo`, `profondita`, `data` in formato **yyyyMMdd**)
- `==` uguale a

Operatori logici:
- `and`
- `or`

## Compilazione

Per compilare l'applicazione è necessario avere il Java Development Kit 11 e Maven installati ed
eseguire i seguenti comandi da terminale:

`git clone https://github.com/mrgian/progetto-oop.git`

`cd progetto-oop`

`mvn clean package`

se la build va a buon fine dovresti ricevere un avviso di questo tipo:

`[INFO] BUILD SUCCESS`

e troverai il l'eseguibile nella directory `target` con il nome `terremoti-api-1.0.jar`

Per eseguire l'applicazione basta eseguire il comando `java -jar terremoti-api-1.0.jar`


## Diagramma dei casi d'uso

<img src="https://github.com/mrgian/progetto-oop/raw/master/images/uml/use_case_diagram.png" width="50%">

## Diagramma delle classi

<img src="https://github.com/mrgian/progetto-oop/raw/master/images/uml/class_diagram.png">
