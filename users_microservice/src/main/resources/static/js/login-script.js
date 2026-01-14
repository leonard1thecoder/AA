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

// Email validation
email.addEventListener('input', () => {
  if (!email.value.trim()) {
    setError(email, emailError, 'Email is required.');
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
    setError(email, emailError, 'Please enter a valid email address.');
  } else {
    setValid(email, emailError);
  }
});

// Password validation
password.addEventListener('input', () => {
  if (!password.value.trim()) {
    setError(password, passwordError, 'Password is required.');
  } else if (password.value.length < 6) {
    setError(password, passwordError, 'Use at least 6 characters.');
  } else {
    setValid(password, passwordError);
  }
});

// Handle login
async function handleLogin(event) {
  event.preventDefault(); // prevent form reload

  const username = email.value; // use email as username
  const pwd = password.value;

  try {
    const response = await fetch('/dev/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ usersEmailAddress : username, password: pwd })
    });

    if (!response.ok) {
      // Try to parse error JSON if server sends one
      const errorData = await response.json().catch(() => null);
      const message = errorData?.message || 'Login failed';
      throw new Error(message);
    }

    const data = await response.json();
    console.log('Login success:', data);

    // Example: store JWT token
    localStorage.setItem('token', data.token);

    // Redirect to home/dashboard
    window.location.href = '/home';

  } catch (error) {
    console.error('Error:', error);
    alert(error.message); // show server error message
  }
}

// Attach submit handler
form.addEventListener('submit', (e) => {
  // Run validations first
  email.dispatchEvent(new Event('input'));
  password.dispatchEvent(new Event('input'));

  // Then call login
  handleLogin(e);
});