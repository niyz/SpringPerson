# SpringPerson
- Elliot Gustafsson
- Maria Johnsson
- Simon Nilsson
- Per-Anders Karlsson

# Postman stuff.

- ### add new person.
send all the person values

returns ```true ``` or ```false```
```
POST http://<url>/add
```
example:
```
{
        "name": "lisa",
        "personID": "197702136533",
        "lastName": "leifson",
        "phoneIDList": [
            "073789873213",
            "072787611235"
        ],
        "emailList": [
            "svensss2@svenne.se"
        ],
        "address": {
            "country": "swe",
            "city": "vxo",
            "street": "klo",
            "postalCode": "1234",
            "streetNum": "1"
        }
    }
```

- ### add relation
requires that both persons are in the database.
the relation is done by setting person id and type of relation
returns ```true ``` or ```false```

example:
````
POST http://<url>/add/relation
````

````
[{
	"personID": "197702136544",
	"relation": "father"
},{
	"personID": "197702136533",
	"relation": "son"
}]
````

- ### update a person
Send all the old values and new values
ass an array.
returns ```true ``` or ```false```

````
POST http://<url>/update
````
example:
````
[{
        "name": "sven",
        "personID": "197702136503",
        "lastName": "andersson",
        "phoneIDList": [
            "07378987",
            "07278765"
        ],
        "emailList": [
            "sven@svenne.se"
        ],
        "address": {
            "country": "swe",
            "city": "vxo",
            "street": "klo",
            "postalCode": "1234",
            "streetNum": "1"
        }
    },
{
        "name": "sven",
        "personID": "197702136543",
        "lastName": "andersson",
        "phoneIDList": [
            "07378987",
            "07278765"
        ],
        "emailList": [
            "sven@svenne.se"
        ],
        "address": {
            "country": "swe",
            "city": "vxo",
            "street": "klo",
            "postalCode": "1234",
            "streetNum": "1"
        }
    }]
````

- ### delete person

to delete a person we send the person number to the database
this will remove the person and all relations

returns ```true ``` or ```false```

````
POST http://<url>/delete
````
example:
````
{
	"personID": "197702136533"
}
````

- ### delete relation
to only delete a relation 

returns ```true ``` or ```false```

````
POST http://<url>/delete
````
example:
````
{
	"personID": "197702136533"
}
````
