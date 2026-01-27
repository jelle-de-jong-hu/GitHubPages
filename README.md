# S4 Project

This project is part of an assignment for 4rd semester of the ICT bachelor programme at the
Hogeschool Utrecht, University of Applied Sciences.

## Installatie Backend
Om de backend lokaal te draaien, heb je docker nodig.
1. Clone de repository
2. Navigeer in de terminal naar de hoofdmap van het project
3. Start de docker met het volgende commando:

```bash
   docker-compose up
   ```  
4. De database is nu bereikbaar op `localhost:15432`
5. Start de backend server met het volgende commando:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
7. De backend is nu bereikbaar op `http://localhost:8080`
8. Je kunt de API testen met src/test/resources/lingo.http en src/test/resources/lingo.http


## Installatie Frontend
* Frontend gebaseerd op https://github.com/tastejs/todomvc/tree/gh-pages/examples/lit
Om dit project lokaal te draaien, heb je [Node.js](https://nodejs.org/) nodig.
1. Clone de repository
2. Navigeer in de terminal naar de hoofdmap van het project
3. Voer het volgende commando uit om de benodigde dependencies te installeren:
   ```bash
   npm install
   ```
4. Start de ontwikkelserver met het volgende commando:
   ```bash
   npm start
   ```
5. Open je webbrowser en ga naar `http://localhost:3000` om de applicatie te bekijken.

## Architectuur
https://github.com/HU-SD-S4-Studenten-F2526/kennisbank/wiki/Lingo#architectuur-trainer