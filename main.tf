terraform {
  required_providers {
    docker = {
        source = "kreuzwerker/docker"
        version = "3.0.2"
    }
  }
}
provider "docker" {}

    resource "docker_container" "nginx_container" {
        name = "nginx"
        image = "nginx:latest"
        ports {
            external = var.nginx_external_port[terraform.workspace]
            internal = 80
        }
    } 