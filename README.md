Movie Reviews System API

Before running:
1) Install Docker and Docker-Compose if not yet installed.
2) Request a free Api Key in https://www.omdbapi.com/apikey.aspx
3) Open the file ./reviews-api/src/main/resources/application.properties and set the value of the property omdbApiKey to be the apikey you received by email in step 2.
4) (Optional) Open the file ./users-api/src/main/resources/application.properties and the value of the property jwt_secret to a random value of your choice (this value is used in the hash algorithm to create the Authentication tokens)

How to run:
Run in the terminal at the root of the project:
docker-compose up -d

Optionally, you can run the following commando to see the logs while the application is starting (press control + C to stop listening for these logs after the application finished start up process)
docker-compose logs -f

To shutdown the application, run the following command:
docker-compose down

The application endpoints are documented in the json file at the root of the project. Import it to Postman in order to see it.

To facilitate testing, the following users are automatically created when the application starts (all registered with password 123):
MODERADOR1  - Authorities: LEITOR, BÁSICO, AVANÇADO, MODERADOR
AVANÇADO1   - Authorities: LEITOR, BÁSICO, AVANÇADO
BÁSICO      - Authorites: LEITOR, BÁSICO

Use the Authenticate endpoint to get an access token corresponding to one of these users to test endpoints restricted to users with a certain authority.
All new registered users after the start of the application starts only the authority LEITOR.