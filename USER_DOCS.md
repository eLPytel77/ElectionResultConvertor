# Election result convertor

# Overview

Election result convertor is a program with text UI that takes election results in Czech republic and computes the resulting number of deputies for each political party depending on the system.

## Election system

Great part of the program is configuring your own system. 
Configuring your system contains editing the constituencies for the first scrutinium, choosing the mandates computation, the threshold and number of mandates.

Currently there are six hardcoded election systems:
- D'Hondt divisor
- Saint-Lagueh divisor
- Imperiali Divisor
- Hare quote
- Hagenbach-Bischoffov quote
- Imperiali quote

In the divisors systems all mandates are computed in the first scrutinium. In the quotes types systems there is second scrutinium for which is used the D'Hodnt method.

# Get started

The application is using Java 25 and maven build system. 
For proper run of the program you will need the input data in the format as in the example.xml file, which contains the results of the Czech elections in 2025. You can create the file on your own or download it from the [czech statistical office website](https://www.volby.cz/opendata/ps2025/ps2025_opendata.htm) (You will need "celkové výsledky za čr a kraje").
This input file needs to be in the same directory as the pom.xml file.

After succesfully building the application you can run it using the mave command ```mvn exec:java```.
The text user interface will acompany you through the program workflow. At the end you will be asked to type in the output file. You don't really have, if you don't want, it will print the output into the console.