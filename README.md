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
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/

TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

1. Definitions.

   "License" shall mean the terms and conditions for use, reproduction,
   and distribution as defined by Sections 1 through 9 of this document.
   <br>
   "Licensor" shall mean the copyright owner or entity authorized by
   the copyright owner that is granting the License.
   <br>
   "Legal Entity" shall mean the union of the acting entity and all
   other entities that control, are controlled by, or are under common
   control with that entity. For the purposes of this definition,
   "control" means (i) the power, direct or indirect, to cause the
   direction or management of such entity, whether by contract or
   otherwise, or (ii) ownership of fifty percent (50%) or more of the
   outstanding shares, or (iii) beneficial ownership of such entity.
   <br>
   "You" (or "Your") shall mean an individual or Legal Entity
   exercising permissions granted by this License.
   <br>
   "Source" form shall mean the preferred form for making modifications,
   including but not limited to software source code, documentation
   source, and configuration files.
   <br>
   "Object" form shall mean any form resulting from mechanical
   transformation or translation of a Source form, including but
   not limited to compiled object code, generated documentation,
   and conversions to other media types.
   <br>
   "Work" shall mean the work of authorship, whether in Source or
   Object form, made available under the License, as indicated by a
   copyright notice that is included in or attached to the work
   (an example is provided in the Appendix below).
   <br>
   "Derivative Works" shall mean any work, whether in Source or Object
   form, that is based on (or derived from) the Work and for which the
   editorial revisions, annotations, elaborations, or other modifications
   represent, as a whole, an original work of authorship. For the purposes
   of this License, Derivative Works shall not include works that remain
   separable from, or merely link (or bind by name) to the interfaces of,
   the Work and Derivative Works thereof.<br><br>
2. 
   "Contribution" shall mean any work of authorship, including
   the original version of the Work and any modifications or additions
   to that Work or Derivative Works thereof, that is intentionally
   submitted to Licensor for inclusion in the Work by the copyright owner
   or by an individual or Legal Entity authorized to submit on behalf of
   the copyright owner. For the purposes of this definition, "submitted"
   means any form of electronic, verbal, or written communication sent
   to the Licensor or its representatives, including but not limited to
   communication on electronic mailing lists, source code control systems,
   and issue tracking systems that are managed by, or on behalf of, the
   Licensor for the purpose of discussing and improving the Work, but
   excluding communication that is conspicuously marked or otherwise
   designated in writing by the copyright owner as "Not a Contribution."
   <br>
   "Contributor" shall mean Licensor and any individual or Legal Entity
   on behalf of whom a Contribution has been received by Licensor and
   subsequently incorporated within the Work.<br><br>

3. Grant of Copyright License. Subject to the terms and conditions of
   this License, each Contributor hereby grants to You a perpetual,
   worldwide, non-exclusive, no-charge, royalty-free, irrevocable
   copyright license to reproduce, prepare Derivative Works of,
   publicly display, publicly perform, sublicense, and distribute the
   Work and such Derivative Works in Source or Object form.<br><br>

4. Grant of Patent License. Subject to the terms and conditions of
   this License, each Contributor hereby grants to You a perpetual,
   worldwide, non-exclusive, no-charge, royalty-free, irrevocable
   (except as stated in this section) patent license to make, have made,
   use, offer to sell, sell, import, and otherwise transfer the Work,
   where such license applies only to those patent claims licensable
   by such Contributor that are necessarily infringed by their
   Contribution(s) alone or by combination of their Contribution(s)
   with the Work to which such Contribution(s) was submitted. If You
   institute patent litigation against any entity (including a
   cross-claim or counterclaim in a lawsuit) alleging that the Work
   or a Contribution incorporated within the Work constitutes direct
   or contributory patent infringement, then any patent licenses
   granted to You under this License for that Work shall terminate
   as of the date such litigation is filed.<br><br>

