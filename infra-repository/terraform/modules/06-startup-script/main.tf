locals {
  script = <<-EOT
#!/bin/bash
# Automatically update the operating system
apt-get update
apt-get upgrade -y

# Install prerequisites including Git
apt-get install -y ca-certificates curl gnupg lsb-release git

# Install Docker and Docker Compose
mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
apt-get update
apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Enable Docker service and ensure it starts automatically after reboot
systemctl enable docker
systemctl start docker

# Create deployment user and add to docker group
useradd -m -s /bin/bash deploy || echo "User deploy already exists"
usermod -aG docker deploy
usermod -aG docker ${var.vm_user}

# Create deployment and log directories
mkdir -p /opt/microservices/config
mkdir -p /opt/microservices/logs
chown -R deploy:deploy /opt/microservices

# Prepare environment variable locations
touch /opt/microservices/config/.env
chown deploy:deploy /opt/microservices/config/.env
chmod 600 /opt/microservices/config/.env
EOT
}
