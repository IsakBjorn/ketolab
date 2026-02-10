# POC for hvordan man kan bruke KETO for å sjekke tilganger
Dette er en minmal POC som tester hvordan vi kan bruke [ory:keto](https://github.com/ory/keto) for å tildele og sjekke tilganger for brukere.

## Hvordan teste

- Kjør opp KETO med compose filen
- Start KetoLabApplication.kt
- Start frontenden
   - Gå til frontend-poc og kjør `npm install` og `npm run dev`
- Bruk IDEA sin httpclient og requestene som ligger i httpRequests for å tildele og trekke tilbake tilganger
- På http://localhost:5173 kan du sjekke hvilke tilganger en gitt bruker har 