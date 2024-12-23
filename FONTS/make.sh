#!/bin/bash

# Construcción del proyecto
echo "Ejecutando ./gradlew build..."
./gradlew build
if [ $? -ne 0 ]; then
  echo "Error durante la construcción. Revisa los logs."
  exit 1
fi

# Ejecución de la aplicación
echo "Ejecutando ./gradlew run..."
./gradlew run
if [ $? -ne 0 ]; then
  echo "Error durante la ejecución. Revisa los logs."
  exit 1
fi

echo "Aplicación ejecutada correctamente."

