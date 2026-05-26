// Minimal database for quick integration
const PROBLEMS = [
  { id: 1, title: "Two Sum", difficulty: "Easy", desc: "Find two numbers adding up to target.", code: "public class Solution {}" },
  { id: 2, title: "Binary Search", difficulty: "Easy", desc: "Search sorted array in O(log N) runtime.", code: "public class BinarySearch {}" },
  { id: 3, title: "Longest Substring", difficulty: "Medium", desc: "Find length of longest substring without repeating characters.", code: "public class LongestSubstring {}" }
];

let activeTheme = 'dark';
let activeProblem = PROBLEMS[1];

// 1. Light/Dark Theme State Manager
function initTheme() {
  activeTheme = localStorage.getItem('theme') || 
    (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
  document.documentElement.setAttribute('data-theme', activeTheme);
}

function toggleTheme() {
  activeTheme = activeTheme === 'dark' ? 'light' : 'dark';
  document.documentElement.setAttribute('data-theme', activeTheme);
  localStorage.setItem('theme', activeTheme);
}

// 2. Rendering and selection
function renderProblems() {
  const container = document.getElementById('problems-container');
  container.innerHTML = '';
  PROBLEMS.forEach(p => {
    const card = document.createElement('div');
    card.className = `problem-card ${activeProblem.id === p.id ? 'active' : ''}`;
    card.innerHTML = `<strong>${p.title}</strong> <span style="font-size:0.75rem;float:right;">${p.difficulty}</span>`;
    card.addEventListener('click', () => {
      activeProblem = p;
      document.getElementById('active-problem-title').textContent = p.title;
      document.getElementById('active-problem-desc').textContent = p.desc;
      document.getElementById('editor-textarea').value = p.code;
      renderProblems();
    });
    container.appendChild(card);
  });
}

// Initializer
window.addEventListener('DOMContentLoaded', () => {
  initTheme();
  document.getElementById('theme-toggle').addEventListener('click', toggleTheme);
  
  // Set defaults
  document.getElementById('active-problem-title').textContent = activeProblem.title;
  document.getElementById('active-problem-desc').textContent = activeProblem.desc;
  document.getElementById('editor-textarea').value = activeProblem.code;
  
  renderProblems();
});
