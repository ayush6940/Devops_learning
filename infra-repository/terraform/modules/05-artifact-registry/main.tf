resource "google_artifact_registry_repository" "docker_repo" {
  location      = var.region
  repository_id = "${var.project_name}-repo-${var.environment}"
  description   = "Docker repository for microservices"
  format        = "DOCKER"
}
