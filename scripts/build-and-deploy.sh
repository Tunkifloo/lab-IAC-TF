#!/bin/bash
# Guarda este script como build-and-deploy.sh

set -e  # Detiene el script si algún comando falla

echo "🔨 Compilando el proyecto con Maven..."
mvn clean package -DskipTests

echo "🏗️ Construyendo la imagen Docker..."
docker-compose build

echo "🚀 Desplegando los contenedores..."
docker-compose up -d

echo "✅ ¡Despliegue completado!"
echo "Para ver los logs, ejecuta: docker-compose logs -f app_mtd"