@echo off

call mvn install:install-file -Dfile=lib/Factions.jar -DgroupId=com.massivecraft -DartifactId=factions -Dversion=2.8.4 -Dpackaging=jar

call mvn install:install-file -Dfile=lib/Massivecore.jar -DgroupId=com.massivecraft -DartifactId=massivecore -Dversion=2.8.4 -Dpackaging=jar

pause