5. Redistribution. You may reproduce and distribute copies of the
   Work or Derivative Works thereof in any medium, with or without
   modifications, and in Source or Object form, provided that You
   meet the following conditions:

   (a) You must give any other recipients of the Work or
   Derivative Works a copy of this License; and

   (b) You must cause any modified files to carry prominent notices
   stating that You changed the files; and

   (c) You must retain, in the Source form of any Derivative Works
   that You distribute, all copyright, patent, trademark, and
   attribution notices from the Source form of the Work,
   excluding those notices that do not pertain to any part of
   the Derivative Works; and

   (d) If the Work includes a "NOTICE" text file as part of its
   distribution, then any Derivative Works that You distribute must
   include a readable copy of the attribution notices contained
   within such NOTICE file, excluding those notices that do not
   pertain to any part of the Derivative Works, in at least one
   of the following places: within a NOTICE text file distributed
   as part of the Derivative Works; within the Source form or
   documentation, if provided along with the Derivative Works; or,
   within a display generated by the Derivative Works, if and
   wherever such third-party notices normally appear. The contents
   of the NOTICE file are for informational purposes only and
   do not modify the License. You may add Your own attribution
   notices within Derivative Works that You distribute, alongside
   or as an addendum to the NOTICE text from the Work, provided
   that such additional attribution notices cannot be construed
   as modifying the License.
   <br>
   You may add Your own copyright statement to Your modifications and
   may provide additional or different license terms and conditions
   for use, reproduction, or distribution of Your modifications, or
   for any such Derivative Works as a whole, provided Your use,
   reproduction, and distribution of the Work otherwise complies with
   the conditions stated in this License.<br><br>

6. Submission of Contributions. Unless You explicitly state otherwise,
   any Contribution intentionally submitted for inclusion in the Work
   by You to the Licensor shall be under the terms and conditions of
   this License, without any additional terms or conditions.
   Notwithstanding the above, nothing herein shall supersede or modify
   the terms of any separate license agreement you may have executed
   with Licensor regarding such Contributions.<br><br>

7. Trademarks. This License does not grant permission to use the trade
   names, trademarks, service marks, or product names of the Licensor,
   except as required for reasonable and customary use in describing the
   origin of the Work and reproducing the content of the NOTICE file.<br><br>

8. Disclaimer of Warranty. Unless required by applicable law or
   agreed to in writing, Licensor provides the Work (and each
   Contributor provides its Contributions) on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
   implied, including, without limitation, any warranties or conditions
   of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
   PARTICULAR PURPOSE. You are solely responsible for determining the
   appropriateness of using or redistributing the Work and assume any
   risks associated with Your exercise of permissions under this License.<br><br>

9. Limitation of Liability. In no event and under no legal theory,
   whether in tort (including negligence), contract, or otherwise,
   unless required by applicable law (such as deliberate and grossly
   negligent acts) or agreed to in writing, shall any Contributor be
   liable to You for damages, including any direct, indirect, special,
   incidental, or consequential damages of any character arising as a
   result of this License or out of the use or inability to use the
   Work (including but not limited to damages for loss of goodwill,
   work stoppage, computer failure or malfunction, or any and all
   other commercial damages or losses), even if such Contributor
   has been advised of the possibility of such damages.<br><br>

10. Accepting Warranty or Additional Liability. While redistributing
    the Work or Derivative Works thereof, You may choose to offer,
    and charge a fee for, acceptance of support, warranty, indemnity,
    or other liability obligations and/or rights consistent with this
    License. However, in accepting such obligations, You may act only
    on Your own behalf and on Your sole responsibility, not on behalf
    of any other Contributor, and only if You agree to indemnify,
    defend, and hold each Contributor harmless for any liability
    incurred by, or claims asserted against, such Contributor by reason
    of your accepting any such warranty or additional liability.<br><br>

END OF TERMS AND CONDITIONS

APPENDIX: How to apply the Apache License to your work.

      To apply the Apache License to your work, attach the following
      boilerplate notice, with the fields enclosed by brackets "[]"
      replaced with your own identifying information. (Don't include
      the brackets!)  The text should be enclosed in the appropriate
      comment syntax for the file format. We also recommend that a
      file or class name and description of purpose be included on the
      same "printed page" as the copyright notice for easier
      identification within third-party archives.

Copyright 2022 University of Zurich

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
