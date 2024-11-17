JUNIT_JAR="lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar"

javac --release 22 Domini/*.java

mv Domini/*.class ../EXE/Domini


javac --release 22 Drivers/*.java

mv Drivers/DriverBruteForce.class ../EXE/Drivers/DriverBruteForce
mv Drivers/DriverDosAproximacio.class ../EXE/Drivers/DriverDosAproximacio
mv Drivers/DriverComplet.class ../EXE/Drivers/DriverComplet


javac --release 22 -classpath $JUNIT_JAR:../EXE/Domini: JUnit/*.java

mv JUnit/ProducteTest.class ../EXE/JUnit/ProducteTest
mv JUnit/CjtProductesTest.class ../EXE/JUnit/CjtProductesTest
mv JUnit/BruteForceTest.class ../EXE/JUnit/BruteForceTest
mv JUnit/DosAproximacioTest.class ../EXE/JUnit/DosAproximacioTest

mv Domini/*.class ../EXE/Domini

