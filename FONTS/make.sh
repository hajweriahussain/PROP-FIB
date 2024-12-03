JUNIT_JAR="lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar"

echo "Compilant classes del domini..."
javac --release 22 Domini/*.java
mv Domini/*.class ../EXE/Domini

echo "Compilant Drivers..."
javac --release 22 Drivers/*.java
mv Drivers/*.class ../EXE/Drivers


echo "Compilant tests unitaris JUnit..."
javac --release 22 -classpath $JUNIT_JAR:../EXE/Domini: JUnit/*.java
mv JUnit/*.class ../EXE/JUnit
mv Domini/*.class ../EXE/Domini

