const form = document.getElementById('resetForm');
const passwordField = document.getElementById('password');
const confirmField = document.getElementById('confirm');

const passwordError = document.getElementById('passwordError');
const confirmError = document.getElementById('confirmError');

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

passwordField.addEventListener('input', () => {
  if (!passwordField.value.trim()) {
    setError(passwordField, passwordError, 'Password is required.');
  } else if (passwordField.value.length < 6) {
    setError(passwordField, passwordError, 'Use at least 6 characters.');
  } else {
    setValid(passwordField, passwordError);
  }
});

confirmField.addEventListener('input', () => {
  if (!confirmField.value.trim()) {
    setError(confirmField, confirmError, 'Please confirm your password.');
  } else if (confirmField.value !== passwordField.value) {
    setError(confirmField, confirmError, 'Passwords do not match.');
  } else {
    setValid(confirmField, confirmError);
  }
});

form.addEventListener('submit', (e) => {
  e.preventDefault();
  passwordField.dispatchEvent(new Event('input'));
  confirmField.dispatchEvent(new Event('input'));

  const hasErrors =
    passwordField.classList.contains('error') ||
    confirmField.classList.contains('error');

  if (hasErrors) return;

  alert('Password updated successfully!');
  // TODO: Replace with fetch('/api/auth/reset', { method: 'POST', body: JSON.stringify({ password }) })
});