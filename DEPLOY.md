# Deploy notes – NetraaMart (GitHub-only phase)

## Current status (20 Sep 2026)

| Item | Status |
|------|--------|
| F1–F8 features in source | Done |
| AI chatbot (mock FAQ) | Done |
| Docs (README, SETUP, SECURITY, PROGRESS, RETRO, checklist) | Done |
| CI workflow (`.github/workflows/ci.yml`) | Present |
| Live public URL | Pending (needs Tomcat host / lab PC) |
| Backup demo video | Pending |

## When you get a machine with JDK 17 + Maven + Tomcat

```bash
git clone https://github.com/Netraa-shree/Ecommerce-NetraaMart.git
cd Ecommerce-NetraaMart
mvn clean package
# Copy target/NetraaMart.war into Tomcat webapps/
# Start Tomcat, open http://host:8080/NetraaMart/
# Check http://host:8080/NetraaMart/api/v1/health
