## Jooby - Image Server
### Description
The project contains a simple web application which supports image uploading and image searching. It could be viewed as a temporary or private cloud space that manages the images you uploaded.

|Framework|[Jooby](https://jooby.org/)|
|:-:|:-:|
|Database|[SQLite](https://www.sqlite.org/index.html)|
|Build Tool|[Gradle v4.8](https://gradle.org/)|
|Front End|[Semantic UI v2.4.1](https://semantic-ui.com/)|


### Overview
|Note|Screenshot|
|:-:|:-:|
|Main Page|![Imgur](https://i.imgur.com/surgdwB.png)|
|Upload Image or Zipped Images|![Imgur](https://i.imgur.com/GnIqwkL.png)|
|View Image|![Imgur](https://i.imgur.com/dvdZiQd.png)|
|Remove Image|![Imgur](https://i.imgur.com/AEfKQPg.png)|


### Prerequisite
#### Java
* JDK/JRE 8+
* Setup Environment Variable $JAVA_HOME
* [Download](https://www.oracle.com/technetwork/java/javase/downloads/index.html) and Install


#### Gradle
* Version 4.8+
* [Download Zip](https://gradle.org/next-steps/?version=5.4.1&format=bin)
* Unzip and Copy the Gradle Library Path (`/tmp/gradle-5.4.1`)
* Modify Line No.36 in File `activate` in the Repository to Yours
* Run `$source activate` to Modify the Gradle Environment Variable


#### Disable Gradle Daemon
* Create File `$USER_HOME/.gradle/gradle.properties`
* Add `org.gradle.daemon=false` to the File
* [Reference](https://docs.gradle.org/current/userguide/gradle_daemon.html)


### Setup and Run Server
#### IP and Port
* Modify `host` and `port` in `conf/application.conf`
* Defaul Host `0.0.0.0` and Port `3318`


#### Database
* `jooby.db` Powered by Sqlite3
* Feel Free to Drop the Tables Before App Start
* [Download Database Browser (Optional)](https://sqlitebrowser.org/dl/)


#### Run
* UNIX - `$chmod +x gradlew` and `$./gradlew joobyRun`
* Windows - `$gradlew.bat joobyRun`
* Browse the Interface via `http://YOUR_IP/images`

