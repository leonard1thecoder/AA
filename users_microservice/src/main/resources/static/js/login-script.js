const form = document.getElementById('loginForm');
const email = document.getElementById('email');
const password = document.getElementById('password');
const emailError = document.getElementById('emailError');
const passwordError = document.getElementById('passwordError');

function setError(input, el, message) {
  input.classList.remove('valid');
  input.classList.add('error');
  el.textContent = message;
}
function setValid(input, el) {
  input.classList.remove('error');
  input.classList.add('valid');
  el.textContent = '';
}

email.addEventListener('input', () => {
  if (!email.value.trim()) {
    setError(email, emailError, 'Email is required.');
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
    setError(email, emailError, 'Please enter a valid email address.');
  } else {
    setValid(email, emailError);
  }
});

password.addEventListener('input', () => {
  if (!password.value.trim()) {
    setError(password, passwordError, 'Password is required.');
  } else if (password.value.length < 6) {
    setError(password, passwordError, 'Use at least 6 characters.');
  } else {
    setValid(password, passwordError);
  }
});

form.addEventListener('submit', (e) => {
  e.preventDefault();
  email.dispatchEvent(new Event('input'));
  password.dispatchEvent(new Event('input'));

  const hasErrors = email.classList.contains('error') || password.classList.contains('error');
  if (hasErrors) return;

  alert('Logging in...');
  // TODO: Replace with fetch('/api/auth/login', { ... }) to integrate backend
});