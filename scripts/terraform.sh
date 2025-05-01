#!/bin/bash

# Colores para la salida
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

function show_help {
  echo -e "${YELLOW}Uso: $0 [COMANDO] [ENTORNO]${NC}"
  echo -e "Comandos disponibles:"
  echo -e "  ${GREEN}init${NC}        Inicializa Terraform"
  echo -e "  ${GREEN}plan${NC}        Muestra los cambios que se aplicarán"
  echo -e "  ${GREEN}apply${NC}       Aplica los cambios"
  echo -e "  ${GREEN}destroy${NC}     Destruye la infraestructura"
  echo -e "  ${GREEN}workspace${NC}   Muestra el workspace actual"
  echo -e "  ${GREEN}outputs${NC}     Muestra las salidas"
  echo -e ""
  echo -e "Entornos disponibles:"
  echo -e "  ${GREEN}dev${NC}         Entorno de desarrollo"
  echo -e "  ${GREEN}staging${NC}     Entorno de pruebas"
  echo -e "  ${GREEN}prod${NC}        Entorno de producción"
  echo -e ""
  echo -e "Ejemplos:"
  echo -e "  $0 init"
  echo -e "  $0 plan dev"
  echo -e "  $0 apply staging"
  echo -e "  $0 destroy dev"
}

# Verificar que Terraform está instalado
if ! command -v terraform &> /dev/null; then
  echo -e "${RED}Error: Terraform no está instalado.${NC}"
  echo -e "Instálalo desde https://www.terraform.io/downloads.html"
  exit 1
fi

# Si no hay argumentos, mostrar ayuda
if [ $# -eq 0 ]; then
  show_help
  exit 0
fi

COMMAND=$1
ENVIRONMENT=$2

# Inicializar Terraform
if [ "$COMMAND" == "init" ]; then
  echo -e "${YELLOW}Inicializando Terraform...${NC}"
  terraform init
  echo -e "${GREEN}Inicialización completada.${NC}"
  exit 0
fi

# Verificar que se ha inicializado
if [ ! -d ".terraform" ]; then
  echo -e "${RED}Error: Terraform no ha sido inicializado. Ejecuta '$0 init' primero.${NC}"
  exit 1
fi

# Si el comando requiere un entorno, verificar que se ha proporcionado
if [ "$COMMAND" == "plan" ] || [ "$COMMAND" == "apply" ] || [ "$COMMAND" == "destroy" ]; then
  if [ -z "$ENVIRONMENT" ]; then
    echo -e "${RED}Error: El comando '$COMMAND' requiere especificar un entorno.${NC}"
    show_help
    exit 1
  fi
fi

# Gestionar workspaces
if [ ! -z "$ENVIRONMENT" ]; then
  # Verificar si el workspace existe
  WORKSPACE_EXISTS=$(terraform workspace list | grep "$ENVIRONMENT" || true)
  
  if [ -z "$WORKSPACE_EXISTS" ]; then
    echo -e "${YELLOW}Creando workspace '$ENVIRONMENT'...${NC}"
    terraform workspace new "$ENVIRONMENT"
  else
    echo -e "${YELLOW}Cambiando a workspace '$ENVIRONMENT'...${NC}"
    terraform workspace select "$ENVIRONMENT"
  fi
fi

# Ejecutar el comando correspondiente
case "$COMMAND" in
  plan)
    echo -e "${YELLOW}Planificando cambios para el entorno '$ENVIRONMENT'...${NC}"
    terraform plan
    ;;
  apply)
    echo -e "${YELLOW}Aplicando cambios para el entorno '$ENVIRONMENT'...${NC}"
    terraform apply
    ;;
  destroy)
    echo -e "${RED}ADVERTENCIA: Vas a destruir el entorno '$ENVIRONMENT'.${NC}"
    read -p "¿Estás seguro? (s/N): " CONFIRM
    if [[ "$CONFIRM" == "s" || "$CONFIRM" == "S" ]]; then
      echo -e "${YELLOW}Destruyendo entorno '$ENVIRONMENT'...${NC}"
      terraform destroy
    else
      echo -e "${GREEN}Operación cancelada.${NC}"
    fi
    ;;
  workspace)
    echo -e "${YELLOW}Workspace actual:${NC} ${GREEN}$(terraform workspace show)${NC}"
    echo -e "${YELLOW}Workspaces disponibles:${NC}"
    terraform workspace list
    ;;
  outputs)
    echo -e "${YELLOW}Mostrando outputs del entorno '$(terraform workspace show)'...${NC}"
    terraform output
    ;;
  *)
    echo -e "${RED}Comando desconocido: $COMMAND${NC}"
    show_help
    exit 1
    ;;
esac