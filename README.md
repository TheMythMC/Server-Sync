# Server-Sync
A Fabric mod that syncs your Minecraft server with your Discord server!  

## Using
Everything can be done using the `/set` command  
The main things that you MUST set are `bot_token` and `channel_id`
You can then configure `members_send_messages` if you want to.  
### I want to edit the file manually, why is it JSON5? How do I open it?  
You can open this file using a normal text editor, however it is JSON5 because you can see exactly what you are changing.  

## Installation
1. Download and install [MultiMC](https://multimc.org/#Download) if you haven't already.
1. If you haven't already, press "create instance", and press "import from zip", and paste the following URL into the text field: https://cdn.discordapp.com/attachments/666758878813487136/699323306637262928/fabric-alpha.zip
1. Download Server-Sync from the [releases page](https://github.com/TheMythMC/Server-Sync/releases).
1. Click on your new MultiMC instance and click "edit instance" on the right. Click "loader mods" then "add", and navigate to the mod you just downloaded, and press OK.

## Contributing
1. Clone the repository
   ```
   git clone https://github.com/TheMythMC/Server-Sync
   cd Server-Sync
   ```
1. Generate the Minecraft source code
   ```
   ./gradlew genSources
   ```
    - Note: on Windows, use `gradlew` rather than `./gradlew`.
1. Import the project into your preferred IDE.
    1. If you use IntelliJ (the preferred option), you can simply import the project as a Gradle project.
    1. If you use Eclipse, you need to `./gradlew eclipse` before importing the project as an Eclipse project.
1. Edit the code
1. After testing in the IDE, build a JAR to test whether it works outside the IDE too
   ```
   ./gradlew build
   ```
   The mod JAR may be found in the `build/libs` directory
1. [Create a pull request](https://help.github.com/en/articles/creating-a-pull-request)
   so that your changes can be integrated into Server-Sync
    - Note: for large contributions, create an issue before doing all that
      work, to ask whether your pull request is likely to be accepted
