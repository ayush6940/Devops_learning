module "network" {
  source = "../../modules/01-network"

  project_name = var.project_name
  environment  = var.environment
}

# You can add the other modules here similarly, for example:
# module "firewall" {
#   source = "../../modules/02-firewall"
#   ...
# }
