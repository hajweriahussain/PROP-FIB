JUNIT_JAR="lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar"

javac --release 22 Domini/*.java

mv Domini/*.class ../EXE/Domini


javac --release 22 Drivers/*.java

mv Drivers/*.class ../EXE/Drivers


javac --release 22 -classpath $JUNIT_JAR:../EXE/Domini: JUnit/*.java

mv JUnit/*.class ../EXE/JUnit

mv Domini/*.class ../EXE/Domini

