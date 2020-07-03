<p align="center"><img src="https://cdn.icon-icons.com/icons2/2106/PNG/512/earthquake_icon_129734.png" style="width:150px;height:150px;"></p>
<h1 align="center">Terremoti REST API</h1>
<h3 align="center">
Open Source REST API per informazioni sui terremoti
</h3>

Questa REST API permette di ottenere informazioni utili sui terremoti degli ultimi 7 giorni registrati dalla **Rete Sismica Nazionale**.
I dati sono ricavati dai tweet postati dall'account Twitter dell'**Istituto Nazionale di Geofisica e Vulcanologia** (@INGVterremoti).


## Uso

Per usufruire dell'API è necessario fare delle richieste `GET` o `POST` all'url `https://mrgian.it/terremoti` (oppure all'url `http://localhost:8080` se si fa partire l'applicazione su una macchina locale) specificando la rotta e il body della richiesta a seconda dei dati che si vuole ricevere.

## Rotte

|Tipo|Rotta                  |Descrizione|
|----|-----------------------|-----------|
|GET |`/terremoti`           |Restituisce le informazioni sui terremoti degli ultimi 7 giorni|
|GET |`/terremoti/stats`     |Restituisce le statistiche sui terremoti degli ultimi 7 giorni|
|GET |`/terremoti/metadata`  |Restituisce i metadati|
|POST|`/terremoti`           |Restituisce le informazioni sui terremoti degli ultimi 7 giorni filtrati con le regole specificate nel body della richiesta (vedi la sezione **Filtri**)|


## Formato dei dati

I dati vengono sempre restituiti in formato JSON con i seguenti campi per le informazioni sui terremoti:

- `valoreMagnitudo` valore della magnitudo
- `tipoMagnitudo` tipo di magnitudo, locale (ML) o momento (Mw)
- `ora` ora a cui è avvenuto il terremoto in formato **hh:mm**
- `data` data in cui è avvenuto il terremoto in formato **dd-MM-yyyy**
- `localita` luogo in cui è avvenuto il terremoto
- `profondita` profondità alla quale è avvenuto il terremoto espressa in km
- `link` link da seguire per avere maggiori informazioni sul terremoto

e con i seguenti campi per le statistiche:

- `mediaMagnitudo` media delle magnitudo
- `mediaGiorno` media dei terremoti in un giorno
- `mediaProfondita` media delle profondità


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
	"mediaMagnitudo": 3.3100002,
	"mediaGiorno": 1.6666666,
	"mediaProfondita": 55.2
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

### Lista degli operatori

Operatori condizionali:
- `>` maggiore di
- `<` minore di
- `>=` maggiore o uguale di
- `<=` minore o uguale di
- `==` uguale a

Operatori logici:
- `and`
- `or`
