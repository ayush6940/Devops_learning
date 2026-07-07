# Service Account for the Compute Engine VM to run under
resource "google_service_account" "vm_sa" {
  account_id   = "${var.project_name}-vm-sa-${var.environment}"
  display_name = "VM Service Account for ${var.environment}"
}

# Service Account for GitHub Actions CI/CD
resource "google_service_account" "github_actions_sa" {
  account_id   = "${var.project_name}-gh-sa-${var.environment}"
  display_name = "GitHub Actions Service Account for ${var.environment}"
}

# Grant GitHub Actions Service Account the ability to push to Artifact Registry
# We use artifactregistry.writer because it is the minimum required role to push images.
resource "google_project_iam_member" "artifact_registry_writer" {
  project = var.project_id
  role    = "roles/artifactregistry.writer"
  member  = "serviceAccount:${google_service_account.github_actions_sa.email}"
}
