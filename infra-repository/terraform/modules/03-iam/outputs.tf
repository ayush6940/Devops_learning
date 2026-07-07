output "vm_service_account_email" {
  description = "The email of the VM service account"
  value       = google_service_account.vm_sa.email
}

output "github_actions_sa_email" {
  description = "The email of the GitHub Actions service account"
  value       = google_service_account.github_actions_sa.email
}
