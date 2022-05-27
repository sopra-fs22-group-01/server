# Cards Against Humanity Online
This project contains everything you need to play a fun game of Cards Against Humanity online with your friends. Enjoy our twist to the game and try out the additional features such as custom cards, and supervotes. 
## Introduction
### Main Goal
This project's main goal is to enable players to play Cards Against Humanity online. Compared to the original Cards Against Humanity game, we also changed some of the original mechanics and added some additional fun features. By doing so we learned a lot about developing a web application and how to work efficiently in a group of 5.

### Motivation
Our general motivation for this project was to expand our knowledge about software development with experience and practice. We choose to create a card game which we all consider fun to play to help us stay motivated and keep morals up.

## Technologies
The technologies used to develop the server side of this project were the following:

-   IntelliJ and Visual Studio Code as IDEs
-   Git and GitHub for the version control and project organization
-   Heroku for the deployment
-   Postman for testing endpoints
-   Spring as application framework
-   Gradle as build manager


## Main Components
In our backend code, we have five main components that are crucial for the game. They are all contained in our [Main]( src/main/java/ch/uzh/ifi/hase/soprafs22/) folder.

1.   The [GameManager](src/main/java/ch/uzh/ifi/hase/soprafs22/game/GameManager.java) class, responsible for managing [Lobbies](src/main/java/ch/uzh/ifi/hase/soprafs22/game/Lobby.java) and [Matches](src/main/java/ch/uzh/ifi/hase/soprafs22/game/Match.java). This means storing the available lobbies, creating and deleting of lobbies and matches.
2.   The [Lobby](src/main/java/ch/uzh/ifi/hase/soprafs22/game/Lobby.java) class, responsible for gathering players and starting new [Matches](src/main/java/ch/uzh/ifi/hase/soprafs22/game/Match.java).
3.   The [Match](src/main/java/ch/uzh/ifi/hase/soprafs22/game/Match.java) class, responsible for storing the [Round](src/main/java/ch/uzh/ifi/hase/soprafs22/game/Round.java), keeping count of scores, round number and supervotes.
4.   The [Round](src/main/java/ch/uzh/ifi/hase/soprafs22/game/Round.java), responsible for storing the black card, the hands of the players, the chosen cards, handling the different countdowns and determining the winner of each round.
5.   The [User](src/main/java/ch/uzh/ifi/hase/soprafs22/entity/User.java) entity class, used to store information about the user in the database like the userID, username, password and statistical information about the game they participated in.

# Launch and Development
To help you get started with this application, in the following paragraph you will find all the important information on the used framework, what commands are used to build and run the project, how to run the tests and which dependencies exist.

## Getting started with Spring Boot
First of all we advise everyone that is not familiar with Spring Boot ro read the following documentation and Guides:

-   Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/index.html
-   Guides: http://spring.io/guides
    -   Building a RESTful Web Service: http://spring.io/guides/gs/rest-service/
    -   Building REST services with Spring: http://spring.io/guides/tutorials/bookmarks/

## Setup this project with the IDE of your choice

Download your IDE of choice: (e.g., [Eclipse](http://www.eclipse.org/downloads/), [IntelliJ](https://www.jetbrains.com/idea/download/)), [Visual Studio Code](https://code.visualstudio.com/) and make sure Java 15 is installed on your system (for Windows-users, please make sure your JAVA_HOME environment variable is set to the correct version of Java).

1. File -> Open... -> SoPra Server Template
2. Accept to import the project as a `gradle project`

To build right click the `build.gradle` file and choose `Run Build`

### VS Code
The following extensions will help you to run it more easily:
-   `pivotal.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`
-   `richardwillis.vscode-gradle`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs22` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle

You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`


## Debugging

If something is not working and/or you don't know what is going on. We highly recommend that you use a debugger and step
through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command),
do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug"Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

## Testing
We wrote alot of testcases (Unit tests, integration tests and REST interface tests). These testcases are run automatically, everytime the application is build, or can be run individually in the [Tests](server/src/test/java/ch/uzh/ifi/hase/soprafs22) folder.

If you want to write your own testcases and for more information about testing in Spring Boot have a look here: https://www.baeldung.com/spring-boot-testing

To test new REST API Endpoints quickly, we  recommend to use [Postman](https://www.getpostman.com).

## Server deployement
For the deployement of the server, we recommend using [Heroku](https://id.heroku.com/)

## External dependencies
This software makes use of the following third party libraries:
- SimpleJSON API
- Gradle
- Spring Boot

## Roadmap
**Possible  features that new developers can add:**
- Accessible functionalities to personalize properties of matches, like the timers, how many points for the win are needed, and the number of supervotes each player gets.
- Multiple sets of Cards to choose from; for example a family friendly with less offensive and less sexual content could be added.

## Authors and Acknowledgement
### Authors
- [Larissa Senning](https://github.com/orgs/sopra-fs22-group-01/people/lsenni)
- [Nevio Liberato](https://github.com/orgs/sopra-fs22-group-01/people/sircher)
- [Patric Salvisberg](https://github.com/orgs/sopra-fs22-group-01/people/PatSalvis)
- [Seyyid Palta](https://github.com/orgs/sopra-fs22-group-01/people/SeyyidPalta)
- [Thu An Phan](https://github.com/orgs/sopra-fs22-group-01/people/thphan21)


### Acknowledgement
We want acknowledge the work that was done for the template we used to create this project upon. The server template was created by [royru](https://github.com/royru), [raffi07](https://github.com/raffi07), [meck93](https://github.com/meck93), [JaanWilli](https://github.com/JaanWilli).


## License

Consult this [License] for further information.
