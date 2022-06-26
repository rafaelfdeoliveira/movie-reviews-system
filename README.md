Movie Reviews System API

Before running:
1) Install Docker and Docker-Compose if not yet installed.
2) Request a free Api Key in https://www.omdbapi.com/apikey.aspx
3) Open the file ./reviews-api/src/main/resources/application.properties and set the value of the property omdbApiKey to be the apikey you received by email in step 2.
4) (Optional) Open the file ./users-api/src/main/resources/application.properties and the value of the property jwt_secret to a random value of your choice (this value is used in the hash algorithm to create the Authentication tokens)

How to run:
Run in the terminal at the root of the project:
docker-compose up -d