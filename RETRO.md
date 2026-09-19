# Retro – NetraaMart build

## What worked

- Clear F1–F8 feature split made implementation ordered
- Maven WAR + H2 kept local setup simple
- Mock payment and mock chatbot avoided external API blockers
- AuthFilter + role checks covered security baseline early

## What was hard

- Uploading nested folders to GitHub via web UI (must put `src` at repo root)
- Office laptop limits (no easy local Tomcat install)
- Keeping many meaningful commits without local Git for push

## One change for next sprint

- Use GitHub Desktop or Codespaces early so full commit history lands on remote in one push
- Deploy to a free Tomcat host as soon as F5 works, not only at the end

## Next (Final Review)

- Wire a real ChatProvider behind a config flag if required
- Live URL + regression pass + slide deck + backup video
