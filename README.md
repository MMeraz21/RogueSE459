# RogueSE459

## General Workflow

### 1. When working on a new feature, start by pulling the latest changes from the main branch
```bash
git checkout main
git pull origin main
```

### 2. Create a new branch for the feature you're working on
Name your branch after the feature or task you're working on:
```bash
git checkout -b feature/example-feature
```

### 3. Work on the branch, make sure to do this often in case you ever need to go back to debug
```bash
git add .
git commit -m "Worked on feature"
git push -u origin feature/example-feature
```
### 4. Open a PR for review on GitHub
- Go to the [RogueSE459 repository](https://github.com/MMeraz21/RogueSE459).
- Open a Pull Request (PR) from your branch to `main`.
- Add a short description of your changes and request reviews from your teammates.

### 5. Review and Merge
- Once everyone approves the PR, merge it into `main` on GitHub.
- Pull the latest changes into your local `main` branch:
  ```bash
  git checkout main
  git pull origin main
  ```


## 🎮 Playing the Game  

### 🕹 Movement  
- Move around the world using the **WASD** keys.  
- Pressing a **WASD** key into a monster will initiate combat.  
- Combat is turn-based, with each combatant taking a turn to attempt a hit.  

### 🔥 Commands  
- **E** - Eat food from your inventory (helps satisfy hunger).  
- **R** - Cycle through weapons in your inventory (equip the next one).  
- **T** - Cycle through armor in your inventory (equip the next one).  
- **X** - Use a **scroll** from your inventory.  
- **Z** - Use a **stick** from your inventory.  
- **O** - Equip a **ring** (rings with effects are applied automatically).  
- **P** - Use a **potion** from your inventory
