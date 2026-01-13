const form = document.getElementById('forgotForm');
const emailField = document.getElementById('email');
const emailError = document.getElementById('emailError');

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

emailField.addEventListener('input', () => {
  if (!emailField.value.trim()) {
    setError(emailField, emailError, 'Email is required.');
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailField.value)) {
    setError(emailField, emailError, 'Please enter a valid email address.');
  } else {
    setValid(emailField, emailError);
  }
});

form.addEventListener('submit', (e) => {
  e.preventDefault();
  emailField.dispatchEvent(new Event('input'));

  if (emailField.classList.contains('error')) return;

  alert('Password reset link sent to your email!');
  // TODO: Replace with fetch('/api/auth/forgot', { method: 'POST', body: JSON.stringify({ email }) })
});