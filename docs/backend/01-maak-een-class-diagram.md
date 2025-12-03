# Maak een class diagram voor de Lingo trainer

## Voorbereiding
- Zorg dat je de mermaid plugin in je IDE hebt geïnstalleerd.
- Bekijk de [mermaid documentatie](https://mermaid-js.github.io/mermaid/#/classDiagram) voor het maken van class diagrammen.

## Opdracht
Aan de hand van gemaakte user story en scenario’s zijn we meer te weten gekomen over ons domein en de gewenste functionaliteit, het gedrag. Nu kunnen we nadenken over hoe we deze functionaliteit kunnen verdelen over domeinobjecten. Je hoeft hier nog geen rekening mee te houden met de opslag; hier gebruiken we later Hibernate (ORM) voor.
Vertrek vanuit de use cases / scenario’s en schets een domeinmodel dat weergeeft over welke globale domeinconcepten je de verantwoordelijkheden wil verdelen. Je kunt natuurlijk de opdrachtbeschrijving er weer bijpakken. Vaak zijn de zelfstandige naamwoorden die je hierin herkent leidend. Denk ook na over hoe je deze kunt vertalen naar Java. Denk terug aan backend bij semester 3 en de elementen van het Object Model van Booch (abstraction, encapsulation, modularity en hierarchy) en wees niet bang om met veel klassen, interfaces en enums te werken!

- Welke identificeerbare, centrale domeinobjecten (entiteiten) heb je nodig? Hoe gaan we deze entiteiten identificeren?  
- Welke ondersteunende objecten heb je nodig die een of meer waardes vertegenwoordigen of zich kunnen richten op één aspect van functionaliteit?
- Zijn er objecten nodig die een soort data-kapstokje zijn om bijvoorbeeld een uitkomst weer te geven (data transfer objects)?
- Welke verzamelingen van objecten heb je nodig (lists, sets, maps)? Kan je dit in een klasse stoppen die meer bij de termen van het domein past? 
Bijvoorbeeld: geen Map<Student, boolean> maar een AttendenceMap
- Zijn er concepten die je als string of meerdere booleans weer wilt geven? Kan je deze misschien beperken tot een kleine set aan mogelijkheden in de vorm van een enum?
Bijvoorbeeld: enum TrafficLight { RED, ORANGE, GREEN }
- Welk object is het centrale aanspreekpunt?

## Inleveren
Nadat je een schets hebt gemaakt, werk je deze uit in een class diagram met mermaid:
- Maak een .md bestand aan in de docs/backend map met de naam `01-antwoord.md`.

Dit zijn de eisen die we stellen aan het domeinmodel:
- De relaties tussen de klassen zijn zichtbaar.
- De attributen van de klassen zijn zichtbaar.

