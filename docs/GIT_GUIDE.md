# Step-by-step Git and GitHub submission (Windows / VS Code)

## A. Configure Git once
Open VS Code terminal:
```powershell
git --version
git config --global user.name "Your Name"
git config --global user.email "your-github-email@example.com"
git config --global init.defaultBranch main
```
Use the email associated with your GitHub account (or your GitHub noreply email).

## B. Create the GitHub repository
1. Sign in to GitHub.
2. Click **New repository**.
3. Repository name: `civicpulse-nexus`.
4. Choose **Private** if your internship instructions do not require public visibility.
5. Do not initialize with README, `.gitignore`, or license because this project already contains them.
6. Click **Create repository** and copy the HTTPS repository URL.

## C. Initialize and inspect locally
From the project root (the folder containing `citizen-service`, `grievance-service`, `frontend`):
```powershell
git init
git status
```
Check that `target`, `node_modules`, `.env`, passwords and generated build files are not staged.

## D. Stage and commit
```powershell
git add .
git status
git diff --cached --stat
git commit -m "Milestone 1: citizen and grievance management"
git branch -M main
```
If Git says there is nothing to commit, check whether files are saved and whether `.gitignore` is hiding them.

## E. Connect to GitHub and push
Replace the URL with the HTTPS URL copied from your repository:
```powershell
git remote add origin https://github.com/YOUR-USERNAME/civicpulse-nexus.git
git remote -v
git push -u origin main
```
GitHub may open a browser sign-in window. Authenticate there. Do not put a password or token directly into source code or a command.

## F. For future updates
After changing code:
```powershell
git status
git add .
git diff --cached
git commit -m "Add grievance workflow and SLA monitoring"
git push
```

## G. Recommended team workflow
- One member creates the GitHub repository and adds teammates as collaborators.
- Each member clones the same repository: `git clone <HTTPS-URL>`.
- Each member works on a separate branch, e.g. `feature/citizen-api`, `feature/grievance-workflow`, `feature/react-ui`.
- Push the branch, open a Pull Request, request review, resolve conflicts, then merge into `main`.
- Pull the latest main before starting new work: `git switch main` then `git pull`.
- Do not let multiple members edit the same files simultaneously without coordination.

## H. Final submission checks
- Confirm GitHub shows all required folders and README.
- Confirm both services and frontend run on another team member's machine.
- Confirm no `target/`, `node_modules/`, `.env`, real credentials or personal Aadhaar data were committed.
- Add screenshots to `docs/screenshots/` (create the folder and add the six validation/demo screenshots).
- Copy the repository URL into the internship submission form.
