JUNIT_JAR="../FONTS/lib/junit-4.13.2.jar"
HAMCREST_JAR="../FONTS/lib/hamcrest-core-1.3.jar"
TEST_DIR="." 

echo "Executant BruteForceTest..."
java -cp $TEST_DIR:$JUNIT_JAR:$HAMCREST_JAR org.junit.runner.JUnitCore JUnit.BruteForceTest
echo "----------------------------------------------------------------------------"

echo "Executant CjtProductesTest..."
java -cp $TEST_DIR:$JUNIT_JAR:$HAMCREST_JAR org.junit.runner.JUnitCore JUnit.CjtProductesTest
echo "----------------------------------------------------------------------------"

echo "Executant DosAproximacioTest..."
java -cp $TEST_DIR:$JUNIT_JAR:$HAMCREST_JAR org.junit.runner.JUnitCore JUnit.DosAproximacioTest
echo "----------------------------------------------------------------------------"

echo "Executant PrestatgeriaTest..."
java -cp $TEST_DIR:$JUNIT_JAR:$HAMCREST_JAR org.junit.runner.JUnitCore JUnit.PrestatgeriaTest
echo "----------------------------------------------------------------------------"

echo "Executant ProducteTest..."
java -cp $TEST_DIR:$JUNIT_JAR:$HAMCREST_JAR org.junit.runner.JUnitCore JUnit.ProducteTest
echo "----------------------------------------------------------------------------"
