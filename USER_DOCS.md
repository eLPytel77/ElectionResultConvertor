# Election Result Converter

# Overview

Election Result Converter is a program with text-based UI that takes election results in the Czech Republic and computes the resulting number of deputies for each political party depending on the system.

## Election system

A great part of the program is configuring your own system. 
Configuring your system involves editing the constituencies for the first scrutinium, choosing the mandates computation, the threshold and number of mandates.

Currently there are six hardcoded election systems:
- D'Hondt divisor
- Saint-Lagueh divisor
- Imperiali Divisor
- Hare quote
- Hagenbach-Bischoffov quote
- Imperiali quote

In the divisors systems all mandates are computed in the first scrutinium. In the quotes types systems there is second scrutinium for which is used the D'Hondt method.

## Statistics

Other functionality of this application is showing the user basic statistics about their chosen system. Those statistics are:
- Proportionality indicator
- Vote to seat ratio
- Threshold impact

The proportionality indicator is in reality the Gallagher index, which measures the difference between the percentage of votes each party gets and the percentage of seats each party gets in the chosen system.

Vote to seat ratio is really just a number of votes per mandate.

Threshold impact is a percentage of lost votes. In other words, votes for the parties that didn't pass the threshold.

# Get started

The application uses Java 25 and Maven build system. 
For proper running of the program you will need the input data in the format of the example.xml file, which contains the results of the Czech elections in 2025. You can create the file yourself or download it from the [Czech Statistical Office website](https://www.volby.cz/opendata/ps2025/ps2025_opendata.htm) (You will need "celkové výsledky za čr a kraje").
This input file needs to be in the same directory as the pom.xml file.

After successfully building the application you can run it using the maven command ```mvn exec:java```.
The text user interface will accompany you through the program workflow. At the end you will be asked to type in the output file. You don't really have to specify an output file. If you don't want to, it will print the output to the console.