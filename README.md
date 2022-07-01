Movie Reviews System API

Before running:
1) Install Docker (https://docs.docker.com/get-docker/) and Docker-Compose (https://docs.docker.com/compose/install/) if not yet installed.
2) Request a free Api Key at https://www.omdbapi.com/apikey.aspx
3) Open the file /reviews-api/src/main/resources/application.properties and set the value of the property omdbApiKey to be the apikey you received by email in step 2.
4) (Optional) Open the file /users-api/src/main/resources/application.properties and set the value of the property jwt_secret to a random value of your choice (This is recommended in production for security reasons as this value is used in the hash algorithm to create the Authentication tokens)

How to run:
Run in the terminal at the root of the project:
docker-compose up -d

Optionally, you can run the following commando to see the logs while the application is running (press control + C to stop listening for these logs)
docker-compose logs -f

To shutdown the application, run the following command:
docker-compose down

The application endpoints are all documented with description and examples in the json file at the root of the project named Movie Reviews System.postman_collection.json.
Import it to Postman (https://www.postman.com/downloads/) in order to see it.
The base Url for all endpoints is http://localhost:8382/

To facilitate testing, the following users are automatically created when the application starts (all registered with password 123):
MODERADOR1  - Authorities: LEITOR, BÁSICO, AVANÇADO, MODERADOR

AVANÇADO1   - Authorities: LEITOR, BÁSICO, AVANÇADO

BÁSICO      - Authorites: LEITOR, BÁSICO

All new registered users after the start of the application starts only with the authority LEITOR. The Authority levels are cummulative. A user with certain authority level always has all authorities below his greatest level too.

Use the Authenticate endpoint to get an access token corresponding to one of these users to test endpoints restricted to users with a certain authority.
All endpoints, except Authentication (/authenticate) and Register User (/sign_up), requires authentication. The authentication token must be sent in the header of the request in the key Authorization after the word Bearer as follows: Bearer <token>

The following endpoints require a greater Authority level than the starting LEITOR authority as follows:

Register Movie Commentary (Http.post in /commentary) and Register Commentary Reply (Http.post em /commentary/reply) require the authority BÁSICO

Register Commentary Evaluation (Http.post in /commentary/evaluation) requires the authority AVANÇADO

Get All Users (/user/all), Get User By UserName (/user/find), Make User Admin (user/admin) and Delete Movie Commentary (Http.delete in /commentary) require the authority MODERADOR

New registered users starts with 0 points in this system. Some actions give points to the user that made the respective request (check in the docs in Postman). When the user hits 20, 100 and 1000 points, he gains the authorites BÁSICO, AVANÇADO and MODERADOR, respectively.
