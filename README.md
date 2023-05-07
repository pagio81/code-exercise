# Littlepay Java Code Exercise

## How the hypothetical system works

Before boarding a bus at a bus stop, passengers tap their credit card (identified by the PAN, or Primary Account
Number) on a card reader. This is called a tap on. When the passenger gets off the bus, they tap their card
again. This is called a tap off. The amount to charge the passenger for the trip is determined by the stops where
the passenger tapped on and tapped off. The amount the passenger will be charged for the trip will be
determined as follows:
```
Trips between Stop 1 and Stop 2 cost $3.25
Trips between Stop 2 and Stop 3 cost $5.50
Trips between Stop 1 and Stop 3 cost $7.30
```
Note that the above prices apply for travel in either direction (e.g. a passenger can tap on at stop 1 and tap off
at stop 2, or they can tap on at stop 2 and can tap off at stop 1. In either case, they would be charged $3.25).
### Completed Trips
If a passenger taps on at one stop and taps off at another stop, this is called a complete trip. The amount the
passenger will be charged for the trip will be determined based on the table above. For example, if a passenger
taps on at stop 3 and taps off at stop 1, they will be charged $7.30.
### Incomplete trips
If a passenger taps on at one stop but forgets to tap off at another stop, this is called an incomplete trip. The
passenger will be charged the maximum amount for a trip from that stop to any other stop they could have
travelled to. For example, if a passenger taps on at Stop 2, but does not tap off, they could potentially have
travelled to either stop 1 ($3.25) or stop 3 ($5.50), so they will be charged the higher value of $5.50.
### Cancelled trips
If a passenger taps on and then taps off again at the same bus stop, this is called a cancelled trip. The
passenger will not be charged for the trip

## Inputs
The applications receives as input a file with the following format:

*taps.csv*
```
ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
1, 22-01-2018 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559
2, 22-01-2018 13:05:00, OFF, Stop2, Company1, Bus37, 5500005555555559
```
## Output
Tha application produces as output a file with the following format:

*trips.csv*
```
Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status
22-01-2018 13:00:00, 22-01-2018 13:05:00, 900, Stop1, Stop2, $3.25, Company1, B37, 5500005555555559, COMPLETED
```

## Build and Run 
The application is a spriong boot maven project designed as shell command application
Build:
```
mvn clean install
```
Run:
```
java -jar target/code-exercise-0.0.1-SNAPSHOT.jar
```
Once the shell is open, digit `help` for the available commands:
```
shell:> help
> AVAILABLE COMMANDS

> Built-In Commands
>        help: Display help about available commands
>        stacktrace: Display the full stacktrace of the last error.
>        clear: Clear the shell screen.
>        quit, exit: Exit the shell.
>        history: Display or save the history of previously run commands
>        version: Show version info
>        script: Read and execute commands from a file.

> Code Excercise Commands
>        process: Process input file "taps.csv" and produce output file "trips.csv"
>        process-file: Process input/output files with given locations 
```

For example
```
process
```

Will process the default file `taps.csv` which contains the following scenarios:

PAN: 5500005555555559 -> 2 completed trips (Stop1 -> Stop2) & (Stop2 -> Stop3)
PAN: 5454545454545454 -> 1 completed trip (Stop1 -> Stop3)
PAN: 36700102000000 -> 1 completed trip (Stop2 -> Stop1)
PAN: 34343434343434 -> 1 incomplete trip (Stop2)
PAN: 5555555555554444 -> 1 cancelled trip (Stop1)

Here an example to process a file in a different location:

```
process-file --input /Users/francescopagetti/custom-taps.csv --output /Users/francescopagetti/trips-results.csv
```

## Assumptions

* input file is well formed and is not missing data, otherwise application would end with an error (e.g. bus on tagOn is different from tag off)
* PAN won't be validated, this opens the possibility to use tokens instead of real credit card numbers
* if user in the same file tagged on and off multiple times, e.g. changed a bus, the tags are registered in the correct order and it is ok as outcome to have 2 trips 
* Application was written to minimise the database entries (need to register only fare for Stop1->Stop2 , Stop2-Stop1 is dealt programmatically)
* DB is loaded in memory, its is not expected a huge amount (~100k stops are less than 10MB) 
* Application was optimised to don't load input and output files all in memory, instead stream them as they may be vey big