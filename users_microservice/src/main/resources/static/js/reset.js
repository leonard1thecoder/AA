const form = document.getElementById('resetForm');
const passwordField = document.getElementById('password');
const confirmField = document.getElementById('confirm');

form.addEventListener('submit', (e) => {
  e.preventDefault();

  const payload = {
    password: passwordField.value,
    confirm: confirmField.value
  };

  fetch('/dev/api/auth/reset-password', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  })
      .then(response => {
        if (!response.ok) throw new Error('Reset failed');
        return response.json();
      })
      .then(data => {
        alert('Password updated successfully!');
      })
      .catch(err => {
        alert('Error: ' + err.message);
      });
});