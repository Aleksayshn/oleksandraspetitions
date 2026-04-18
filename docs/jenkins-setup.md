# Jenkins and GitHub Webhook Setup

This guide keeps the CI/CD setup simple for the assignment.

## 1. Jenkins plugins to install

Install these plugins in Jenkins:

- `Pipeline`
- `Git`
- `GitHub`
- `Credentials Binding`

Useful later for deployment:

- `SSH Agent`

## 2. Create a Jenkins pipeline job

1. Open Jenkins.
2. Click `New Item`.
3. Enter a job name such as `oleksandraspetitions-pipeline`.
4. Choose `Pipeline`.
5. Click `OK`.

In the job configuration:

1. Under `General`, you can optionally add the GitHub repository URL.
2. Under `Build Triggers`, tick:
   - `GitHub hook trigger for GITScm polling`
3. Under `Pipeline`, choose:
   - `Pipeline script from SCM`
4. Set `SCM` to:
   - `Git`
5. Add the repository URL:
   - for example `https://github.com/<your-username>/oleksandraspetitions.git`
6. If the repository is private, add Jenkins Git credentials.
7. Set the branch to:
   - `*/main`
   - or your actual working branch
8. Set `Script Path` to:
   - `Jenkinsfile`
9. Save the job.

## 3. Test the pipeline manually first

Before using webhooks:

1. Open the pipeline job.
2. Click `Build Now`.
3. Confirm the stages run:
   - Checkout
   - Build
   - Test
   - Package
   - Archive WAR
   - Manual Approval
   - Deploy Placeholder

The WAR artifact should be archived from:

```text
target/oleksandraspetitions.war
```

## 4. Configure the GitHub webhook

In GitHub:

1. Open your repository.
2. Go to `Settings` -> `Webhooks`.
3. Click `Add webhook`.
4. Set `Payload URL` to:

```text
http://<jenkins-host>:8080/github-webhook/
```

Examples:

- local Jenkins:
  - use a public tunnel or accessible address if GitHub must reach your machine
- Jenkins on EC2:
  - `http://<your-ec2-public-ip>:8080/github-webhook/`

5. Set `Content type` to:
   - `application/json`
6. Choose:
   - `Just the push event`
7. Make sure the webhook is `Active`.
8. Click `Add webhook`.

## 5. Notes for local Jenkins first

If Jenkins runs only on your laptop, GitHub usually cannot reach `localhost` directly.

Simple options:

- test the pipeline manually first
- later move Jenkins to EC2
- or temporarily expose local Jenkins with a tunnel tool during testing

For the assignment, manual testing first is acceptable while you are still setting up.

## 6. Credentials needed later for EC2 deployment

When you replace the deploy placeholder with a real EC2 deploy step, you will likely need:

- Git credentials in Jenkins
  - only if the repository is private
- EC2 SSH private key
  - so Jenkins can connect to the EC2 server
- EC2 host details
  - public IP or DNS name
- EC2 username
  - often `ec2-user` or `ubuntu`
- Tomcat deployment path
  - for example the `webapps` folder

Keep the credentials in Jenkins `Manage Credentials`.
Do not hardcode keys, usernames, or server addresses in the `Jenkinsfile`.

## 7. What the pipeline is doing

The current `Jenkinsfile` is simple on purpose:

- checks out code from GitHub
- builds the project with Maven wrapper
- runs tests
- packages the WAR file
- archives the WAR artifact
- waits for manual approval before deployment
- shows a deploy placeholder for the future EC2 Tomcat step

This is enough for the assignment now and can be extended later without changing the project structure.
