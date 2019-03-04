# Quarkus demo: Infinispan Client

This example showcases how to use Infinispan client with Quarkus. 

# Run infinispan server

- Running with docker `docker run -it -p 11222:11222 jboss/infinispan-server:latest`
- Download the server from `http://www.infinsispan.org` and run `./bin/standalone.sh`

# Harry-Potter demo

The demo will load characters and spells. Characters do execute spells, and we want to monitor who is in
Hogwarts and is executing magic.

A Character has an id, name, biography and type (other, student, teacher, muggle).
A Spell has an id, name and description.
A Spell is performed by a character. This character can or cannot be at Hogwarts.

## Data loading
`DataLoader` class loads characters and spells in two separate stores.
This is done on startup time

## Magic is going on
[Hogwarts Magic Creator](src/main/java/org/acme/infinispanclient/service/HogwartsMagicCreator.java) is going to emulate
characters performing some magic. Randomly will pick up a character and a spell, and perform the magic!
Characters in Hogwarts are teachers and students. 

## Search Search
[A simple REST service](src/main/java/org/acme/infinispanclient/CharactersResource.java) is available to query
characters by id or perform a full-text search on top of the name or the biography. 

## Magic Socket
[A socket](src/main/java/org/acme/infinispanclient/HogwartsMagicSocket.java) that performs Continuous Query make possible to
monitor which characters in Hogwarts are now performing some magic.


# Run the demo
In dev mode, just run `mvn clean compile quarkus:dev`
Go to `http://localhost:8080` and monitor the magic!
You will be connected to the monitoring socket and you will find there the links to perform some REST search.