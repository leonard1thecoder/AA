const form = document.getElementById('signupForm');

form.addEventListener('submit', (e) => {
    e.preventDefault();

    const payload = Object.fromEntries(new FormData(form));

    fetch('dev/api/auth/sign-up', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
        .then(response => {
            if (!response.ok) throw new Error('Signup failed');
            return response.json();
        })
        .then(data => {
            alert('Account created successfully!');
        })
        .catch(err => {
            alert('Error: ' + err.message);
        });
});