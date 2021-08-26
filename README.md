# General info

The application is developed in **Scala** and use:
 - **spark** framework to:
    - read input csv files (path hardcoded as **/tmp/trg/data/inputdata** - it can be made configurable using application.conf file)
    - transform data to desired output
    - write output data in parquet format (on **/tmp/trg/data/outputdata**)
 - **Play** framework to expose data via WS api

## Application run

Install [sbt](http://www.scala-sbt.org/) on your machine

Application can be run in two ways:

####1. using **sbt** tool, from solution directory run the command:
   
```sbt runProd```

####2. using docker containers:
        
    - build docker image using command:

        ```sbt docker:publishLocal```

    - start docker container using command:
      
        ```docker-compose up -d```

On startup the application will load the source csv files, transform the data according to specification and write the output in parquet format

The application expose some api's to expose the data; the parquet data is loaded and cached in memory (using spark), therefore after the first call is made the subsequent api calls are very fast

The list of API's ia available at: http://localhost:9000/

- http://localhost:9000/sample_data - return a random 50 records (in json format)
```json
[ {
  "crimeID" : "9cb9570b88add1069a0462eca4a8534cf164d7d635f6d266fb4b575941f16b54",
  "districtName" : "metropolitan",
  "latitude" : 51.5379,
  "longitude" : -0.117013,
  "crimeType" : "Violence and sexual offences",
  "lastOutcome" : "Investigation complete; no suspect identified"
}, {
  "crimeID" : "05e777779a550828db9ce15c8d2dbd6916517fd6268280673476499259c56c6f",
  "districtName" : "leicestershire",
  "latitude" : 52.485901,
  "longitude" : -0.920607,
  "crimeType" : "Violence and sexual offences",
  "lastOutcome" : "Unable to prosecute suspect"
}, {
.........
```
- http://localhost:9000/filter_data - data filtered by **crimeId**, 

    ex: http://localhost:9000/filter_data?crimeId="2df48b560e62bc0478f46090f063a19de8d305ec29e741f445be0ed5ca9a9a76" will return:

```json
[ {
  "crimeID" : "2df48b560e62bc0478f46090f063a19de8d305ec29e741f445be0ed5ca9a9a76",
  "districtName" : "greater-manchester",
  "latitude" : 53.390932,
  "longitude" : -2.197391,
  "crimeType" : "Violence and sexual offences",
  "lastOutcome" : "Status update unavailable"
} ]
```

- http://localhost:9000/crimes_by_location

```json
[ {
  "districtName" : "dorset",
  "count" : 7689
}, {
  "districtName" : "cheshire",
  "count" : 8797
}, {
  "districtName" : "lancashire",
  "count" : 20719
}, {
  "districtName" : "hampshire",
  "count" : 15965
}, {
  "districtName" : "merseyside",
  "count" : 14929
}, {
  "districtName" : "cambridgeshire",
  "count" : 7582
}, {
  "districtName" : "south-wales",
  "count" : 14018
}, {
  ...
}
```

- http://localhost:9000/crimes_by_crime_type

```json
[ {
  "crimeType" : "Bicycle theft",
  "count" : 10757
}, {
  "crimeType" : "Public order",
  "count" : 44419
}, {
  "crimeType" : "Drugs",
  "count" : 11502
}, {
  "crimeType" : "Other crime",
  "count" : 9252
}, {
  "crimeType" : "Robbery",
  "count" : 7231
}, {
  "crimeType" : "Criminal damage and arson",
  "count" : 51311
}, {
  "crimeType" : "Theft from the person",
  "count" : 8972
}, {
  "crimeType" : "Shoplifting",
  "count" : 31499
}, {
  "crimeType" : "Burglary",
  "count" : 34577
}, {
  "crimeType" : "Other theft",
  "count" : 50848
}, {
  "crimeType" : "Possession of weapons",
  "count" : 4110
}, {
  "crimeType" : "Violence and sexual offences",
  "count" : 172869
}, {
  "crimeType" : "Vehicle crime",
  "count" : 36883
}, {
  "crimeType" : "Anti-social behaviour",
  "count" : 146777
} ]
```
