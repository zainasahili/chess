# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh

java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
## Server Design diagram
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxMDBPN4-IFoOxyTAADIQaJJAJpDJZbt5IvFEvVOpNVoGdQJNBD0WjWYvN4cA7FkGFfMlsOtyjrsXyfksoEAucBaymqKDlAgh48j0b4oB++ivEsP5yoYLphm6ZIUgatLlqOJpEuGFocjA3K8gagrCjAqGhhRbqdgm8F2gK2iOs6BL4Sy5Set6QYkSOozkUybGRhy0YwLGwY8RxnYAWmB5HtmuaYKp7HXmmQEVuOrZfNOs7NkZi7tn+XY5GA5QVP2TiNEOUyoSsJlBmZ84TJMBxmJwpheL4-gBF4KDoPuh6+MwJ7pJkmDZOYbK-vZ0gAKJ7ml9Rpc0LSPqoz7dKZjboFZbKqeUxVztp0EdrBTryjAiH2NFAaeSVaDYspeGsYJMDkmAbV1h1klmhGhSWjRPK2tODFhFVpWutKSZXnBlXtdV3X8b1pJGCg3CZDAC1oCxUnLRN1EcPtFKGMdMAAGbeMMR1Bg6S3mitRRrTAirKrxibJrVaa-SqOmfSlpbYdQdWnIlGB9gO-kruuwWBJCtp7tCMAAOKjqysVnglF7MPVN7Y1luX2KORUbaVv7lUD63DdVYOrQ1nHILEuOjKoQ0ziN-24dtZ27QNfNeaNlEyeUtEza92hCvNtMne9Eb1eCL3M3TOEagJosUtzajiwLqtUTL039Xjiua-z1Wm+DOEIdCAA8VM8-kgsqYzTXQobrKaQgeZA7p0P6VMbtqOMlT9BHACS0hRwAjL2ADMAAsTynpkBoVj5Tw6AgoANjnIF5+Ho4AHKjj5ewwI0UPHJ9cN2ZUjnNC5fQR6oUcVDHo7x0nqcZ1MWf6qRozufnhcgMX4-3H0XwR1Xow13XS4BSjm4BNgPhQNg3DwLqh2GykcXnrZyV6fZtQNJT1PBMrQ5L6ODd1f+3vHbMPTP6MUFAm-X12ZCSPigQ2xs5xfwjmgZeKA9hdR1j1EW7p+oUnAegSW0kLrm15LNBWjFjqnTGiHIBNsvKe0QWNYBXpMiG3jrCH+KAMHnXZOUOScdpBHV5AwwhUt1aNVodIchgN-5pmEjQ0c6YcyBxqiI4hgFy6jAHuUZO6cYCvxBLDYm9k24wA7uwweqj17IyClvSw+1ELJBgAAKQgDyHGEiAgFyLkTC+pM0zVEpPeFoEcaZaxfL0fewAzFQDgBARCUBZjsPUQzERTNbboC-oE4JoTwmRP7tIP+qZL7fRsTyMBn9SxJMoCk6AaTFEZPgXBXWO1kFi2Okwj6WCpo4PlvIa2BD7Z8M4gQraMg9bIPutNMB7CGnjRYQ9C2J9GLsJ4dJLpZJRzxgQcLSh1jbFoHycrRJhdklhNKZbcpcDZnMMmn4G0ephnpMxNbIpIS9mwGOR9eZP0lQqm6sI1MCpXkyKyQ7UOVxolNy0TARyphlyBQ3CFLwQSuxelgMAbA+9CDxESKfQmzdsklFShlLKOVWjGDKh84EQluB4FhNiVmgCNYgFJXCSp7NqlIJJXCg0vNRlm3gJlBQ4QmIdA0J0tmGsQbkJWeGZleA-bkseWMyacAuU8q7tK4hQrvm9NVuKqACk2VKulpytK3LSEdR1c84VYM3FfL+vTIFtltEDnbr0BuzoiUlmFeCoAA
