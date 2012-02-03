set SCRIPT_DIR=%~dp0
java -Djava.library.path="lib" -Xmx512M -jar "%SCRIPT_DIR%sbt-launch.jar" %*